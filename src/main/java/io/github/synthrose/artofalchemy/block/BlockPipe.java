package io.github.synthrose.artofalchemy.block;

import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import io.github.synthrose.artofalchemy.blockentity.BlockEntityPipe;
import io.github.synthrose.artofalchemy.blockentity.BlockEntityPipe.IOFace;
import io.github.synthrose.artofalchemy.item.AoAItems;
import io.github.synthrose.artofalchemy.item.ItemEssentiaPort;
import io.github.synthrose.artofalchemy.transport.EssentiaNetwork;
import io.github.synthrose.artofalchemy.transport.EssentiaNetworker;
import io.github.synthrose.artofalchemy.transport.NetworkElement;
import io.github.synthrose.artofalchemy.transport.NetworkNode;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.*;

public class BlockPipe extends Block implements NetworkElement, BlockEntityProvider {

	private static VoxelShape boundingBox = VoxelShapes.cuboid(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);

	public BlockPipe() {
		super(Settings.of(Material.ORGANIC_PRODUCT).strength(0.1f).nonOpaque().noCollision().sounds(BlockSoundGroup.NETHERITE));
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

	private Map<Direction, IOFace> getFaces(World world, BlockPos pos) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof BlockEntityPipe) {
			return ((BlockEntityPipe) be).getFaces();
		} else {
			Map<Direction, IOFace> map = new HashMap<>();
			for (Direction dir : Direction.values()) {
				map.put(dir, IOFace.NONE);
			}
			return map;
		}
	}

	private IOFace getFace(World world, BlockPos pos, Direction dir) {
		return getFaces(world, pos).get(dir);
	}

	private void setFace(World world, BlockPos pos, Direction dir, IOFace face) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof BlockEntityPipe) {
			((BlockEntityPipe) be).setFace(dir, face);
		}
	}

	public boolean hasNodes(World world, BlockPos pos) {
		return !getNodes(world, pos).isEmpty();
	}

	public Set<NetworkNode> getNodes(World world, BlockPos pos) {
		HashSet<NetworkNode> nodes = new HashSet<>();
		Map<Direction, IOFace> faces = getFaces(world, pos);
		for (Direction dir : faces.keySet()) {
			IOFace face = faces.get(dir);
			if (face.isNode()) {
				nodes.add(new NetworkNode(world, face.getType(), pos, dir));
			}
		}
		return nodes;
	}

	public boolean faceOpen(World world, BlockPos pos, Direction dir) {
		if (world.getBlockState(pos).getBlock() == this) {
			IOFace face = getFace(world, pos, dir);
			return (face == IOFace.NONE || face == IOFace.CONNECT);
		} else {
			return false;
		}
	}

	public boolean isConnected(World world, BlockPos pos, Direction dir) {
		Map<Direction, IOFace> theseFaces = getFaces(world, pos);
		Map<Direction, IOFace> otherFaces = getFaces(world, pos.offset(dir));
		return (theseFaces.get(dir) == IOFace.CONNECT && otherFaces.get(dir.getOpposite()) == IOFace.CONNECT);
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

	@Override
	public Set<BlockPos> getConnections(World world, BlockPos pos) {
		Set<BlockPos> connections = new HashSet<>();
		Map<Direction, IOFace> faces = getFaces(world, pos);
		for (Direction dir : faces.keySet()) {
			if (faces.get(dir) == IOFace.CONNECT) {
				connections.add(pos.offset(dir));
			}
		}
		return connections;
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		super.neighborUpdate(state, world, pos, block, fromPos, notify);
		for (Direction dir : Direction.values()) {
			if (fromPos.subtract(pos) == dir.getVector()) {
				if (faceOpen(world, pos, dir) && faceOpen(world, fromPos, dir.getOpposite())) {
					setFace(world, pos, dir, IOFace.CONNECT);
				} else if (getFace(world, pos, dir) == IOFace.CONNECT) {
					setFace(world, pos, dir, IOFace.NONE);
				}
			}
		}
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		for (Direction dir : Direction.values()) {
			if (faceOpen(world, pos.offset(dir), dir)) {
				setFace(world, pos, dir, IOFace.CONNECT);
			}
		}
		if (!world.isClient()) {
			EssentiaNetworker.get((ServerWorld) world).add(pos);
		}
	}

	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		super.onBroken(world, pos, state);
		Map<Direction, IOFace> faces = getFaces(world.getWorld(), pos);
		for (Direction dir : faces.keySet()) {
			dropStack(world.getWorld(), pos, new ItemStack(ItemEssentiaPort.getItem(faces.get(dir))));
		}
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		if (!world.isClient()) {
			EssentiaNetworker.get((ServerWorld) world).add(pos);
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean notify) {
		super.onStateReplaced(state, world, pos, newState, notify);
		if (!world.isClient()) {
			EssentiaNetworker.get((ServerWorld) world).remove(pos, getConnections(world, pos));
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		Direction dir = player.isSneaking() ? hit.getSide().getOpposite() : hit.getSide();
		ItemStack heldStack = player.getStackInHand(hand);
		if (heldStack.getItem() == AoAItems.MYSTERIOUS_SIGIL) {
			if (!world.isClient()) {
				Optional<EssentiaNetwork> network = EssentiaNetworker.get((ServerWorld) world).getNetwork(pos);
				if (network.isPresent()) {
					player.sendSystemMessage(new LiteralText(network.get().getUuid().toString() + " w/ " + network.get().getNodes().size() + " nodes"), new UUID(0, 0));
				} else {
					player.sendSystemMessage(new LiteralText("no network"), new UUID(0, 0));
				}
			}
			return ActionResult.SUCCESS;
		}
		if (TagRegistry.item(ArtOfAlchemy.id("usable_on_pipes")).contains(heldStack.getItem())) {
			return ActionResult.PASS;
		}
		IOFace face = getFace(world, pos, dir);
		switch (face) {
			case NONE:
			case CONNECT:
				world.playSound(null, pos, SoundEvents.BLOCK_NETHERITE_BLOCK_FALL, SoundCategory.BLOCKS, 0.6f, 1.0f);
				if (heldStack.getItem() instanceof ItemEssentiaPort) {
					setFace(world, pos, dir, ((ItemEssentiaPort) heldStack.getItem()).IOFACE);
					if (!player.abilities.creativeMode) {
						heldStack.decrement(1);
					}
				} else {
					setFace(world, pos, dir, IOFace.BLOCK);
				}
				break;
			case BLOCK:
			case INSERTER:
			case EXTRACTOR:
			case PASSIVE:
				world.playSound(null, pos, SoundEvents.BLOCK_NETHERITE_BLOCK_HIT, SoundCategory.BLOCKS, 0.6f, 1.0f);
				if (!player.abilities.creativeMode) {
					ItemStack stack = new ItemStack(ItemEssentiaPort.getItem(face));
					dropStack(world, pos, stack);
				}
				if (faceOpen(world, pos.offset(dir), dir.getOpposite())) {
					setFace(world, pos, dir, IOFace.CONNECT);
				} else {
					setFace(world, pos, dir, IOFace.NONE);
				}
				break;
		}
		if (!world.isClient()) {
			EssentiaNetworker networker = EssentiaNetworker.get((ServerWorld) world);
			networker.remove(pos, getConnections(world, pos));
			networker.add(pos);
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new BlockEntityPipe();
	}
}
