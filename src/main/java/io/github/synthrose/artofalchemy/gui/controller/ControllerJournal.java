package io.github.synthrose.artofalchemy.gui.controller;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Alignment;
import io.github.synthrose.artofalchemy.AoAHelper;
import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import io.github.synthrose.artofalchemy.gui.widget.WFormulaList;
import io.github.synthrose.artofalchemy.item.AbstractItemFormula;
import io.github.synthrose.artofalchemy.item.ItemJournal;
import io.github.synthrose.artofalchemy.network.AoAClientNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ControllerJournal extends CottonCraftingController {

	Hand hand;
	WTextField searchBar;
	// When LibGUI fixes the dedi server crash, these hacks will be removed
	WButton clearButton = null;
	WFormulaList formulaList;
	ItemStack journal;

	Inventory inventory = new BasicInventory(1) {
		@Override
		public boolean isValidInvStack(int slot, ItemStack stack) {
			return (stack.getItem() instanceof AbstractItemFormula) && !(stack.getItem() instanceof ItemJournal);
		}
	};

	@SuppressWarnings("MethodCallSideOnly")
	public ControllerJournal(int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx, Hand hand) {
		super(null, syncId, playerInventory);
		blockInventory = inventory;

		this.hand = hand;
		this.journal = playerInventory.player.getStackInHand(hand);

		WGridPanel root = new WGridPanel(1);
		setRootPanel(root);
		root.setSize(160, 128 + 18 * 5);

		WSprite slotIcon = new WSprite(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/add_formula.png"));
		root.add(slotIcon, 1, 16, 16, 16);

		WItemSlot slot = WItemSlot.of(inventory, 0);
		root.add(slot, 1, 16);

		searchBar = new WTextField() {
			public void setSize(int x, int y) {
				super.setSize(x, y);
			}

			@Override
			public void onKeyPressed(int ch, int key, int modifiers) {
				super.onKeyPressed(ch, key, modifiers);
				formulaList.refresh(journal, this.text);
			}
		};
		root.add(searchBar, 22, 14, 6 * 18 + 6, 12);

		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			clearButton = new WButton(new LiteralText("❌"));
			clearButton.setAlignment(Alignment.CENTER);
			clearButton.setEnabled(ItemJournal.getFormula(journal) != Items.AIR);
			clearButton.setOnClick(() -> {
				ItemJournal.setFormula(this.journal, Items.AIR);
				AoAClientNetworking.sendJournalSelectPacket(Registry.ITEM.getId(Items.AIR));
			});
			root.add(clearButton, 7 * 18 + 14, 14, 20, 20);
		}
		
		WSprite background = new WSprite(new Identifier(ArtOfAlchemy.MOD_ID, "textures/gui/rune_bg.png"));
		root.add(background, 0, 2 * 18 + 10, 9 * 18, 5 * 18);

		formulaList = new WFormulaList(journal);
		formulaList.refresh();
		root.add(formulaList, 0, 2 * 18, 9 * 18 - 2, 6 * 17 - 1);

		WLabel title = new WLabel(journal.getName());
		title.setAlignment(Alignment.CENTER);
		root.add(title, 0, 0, 9 * 18, 18);
		
		root.add(this.createPlayerInventoryPanel(), 0, 8 * 18);
		
		root.validate(this);
	}

	@Override
	public void close(PlayerEntity player) {
		dropInventory(player, world, inventory);
		super.close(player);
	}

	@Override
	public ItemStack onSlotClick(int slotNumber, int button, SlotActionType action, PlayerEntity player) {
		if (slotNumber >= 0 && slotNumber < slots.size()) {
			Slot slot = getSlot(slotNumber);
			if (slot != null) {
				if (slot.getStack().getItem() instanceof ItemJournal) {
					return ItemStack.EMPTY;
				}
			}
		}
		ItemStack stack = super.onSlotClick(slotNumber, button, action, player);
		tryAddPage();
		refresh();
		return stack;
	}

	public void tryAddPage() {
		ItemStack stack = inventory.getInvStack(0);
		if (stack.getItem() instanceof AbstractItemFormula) {
			if (ItemJournal.addFormula(journal, AoAHelper.getTarget(stack))) {
				stack.decrement(1);
				inventory.markDirty();
				playerInventory.markDirty();
			}
		}
	}

	public void refresh() {
		if (journal.getItem() instanceof ItemJournal) {
			formulaList.refresh(journal, searchBar.getText());
			if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
				clearButton.setEnabled(ItemJournal.getFormula(journal) != Items.AIR);
			}
		} else {
			this.close(playerInventory.player);
		}
	}

}
