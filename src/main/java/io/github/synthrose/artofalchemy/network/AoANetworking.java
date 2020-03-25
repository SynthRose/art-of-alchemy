package io.github.synthrose.artofalchemy.network;

import java.util.stream.Stream;

import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;
import io.github.synthrose.artofalchemy.essentia.EssentiaStack;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AoANetworking {

	public static final Identifier ESSENTIA_PACKET = ArtOfAlchemy.id("update_essentia");
	public static final Identifier ESSENTIA_PACKET_REQ = ArtOfAlchemy.id("update_essentia_req");

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
	
	public static void sendEssentiaPacketWithRequirements(World world, BlockPos pos,
			int essentiaId, EssentiaContainer container, EssentiaStack required) {
		Stream<PlayerEntity> players = PlayerStream.watching(world, pos);
		
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeInt(essentiaId);
		data.writeCompoundTag(container.toTag());
		data.writeCompoundTag(required.toTag());
		data.writeBlockPos(pos);
		
		players.forEach(player -> {
			ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ESSENTIA_PACKET_REQ, data);
		});
	}

}
