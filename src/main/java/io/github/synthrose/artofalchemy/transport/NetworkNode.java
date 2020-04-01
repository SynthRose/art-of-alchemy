package io.github.synthrose.artofalchemy.transport;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Optional;

public class NetworkNode {

    private World world;
    private NetworkNode.Type type;
    private BlockPos pos;
    private Direction dir;

    public NetworkNode(World world, Type type, BlockPos pos, Direction dir) {
        this.world = world;
        this.type = type;
        this.pos = pos;
        this.dir = dir;
    }

    public NetworkNode(World world, Type type, BlockPos pos) {
        this(world, type, pos, null);
    }

    public NetworkNode.Type getType() {
        return type;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Optional<Direction> getDirection() {
        return Optional.of(dir);
    }

    public BlockEntity getBlockEntity() {
        if (dir != null) {
            return world.getBlockEntity(pos.offset(dir));
        } else {
            return world.getBlockEntity(pos);
        }
    }

    public enum Type implements StringIdentifiable {
        PULL,
        PUSH,
        PASSIVE;

        private final String string;

        Type() {
            this.string = toString().toLowerCase();
        }

        @Override
        public String asString() {
            return string;
        }
    }

}
