package net.synthrose.artofalchemy.gui;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.synthrose.artofalchemy.essentia.EssentiaContainer;

@Environment(EnvType.CLIENT)
public class ScreenSynthesizer extends CottonInventoryScreen<ControllerSynthesizer>
	implements EssentiaScreen {

	public ScreenSynthesizer(ControllerSynthesizer container, PlayerEntity player) {
		super(container, player);
	}

	@Override
	public void updateEssentia(int essentiaId, EssentiaContainer essentia, BlockPos pos) {
		handler.updateEssentia(essentiaId, essentia, pos);
	}
		
}
