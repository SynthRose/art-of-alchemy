package io.github.synthrose.artofalchemy.gui.widget;

import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.synthrose.artofalchemy.item.ItemJournal;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class WFormulaList extends WListPanel<Item, WFormulaListItem> {

    protected ItemStack journal;
    private final Hand hand;

    public WFormulaList(ItemStack journal, Hand hand) {
        super(ItemJournal.getEntries(journal), () -> new WFormulaListItem(journal, hand), null);
        this.configurator = (formula, listItem) -> {
            listItem.refresh(this.journal, formula);
            listItem.setSize(8 * 18, 16);
        };
        this.hand = hand;
        this.cellHeight = 16;
        this.journal = journal;
    }

    @SuppressWarnings("MethodCallSideOnly")
    public void refresh(ItemStack journal, String filter) {
        this.journal = journal;
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            data = ItemJournal.getEntries(journal);
            data.sort((Item item1, Item item2) -> item1.getName().toString().compareToIgnoreCase(item2.getName().toString()));
            data.removeIf((item) -> {
                String lcFilter = filter.toLowerCase();
                if (item.getName().asString().toLowerCase().contains(lcFilter)) {
                    return false;
                } else return !Registry.ITEM.getId(item).getPath().contains(lcFilter);
            });
        }
        reconfigure();
        layout();
    }

    public void refresh() {
        refresh(this.journal, "");
    }

    protected void reconfigure() {
        for (Map.Entry<Item, WFormulaListItem> entry : configured.entrySet()) {
            configurator.accept(entry.getKey(), entry.getValue());
        }
    }

}
