package io.github.synthrose.artofalchemy.blockentity;

import io.github.synthrose.artofalchemy.essentia.AoAEssentia;
import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;

public class BlockEntityAstroCentrifuge extends AbstractBlockEntityCentrifuge {
    public BlockEntityAstroCentrifuge() {
        super(AoABlockEntities.ASTRO_CENTRIFUGE);
        outputs = new EssentiaContainer[]{
                outputOf(AoAEssentia.MERCURY, AoAEssentia.VENUS, AoAEssentia.TELLUS, AoAEssentia.MARS),
                outputOf(AoAEssentia.JUPITER, AoAEssentia.SATURN, AoAEssentia.URANUS, AoAEssentia.NEPTUNE),
                outputOf(AoAEssentia.APOLLO, AoAEssentia.DIANA, AoAEssentia.CERES, AoAEssentia.PLUTO),
                outputOf(AoAEssentia.VOID)
        };
    }
}
