package io.github.synthrose.artofalchemy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;

public class BlockTank extends Block {

    public static final BooleanProperty CONNECTED_TOP = BooleanProperty.of("connected_top");
    public static final BooleanProperty CONNECTED_BOTTOM = BooleanProperty.of("connected_bottom");
    public static final Settings SETTINGS = Settings.of(Material.GLASS).nonOpaque().strength(0.5f).sounds(BlockSoundGroup.GLASS);

    public BlockTank() {
        super(SETTINGS);
        setDefaultState(getDefaultState().with(CONNECTED_TOP, false).with(CONNECTED_BOTTOM, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CONNECTED_TOP).add(CONNECTED_BOTTOM);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (ctx.getWorld().getBlockState(ctx.getBlockPos().offset(Direction.DOWN)).getBlock() == this) {
            state = state.with(CONNECTED_TOP, true);
        }
        if (ctx.getWorld().getBlockState(ctx.getBlockPos().offset(Direction.UP)).getBlock() == this) {
            state = state.with(CONNECTED_BOTTOM, true);
        }
        return state;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, IWorld world, BlockPos pos, BlockPos posFrom) {
        if (posFrom.equals(pos.offset(Direction.DOWN))) {
            if (newState.getBlock() == this) {
                return state.with(CONNECTED_TOP, true);
            } else {
                return state.with(CONNECTED_TOP, false);
            }
        }
        if (posFrom.equals(pos.offset(Direction.UP))) {
            if (newState.getBlock() == this) {
                return state.with(CONNECTED_BOTTOM, true);
            } else {
                return state.with(CONNECTED_BOTTOM, false);
            }
        }
        return state;
    }
}
