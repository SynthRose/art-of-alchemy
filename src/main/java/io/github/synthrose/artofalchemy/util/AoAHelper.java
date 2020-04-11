package io.github.synthrose.artofalchemy.util;

import io.github.synthrose.artofalchemy.item.ItemAlchemyFormula;
import io.github.synthrose.artofalchemy.item.ItemJournal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

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

	public static Vec3i integerColor(int color) {
		int r = (color & 0xFF0000) >> 16;
		int g = (color & 0x00FF00) >> 8;
		int b = (color & 0x0000FF);
		return new Vec3i(r, g, b);
	}

	public static Vec3d decimalColor(int color) {
		double r = (color & 0xFF0000) >> 16;
		double g = (color & 0x00FF00) >> 8;
		double b = (color & 0x0000FF);
		return new Vec3d(r / 0xFF, g / 0xFF, b / 0xFF);
	}

	public static int combineColor(Vec3d color) {
		int r = (int) (color.getX() * 0xFF);
		int g = (int) (color.getY() * 0xFF);
		int b = (int) (color.getZ() * 0xFF);
		return (r << 16) | (g << 8) | (b);
	}

	public static int combineColor(Vec3i color) {
		return (color.getX() << 16) | (color.getY() << 8) | (color.getZ());
	}
	
}
