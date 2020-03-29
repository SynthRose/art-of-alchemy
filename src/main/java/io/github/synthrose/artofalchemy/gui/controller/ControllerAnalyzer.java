package io.github.synthrose.artofalchemy.gui.controller;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.Alignment;
import io.github.synthrose.artofalchemy.AoAHelper;
import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import io.github.synthrose.artofalchemy.item.AbstractItemFormula;
import io.github.synthrose.artofalchemy.item.AoAItems;
import io.github.synthrose.artofalchemy.item.ItemAlchemyFormula;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class ControllerAnalyzer extends CottonCraftingController {

	Inventory inventory = new BasicInventory(4) {
		@Override
		public boolean isValidInvStack(int slot, ItemStack stack) {
			if (slot == 0) {
				return stack.getItem() == Items.PAPER;
			} else if (slot == 1) {
				return stack.getItem() == Items.INK_SAC;
			} else if (slot == 2) {
				return true;
			} else {
				return false;
			}
		}
	};

	public ControllerAnalyzer(int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx) {
		super(null, syncId, playerInventory);
		blockInventory = inventory;

		WGridPanel root = new WGridPanel(1);
		setRootPanel(root);
		root.setSize(160, 128 + 36);
		
		WSprite background = new WSprite(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/rune_bg.png"));
		root.add(background, 0, 0, 9 * 18, 5 * 18);

		WSprite paperIcon = new WSprite(new Identifier("minecraft", "textures/item/paper.png"));
		root.add(paperIcon, 2 * 18 + 4, 2 * 18, 16, 16);

		WItemSlot paperSlot = WItemSlot.of(inventory, 0);
		root.add(paperSlot, 2 * 18 + 4, 2 * 18);

		WSprite inkIcon = new WSprite(new Identifier("minecraft", "textures/item/ink_sac.png"));
		root.add(inkIcon, 4 * 18, 1 * 18 - 4, 16, 16);
		
		WItemSlot inkSlot = WItemSlot.of(inventory, 1);
		root.add(inkSlot, 4 * 18, 1 * 18 - 4);

		WSprite targetIcon = new WSprite(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/target.png"));
		root.add(targetIcon, 4 * 18, 3 * 18 + 4, 18, 18);

		WItemSlot targetSlot = WItemSlot.of(inventory, 2);
		root.add(targetSlot, 4 * 18, 3 * 18 + 4);

		WItemSlot outSlot = WItemSlot.outputOf(inventory, 3);
		root.add(outSlot, 6 * 18, 2 * 18);
		
		WSprite arrow = new WSprite(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/progress_off.png"));
		root.add(arrow, 3 * 18, 2 * 18, 3 * 18, 18);
		
		WLabel title = new WLabel(new TranslatableText("block.artofalchemy.analysis_desk"),
				WLabel.DEFAULT_TEXT_COLOR);
		title.setAlignment(Alignment.CENTER);
		root.add(title, 0, -1, 9 * 18, 18);
		
		root.add(this.createPlayerInventoryPanel(), 0, 5 * 18);
		
		root.validate(this);

	}

	@Override
	public void close(PlayerEntity player) {
		inventory.removeInvStack(3);
		dropInventory(player, world, inventory);
		super.close(player);
	}

	@Override
	public ItemStack onSlotClick(int slotNumber, int button, SlotActionType action, PlayerEntity player) {
		int outputBefore = inventory.getInvStack(3).getCount();
		ItemStack stack = super.onSlotClick(slotNumber, button, action, player);
		if (stack == null) {
			stack = ItemStack.EMPTY;
		}
		int outputAfter = inventory.getInvStack(3).getCount();
		if (!stack.isEmpty() && slotNumber == 39 && outputAfter < outputBefore) {
			inventory.getInvStack(0).decrement(outputBefore - outputAfter);
			inventory.getInvStack(1).decrement(outputBefore - outputAfter);
		}
		updateRecipe();
		return stack;
	}

	public void updateRecipe() {
		if (!world.isClient) {
			if (inventory.getInvStack(0).getItem() == Items.PAPER && inventory.getInvStack(1).getItem() == Items.INK_SAC
					&& !inventory.getInvStack(2).isEmpty()) {
				ItemStack formula = new ItemStack(AoAItems.ALCHEMY_FORMULA);
				ItemAlchemyFormula.setFormula(formula, AoAHelper.getTarget(inventory.getInvStack(2)));
				inventory.setInvStack(3, formula);
			} else {
				inventory.setInvStack(3, ItemStack.EMPTY);
			}
			inventory.markDirty();
			((ServerPlayerEntity) playerInventory.player).networkHandler.sendPacket(
					new ScreenHandlerSlotUpdateS2CPacket(syncId, 39, inventory.getInvStack(3)));
		}
	}

}
