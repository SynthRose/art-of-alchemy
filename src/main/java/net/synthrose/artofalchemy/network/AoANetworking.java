package net.synthrose.artofalchemy.network;

import java.util.stream.Stream;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.synthrose.artofalchemy.ArtOfAlchemy;
import net.synthrose.artofalchemy.essentia.EssentiaContainer;

public class AoANetworking {

	public static final Identifier ESSENTIA_PACKET = ArtOfAlchemy.id("update_essentia");

	public static void initializeNetworking() {
		
	}
	
	public static void sendEssentiaPacket(World world, BlockPos pos,
			int essentiaId, EssentiaContainer container) {
		Stream<PlayerEntity> players = PlayerStream.watching(world, pos);
		
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeInt(essentiaId);
		data.writeCompoundTag(container.toTag());
		data.writeBlockPos(pos);
		
		players.forEach(player -> {
			ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ESSENTIA_PACKET, data);
		});
	}

}
