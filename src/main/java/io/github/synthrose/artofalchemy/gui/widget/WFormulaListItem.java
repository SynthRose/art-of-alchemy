package io.github.synthrose.artofalchemy.gui.widget;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.Alignment;
import io.github.synthrose.artofalchemy.item.ItemJournal;
import io.github.synthrose.artofalchemy.network.AoAClientNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class WFormulaListItem extends WPlainPanel {

	private boolean selected = false;
	private ItemStack journal;
	private Item formula = Items.AIR;
	private final WItemScalable itemDisplay;
	private final WLabel formulaLabel;
	private final WLabel typeLabel;
	private final WButton setButton;

	@SuppressWarnings("MethodCallSideOnly")
	public WFormulaListItem(ItemStack journal, Item formula) {
		super();
		this.journal = journal;

		List<ItemStack> itemStackList = new ArrayList<>();
		itemStackList.add(new ItemStack(formula));
		itemDisplay = new WItemScalable(itemStackList);
		itemDisplay.setParent(this);
		add(itemDisplay, -2, 0, 16, 16);

		formulaLabel = new WLabel(itemStackList.get(0).getName());
		formulaLabel.setAlignment(Alignment.LEFT);
		formulaLabel.setParent(this);
		add(formulaLabel, 16, 0);

		typeLabel = new WLabel(new TranslatableText("gui.artofalchemy.formula_type.alchemy").formatted(Formatting.DARK_PURPLE));
		typeLabel.setAlignment(Alignment.LEFT);
		typeLabel.setParent(this);
		add(typeLabel, 16, 8);

		setButton = new WButton(new LiteralText("✔"));
		setButton.setAlignment(Alignment.CENTER);
		setButton.setParent(this);
		add(setButton, 7 * 18 - 1, -3, 20, 20);
		setButton.setOnClick(() -> {
			ItemJournal.setFormula(this.journal, this.formula);
			AoAClientNetworking.sendJournalSelectPacket(Registry.ITEM.getId(this.formula));
		});

		refresh(journal, formula);
	}

	public WFormulaListItem(ItemStack journal) {
		this(journal, Items.AIR);
	}

	public void refresh(ItemStack journal, Item formula) {
		this.formula = formula;
		itemDisplay.getItems().clear();
		itemDisplay.getItems().add(new ItemStack(formula));
		formulaLabel.setText(itemDisplay.getItems().get(0).getName());
		selected = ItemJournal.getFormula(journal) == formula;
		setButton.setEnabled(!selected);
	}
}
