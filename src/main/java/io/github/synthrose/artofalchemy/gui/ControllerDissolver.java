package io.github.synthrose.artofalchemy.gui;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.WBar.Direction;
import io.github.cottonmc.cotton.gui.widget.data.Alignment;
import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;
import io.github.synthrose.artofalchemy.essentia.HasEssentia;
import io.github.synthrose.artofalchemy.recipe.AoARecipes;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ControllerDissolver extends CottonCraftingController {
	
	final BlockPos pos;
	final WEssentiaPanel essentiaPanel;
	
	@SuppressWarnings("MethodCallSideOnly")
	public ControllerDissolver(int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx) {
		super(AoARecipes.DISSOLUTION, syncId, playerInventory, getBlockInventory(ctx), getBlockPropertyDelegate(ctx));
		
		pos = ctx.run((world, pos) -> pos, null);
		
		WGridPanel root = new WGridPanel(1);
		setRootPanel(root);
		root.setSize(162, 128 + 36);
		
		WSprite background = new WSprite(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/rune_bg.png"));
		root.add(background, 0, 0, 9 * 18, 5 * 18);
		
		WItemSlot inSlot = WItemSlot.of(blockInventory, 0);
		root.add(inSlot, 2 * 18, 2 * 18);
		
		WBar tankBar = new WBar(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/tank_empty.png"),
				new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/tank_full.png"),
				0, 1, Direction.UP);
		tankBar.withTooltip("gui." + ArtOfAlchemy.MOD_ID + ".alkahest_tooltip");
		root.add(tankBar, 0, 18, 2 * 18, 3 * 18);
		
		WBar progressBar = new WBar(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/progress_off.png"),
				new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/progress_magenta.png"), 2, 3, Direction.RIGHT);
		root.add(progressBar, 3 * 18, 2 * 18, 3 * 18, 18);
		
		WLabel title = new WLabel(new TranslatableText("block.artofalchemy.dissolution_chamber"), WLabel.DEFAULT_TEXT_COLOR);
		title.setAlignment(Alignment.CENTER);
		root.add(title, 0, -1, 9 * 18, 18);
		
		WDynamicLabel alert = new WDynamicLabel(() -> {
			switch (propertyDelegate.get(4)) {
			case 2:
				return I18n.translate("gui." + ArtOfAlchemy.MOD_ID + ".alkahest_warning");
			case 3:
				return I18n.translate("gui." + ArtOfAlchemy.MOD_ID + ".buffer_warning");
			default:
				return "";
			}
		}, 0xFF5555);
		alert.setAlignment(Alignment.CENTER);
		root.add(alert, 0, -1 * 18, 9 * 18, 18);
		
		EssentiaContainer essentia = getEssentia(ctx);
		essentiaPanel = new WEssentiaPanel(essentia);
		root.add(essentiaPanel, 6 * 18 - 2, 18 - 7, 3 * 18, 4 * 18);
		
		root.add(this.createPlayerInventoryPanel(), 0, 5 * 18);
		
		root.validate(this);
		
	}
	
	public void updateEssentia(int essentiaId, EssentiaContainer essentia, BlockPos pos) {
		if (pos.equals(this.pos)) {
			essentiaPanel.updateEssentia(essentia);
		}
	}

	private static EssentiaContainer getEssentia(ScreenHandlerContext ctx) {
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
