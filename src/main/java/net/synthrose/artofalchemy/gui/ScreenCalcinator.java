package net.synthrose.artofalchemy.gui;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;

public class ScreenCalcinator extends CottonInventoryScreen<ControllerCalcinator> {

	public ScreenCalcinator(ControllerCalcinator container, PlayerEntity player) {
		super(container, player);
	}

}
