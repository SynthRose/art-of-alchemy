package net.synthrose.artofalchemy;

import java.util.Random;

import net.minecraft.util.math.MathHelper;

public class AoAHelper {
	
	public static int stochasticRound(double x) {
		Random r = new Random();
		double frac = MathHelper.fractionalPart(x);
		int rounding = r.nextDouble() >= frac ? 0 : 1;
		return (int) (Math.floor(x) + rounding);
	}
	
}
