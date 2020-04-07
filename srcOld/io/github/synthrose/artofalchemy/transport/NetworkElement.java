package io.github.synthrose.artofalchemy.transport;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Set;

public interface NetworkElement {
    boolean hasNodes(World world, BlockPos pos);
    Set<NetworkNode> getNodes(World world, BlockPos pos);
    boolean isConnected(World world, BlockPos pos, Direction dir);
    boolean isConnected(World world, BlockPos pos, BlockPos other);
    Set<BlockPos> getConnections(World world, BlockPos pos);
}
