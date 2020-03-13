package net.synthrose.artofalchemy.gui;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WBar.Direction;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.Alignment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.BlockContext;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.synthrose.artofalchemy.ArtOfAlchemy;

public class ControllerCalcinator extends CottonCraftingController {

	public ControllerCalcinator(int syncId, PlayerInventory playerInventory, BlockContext ctx) {
		super(RecipeType.SMELTING, syncId, playerInventory,
				getBlockInventory(ctx), getBlockPropertyDelegate(ctx));
		
		
		WGridPanel root = new WGridPanel();
		setRootPanel(root);
		root.setSize(160, 128 + 36);
		
		WSprite background = new WSprite(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/rune_bg.png"));
		root.add(background, 0, 0, 9, 5);
		
		WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);
		root.add(itemSlot, 2, 1);
		
		WItemSlot fuelSlot = WItemSlot.of(blockInventory, 1);
		root.add(fuelSlot, 2, 3);
		
		WItemSlot outSlot = WItemSlot.outputOf(blockInventory, 2);
		root.add(outSlot, 6, 2);
		
		WBar fuelBar = new WBar(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/fire_off.png"),
				new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/fire_on.png"),
				0, 1, Direction.UP);
		root.add(fuelBar, 2, 2);
		
		WBar progressBar = new WBar(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/progress_off.png"),
				new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/progress_yellow.png"),
				2, 3, Direction.RIGHT);
		root.add(progressBar, 3, 2, 3, 1);
		
		WLabel title = new WLabel(new TranslatableText("block.artofalchemy.calcination_furnace"),
				WLabel.DEFAULT_TEXT_COLOR);
		title.setAlignment(Alignment.CENTER);
		root.add(title, 0, 0, 9, 1);
		
		
		root.add(this.createPlayerInventoryPanel(), 0, 5);
		
		root.validate(this);
	}

}
