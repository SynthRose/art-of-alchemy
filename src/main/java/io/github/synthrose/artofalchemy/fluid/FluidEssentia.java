package io.github.synthrose.artofalchemy.fluid;

import io.github.synthrose.artofalchemy.block.AoABlocks;
import io.github.synthrose.artofalchemy.essentia.Essentia;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.WorldView;
import io.github.synthrose.artofalchemy.item.AoAItems;

abstract class FluidEssentia extends BaseFluid {
	
	protected final Essentia essentia;
	
	public FluidEssentia(Essentia essentia) {
		this.essentia = essentia;
	}
	
	public Essentia getEssentiaType() {
		return essentia;
	}

	@Override
	public Fluid getStill() {
		return AoAFluids.ESSENTIA_FLUIDS.get(essentia);
	}
	
	@Override
	public Fluid getFlowing() {
		return AoAFluids.ESSENTIA_FLUIDS_FLOWING.get(essentia);
	}
	
	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == getStill() || fluid == getFlowing();
	}

	@Override
	protected boolean isInfinite() {
		return false;
	}

	@Override
	protected void beforeBreakingBlock(IWorld world, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = state.getBlock().hasBlockEntity() ? world.getBlockEntity(pos) : null;
		Block.dropStacks(state, world.getWorld(), pos, blockEntity);
	}

	@Override
	protected int method_15733(WorldView world) {
		// Seems to be distance-related; water is 4, and so too will essentia
		return 4;
	}

	@Override
	protected int getLevelDecreasePerBlock(WorldView world) {
		return 1;
	}

	@Override
	public Item getBucketItem() {
		return AoAItems.ESSENTIA_BUCKETS.get(essentia);
	}

	@Override
	protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid,
			Direction direction) {
		return false;
	}

	@Override
	public int getTickRate(WorldView world) {
		return 5;
	}

	@Override
	protected float getBlastResistance() {
		return 100.0F;
	}

	@Override
	protected BlockState toBlockState(FluidState state) {
		return AoABlocks.ESSENTIA.get(essentia).getDefaultState().with(Properties.LEVEL_15, method_15741(state));
	}
	
	public static class Flowing extends FluidEssentia {
		
		public Flowing(Essentia essentia) {
			super(essentia);
		}
		
//		@Override
//		protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid,
//				Direction direction) {
//			if (AoAFluids.ESSENTIA_FLUIDS.containsValue(fluid) ||
//				AoAFluids.ESSENTIA_FLUIDS_FLOWING.containsValue(fluid) ||
//				AoAFluids.ALKAHEST == fluid ||
//				AoAFluids.ALKAHEST_FLOWING == fluid) {
//				return true;
//			} else {
//				return super.canBeReplacedWith(state, world, pos, fluid, direction);
//			}
//		}

		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
			super.appendProperties(builder);
			builder.add(LEVEL);
		}
		
		@Override
		public int getLevel(FluidState state) {
			return state.get(LEVEL);
		}

		@Override
		public boolean isStill(FluidState state) {
			return false;
		}
		
	}
	
	public static class Still extends FluidEssentia {
		
		public Still(Essentia essentia) {
			super(essentia);
		}

		@Override
		public int getLevel(FluidState state) {
			return 8;
		}
		
		@Override
		public boolean isStill(FluidState state) {
			return true;
		}
		
	}

}
