package io.github.synthrose.artofalchemy.gui.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.synthrose.artofalchemy.gui.handler.HandlerProjector;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;

@Environment(EnvType.CLIENT)
public class ScreenProjector extends CottonInventoryScreen<HandlerProjector> {

	public ScreenProjector(HandlerProjector container, PlayerEntity player) {
		super(container, player);
	}

}
