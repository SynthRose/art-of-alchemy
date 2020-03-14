package net.synthrose.artofalchemy.fluid;

import net.minecraft.fluid.BaseFluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.synthrose.artofalchemy.ArtOfAlchemy;

public class AoAFluids {
	
	public static BaseFluid ALKAHEST;
	public static BaseFluid ALKAHEST_FLOWING;

	public static void registerFluids() {
		ALKAHEST 			= register("alkahest", new FluidAlkahest.Still());
		ALKAHEST_FLOWING	= register("flowing_alkahest", new FluidAlkahest.Flowing());
	}
	
	public static BaseFluid register(String name, BaseFluid fluid) {
		return Registry.register(Registry.FLUID, new Identifier(ArtOfAlchemy.MOD_ID, name), fluid);
	}
	
}
