package io.github.synthrose.artofalchemy.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ItemJournal extends AbstractItemFormula {

    public ItemJournal(Settings settings) {
        super(settings.maxCount(1));
    }

    public static Identifier getId() {
        return Registry.ITEM.getId(AoAItems.JOURNAL);
    }

    public static Item getFormula(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("selected")) {
            Identifier id = new Identifier(tag.getString("selected"));
            return Registry.ITEM.get(id);
        } else {
            return Items.AIR;
        }
    }

    public static void setFormula(ItemStack stack, Item formula) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put("selected", StringTag.of(Registry.ITEM.getId(formula).toString()));
    }

    public static void setFormula(ItemStack stack, Identifier formula) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put("selected", StringTag.of(formula.toString()));
    }

    public static ListTag getOrCreateEntriesTag(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("entries", 9)) {
            return tag.getList("entries", 8);
        } else {
            ListTag entries = new ListTag();
            tag.put("entries", entries);
            return entries;
        }
    }

    public static ListTag getEntriesTag(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("entries", 9)) {
            return tag.getList("entries", 8);
        } else {
            return null;
        }
    }

    public static List<Item> getEntries(ItemStack stack) {
        List<Item> list = new ArrayList<>();
        ListTag entries = getOrCreateEntriesTag(stack);
        for (int i = 0; i < entries.size(); i++) {
            Item item = Registry.ITEM.get(Identifier.tryParse(entries.getString(i)));
            if (item != Items.AIR) {
                list.add(item);
            }
        }
        return list;
    }

    public static boolean addFormula(ItemStack stack, Item formula) {
        ListTag entries = getOrCreateEntriesTag(stack);
        StringTag newEntry = StringTag.of(Registry.ITEM.getId(formula).toString());
        if (!entries.contains(newEntry) && formula != Items.AIR) {
            entries.add(newEntry);
            return true;
        } else {
            return false;
        }
    }

    public static boolean addFormula(ItemStack stack, Identifier formula) {
        ListTag entries = getOrCreateEntriesTag(stack);
        StringTag newEntry = StringTag.of(formula.toString());
        if (!entries.contains(newEntry) && formula.toString() != "minecraft:air") {
            entries.add(newEntry);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            ContainerProviderRegistry.INSTANCE.openContainer(getId(), user,
                    (packetByteBuf -> {
                        packetByteBuf.writeBlockPos(user.getBlockPos());
                        packetByteBuf.writeEnumConstant(hand);
                    }));
        }
        return super.use(world, user, hand);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext ctx) {
        int entryCount = getEntriesTag(stack) == null? 0 : getEntriesTag(stack).size();
        if (getFormula(stack) != Items.AIR) {
            tooltip.add(new TranslatableText(getFormula(stack).getTranslationKey()).formatted(Formatting.DARK_PURPLE));
        }
        tooltip.add(new TranslatableText("item.artofalchemy.alchemical_journal.tooltip_entries", entryCount).formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, ctx);
    }

}
