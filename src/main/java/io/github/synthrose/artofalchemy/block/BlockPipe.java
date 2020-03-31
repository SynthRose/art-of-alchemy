package io.github.synthrose.artofalchemy.block;

import io.github.synthrose.artofalchemy.imixin.IMixinWorld;
import io.github.synthrose.artofalchemy.transport.NetworkElement;
import io.github.synthrose.artofalchemy.transport.NetworkNode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.*;

public class BlockPipe extends Block implements NetworkElement {

	private static VoxelShape boundingBox = VoxelShapes.cuboid(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
	private Map<Direction, EnumProperty<IOFace>> faces;

	public BlockPipe() {
		super(Settings.of(Material.METAL).nonOpaque().noCollision());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return boundingBox;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return boundingBox;
	}

	@Override
	public VoxelShape getRayTraceShape(BlockState state, BlockView world, BlockPos pos) {
		return boundingBox;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return boundingBox;
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		this.faces = new HashMap<>();
		for (Direction dir : Direction.values()) {
			faces.put(dir, EnumProperty.of(dir.asString(), IOFace.class));
			builder.add(faces.get(dir));
		}
	}

	public EnumProperty<IOFace> getProperty(Direction dir) {
		return faces.get(dir);
	}

	public boolean hasNodes(World world, BlockPos pos) {
		return !getNodes(world, pos).isEmpty();
	}

	public Set<NetworkNode> getNodes(World world, BlockPos pos) {
		HashSet<NetworkNode> nodes = new HashSet<>();
		BlockState state = world.getBlockState(pos);
		for (Direction dir : faces.keySet()) {
			IOFace face = state.get(faces.get(dir));
			if (face.isNode()) {
				nodes.add(new NetworkNode(world, face.getType(), pos, dir));
			}
		}
		return nodes;
	}

	public boolean faceOpen(BlockState state, Direction dir) {
		if (state.getBlock() == this) {
			IOFace face = state.get(getProperty(dir));
			return (face == IOFace.NONE || face == IOFace.CONNECT);
		} else {
			return false;
		}
	}

	public boolean isConnected(World world, BlockPos pos, Direction dir) {
		BlockState state = world.getBlockState(pos);
		BlockState other = world.getBlockState(pos.offset(dir));
		return (state.get(faces.get(dir)) == IOFace.CONNECT && other.get(faces.get(dir.getOpposite())) == IOFace.CONNECT);
	}

	public boolean isConnected(World world, BlockPos pos1, BlockPos pos2) {
		BlockPos difference = pos2.subtract(pos1);
		for (Direction dir : Direction.values()) {
			if (difference.equals(dir.getVector())) {
				return isConnected(world, pos1, dir);
			}
		}
		return false;
	}

	public int getConnectionCount(BlockState state) {
		int i = 0;
		for (Direction dir : faces.keySet()) {
			if (state.get(getProperty(dir)) == IOFace.CONNECT) {
				i++;
			}
		}
		return i;
	}

	public int getConnectionCount(World world, BlockPos pos) {
		return getConnectionCount(world.getBlockState(pos));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = super.getPlacementState(ctx);
		for (Direction dir : Direction.values()) {
			BlockState other = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(dir));
			if (faceOpen(other, dir)) {
				state.with(getProperty(dir), IOFace.CONNECT);
			}
		}
		return state;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, IWorld world, BlockPos pos, BlockPos posFrom) {
		if (faceOpen(state, direction) && faceOpen(newState, direction.getOpposite())) {
			return state.with(getProperty(direction), IOFace.CONNECT);
		} else if (state.get(getProperty(direction)) == IOFace.CONNECT) {
			return state.with(getProperty(direction), IOFace.NONE);
		} else {
			return state;
		}
 	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		((IMixinWorld) world).getEssentiaNetworker().add(pos);
	}

	@Override
	public void onBroken(IWorld world, BlockPos pos, BlockState state) {
		((IMixinWorld) world).getEssentiaNetworker().remove(pos);
		super.onBroken(world, pos, state);
	}

	public enum IOFace implements StringIdentifiable {
		NONE,
		CONNECT,
		PULL(NetworkNode.Type.PULL),
		PUSH(NetworkNode.Type.PUSH),
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
