package net.synthrose.artofalchemy.network;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.synthrose.artofalchemy.essentia.EssentiaContainer;
import net.synthrose.artofalchemy.gui.EssentiaScreen;

public class AoAClientNetworking {
	
	public static void initializeClientNetworking() {
		ClientSidePacketRegistry.INSTANCE.register(AoANetworking.ESSENTIA_PACKET,
			(ctx, data) -> {
				int essentiaId = data.readInt();
				CompoundTag tag = data.readCompoundTag();
				BlockPos pos = data.readBlockPos();
				ctx.getTaskQueue().execute(() -> {
					EssentiaContainer container = new EssentiaContainer(tag);
					Screen screen = MinecraftClient.getInstance().currentScreen;
					if (screen instanceof EssentiaScreen) {
						((EssentiaScreen) screen).updateEssentia(essentiaId, container, pos);
					}
				});
			});
	}

}
