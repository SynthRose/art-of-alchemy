package io.github.synthrose.artofalchemy.gui.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.synthrose.artofalchemy.gui.controller.ControllerAnalyzer;
import io.github.synthrose.artofalchemy.gui.controller.ControllerJournal;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;

@Environment(EnvType.CLIENT)
public class ScreenJournal extends CottonInventoryScreen<ControllerJournal> {

	public ScreenJournal(ControllerJournal container, PlayerEntity player) {
		super(container, player);
	}

	public void refresh() {
		((ControllerJournal) description).refresh();
	}

}
