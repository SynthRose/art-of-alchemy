package io.github.synthrose.artofalchemy;

import io.github.synthrose.artofalchemy.item.ItemAlchemyFormula;
import io.github.synthrose.artofalchemy.item.ItemJournal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class AoAHelper {
	
	public static int stochasticRound(double x) {
		Random r = new Random();
		double frac = MathHelper.fractionalPart(x);
		int rounding = r.nextDouble() >= frac ? 0 : 1;
		return (int) (Math.floor(x) + rounding);
	}

	public static Item getTarget(ItemStack stack) {
		if (stack.getItem() instanceof ItemAlchemyFormula) {
			return ItemAlchemyFormula.getFormula(stack);
		} else if (stack.getItem() instanceof ItemJournal) {
			return ItemJournal.getFormula(stack);
		} else {
			return stack.getItem();
		}
	}
	
}
