package net.synthrose.artofalchemy.gui;

import net.minecraft.util.math.BlockPos;
import net.synthrose.artofalchemy.essentia.EssentiaContainer;

public interface EssentiaScreen {

	void updateEssentia(int essentiaId, EssentiaContainer container, BlockPos pos);
	
}
