package net.synthrose.artofalchemy.fluid;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.fluid.BaseFluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.synthrose.artofalchemy.ArtOfAlchemy;
import net.synthrose.artofalchemy.EssentiaType;

public class AoAFluids {
	
	public static BaseFluid ALKAHEST;
	public static BaseFluid ALKAHEST_FLOWING;
	public static Map<EssentiaType, BaseFluid> ESSENTIA_FLUIDS = new HashMap<>();
	public static Map<EssentiaType, BaseFluid> ESSENTIA_FLUIDS_FLOWING = new HashMap<>();

	public static void registerFluids() {
		ALKAHEST 			= register("alkahest", new FluidAlkahest.Still());
		ALKAHEST_FLOWING	= register("flowing_alkahest", new FluidAlkahest.Flowing());
		
		for (EssentiaType essentia : EssentiaType.values()) {
			ESSENTIA_FLUIDS.put(essentia, register("essentia_" + essentia.getName(),
					new FluidEssentia.Still(essentia)));
			ESSENTIA_FLUIDS_FLOWING.put(essentia, register("flowing_essentia_" + essentia.getName(),
					new FluidEssentia.Flowing(essentia)));
		}
	}
	
	public static BaseFluid register(String name, BaseFluid fluid) {
		return Registry.register(Registry.FLUID, new Identifier(ArtOfAlchemy.MOD_ID, name), fluid);
	}
	
}
