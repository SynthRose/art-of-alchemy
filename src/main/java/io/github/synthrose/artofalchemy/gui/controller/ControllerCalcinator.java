package io.github.synthrose.artofalchemy.gui.controller;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.WBar.Direction;
import io.github.cottonmc.cotton.gui.widget.data.Alignment;
import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class ControllerCalcinator extends SyncedGuiDescription {

	public ControllerCalcinator(int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx) {
		super(syncId, playerInventory, getBlockInventory(ctx), getBlockPropertyDelegate(ctx));
		
		WGridPanel root = new WGridPanel(1);
		setRootPanel(root);
		root.setSize(160, 128 + 36);
		
		WSprite background = new WSprite(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/rune_bg.png"));
		root.add(background, 0, 0, 9 * 18, 5 * 18);
		
		WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);
		root.add(itemSlot, 2 * 18, 18);
		
		WItemSlot fuelSlot = WItemSlot.of(blockInventory, 1);
		root.add(fuelSlot, 2 * 18, 3 * 18);
		
		WItemSlot outSlot = WItemSlot.outputOf(blockInventory, 2);
		root.add(outSlot, 6 * 18 + 4, 2 * 18);
		
		WBar fuelBar = new WBar(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/fire_off.png"),
				new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/fire_on.png"),
				0, 1, Direction.UP);
		root.add(fuelBar, 2 * 18 + 1, 2 * 18 + 1, 18, 18);
		
		WBar progressBar = new WBar(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/progress_off.png"),
				new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/progress_yellow.png"),
				2, 3, Direction.RIGHT);
		root.add(progressBar, 3 * 18, 2 * 18 + 1, 3 * 18, 18);
		
		WLabel title = new WLabel(new TranslatableText("block.artofalchemy.calcination_furnace"),
				WLabel.DEFAULT_TEXT_COLOR);
		title.setAlignment(Alignment.CENTER);
		root.add(title, 0, -1, 9 * 18, 18);
		
		root.add(this.createPlayerInventoryPanel(), 0, 5 * 18);
		
		root.validate(this);
	}

}
