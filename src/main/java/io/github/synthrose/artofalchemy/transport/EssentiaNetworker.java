package io.github.synthrose.artofalchemy.transport;

import io.github.synthrose.artofalchemy.ArtOfAlchemy;
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

import java.util.*;

public class EssentiaNetworker extends PersistentState {
    public static final int PER_TICK_LIMIT = 1024;
    protected final ServerWorld world;
    protected final Set<EssentiaNetwork> networks = new HashSet<>();
    protected final Set<BlockPos> orphans = new HashSet<>();
    protected int processed = 0;

    protected final Set<BlockPos> legacyOrphans = new HashSet<>();
    protected final Map<BlockPos, EssentiaNetwork> cache = new HashMap<>();

    public EssentiaNetworker(ServerWorld world) {
        super(getName(world.dimension));
        this.world = world;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        System.out.println(tag.toString());
        ListTag networkList = tag.getList("networks", NbtType.LIST);
        for (Tag networkTag : networkList) {
            if (networkTag instanceof ListTag && ((ListTag) networkTag).size() > 0) {
                networks.add(new EssentiaNetwork(world, (ListTag) networkTag));
            }
        }
        ListTag orphanList = tag.getList("orphans", NbtType.LIST);
        for (Tag orphanTag : orphanList) {
            if (orphanTag instanceof ListTag) {
                ListTag posTag = (ListTag) orphanTag;
                BlockPos pos = new BlockPos(posTag.getInt(0), posTag.getInt(1), posTag.getInt(2));
                orphans.add(pos.toImmutable());
            }
        }
        ListTag legacyList = tag.getList("network_positions", NbtType.LIST);
        for (Tag orphanTag : legacyList) {
            if (orphanTag instanceof ListTag) {
                ListTag posTag = (ListTag) orphanTag;
                BlockPos pos = new BlockPos(posTag.getInt(0), posTag.getInt(1), posTag.getInt(2));
                legacyOrphans.add(pos.toImmutable());
            }
        }
        rebuildCache();
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        ListTag networkList = new ListTag();
        for (EssentiaNetwork network : networks) {
            if (network.getSize() > 0) {
                networkList.add(network.toTag());
            }
        }
        tag.put("networks", networkList);
        ListTag orphanList = new ListTag();
        for (BlockPos pos : orphans) {
            ListTag posTag = new ListTag();
            posTag.add(IntTag.of(pos.getX()));
            posTag.add(IntTag.of(pos.getY()));
            posTag.add(IntTag.of(pos.getZ()));
            orphanList.add(posTag);
        }
        tag.put("orphans", orphanList);
        System.out.println(tag.toString());
        System.out.println(cache.toString());
        return tag;
    }

    public void rebuildCache() {
        cache.clear();
        for (EssentiaNetwork network : networks) {
            for (BlockPos pos : network.getPositions()) {
                cache.put(pos.toImmutable(), network);
            }
        }
    }

    public static EssentiaNetworker get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(() -> new EssentiaNetworker(world), getName(world.dimension));
    }

    public static String getName(Dimension dimension) {
        return "essentia" + dimension.getType().getSuffix();
    }

    public void tick() {
        processed = 0;
        for (BlockPos pos : new HashSet<>(orphans)) {
            if (processed < PER_TICK_LIMIT) {
                add(pos.toImmutable());
            } else {
                break;
            }
        }
        for (BlockPos pos : new HashSet<>(legacyOrphans)) {
            if (processed < PER_TICK_LIMIT) {
                recursiveAdd(pos.toImmutable());
            } else {
                break;
            }
        }
        for (EssentiaNetwork network : networks) {
            network.tick();
        }
    }

    public Optional<EssentiaNetwork> getNetwork(BlockPos pos) {
        {
            EssentiaNetwork network = cache.get(pos);
            if (network != null) {
                if (network.contains(pos)) {
                    return Optional.of(network);
                }
            }
        }
        for (EssentiaNetwork network : networks) {
            if (network.contains(pos)) {
                cache.put(pos.toImmutable(), network);
                return Optional.of(network);
            }
        }
        return Optional.empty();
    }

    public Set<EssentiaNetwork> getConnectedNetworks(BlockPos pos) {
        Set<EssentiaNetwork> connectedNetworks = new HashSet<>();
        for (BlockPos other : getConnections(pos)) {
            Optional<EssentiaNetwork> network = getNetwork(other);
            network.ifPresent(connectedNetworks::add);
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

    public void add(BlockPos pos) {
        processed++;
        orphans.remove(pos);
        if (!getNetwork(pos).isPresent()) {
            // Otherwise, add it to any connected networks, creating a new one or merging if necessary
            EssentiaNetwork network = merge(getConnectedNetworks(pos).toArray(new EssentiaNetwork[0]));
            network.add(pos.toImmutable());
            cache.put(pos.toImmutable(), network);
            markDirty();
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
                for (BlockPos pos : mergedNetwork.getPositions()) {
                    cache.put(pos, mergedNetwork);
                }
                this.networks.remove(network);
            }
            mergedNetwork.rebuildNodes();
            markDirty();
            return mergedNetwork;
        }
    }

    public void remove(BlockPos pos, Set<BlockPos> connections) {
        processed++;
        getNetwork(pos).ifPresent((network) -> {
            cache.remove(pos);
            network.remove(pos);
            if (network.getSize() == 0 || connections.size() > 1) {
                for (BlockPos netPos : network.getPositions()) {
                    orphans.add(netPos.toImmutable());
                    cache.remove(netPos);
                }
                networks.remove(network);
            }
            markDirty();
        });
    }

    @Deprecated
    public void recursiveAdd(BlockPos pos) {
        if (processed < PER_TICK_LIMIT) {
            if (!getNetwork(pos).isPresent()) {
                add(pos.toImmutable());
                legacyOrphans.remove(pos);
                Set<BlockPos> connections = getConnections(pos);
                connections.forEach(this::recursiveAdd);
            }
        } else {
            legacyOrphans.add(pos.toImmutable());
            ArtOfAlchemy.log(Level.WARN, "Reached essentia network processing limit at [" + pos.getX() +
                    ", " + pos.getY() + ", " + pos.getZ()+ "] in " + Registry.DIMENSION_TYPE.getId(world.getDimension().getType()));
        }
    }


}
