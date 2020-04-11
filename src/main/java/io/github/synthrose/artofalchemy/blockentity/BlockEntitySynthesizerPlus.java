package io.github.synthrose.artofalchemy.blockentity;

import io.github.synthrose.artofalchemy.AoAConfig;
import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;

public class BlockEntitySynthesizerPlus extends BlockEntitySynthesizer {

    private int maxTier;
    private float speedMod;
    private int tankSize;

    public BlockEntitySynthesizerPlus() {
        super(AoABlockEntities.SYNTHESIZER_PLUS);
        AoAConfig.SynthesizerSettings settings = AoAConfig.get().synthesizerSettings;
        tankSize = settings.tankPlus;
        speedMod = settings.speedPlus;
        maxTier = settings.maxTierPlus.tier;
        essentiaContainer = new EssentiaContainer()
                .setCapacity(getTankSize())
                .setInput(true)
                .setOutput(false);
    }

    @Override
    public int getMaxTier() {
        return maxTier;
    }

    @Override
    public float getSpeedMod() {
        return speedMod;
    }

    @Override
    public int getTankSize() {
        return tankSize;
    }

}
