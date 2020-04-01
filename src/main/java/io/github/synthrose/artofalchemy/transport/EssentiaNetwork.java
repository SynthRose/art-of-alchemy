package io.github.synthrose.artofalchemy.transport;
import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EssentiaNetwork {
    protected final World world;
    protected final Set<BlockPos> positions = new HashSet<>();
    protected final Set<NetworkNode> nodes = new HashSet<>();
    protected final Set<NetworkNode> pullers = new HashSet<>();
    protected final Set<NetworkNode> pushers = new HashSet<>();
    protected final Set<NetworkNode> passives = new HashSet<>();
    protected final UUID uuid = UUID.randomUUID();
    protected long lastTicked;

    EssentiaNetwork(World world) {
        this.world = world;
        lastTicked = world.getTime();
    }

    public World getWorld() {
        return world;
    }

    public Set<BlockPos> getPositions() {
        return positions;
    }

    public int getSize() {
        return positions.size();
    }

    public boolean contains(BlockPos pos) { return positions.contains(pos); }

    public Set<NetworkNode> getNodes() {
        return nodes;
    }

    public void rebuildNodes() {
        nodes.clear();
        for (BlockPos pos : positions) {
            Block block = world.getBlockState(pos).getBlock();
            if (block instanceof NetworkElement) {
                nodes.addAll(((NetworkElement) block).getNodes(world, pos));
            }
        }
        pullers.clear();
        pushers.clear();
        passives.clear();
        for (NetworkNode node : nodes) {
            switch (node.getType()) {
                case PULL:
                    pullers.add(node);
                    break;
                case PUSH:
                    pushers.add(node);
                    break;
                case PASSIVE:
                    passives.add(node);
                    break;
            }
        }
    }

    public void rebuildNodes(BlockPos pos) {
        nodes.removeIf((node) -> node.getPos() == pos);
        pullers.removeIf((node) -> node.getPos() == pos);
        pushers.removeIf((node) -> node.getPos() == pos);
        passives.removeIf((node) -> node.getPos() == pos);

        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof NetworkElement) {
            Set<NetworkNode> newNodes = ((NetworkElement) block).getNodes(world, pos);
            for (NetworkNode node : newNodes) {
                nodes.add(node);
                switch (node.getType()) {
                    case PULL:
                        pullers.add(node);
                        break;
                    case PUSH:
                        pushers.add(node);
                        break;
                    case PASSIVE:
                        passives.add(node);
                        break;
                }
            }
        }
    }

    public void tick() {
        if (world.getTime() < lastTicked + 5) {
            return;
        }
        lastTicked = world.getTime();

        for (NetworkNode pusher : pushers) {
            for (NetworkNode puller : pullers) {
                transfer(pusher, puller);
            }
            for (NetworkNode passive : passives) {
                transfer(pusher, passive);
            }
        }
        for (NetworkNode puller : pullers) {
            for (NetworkNode passive : passives) {
                transfer(passive, puller);
            }
        }
    }

    public void transfer(NetworkNode from, NetworkNode to) {
        BlockEntity fromBE = from.getBlockEntity();
        BlockEntity toBE = to.getBlockEntity();
        if (fromBE instanceof HasEssentia && toBE instanceof HasEssentia) {
            for (int i = 0; i < ((HasEssentia) fromBE).getNumContainers(); i++) {
                EssentiaContainer fromContainer;
                if (from.getDirection().isPresent()) {
                    fromContainer = ((HasEssentia) fromBE).getContainer(from.getDirection().get().getOpposite());
                } else {
                    fromContainer = ((HasEssentia) fromBE).getContainer();
                }
                for (int j = 0; j < ((HasEssentia) toBE).getNumContainers(); j++) {
                    EssentiaContainer toContainer;
                    if (to.getDirection().isPresent()) {
                        toContainer = ((HasEssentia) toBE).getContainer(to.getDirection().get().getOpposite());
                    } else {
                        toContainer = ((HasEssentia) toBE).getContainer();
                    }
                    fromContainer.pushContents(toContainer);
                }
            }
            fromBE.markDirty();
            toBE.markDirty();
        }
    }

    public boolean add(BlockPos pos) {
        return positions.add(pos.toImmutable());
    }

    public boolean remove(BlockPos pos) {
        return positions.remove(pos);
    }

}
