package net.synthrose.artofalchemy.gui;

import net.minecraft.util.math.BlockPos;
import net.synthrose.artofalchemy.essentia.EssentiaContainer;
import net.synthrose.artofalchemy.essentia.EssentiaStack;

public interface EssentiaScreen {

	void updateEssentia(int essentiaId, EssentiaContainer container, BlockPos pos);
	
	default void updateEssentia(int essentiaId, EssentiaContainer container, EssentiaStack required, BlockPos pos) {
		updateEssentia(essentiaId, container, pos);
	}
	
}
