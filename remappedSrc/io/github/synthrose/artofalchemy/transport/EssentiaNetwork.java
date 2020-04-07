package io.github.synthrose.artofalchemy.transport;
import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
    protected boolean dirty;

    EssentiaNetwork(World world) {
        this.world = world;
        lastTicked = world.getTime();
    }

    EssentiaNetwork(World world, ListTag tag) {
        this(world);
        fromTag(tag);
    }

    public World getWorld() {
        return world;
    }

    public UUID getUuid() {
        return uuid;
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

    public void markDirty() {
        dirty = true;
    }

    public void fromTag(ListTag tag) {
        for (Tag listElement : tag) {
            if (listElement instanceof ListTag) {
                ListTag posTag = (ListTag) listElement;
                BlockPos pos = new BlockPos(posTag.getInt(0), posTag.getInt(1), posTag.getInt(2));
                if (world.getBlockState(pos).getBlock() instanceof NetworkElement) {
                    add(pos);
                }
            }
        }
    }

    public ListTag toTag() {
        ListTag tag = new ListTag();
        for (BlockPos pos : positions) {
            ListTag posTag = new ListTag();
            posTag.add(IntTag.of(pos.getX()));
            posTag.add(IntTag.of(pos.getY()));
            posTag.add(IntTag.of(pos.getZ()));
            tag.add(posTag);
        }
        return tag;
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

    public void removeNodes(BlockPos pos) {
        nodes.removeIf((node) -> node.getPos().equals(pos));
        pullers.removeIf((node) -> node.getPos().equals(pos));
        pushers.removeIf((node) -> node.getPos().equals(pos));
        passives.removeIf((node) -> node.getPos().equals(pos));
    }

    public void addNodes(BlockPos pos) {
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
        if (dirty) {
            rebuildNodes();
            dirty = false;
        }

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
        if (!positions.contains(pos)) {
            addNodes(pos);
            return positions.add(pos);
        } else {
            return false;
        }
    }

    public boolean remove(BlockPos pos) {
        if (positions.contains(pos)) {
            removeNodes(pos);
            return positions.remove(pos);
        } else {
            return false;
        }
    }

}
