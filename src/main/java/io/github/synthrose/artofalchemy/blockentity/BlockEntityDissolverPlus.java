package io.github.synthrose.artofalchemy.blockentity;

import io.github.synthrose.artofalchemy.AoAConfig;
import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;

public class BlockEntityDissolverPlus extends BlockEntityDissolver {

	private float speedMod;
	private int tankSize;
	private float yield;

	public BlockEntityDissolverPlus() {
		super(AoABlockEntities.DISSOLVER_PLUS);
		AoAConfig.DissolverSettings settings = AoAConfig.get().dissolverSettings;
		tankSize = settings.tankPlus;
		speedMod = settings.speedPlus;
		yield = settings.yieldPlus;
		maxAlkahest = getTankSize();
		essentia = new EssentiaContainer()
				.setCapacity(getTankSize())
				.setInput(false)
				.setOutput(true);
	}

	@Override
	public float getSpeedMod() {
		return speedMod;
	}

	@Override
	public int getTankSize() {
		return tankSize;
	}

	@Override
	public float getEfficiency() {
		return yield;
	}
	
}