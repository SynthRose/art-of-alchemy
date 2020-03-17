package net.synthrose.artofalchemy.render;

import java.util.Map.Entry;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.synthrose.artofalchemy.block.AoABlocks;
import net.synthrose.artofalchemy.essentia.Essentia;
import net.synthrose.artofalchemy.essentia.RegistryEssentia;
import net.synthrose.artofalchemy.fluid.AoAFluids;
import net.synthrose.artofalchemy.item.AoAItems;


public class AoARenderers {
	
	public static void registerRenderers() {
		RendererFluid.setupFluidRendering(AoAFluids.ALKAHEST, AoAFluids.ALKAHEST_FLOWING,
    			new Identifier("minecraft", "water"), 0xAA0077);
    	RendererFluid.markTranslucent(AoAFluids.ALKAHEST, AoAFluids.ALKAHEST_FLOWING);
    	
    	RegistryEssentia.INSTANCE.forEach((Essentia essentia) -> {
    		Fluid still = AoAFluids.ESSENTIA_FLUIDS.get(essentia);
    		Fluid flowing = AoAFluids.ESSENTIA_FLUIDS_FLOWING.get(essentia);
    		RendererFluid.setupFluidRendering(still, flowing, new Identifier("minecraft", "water"), essentia.getColor());
    		RendererFluid.markTranslucent(still, flowing);
    	});
    	
    	ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) ->
			0xAA0077, AoABlocks.DISSOLVER);
    	
    	
    	for (Entry<Essentia, Item> entry : AoAItems.ESSENTIA_VESSELS.entrySet()) {
    		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
        		if (tintIndex == 0) {
        			if (entry.getKey() == null) {
        				return 0xAA0077;
        			} else {
        				int color = entry.getKey().getColor();
        				int r = ((color >> 16) & 0xFF);
        				int g = ((color >> 8) & 0xFF);
        				int b = (color & 0xFF);
        				r *= 1.1;
        				g *= 1.1;
        				b *= 1.1;
        				r = MathHelper.clamp(r, 0, 0xFF);
        				g = MathHelper.clamp(g, 0, 0xFF);
        				b = MathHelper.clamp(b, 0, 0xFF);
        				return ((r << 16) + (g << 8) + b);
        			}
        		} else {
        			return 0xFFFFFF;
        		}
        	}, entry.getValue());
    	}
    	
//    	ItemRendererRegistry.INSTANCE.register(AoAItems.ESSENTIA_VESSELS.get(null),
//    			RendererItemEssentiaVessel::new);
    	
	}

}
