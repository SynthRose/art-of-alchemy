package io.github.synthrose.artofalchemy.block;

import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import io.github.synthrose.artofalchemy.item.AoAItems;
import io.github.synthrose.artofalchemy.item.ItemEssentiaPort;
import io.github.synthrose.artofalchemy.transport.EssentiaNetwork;
import io.github.synthrose.artofalchemy.transport.EssentiaNetworker;
import io.github.synthrose.artofalchemy.transport.NetworkElement;
import io.github.synthrose.artofalchemy.transport.NetworkNode;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
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
		super(Settings.of(Material.CLAY).strength(0.1f).nonOpaque().noCollision().sounds(BlockSoundGroup.NETHERITE));
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

	public Set<BlockPos> getConnections(BlockState state, BlockPos pos) {
		Set<BlockPos> connections = new HashSet<>();
		for (Direction dir : faces.keySet()) {
			if (state.get(getProperty(dir)) == IOFace.CONNECT) {
				connections.add(pos.offset(dir));
			}
		}
		return connections;
	}

	@Override
	public Set<BlockPos> getConnections(World world, BlockPos pos) {
		return getConnections(world.getBlockState(pos), pos);
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
		if (!world.isClient()) {
			EssentiaNetworker.get((ServerWorld) world).add(pos);
		}
	}

	@Override
	public void onBroken(IWorld world, BlockPos pos, BlockState state) {
		super.onBroken(world, pos, state);
		if (!world.isClient()) {
			EssentiaNetworker.get((ServerWorld) world).remove(pos, getConnections(state, pos));
		}
		for (Direction dir : faces.keySet()) {
			dropStack((World) world, pos, new ItemStack(ItemEssentiaPort.getItem(state.get(getProperty(dir)))));
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack heldStack = player.getStackInHand(hand);
		if (heldStack.getItem() == AoAItems.MYSTERIOUS_SIGIL) {
			if (!world.isClient()) {
				Optional<EssentiaNetwork> network = EssentiaNetworker.get((ServerWorld) world).getNetwork(pos);
				if (network.isPresent()) {
					player.sendMessage(new LiteralText(network.get().getUuid().toString() + " w/ " + network.get().getNodes().size() + " nodes"));
				} else {
					player.sendMessage(new LiteralText("no network"));
				}
			}
			return ActionResult.SUCCESS;
		}
		if (TagRegistry.item(ArtOfAlchemy.id("usable_on_pipes")).contains(heldStack.getItem())) {
			return ActionResult.PASS;
		}
		EnumProperty<IOFace> property = getProperty(hit.getSide());
		switch (state.get(property)) {
			case NONE:
			case CONNECT:
				world.playSound(null, pos, SoundEvents.BLOCK_NETHERITE_BLOCK_FALL, SoundCategory.BLOCKS, 0.6f, 1.0f);
				if (heldStack.getItem() instanceof ItemEssentiaPort) {
					world.setBlockState(pos, state.with(property,  ((ItemEssentiaPort) heldStack.getItem()).IOFACE));
					if (!player.abilities.creativeMode) {
						heldStack.decrement(1);
					}
				} else {
					world.setBlockState(pos, state.with(property,  IOFace.BLOCK));
				}
				break;
			case BLOCK:
			case INSERTER:
			case EXTRACTOR:
			case PASSIVE:
				world.playSound(null, pos, SoundEvents.BLOCK_NETHERITE_BLOCK_HIT, SoundCategory.BLOCKS, 0.6f, 1.0f);
				if (!player.abilities.creativeMode) {
					ItemStack stack = new ItemStack(ItemEssentiaPort.getItem(state.get(property)));
					dropStack(world, pos, stack);
				}
				if (faceOpen(world.getBlockState(pos.offset(hit.getSide())), hit.getSide().getOpposite())) {
					world.setBlockState(pos, state.with(property,  IOFace.CONNECT));
				} else {
					world.setBlockState(pos, state.with(property,  IOFace.NONE));
				}
				break;
		}
		if (!world.isClient()) {
			EssentiaNetworker.get((ServerWorld) world).update(pos, getConnections(state, pos));
		}
		return ActionResult.SUCCESS;
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
