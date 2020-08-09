package io.github.synthrose.artofalchemy.blockentity;

import io.github.synthrose.artofalchemy.transport.NetworkNode;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;

import java.util.HashMap;
import java.util.Map;

public class BlockEntityPipe extends BlockEntity implements BlockEntityClientSerializable {

    private Map<Direction, IOFace> faces = new HashMap<>();

    public BlockEntityPipe() {
        super(AoABlockEntities.PIPE);
        for (Direction dir : Direction.values()) {
            faces.put(dir, IOFace.NONE);
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        for (Direction dir : Direction.values()) {
            tag.putString(dir.toString(), faces.get(Direction.UP).toString());
        }
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        for (Direction dir : Direction.values()) {
            faces.put(dir, IOFace.valueOf(tag.getString(dir.toString())));
        }
    }

    public Map<Direction, IOFace> getFaces() {
        return faces;
    }

    public void setFaces(Map<Direction, IOFace> faces) {
        this.faces = faces;
    }

    public IOFace getFace(Direction dir) {
        return faces.get(dir);
    }

    public void setFace(Direction dir, IOFace face) {
        faces.put(dir, face);
    }

    public void fromClientTag(CompoundTag tag) {
        fromTag(world.getBlockState(pos), tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag(tag);
    }

    @Override
    public void sync() {
        BlockEntityClientSerializable.super.sync();
    }

    public enum IOFace implements StringIdentifiable {
        NONE,
        CONNECT,
        BLOCK,
        INSERTER(NetworkNode.Type.PULL),
        EXTRACTOR(NetworkNode.Type.PUSH),
        PASSIVE(NetworkNode.Type.PASSIVE);

        private final String string;
        private final NetworkNode.Type type;

        IOFace() {
            this(null);
        }

        IOFace(NetworkNode.Type type) {
            this.string = toString().toLowerCase();
            this.type = type;
        }

        public NetworkNode.Type getType() {
            return type;
        }

        public boolean isNode() {
            return type != null;
        }

        @Override
        public String asString() {
            return string;
        }
    }

}
