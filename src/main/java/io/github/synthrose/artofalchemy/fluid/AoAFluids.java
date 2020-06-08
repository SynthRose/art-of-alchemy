package io.github.synthrose.artofalchemy.fluid;

import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import io.github.synthrose.artofalchemy.essentia.Essentia;
import io.github.synthrose.artofalchemy.essentia.RegistryEssentia;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class AoAFluids {
	
	public static FlowableFluid ALKAHEST;
	public static FlowableFluid ALKAHEST_FLOWING;
	public static final Map<Essentia, FlowableFluid> ESSENTIA_FLUIDS = new HashMap<>();
	public static final Map<Essentia, FlowableFluid> ESSENTIA_FLUIDS_FLOWING = new HashMap<>();

	public static void registerFluids() {
		ALKAHEST 			= register("alkahest", new FluidAlkahest.Still());
		ALKAHEST_FLOWING	= register("flowing_alkahest", new FluidAlkahest.Flowing());
		
		// Register essentia fluids; add-on essentia fluids will be registered to THEIR namespace
		RegistryEssentia.INSTANCE.forEach((Essentia essentia, Identifier id) -> {
			Identifier stillId = new Identifier(id.getNamespace(), "essentia_" + id.getPath());
			Identifier flowId = new Identifier(id.getNamespace(), "flowing_essentia_" + id.getPath());
			ESSENTIA_FLUIDS.put(essentia, register(stillId, new FluidEssentia.Still(essentia)));
			ESSENTIA_FLUIDS_FLOWING.put(essentia, register(flowId, new FluidEssentia.Flowing(essentia)));
		});
	}
	
	public static FlowableFluid register(String name, FlowableFluid fluid) {
		return register(ArtOfAlchemy.id(name), fluid);
	}
	
	public static FlowableFluid register(Identifier id, FlowableFluid fluid) {
		return Registry.register(Registry.FLUID, id, fluid);
	}
	
}
