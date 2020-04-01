package io.github.synthrose.artofalchemy.transport;

import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.PersistentState;
import net.minecraft.world.dimension.Dimension;
import org.apache.logging.log4j.Level;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class EssentiaNetworker extends PersistentState {
    public static final int RECURSIVE_ADD_LIMIT = 1024;
    protected final ServerWorld world;
    protected final Set<EssentiaNetwork> networks = new HashSet<>();
    protected final Set<BlockPos> orphans = new HashSet<>();
    protected int recursiveAdded = 0;

    public EssentiaNetworker(ServerWorld world) {
        super(getName(world.dimension));
        this.world = world;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        ListTag posList = tag.getList("network_positions", NbtType.LIST);
        for (Tag posListEl : posList) {
            if (posListEl instanceof ListTag) {
                ListTag posTag = (ListTag) posListEl;
                BlockPos pos = new BlockPos(posTag.getInt(0), posTag.getInt(1), posTag.getInt(2));
                orphans.add(pos);
            }
        }

    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Set<BlockPos> savedPositions = new HashSet<>(orphans);
        for (EssentiaNetwork network : networks) {
            // Add one position from each network
            savedPositions.add(network.getPositions().toArray(new BlockPos[0])[0]);
        }
        ListTag posList = new ListTag();
        for (BlockPos pos : savedPositions) {
            ListTag posTag = new ListTag();
            posTag.add(IntTag.of(pos.getX()));
            posTag.add(IntTag.of(pos.getY()));
            posTag.add(IntTag.of(pos.getZ()));
            posList.add(posTag);
        }
        tag.put("network_positions", posList);
        return tag;
    }

    public static EssentiaNetworker get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(() -> new EssentiaNetworker(world), getName(world.dimension));
    }

    public static String getName(Dimension dimension) {
        return "essentia" + dimension.getType().getSuffix();
    }

    public void tick() {
        recursiveAdded = 0;
        for (BlockPos pos : new HashSet<>(orphans)) {
            if (recursiveAdded < RECURSIVE_ADD_LIMIT) {
                recursiveAdd(pos);
            } else {
                break;
            }
        }
        for (EssentiaNetwork network : networks) {
            network.tick();
        }
    }

    public Optional<EssentiaNetwork> getNetwork(BlockPos pos) {
        for (EssentiaNetwork network : networks) {
            if (network.contains(pos)) {
                return Optional.of(network);
            }
        }
        return Optional.empty();
    }

    public Set<EssentiaNetwork> getConnectedNetworks(BlockPos pos) {
        Set<EssentiaNetwork> connectedNetworks = new HashSet<>();
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof NetworkElement) {
            for (EssentiaNetwork network : networks) {
                for (BlockPos other : network.getPositions()) {
                    if (((NetworkElement) block).isConnected(world, pos, other)) {
                        connectedNetworks.add(network);
                    }
                }
            }
        }
        return connectedNetworks;
    }

    public Set<BlockPos> getConnections(BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof NetworkElement) {
            return ((NetworkElement) block).getConnections(world, pos);
        } else {
            return new HashSet<>();
        }
    }

    public EssentiaNetwork add(BlockPos pos) {
        Optional<EssentiaNetwork> oldNetwork = getNetwork(pos);
        if (oldNetwork.isPresent()) {
            // If this position is already in a network, just return that network
            return oldNetwork.get();
        } else {
            // Otherwise, add it to any connected networks, creating a new one or merging if necessary
            EssentiaNetwork network = merge(getConnectedNetworks(pos).toArray(new EssentiaNetwork[0]));
            network.add(pos);
            network.rebuildNodes(pos);
            markDirty();
            return network;
        }
    }

    public void recursiveAdd(BlockPos pos) {
        if (recursiveAdded < RECURSIVE_ADD_LIMIT) {
            if (!getNetwork(pos).isPresent()) {
                recursiveAdded++;
                add(pos);
                orphans.remove(pos);
                Set<BlockPos> connections = getConnections(pos);
                connections.forEach(this::recursiveAdd);
            }
        } else {
            orphans.add(pos);
            ArtOfAlchemy.log(Level.WARN, "Reached network addition limit while processing [" + pos.getX() +
                    ", " + pos.getY() + ", " + pos.getZ()+ "] in " + Registry.DIMENSION_TYPE.getId(world.getDimension().getType()));
        }
    }

    // Merges n networks (where n can be 0, thus creating a new, empty network.)
    // Warning: WILL NOT rebuild nodes automatically.
    public EssentiaNetwork merge(EssentiaNetwork... networks) {
        if (networks.length == 1) {
            // If given one network, there's nothing to merge, so just return it
            return networks[0];
        } else {
            // If given 0 or 2+ networks, create a new network with all the positions of the old ones and delete them
            EssentiaNetwork mergedNetwork = new EssentiaNetwork(world);
            this.networks.add(mergedNetwork);
            for (EssentiaNetwork network : networks) {
                mergedNetwork.getPositions().addAll(network.getPositions());
                this.networks.remove(network);
            }
            mergedNetwork.rebuildNodes();
            markDirty();
            return mergedNetwork;
        }
    }

    // Removes this position from a network it's in, deleting the network if it was the last element.
    public void remove(BlockPos pos) {
        getNetwork(pos).ifPresent((network) -> {
            network.remove(pos);
            Set<BlockPos> connections = getConnections(pos);
            if (network.getSize() == 0 || connections.size() > 1) {
                orphans.addAll(connections);
                networks.remove(network);
            } else {
                network.rebuildNodes();
            }
        });
    }

    public void remove(BlockPos pos, Set<BlockPos> connections) {
        getNetwork(pos).ifPresent((network) -> {
            network.remove(pos);
            if (network.getSize() == 0 || connections.size() > 1) {
                orphans.addAll(connections);
                networks.remove(network);
            } else {
                network.rebuildNodes();
            }
            markDirty();
        });
    }

    // Possibly rebuilds the networks associated with a specific position after connections are made or broken.
    public void update(BlockPos pos) {
        remove(pos);
        add(pos);
    }

    // Updates the nodes at a specific position. Not sufficient if connections have been made or broken.
    public void updateNodes(BlockPos pos) {
        getNetwork(pos).ifPresent((network) -> network.rebuildNodes(pos));
        markDirty();
    }

}
