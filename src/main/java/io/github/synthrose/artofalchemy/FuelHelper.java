package io.github.synthrose.artofalchemy;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FuelHelper {

	public static boolean isFuel(Item item) {
		return AbstractFurnaceBlockEntity.createFuelTimeMap().containsKey(item);
	}
	
	public static boolean isFuel(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		} else {
			return isFuel(stack.getItem());
		}
	}
	
	public static int fuelTime(Item item) {
		return AbstractFurnaceBlockEntity.createFuelTimeMap().getOrDefault(item, 0);
	}
	
	public static int fuelTime(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		} else {
			return fuelTime(stack.getItem());
		}
	}

}
