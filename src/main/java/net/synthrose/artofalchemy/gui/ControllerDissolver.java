package net.synthrose.artofalchemy.gui;

import java.util.function.Supplier;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WDynamicLabel;
import io.github.cottonmc.cotton.gui.widget.WBar.Direction;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.Alignment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.BlockContext;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.synthrose.artofalchemy.ArtOfAlchemy;
import net.synthrose.artofalchemy.essentia.EssentiaContainer;
import net.synthrose.artofalchemy.essentia.HasEssentia;
import net.synthrose.artofalchemy.recipe.AoARecipes;

public class ControllerDissolver extends CottonCraftingController {
	
	BlockPos pos;
	WEssentiaPanel essentiaPanel;
	
	public ControllerDissolver(int syncId, PlayerInventory playerInventory, BlockContext ctx) {
		super(AoARecipes.DISSOLUTION, syncId, playerInventory,
				getBlockInventory(ctx), getBlockPropertyDelegate(ctx));
		
		pos = ctx.run((world, pos) -> {
			return pos;
		}, null);
		
		WGridPanel root = new WGridPanel();
		setRootPanel(root);
		root.setSize(162, 128 + 36);
		
		WSprite background = new WSprite(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/rune_bg.png"));
		root.add(background, 0, 0, 9, 5);
		
		WItemSlot inSlot = WItemSlot.of(blockInventory, 0);
		root.add(inSlot, 2, 2);
		
		WBar tankBar = new WBar(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/tank_empty.png"),
				new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/tank_full.png"),
				0, 1, Direction.UP);
		root.add(tankBar, 0, 1, 2, 3);
		
		WBar progressBar = new WBar(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/progress_off.png"),
				new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/progress_magenta.png"),
				2, 3, Direction.RIGHT);
		root.add(progressBar, 3, 2, 3, 1);
		
		WLabel title = new WLabel(new TranslatableText("block.artofalchemy.dissolution_chamber"),
				WLabel.DEFAULT_TEXT_COLOR);
		title.setAlignment(Alignment.CENTER);
		root.add(title, 0, 0, 9, 1);
		
		Supplier<String> mBSupplier = () -> { return Integer.toString(propertyDelegate.get(0)); };
		WDynamicLabel mBLabel = new WDynamicLabel(mBSupplier);
		mBLabel.setAlignment(Alignment.CENTER);
		root.add(mBLabel, 0, 4, 2, 1);
		
		EssentiaContainer essentia = getEssentia(ctx);
		essentiaPanel = new WEssentiaPanel(essentia);
		root.add(essentiaPanel, 6, 1, 3, 4);
		
		root.add(this.createPlayerInventoryPanel(), 0, 5);
		
		root.validate(this);
		
	}
	
	public void updateEssentia(int essentiaId, EssentiaContainer essentia, BlockPos pos) {
		if (pos.equals(this.pos)) {
			essentiaPanel.updateEssentia(essentia);
		}
	}

	private static EssentiaContainer getEssentia(BlockContext ctx) {
		return ctx.run((world, pos) -> {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof HasEssentia) {
				return ((HasEssentia) be).getContainer(0);
			} else {
				return new EssentiaContainer();
			}
		}, new EssentiaContainer());
	}

}
