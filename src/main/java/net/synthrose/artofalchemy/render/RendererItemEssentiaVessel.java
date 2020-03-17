package net.synthrose.artofalchemy.render;

import java.util.function.Function;

import net.arno.client.api.ItemRenderer;
import net.arno.client.api.SpriteRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.synthrose.artofalchemy.ArtOfAlchemy;
import net.synthrose.artofalchemy.essentia.Essentia;
import net.synthrose.artofalchemy.essentia.EssentiaContainer;
import net.synthrose.artofalchemy.essentia.RegistryEssentia;
import net.synthrose.artofalchemy.item.ItemEssentiaVessel;

public class RendererItemEssentiaVessel extends ItemRenderer<ItemEssentiaVessel> {
	
	public RendererItemEssentiaVessel(Item item) {
		super(item);
	}
	
	@Override
	public void render(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
			
		EssentiaContainer container = EssentiaContainer.of(stack);
		Identifier frameId = ArtOfAlchemy.id("item/essentia_vessel_frame");
		Identifier liquidId = ArtOfAlchemy.id("item/essentia_vessel_liquid");
		int liquidColor;
		if (!(stack.getItem() instanceof ItemEssentiaVessel) || ((ItemEssentiaVessel) stack.getItem()).TYPE == null) {
			liquidColor = 0xAA00FF;
		} else {
			liquidColor = ((ItemEssentiaVessel) stack.getItem()).TYPE.getColor();
		}
		
		matrices.push();
		matrices.scale(1.0f, 1.0f, 1.0f);
		matrices.translate(0.0f, 0.0f, 0.0f);

		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(360 - Direction.NORTH.asRotation()));
		Function<Identifier, Sprite> atlas = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
		Sprite frameSprite = atlas.apply(frameId);
		Sprite liquidSprite = atlas.apply(liquidId);
		
		int c = 0xFFFFFF;
		int r = ((c >> 16) & 0xFF);
		int g = ((c >> 8) & 0xFF);
		int b = (c & 0xFF);
		int a = 0xFF;
		int l = 0xF000F0;
		
		SpriteRenderer.beginPass()
			.setup(vertexConsumers, matrices, RenderLayer.getTranslucentNoCrumbling())
			.position(0, 0, 1, 1, 1)
			.sprite(frameSprite)
			.overlay(0, 1)
			.color(r, g, b)
			.alpha(a)
			.light(l)
			.normal(0, 1, 1)
			.next();
		
		r = ((liquidColor >> 16) & 0xFF);
		g = ((liquidColor >> 8) & 0xFF);
		b = (liquidColor & 0xFF);
		SpriteRenderer.beginPass()
			.setup(vertexConsumers, matrices, RenderLayer.getTranslucentNoCrumbling())
			.position(0, 0, 1, 1, 1)
			.sprite(liquidSprite)
			.overlay(0, 1)
			.color(r, g, b)
			.alpha(a)
			.light(l)
			.normal(0, 1, 1)
			.next();
		
		matrices.pop();
		
	}
	
//	@Override
//	public void render(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
//		FluidStorage storage = FluidStorage.of(stack);
//
//		FluidCompartment compartment = storage.getInvHolder(stack.getTag().getInt("selected"));
//
//		Fluid fluid;
//
//		if (compartment == null || compartment.getType() == Fluids.EMPTY) return;
//		else fluid = compartment.getType();
//
//		matrices.push();
//
//		matrices.translate(0.125f, -0.4f, 0);
//		matrices.scale(0.35f, 0.7f, 0);
//
//		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(360 - Direction.NORTH.asRotation()));
//
//		Sprite sprite = FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidSprites(null, null, fluid.getDefaultState())[0];
//
//		PlayerEntity player = MinecraftClient.getInstance().player;
//
//		int c = FluidUtilities.colorOf(player, fluid);
//		int r = ((c >> 16) & 0xFF);
//		int g = ((c >> 8) & 0xFF);
//		int b = (c & 0xFF);
//		int a = 0xff;
//		int l = 15728880;
//
//		SpriteRenderer.beginPass()
//				.setup(vertexConsumers, matrices, RenderLayer.getTranslucentNoCrumbling())
//				.position(0, 0, 1, 1, 1)
//				.sprite(sprite)
//				.overlay(0, 10)
//				.color(r, g, b)
//				.alpha(a)
//				.light(l)
//				.normal(0, 1, 1)
//				.next();
//
//		matrices.pop();
//	}

}
