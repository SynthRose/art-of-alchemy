package io.github.synthrose.artofalchemy.gui.handler;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.WBar.Direction;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import io.github.synthrose.artofalchemy.blockentity.BlockEntitySynthesizer;
import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;
import io.github.synthrose.artofalchemy.essentia.EssentiaStack;
import io.github.synthrose.artofalchemy.transport.HasEssentia;
import io.github.synthrose.artofalchemy.gui.widget.WEssentiaPanel;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class HandlerSynthesizer extends SyncedGuiDescription {
	
	final BlockPos pos;
	final WEssentiaPanel essentiaPanel;

	@SuppressWarnings("MethodCallSideOnly")
	public HandlerSynthesizer(int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx) {
		super(AoAHandlers.SYNTHESIZER, syncId, playerInventory, getBlockInventory(ctx), getBlockPropertyDelegate(ctx));
		
		pos = ctx.run((world, pos) -> pos, null);
		
		WGridPanel root = new WGridPanel(1);
		setRootPanel(root);
		root.setSize(162, 128 + 36);
		
		WSprite background = new WSprite(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/rune_bg.png"));
		root.add(background, 0, 0, 9 * 18, 5 * 18);
		
		WItemSlot inSlot = WItemSlot.of(blockInventory, 0);
		root.add(inSlot, 4 * 18, 18 - 4);
		
		WItemSlot outSlot = WItemSlot.outputOf(blockInventory, 1);
		root.add(outSlot, 6 * 18 + 4, 2 * 18);
		
		WSprite targetIcon = new WSprite(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/target.png"));
		root.add(targetIcon, 4 * 18 + 1, 3 * 18 + 5, 18, 18);
		
		WItemSlot targetSlot = WItemSlot.of(blockInventory, 2);
		root.add(targetSlot, 4 * 18, 3 * 18 + 4);
		
		WBar progressBar = new WBar(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/progress_off.png"),
				new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/progress_cyan.png"), 0, 1, Direction.RIGHT);
		root.add(progressBar, 3 * 18, 2 * 18 + 1, 3 * 18, 18);
		
		WLabel title = new WLabel(new TranslatableText("block.artofalchemy.synthesis_table"), WLabel.DEFAULT_TEXT_COLOR);
		title.setHorizontalAlignment(HorizontalAlignment.CENTER);
		root.add(title, 0, -1, 9 * 18, 18);

		WDynamicLabel alert = new WDynamicLabel(() -> {
			switch (propertyDelegate.get(2)) {
			case 2:
				return I18n.translate("gui." + ArtOfAlchemy.MOD_ID + ".target_warning");
			case 3:
				return I18n.translate("gui." + ArtOfAlchemy.MOD_ID + ".materia_warning");
			case 4:
				return I18n.translate("gui." + ArtOfAlchemy.MOD_ID + ".essentia_warning");
			case 5:
				return I18n.translate("gui." + ArtOfAlchemy.MOD_ID + ".container_warning");
			case 6:
				return I18n.translate("gui." + ArtOfAlchemy.MOD_ID + ".tier_warning");
			default:
				return "";
			}
		}, 0xFF5555);
		alert.setAlignment(HorizontalAlignment.CENTER);
		root.add(alert, 0, -1 * 18, 9 * 18, 18);

		essentiaPanel = new WEssentiaPanel(getEssentia(ctx), getRequirements(ctx));
		root.add(essentiaPanel, 2, 18 - 7, 3 * 18, 4 * 18);
		
		root.add(this.createPlayerInventoryPanel(), 0, 5 * 18);
		
		root.validate(this);
		
	}
	
	public void updateEssentia(int essentiaId, EssentiaContainer essentia, BlockPos pos) {
		if (pos.equals(this.pos)) {
			essentiaPanel.updateEssentia(essentia);
		}
	}
	
	public void updateEssentia(int essentiaId, EssentiaContainer essentia,
                               EssentiaStack required, BlockPos pos) {
		if (pos.equals(this.pos)) {
			essentiaPanel.updateEssentia(essentia, required);
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
	
	private static EssentiaStack getRequirements(ScreenHandlerContext ctx) {
		return ctx.run((world, pos) -> {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof BlockEntitySynthesizer) {
				return ((BlockEntitySynthesizer) be).getRequirements();
			} else {
				return new EssentiaStack();
			}
		}, new EssentiaStack());
	}

}
