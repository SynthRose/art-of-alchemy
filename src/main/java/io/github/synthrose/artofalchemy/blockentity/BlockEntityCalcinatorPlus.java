package io.github.synthrose.artofalchemy.blockentity;

import io.github.synthrose.artofalchemy.block.AoABlocks;

public class BlockEntityCalcinatorPlus extends BlockEntityCalcinator {

	private final int OPERATION_TIME = 100;
	private final float EFFICIENCY = 1.00f;

	public BlockEntityCalcinatorPlus() {
		super(AoABlockEntities.CALCINATOR_PLUS);
	}

	@Override
	public int getOperationTime() {
		return OPERATION_TIME;
	}

	@Override
	public float getEfficiency() {
		return EFFICIENCY;
	}

}