package io.github.synthrose.artofalchemy.gui.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;
import io.github.synthrose.artofalchemy.essentia.EssentiaStack;
import io.github.synthrose.artofalchemy.gui.handler.HandlerSynthesizer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class ScreenSynthesizer extends CottonInventoryScreen<HandlerSynthesizer>
	implements EssentiaScreen {

	public ScreenSynthesizer(HandlerSynthesizer container, PlayerEntity player) {
		super(container, player);
	}

	@Override
	public void updateEssentia(int essentiaId, EssentiaContainer essentia, BlockPos pos) {
		handler.updateEssentia(essentiaId, essentia, pos);
	}
	
	@Override
	public void updateEssentia(int essentiaId, EssentiaContainer essentia, EssentiaStack required, BlockPos pos) {
		handler.updateEssentia(essentiaId, essentia, required, pos);
	}
	
}
