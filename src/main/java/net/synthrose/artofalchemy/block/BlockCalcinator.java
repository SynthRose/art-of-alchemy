package net.synthrose.artofalchemy.block;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.synthrose.artofalchemy.blockentity.BlockEntityCalcinator;

public class BlockCalcinator extends Block implements BlockEntityProvider {
	
	public static DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static BooleanProperty LIT = Properties.LIT;
	public static final Identifier ID = Registry.BLOCK.getId(Blocks.CALCINATOR);
	

	public BlockCalcinator(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(LIT, false));
	}
	
	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(FACING).add(LIT);
	}
	
	@Override
	public int getLuminance(BlockState state) {
		if (state.get(LIT)) { 
			return 15;
		} else {
			return 0;
		}
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
		
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null && blockEntity instanceof BlockEntityCalcinator) {			
			ContainerProviderRegistry.INSTANCE.openContainer(ID, player,
					(packetByteBuf -> packetByteBuf.writeBlockPos(pos)));
		}
		
		return ActionResult.SUCCESS;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new BlockEntityCalcinator();
	}

	@Override
	public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof BlockEntityCalcinator) {
				ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
			}

			super.onBlockRemoved(state, world, pos, newState, moved);
		}
	}
	
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

}
