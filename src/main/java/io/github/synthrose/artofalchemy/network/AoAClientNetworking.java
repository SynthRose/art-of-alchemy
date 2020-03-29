package io.github.synthrose.artofalchemy.network;

import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;
import io.github.synthrose.artofalchemy.essentia.EssentiaStack;
import io.github.synthrose.artofalchemy.gui.screen.EssentiaScreen;
import io.github.synthrose.artofalchemy.gui.screen.ScreenJournal;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class AoAClientNetworking {

	@Environment(EnvType.CLIENT)
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
		
		ClientSidePacketRegistry.INSTANCE.register(AoANetworking.ESSENTIA_PACKET_REQ,
				(ctx, data) -> {
					int essentiaId = data.readInt();
					CompoundTag essentiaTag = data.readCompoundTag();
					CompoundTag requiredTag = data.readCompoundTag();
					BlockPos pos = data.readBlockPos();
					ctx.getTaskQueue().execute(() -> {
						EssentiaContainer container = new EssentiaContainer(essentiaTag);
						EssentiaStack required = new EssentiaStack(requiredTag);
						Screen screen = MinecraftClient.getInstance().currentScreen;
						if (screen instanceof EssentiaScreen) {
							((EssentiaScreen) screen).updateEssentia(essentiaId, container, required, pos);
						}
					});
				});
	}

	public static void sendJournalSelectPacket(Identifier id) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeIdentifier(id);
		ClientSidePacketRegistry.INSTANCE.sendToServer(AoANetworking.JOURNAL_SELECT_PACKET, data);

		Screen screen = MinecraftClient.getInstance().currentScreen;
		if (screen instanceof ScreenJournal) {
			((ScreenJournal) screen).refresh();
		}
	}

}
