package io.github.synthrose.artofalchemy.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class RendererFluid {
	
	public static void setupFluidRendering(Fluid still, Fluid flowing, Identifier texture, int color) {
		Identifier stillTexture = new Identifier(texture.getNamespace(), "block/" + texture.getPath() + "_still");
		Identifier flowTexture = new Identifier(texture.getNamespace(), "block/" + texture.getPath() + "_flow");
		
		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX).register((atlas, registry) -> {
			registry.register(stillTexture);
			registry.register(flowTexture);
		});
		
		Identifier fluidId = Registry.FLUID.getId(still);
		Identifier listenerId = new Identifier(fluidId.getNamespace(), fluidId.getPath() + "_reload_listener");
		Sprite[] sprites = { null, null };
		
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
			.registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			
			@Override
			public void apply(ResourceManager manager) {
				Function<Identifier, Sprite> atlas = MinecraftClient.getInstance()
						.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
				sprites[0] = atlas.apply(stillTexture);
				sprites[1] = atlas.apply(flowTexture);
			}
			
			@Override
			public Identifier getFabricId() {
				return listenerId;
			}
			
		});
		
		FluidRenderHandler renderHandler = new FluidRenderHandler() {
			
			@Override
			public Sprite[] getFluidSprites(BlockRenderView view, BlockPos pos, FluidState state) {
				return sprites;
			}
			
			@Override
			public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state) {
				return color;
			}
			
		};
		
		FluidRenderHandlerRegistry.INSTANCE.register(still, renderHandler);
		FluidRenderHandlerRegistry.INSTANCE.register(flowing, renderHandler);
	}
	
	public static void setupFluidRendering(Fluid still, Fluid flowing, Identifier texture) {
		setupFluidRendering(still, flowing, texture, -1);
	}
	
	public static void markTranslucent(Fluid... fluids) {
		BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), fluids);
	}
}
