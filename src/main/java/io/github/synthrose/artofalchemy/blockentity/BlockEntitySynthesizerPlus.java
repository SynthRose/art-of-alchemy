package io.github.synthrose.artofalchemy.blockentity;

public class BlockEntitySynthesizerPlus extends BlockEntitySynthesizer {

    private static final int MAX_TIER = 7;
    private static final float SPEED_MOD = 0.10f;
    private static final int TANK_SIZE = 8000;

    public BlockEntitySynthesizerPlus() {
        super(AoABlockEntities.SYNTHESIZER_PLUS);
    }

    @Override
    public int getMaxTier() {
        return MAX_TIER;
    }

    @Override
    public float getSpeedMod() {
        return SPEED_MOD;
    }

    @Override
    public int getTankSize() {
        return TANK_SIZE;
    }

}
