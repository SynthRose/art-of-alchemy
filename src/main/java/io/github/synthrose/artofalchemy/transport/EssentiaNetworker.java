package io.github.synthrose.artofalchemy.transport;

import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class EssentiaNetworker {
    protected final World world;
    protected final Set<EssentiaNetwork> networks = new HashSet<>();
    protected final Set<NetworkNode> orphans = new HashSet<>();
    // TODO: Save/load nodes to the world so they can rebuild networks after a restart
    // TODO: Flood-addition

    public EssentiaNetworker(World world) {
        this.world = world;
        WorldTickCallback.EVENT.register((tickingWorld) -> {
            if (world == tickingWorld && world.isClient()) {
                this.tick();
            }
        });
    }

    public void tick() {
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
            return network;
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
            return mergedNetwork;
        }
    }

    // Removes this position from a network it's in, deleting the network if it was the last element.
    // TODO: Figure out how to handle network splits.
    public void remove(BlockPos pos) {
        getNetwork(pos).ifPresent((network) -> {
            network.remove(pos);
            network.rebuildNodes();
            if (network.getPositions().size() == 0) {
                networks.remove(network);
            }
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
    }

}
