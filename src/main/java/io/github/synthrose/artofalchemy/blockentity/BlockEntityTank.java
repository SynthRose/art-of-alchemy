package io.github.synthrose.artofalchemy.blockentity;

import io.github.synthrose.artofalchemy.AoAConfig;
import io.github.synthrose.artofalchemy.block.AoABlocks;
import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;
import io.github.synthrose.artofalchemy.network.AoANetworking;
import io.github.synthrose.artofalchemy.transport.HasEssentia;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class BlockEntityTank extends BlockEntity implements Tickable, HasEssentia, BlockEntityClientSerializable {

    protected EssentiaContainer essentia = new EssentiaContainer()
            .setCapacity(AoAConfig.get().tankCapacity)
            .setInput(true)
            .setOutput(true);

    public BlockEntityTank() {
        super(AoABlockEntities.TANK);
    }

    @Override
    public EssentiaContainer getContainer(Direction dir) {
        return essentia;
    }

    @Override
    public EssentiaContainer getContainer(int id) {
        if (id == 0) {
            return essentia;
        } else {
            return null;
        }
    }

    @Override
    public int getNumContainers() {
        return 1;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.put("essentia", essentia.toTag());
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        essentia = new EssentiaContainer(tag.getCompound("essentia"));
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        fromTag(world.getBlockState(pos), tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag(tag);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (!world.isClient()) {
            sync();
        }
    }

    @Override
    public void sync() {
        AoANetworking.sendEssentiaPacket(world, pos, 0, essentia);
        BlockEntityClientSerializable.super.sync();
    }

    @Override
    public void tick() {
        if (!essentia.isEmpty() && world.getBlockState(pos).getBlock() == AoABlocks.TANK && world.getBlockState(pos.down()).getBlock() == AoABlocks.TANK) {
            BlockEntity other = world.getBlockEntity(pos.down());
            if (other instanceof BlockEntityTank) {
                essentia.mixPushContents(((BlockEntityTank) other).essentia);
                this.markDirty();
                other.markDirty();
            }
        }
    }
}
