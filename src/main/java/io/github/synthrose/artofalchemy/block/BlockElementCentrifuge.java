package io.github.synthrose.artofalchemy.block;

import io.github.synthrose.artofalchemy.blockentity.BlockEntityElementCentrifuge;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class BlockElementCentrifuge extends AbstractBlockCentrifuge {
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new BlockEntityElementCentrifuge();
    }
}
