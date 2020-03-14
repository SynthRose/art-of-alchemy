package net.synthrose.artofalchemy.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.synthrose.artofalchemy.EssentiaType;
import net.synthrose.artofalchemy.blockentity.BlockEntityDissolver;
import net.synthrose.artofalchemy.item.AoAItems;

public class BlockDissolver extends Block implements BlockEntityProvider {
	
	public static BooleanProperty FILLED = BooleanProperty.of("filled");
	public static BooleanProperty LIT = Properties.LIT;
	public static final Settings SETTINGS = FabricBlockSettings
			.of(Material.STONE)
			.hardness(5.0f).resistance(6.0f)
			.nonOpaque()
			.build();
	
	public static Identifier getId() {
		return Registry.BLOCK.getId(AoABlocks.DISSOLVER);
	}

	public BlockDissolver() {
		super(SETTINGS);
		setDefaultState(getDefaultState().with(FILLED, false).with(LIT, false));
	}
	
	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(FILLED).add(LIT);
	}
	
	@Override
	public int getLuminance(BlockState state) {
		if (state.get(LIT)) { 
			return 15;
		} else {
			return 0;
		}
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		
		ItemStack inHand = player.getStackInHand(hand);
		
		if (world.isClient) {
			return ActionResult.SUCCESS;
		}
		
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null && blockEntity instanceof BlockEntityDissolver) {
			BlockEntityDissolver dissolver = (BlockEntityDissolver) blockEntity;
			dissolver.getEssentia(EssentiaType.MERCURY);
			if (inHand.getItem() == AoAItems.ALKAHEST_BUCKET && dissolver.addAlkahest(1000)) {
				if (!player.abilities.creativeMode) {
					player.setStackInHand(hand, new ItemStack(Items.BUCKET));
				}
				world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
			} else if (inHand.getItem() == Items.BUCKET && dissolver.addAlkahest(-1000)) {
				if (!player.abilities.creativeMode) {
					inHand.decrement(1);
					if (inHand.isEmpty()) {
						player.setStackInHand(hand, new ItemStack(AoAItems.ALKAHEST_BUCKET));
					} else {
						player.dropItem(new ItemStack(AoAItems.ALKAHEST_BUCKET), false);
					}
				}
				world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
			} else {
				ContainerProviderRegistry.INSTANCE.openContainer(getId(), player,
						(packetByteBuf -> packetByteBuf.writeBlockPos(pos)));
			}
		}
		
		return ActionResult.SUCCESS;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new BlockEntityDissolver();
	}

	@Override
	public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof BlockEntityDissolver) {
				ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
			}

			super.onBlockRemoved(state, world, pos, newState, moved);
		}
	}

}
