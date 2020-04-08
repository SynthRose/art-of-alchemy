package io.github.synthrose.artofalchemy.blockentity;

public class BlockEntityDissolverPlus extends BlockEntityDissolver {

	private static final float SPEED_MOD = 0.10f;
	private static final int TANK_SIZE = 8000;
	private final float EFFICIENCY = 1.00f;

	public BlockEntityDissolverPlus() {
		super(AoABlockEntities.DISSOLVER_PLUS);
	}

	@Override
	public float getSpeedMod() {
		return SPEED_MOD;
	}

	@Override
	public int getTankSize() {
		return TANK_SIZE;
	}

	@Override
	public float getEfficiency() {
		return EFFICIENCY;
	}
	
}