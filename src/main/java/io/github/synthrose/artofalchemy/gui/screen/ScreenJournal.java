package io.github.synthrose.artofalchemy.gui.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.synthrose.artofalchemy.gui.handler.HandlerJournal;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class ScreenJournal extends CottonInventoryScreen<HandlerJournal> {

	public ScreenJournal(HandlerJournal container, PlayerEntity player) {
		super(container, player);
	}

	public void refresh(ItemStack journal) {
		((HandlerJournal) description).refresh(journal);
	}

}
