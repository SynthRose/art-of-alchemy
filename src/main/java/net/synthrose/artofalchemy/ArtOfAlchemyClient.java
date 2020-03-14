package net.synthrose.artofalchemy;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;
import net.synthrose.artofalchemy.fluid.AoAFluids;
import net.synthrose.artofalchemy.fluid.RendererFluid;
import net.synthrose.artofalchemy.gui.AoAGuis;

public class ArtOfAlchemyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
    	AoAGuis.registerScreens();
    	RendererFluid.setupFluidRendering(AoAFluids.ALKAHEST, AoAFluids.ALKAHEST_FLOWING,
    			new Identifier("minecraft", "water"), 0xAA0066);
    	RendererFluid.markTranslucent(AoAFluids.ALKAHEST, AoAFluids.ALKAHEST_FLOWING);
    }

}