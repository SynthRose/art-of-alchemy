package io.github.synthrose.artofalchemy.gui.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.synthrose.artofalchemy.gui.controller.ControllerCalcinator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;

@Environment(EnvType.CLIENT)
public class ScreenCalcinator extends CottonInventoryScreen<ControllerCalcinator> {

	public ScreenCalcinator(ControllerCalcinator container, PlayerEntity player) {
		super(container, player);
	}

}
