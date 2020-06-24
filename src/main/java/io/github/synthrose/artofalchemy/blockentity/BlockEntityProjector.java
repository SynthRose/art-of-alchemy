package io.github.synthrose.artofalchemy.blockentity;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import io.github.synthrose.artofalchemy.AoAConfig;
import io.github.synthrose.artofalchemy.gui.handler.HandlerProjector;
import io.github.synthrose.artofalchemy.util.ImplementedInventory;
import io.github.synthrose.artofalchemy.block.BlockDissolver;
import io.github.synthrose.artofalchemy.recipe.AoARecipes;
import io.github.synthrose.artofalchemy.recipe.RecipeProjection;
import io.github.synthrose.artofalchemy.transport.HasAlkahest;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

public class BlockEntityProjector extends BlockEntity implements ImplementedInventory,  Tickable,
		PropertyDelegateHolder, BlockEntityClientSerializable, HasAlkahest, SidedInventory, ExtendedScreenHandlerFactory {

	private static final int[] TOP_SLOTS = new int[]{0};
	private static final int[] BOTTOM_SLOTS = new int[]{1};
	private static final int[] SIDE_SLOTS = new int[]{0, 1};
	private int tankSize;
	private int operationTime;
	private int alkahest = 0;
	private int maxAlkahest = getTankSize();
	private int progress = 0;
	private int maxProgress = getOperationTime();
	private boolean lit = false;

	protected final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);
	protected final PropertyDelegate delegate = new PropertyDelegate() {

		@Override
		public int size() {
			return 4;
		}

		@Override
		public void set(int index, int value) {
			switch(index) {
			case 0:
				alkahest = value;
				break;
			case 1:
				maxAlkahest = value;
				break;
			case 2:
				progress = value;
				break;
			case 3:
				maxProgress = value;
				break;
			}
		}

		@Override
		public int get(int index) {
			switch(index) {
			case 0:
				return alkahest;
			case 1:
				return maxAlkahest;
			case 2:
				return progress;
			case 3:
				return maxProgress;
			default:
				return 0;
			}
		}

	};

	public BlockEntityProjector() {
		this(AoABlockEntities.PROJECTOR);
		AoAConfig.ProjectorSettings settings = AoAConfig.get().projectorSettings;
		operationTime = settings.opTime;
		tankSize = settings.tankSize;
		maxProgress = getOperationTime();
		maxAlkahest = getTankSize();
	}

	protected BlockEntityProjector(BlockEntityType type) {
		super(type);
	}

	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new HandlerProjector(syncId, inv, ScreenHandlerContext.create(world, pos));
	}

	@Override
	public Text getDisplayName() {
		return new LiteralText("");
	}

	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		buf.writeBlockPos(pos);
	}

	@Override
	public int getAlkahest() {
		return alkahest;
	}
	
	@Override
	public boolean setAlkahest(int amount) {
		if (amount >= 0 && amount <= maxAlkahest) {
			alkahest = amount;
			markDirty();
			return true;
		} else {
			return false;
		}
	}
	
	private boolean canCraft(RecipeProjection recipe) {
		ItemStack inSlot = items.get(0);
		ItemStack outSlot = items.get(1);
		
		if (recipe == null || inSlot.isEmpty()) {
			return false;
		} else {
			ItemStack outStack = recipe.getOutput();
			int alkCost = recipe.getAlkahest();
			int itemCost = recipe.getCost();
			
			if (alkahest < alkCost || inSlot.getCount() < itemCost) {
				return false;
			} else {
				if (outSlot.isEmpty()) {
					return true;
				} else if (outSlot.getItem() == outStack.getItem()) {
					return outSlot.getCount() <= outSlot.getMaxCount() - outStack.getCount();
				} else {
					return false;
				}
			}
		}
	}
	
	// Be sure to check canCraft() first!
	private void doCraft(RecipeProjection recipe) {
		ItemStack inSlot = items.get(0);
		ItemStack outSlot = items.get(1);

		ItemStack outStack = recipe.getOutput();

		inSlot.decrement(recipe.getCost());
		addAlkahest(-recipe.getAlkahest());

		if (outSlot.isEmpty()) {
			items.set(1, outStack.copy());
		} else {
			outSlot.increment(outStack.getCount());
		}
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("alkahest", alkahest);
		tag.putInt("progress", progress);
		Inventories.toTag(tag, items);
		return super.toTag(tag);
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		Inventories.fromTag(tag, items);
		alkahest = tag.getInt("alkahest");
		progress = tag.getInt("progress");
		maxProgress = getOperationTime();
		maxAlkahest = getTankSize();
	}

	@Override
	public DefaultedList<ItemStack> getItems() {
		return items;
	}
	
	@Override
	public boolean isValid(int slot, ItemStack stack) {
		return slot == 0;
	}

	@Override
	public void tick() {
		boolean dirty = false;
		
		if (!world.isClient()) {
			ItemStack inSlot = items.get(0);
			boolean canWork = false;
			
			if (inSlot.isEmpty() || !hasAlkahest()) {
				canWork = false;
			} else {
				RecipeProjection recipe = world.getRecipeManager()
						.getFirstMatch(AoARecipes.PROJECTION, this, world).orElse(null);
				canWork = canCraft(recipe);
			
				if (canWork) {
					if (progress < maxProgress) {
						if (!lit) {
							world.setBlockState(pos, world.getBlockState(pos).with(BlockDissolver.LIT, true));
							lit = true;
						}
						progress++;
					}
					if (progress >= maxProgress) {
						progress -= maxProgress;
						doCraft(recipe);
						dirty = true;
					}
				}
			}
			
			if (!canWork) {
				if (progress != 0) {
					progress = 0;
				}
				if (lit) {
					lit = false;
					world.setBlockState(pos, world.getBlockState(pos).with(BlockDissolver.LIT, false));
				}
			}
		}
		
		if (dirty) {
			markDirty();
		}
	}

	@Override
	public PropertyDelegate getPropertyDelegate() {
		return delegate;
	}
	
	@Override
	public void markDirty() {
		super.markDirty();
		if (!world.isClient()) {
			sync();
		}
	}
	
	@Override
	public void sync() {
		BlockEntityClientSerializable.super.sync();
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		fromTag(world.getBlockState(pos), tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return toTag(tag);
	}
	
	@Override
	public int[] getAvailableSlots(Direction side) {
		if (side == Direction.UP) {
			return TOP_SLOTS;
		} else if (side == Direction.DOWN) {
			return BOTTOM_SLOTS;
		} else {
			return SIDE_SLOTS;
		}
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return isValid(slot, stack);
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return true;
	}

	public int getTankSize() {
		return tankSize;
	}

	public int getOperationTime() {
		return operationTime;
	}

}
