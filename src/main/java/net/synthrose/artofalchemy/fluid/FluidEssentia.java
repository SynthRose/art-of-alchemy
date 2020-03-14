package net.synthrose.artofalchemy.fluid;

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
import net.synthrose.artofalchemy.EssentiaType;
import net.synthrose.artofalchemy.block.AoABlocks;
import net.synthrose.artofalchemy.item.AoAItems;

abstract class FluidEssentia extends BaseFluid {
	
	protected final EssentiaType essentia;
	
	public FluidEssentia(EssentiaType essentia) {
		this.essentia = essentia;
	}
	
	public EssentiaType getEssentiaType() {
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
		
		public Flowing(EssentiaType essentia) {
			super(essentia);
		}

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
		
		public Still(EssentiaType essentia) {
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
