package io.github.synthrose.artofalchemy.block;

import io.github.synthrose.artofalchemy.blockentity.BlockEntityAstroCentrifuge;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class BlockAstroCentrifuge extends AbstractBlockCentrifuge {
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new BlockEntityAstroCentrifuge();
    }
}
