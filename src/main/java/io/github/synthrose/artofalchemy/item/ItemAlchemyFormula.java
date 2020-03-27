package io.github.synthrose.artofalchemy.item;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class ItemAlchemyFormula extends AbstractItemFormula<Item> {

	public ItemAlchemyFormula(Settings settings) {
		super(settings);
	}

	public static Item getFormula(ItemStack stack) {
		CompoundTag tag = stack.hasTag() ? stack.getTag() : new CompoundTag();
		if (tag.contains("formula")) {
			Identifier id = new Identifier(tag.getString("formula"));
			return Registry.ITEM.get(id);
		} else {
			return Items.AIR;
		}
	}

	public static void setFormula(ItemStack stack, Item formula) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.put("formula", StringTag.of(Registry.ITEM.getId(formula).toString()));
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext ctx) {
		tooltip.add(new TranslatableText(getFormula(stack).getTranslationKey()).formatted(Formatting.GRAY));
		super.appendTooltip(stack, world, tooltip, ctx);
	}
	

}
