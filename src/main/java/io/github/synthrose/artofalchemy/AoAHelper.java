package io.github.synthrose.artofalchemy;

import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class AoAHelper {
	
	public static int stochasticRound(double x) {
		Random r = new Random();
		double frac = MathHelper.fractionalPart(x);
		int rounding = r.nextDouble() >= frac ? 0 : 1;
		return (int) (Math.floor(x) + rounding);
	}
	
}
