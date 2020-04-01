package io.github.synthrose.artofalchemy.blockentity;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import io.github.synthrose.artofalchemy.ImplementedInventory;
import io.github.synthrose.artofalchemy.block.BlockDissolver;
import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;
import io.github.synthrose.artofalchemy.essentia.EssentiaStack;
import io.github.synthrose.artofalchemy.transport.HasEssentia;
import io.github.synthrose.artofalchemy.transport.HasAlkahest;
import io.github.synthrose.artofalchemy.network.AoANetworking;
import io.github.synthrose.artofalchemy.recipe.AoARecipes;
import io.github.synthrose.artofalchemy.recipe.RecipeDissolution;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

public class BlockEntityDissolver extends BlockEntity implements ImplementedInventory,
	Tickable, PropertyDelegateHolder, BlockEntityClientSerializable, HasEssentia,
	HasAlkahest, SidedInventory {
	
	private static final int[] TOP_SLOTS = new int[]{0};
	private static final int[] BOTTOM_SLOTS = new int[]{0};
	private static final int[] SIDE_SLOTS = new int[]{0};
	private final int TANK_SIZE = 4000;
	private final float SPEED_MOD = 2.0f;
	private final float EFFICIENCY = 0.5f;
	private int alkahest = 0;
	private int maxAlkahest = TANK_SIZE;
	private int progress = 0;
	private int maxProgress = 100;
	private int status = 0;
	// Status 0: Can craft
	// Status 1: Generic error (no message)
	// Status 2: Insufficient alkahest
	// Status 3: Full output buffer
	private boolean lit = false;
	
	protected final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
	protected EssentiaContainer essentia = new EssentiaContainer()
		.setCapacity(TANK_SIZE)
		.setInput(false)
		.setOutput(true);
	protected final PropertyDelegate delegate = new PropertyDelegate() {
		
		@Override
		public int size() {
			return 5;
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
			case 4:
				status = value;
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
			case 4:
				return status;
			default:
				return 0;
			}
		}
		
	};
	
	public BlockEntityDissolver() {
		super(AoABlockEntities.DISSOLVER);
	}

	@Override
	public EssentiaContainer getContainer(Direction dir) {
		return getContainer(0);
	}
	
	@Override
	public EssentiaContainer getContainer(int id) {
		if (id == 0) {
			return essentia;
		} else {
			return null;
		}
	}
	
	@Override
	public int getNumContainers() {
		return 1;
	}

	@Override
	public int getAlkahest() {
		return alkahest;
	}
	
	@Override
	public boolean setAlkahest(int amount) {
		if (amount >= 0 && amount <= maxAlkahest) {
			alkahest = amount;
			world.setBlockState(pos, world.getBlockState(pos).with(BlockDissolver.FILLED, alkahest > 0));
			markDirty();
			return true;
		} else {
			return false;
		}
	}
	
	private boolean updateStatus(int status) {
		if (this.status != status) {
			this.status = status;
			markDirty();
		}
		return (status == 0);
	}
	
	private boolean canCraft(RecipeDissolution recipe) {
		ItemStack inSlot = items.get(0);
		
		if (recipe == null || inSlot.isEmpty()) {
			return updateStatus(1);
		} else {
			ItemStack container = recipe.getContainer();
			EssentiaStack results = recipe.getEssentia();
			
			maxProgress = (int) (results.getCount() / SPEED_MOD);
			if (maxProgress < 20) {
				maxProgress = 20;
			}
			
			if (container != ItemStack.EMPTY && inSlot.getCount() != container.getCount()) {
				 return updateStatus(1);
			}
			
			float factor = EFFICIENCY * recipe.getFactor();
			if (inSlot.isDamageable()) {
				factor *= 1.0 - (float) inSlot.getDamage() / inSlot.getMaxDamage();
			}
			results.multiply(factor);
			
			if (results.getCount() > alkahest) {
				return updateStatus(2);
			} else {
				if (!essentia.canAcceptIgnoreIO(results)) {
					return updateStatus(3);
				} else {
					return updateStatus(0);
				}
			}
		}
	}
	
	// Be sure to check canCraft() first!
	private void doCraft(RecipeDissolution recipe) {
		ItemStack inSlot = items.get(0);
		EssentiaStack results = recipe.getEssentia();
		ItemStack container = recipe.getContainer();
		
		float factor = EFFICIENCY * recipe.getFactor();
		if (inSlot.isDamageable()) {
			factor *= 1.0 - (float) inSlot.getDamage() / inSlot.getMaxDamage();
		}
		results.multiply(factor);
		
		if (container != ItemStack.EMPTY) {
			items.set(0, container.copy());
		} else {
			inSlot.decrement(1);
		}
		
		essentia.addEssentia(results);
		alkahest -= results.getCount();
		
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("alkahest", alkahest);
		tag.putInt("progress", progress);
		tag.putInt("max_progress", maxProgress);
		tag.putInt("status", status);
		tag.put("essentia", essentia.toTag());
		Inventories.toTag(tag, items);
		return super.toTag(tag);
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		Inventories.fromTag(tag, items);
		alkahest = tag.getInt("alkahest");
		progress = tag.getInt("progress");
		maxProgress = tag.getInt("max_progress");
		status = tag.getInt("status");
		essentia = new EssentiaContainer(tag.getCompound("essentia"));
		maxAlkahest = TANK_SIZE;
	}

	@Override
	public DefaultedList<ItemStack> getItems() {
		return items;
	}
	
	@Override
	public boolean isValidInvStack(int slot, ItemStack stack) {
		return true;
	}
	

	@Override
	public void tick() {
		boolean dirty = false;
		
		if (!world.isClient()) {
			ItemStack inSlot = items.get(0);
			boolean canWork = false;
			
			if (inSlot.isEmpty()) {
				updateStatus(1);
			} else if (!hasAlkahest()) {
				updateStatus(2);
			} else {
				RecipeDissolution recipe = world.getRecipeManager()
						.getFirstMatch(AoARecipes.DISSOLUTION, this, world).orElse(null);
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
						if (alkahest <= 0) {
							world.setBlockState(pos, world.getBlockState(pos).with(BlockDissolver.FILLED, false));
						}
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
		AoANetworking.sendEssentiaPacket(world, pos, 0, essentia);
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
	public int[] getInvAvailableSlots(Direction side) {
		if (side == Direction.UP) {
			return TOP_SLOTS;
		} else if (side == Direction.DOWN) {
			return BOTTOM_SLOTS;
		} else {
			return SIDE_SLOTS;
		}
	}

	@Override
	public boolean canInsertInvStack(int slot, ItemStack stack, Direction dir) {
		return isValidInvStack(slot, stack);
	}

	@Override
	public boolean canExtractInvStack(int slot, ItemStack stack, Direction dir) {
		if (dir == Direction.DOWN) {
			return TagRegistry.item(ArtOfAlchemy.id("containers")).contains(stack.getItem());
		} else {
			return true;
		}
	}
	
}
