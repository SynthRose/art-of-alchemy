package net.synthrose.artofalchemy.render;

import net.arno.client.api.ItemRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.synthrose.artofalchemy.essentia.EssentiaContainer;
import net.synthrose.artofalchemy.item.ItemEssentiaVessel;

public class RendererItemEssentiaVessel extends ItemRenderer<ItemEssentiaVessel> {

	public RendererItemEssentiaVessel(Item item) {
		super(item);
	}
	
	@Override
	public void render(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
//		EssentiaContainer container = EssentiaContainer.of(stack);
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
