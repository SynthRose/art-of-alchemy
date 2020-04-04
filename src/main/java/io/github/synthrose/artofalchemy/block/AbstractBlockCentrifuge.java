package io.github.synthrose.artofalchemy.block;

import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

abstract public class AbstractBlockCentrifuge extends Block implements BlockEntityProvider {

	public static final int TANK_SIZE = 4000;
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final Settings SETTINGS = Settings
		.of(Material.STONE)
		.strength(5.0f, 6.0f);
	protected EssentiaContainer input = new EssentiaContainer().setCapacity(TANK_SIZE).setInput(true).setOutput(false);
	protected EssentiaContainer[] outputs;

	public AbstractBlockCentrifuge() {
		super(SETTINGS);
		setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
	}
	
	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(FACING, ctx.getPlayerFacing().getOpposite());
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
							  BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		}

		if (player.isSneaking()) {
			world.setBlockState(pos, rotate(state, BlockRotation.CLOCKWISE_90));
		} else {
			world.setBlockState(pos, rotate(state, BlockRotation.COUNTERCLOCKWISE_90));
		}
		return ActionResult.SUCCESS;
	}
	
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
}
