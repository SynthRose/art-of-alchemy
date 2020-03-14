package net.synthrose.artofalchemy;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.synthrose.artofalchemy.fluid.AoAFluids;
import net.synthrose.artofalchemy.fluid.RendererFluid;
import net.synthrose.artofalchemy.gui.AoAGuis;

public class ArtOfAlchemyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
    	AoAGuis.registerScreens();
    	RendererFluid.setupFluidRendering(AoAFluids.ALKAHEST, AoAFluids.ALKAHEST_FLOWING,
    			new Identifier("minecraft", "water"), 0xAA0077);
    	RendererFluid.markTranslucent(AoAFluids.ALKAHEST, AoAFluids.ALKAHEST_FLOWING);
    	
    	for (EssentiaType essentia : EssentiaType.values()) {
    		Fluid still = AoAFluids.ESSENTIA_FLUIDS.get(essentia);
    		Fluid flowing = AoAFluids.ESSENTIA_FLUIDS_FLOWING.get(essentia);
    		RendererFluid.setupFluidRendering(still, flowing, new Identifier("minecraft", "water"), essentia.getColor());
    		RendererFluid.markTranslucent(still, flowing);
    	}
    }

}