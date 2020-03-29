package io.github.synthrose.artofalchemy.gui.screen;

import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;
import io.github.synthrose.artofalchemy.essentia.EssentiaStack;
import net.minecraft.util.math.BlockPos;

public interface EssentiaScreen {

	void updateEssentia(int essentiaId, EssentiaContainer container, BlockPos pos);
	
	default void updateEssentia(int essentiaId, EssentiaContainer container, EssentiaStack required, BlockPos pos) {
		updateEssentia(essentiaId, container, pos);
	}
	
}
