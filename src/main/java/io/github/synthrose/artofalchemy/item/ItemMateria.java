package io.github.synthrose.artofalchemy.item;

import io.github.synthrose.artofalchemy.util.MateriaRank;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMateria extends Item {
	
	private final MateriaRank rank;

	public ItemMateria(Settings settings, MateriaRank rank) {
		super(settings.rarity(rank.rarity));
		this.rank = rank;
	}
	
	public MateriaRank getRank() {
		return rank;
	}
	
	public int getTier() {
		if (rank == null) {
			return 0;
		} else {
			return rank.tier;
		}
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
	      return (stack.hasEnchantments() || getTier() >= 6);
	}

}
