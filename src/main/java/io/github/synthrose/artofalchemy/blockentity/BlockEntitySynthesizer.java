package io.github.synthrose.artofalchemy.blockentity;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import io.github.synthrose.artofalchemy.AoAHelper;
import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import io.github.synthrose.artofalchemy.ImplementedInventory;
import io.github.synthrose.artofalchemy.block.BlockSynthesizer;
import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;
import io.github.synthrose.artofalchemy.essentia.EssentiaStack;
import io.github.synthrose.artofalchemy.transport.HasEssentia;
import io.github.synthrose.artofalchemy.item.ItemMateria;
import io.github.synthrose.artofalchemy.network.AoANetworking;
import io.github.synthrose.artofalchemy.recipe.AoARecipes;
import io.github.synthrose.artofalchemy.recipe.RecipeSynthesis;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.tag.Tag;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

public class BlockEntitySynthesizer extends BlockEntity implements ImplementedInventory,
	Tickable, PropertyDelegateHolder, BlockEntityClientSerializable, HasEssentia, SidedInventory {
	
	private static final int[] TOP_SLOTS = new int[]{0};
	private static final int[] BOTTOM_SLOTS = new int[]{1, 2};
	private static final int[] SIDE_SLOTS = new int[]{1, 2};
	private final double SPEED_MOD = 2.0;
	private final int TANK_SIZE = 4000;
	private int xp = 0;
	private int progress = 0;
	private int maxProgress = 200;
	private int status = 0;
	// Status 0: Working
	// Status 1: Generic error (no message)
	// Status 2: Unknown or missing target item
	// Status 3: Needs materia
	// Status 4: Needs essentia
	// Status 5: Needs container
	// Status 6: Needs XP (no message)
	private boolean lit = false;
	
	protected final DefaultedList<ItemStack> items = DefaultedList.ofSize(3, ItemStack.EMPTY);
	protected EssentiaContainer essentiaContainer = new EssentiaContainer()
		.setCapacity(TANK_SIZE)
		.setInput(true)
		.setOutput(false);
	protected final PropertyDelegate delegate = new PropertyDelegate() {
		
		@Override
		public int size() {
			return 4;
		}
		
		@Override
		public void set(int index, int value) {
			switch(index) {
			case 0:
				xp = value;
				break;
			case 1:
				progress = value;
				break;
			case 2:
				maxProgress = value;
				break;
			case 3:
				status = value;
				break;
			}
		}
		
		@Override
		public int get(int index) {
			switch(index) {
			case 0:
				return xp;
			case 1:
				return progress;
			case 2:
				return maxProgress;
			case 3:
				return status;
			default:
				return 0;
			}
		}
		
	};
	
	public BlockEntitySynthesizer() {
		super(AoABlockEntities.SYNTHESIZER);
	}
	
	@Override
	public EssentiaContainer getContainer(int id) {
		if (id == 0) {
			return essentiaContainer;
		} else {
			return null;
		}
	}
	
	@Override
	public int getNumContainers() {
		return 1;
	}
	
	public boolean hasXp() {
		return xp > 0;
	}
	
	public int getXp() {
		return xp;
	}
	
	public boolean setXp(int amount) {
		if (amount >= 0) {
			xp = amount;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean addXp(int amount) {
		return setXp(xp + amount);
	}
	
	private boolean updateStatus(int status) {
		if (this.status != status) {
			this.status = status;
			markDirty();
		}
		return (status == 0);
	}
	
	private boolean canCraft(RecipeSynthesis recipe) {
		ItemStack inSlot = items.get(0);
		ItemStack outSlot = items.get(1);
		ItemStack targetSlot = items.get(2);
		
		if (recipe == null || targetSlot.isEmpty()) {
			return updateStatus(2);
		} else if (inSlot.isEmpty()) {
			return updateStatus(3);
		} else if (essentiaContainer.isEmpty()) {
			return updateStatus(4);
		} else {
			Ingredient container = recipe.getContainer();
			Ingredient materia = recipe.getMateria();
			EssentiaStack essentia = recipe.getEssentia();
			int cost = recipe.getCost();

			Item target = AoAHelper.getTarget(targetSlot);
			
			int tier = 1;
			if (inSlot.getItem() instanceof ItemMateria) {
				tier = ((ItemMateria) inSlot.getItem()).getTier() + 1;
			}
			
			if (!materia.test(inSlot) || inSlot.getCount() < cost) {
				return updateStatus(3);
			}
			
			if (!essentiaContainer.contains(essentia)) {
				return updateStatus(4);
			}
			
			if (container != Ingredient.EMPTY) {
				if (container.test(outSlot)) {
					if (outSlot.getCount() != 1) {
						return updateStatus(1);
					} else {
						return updateStatus(0);
					}
				} else {
					return updateStatus(5);
				}
			} else {
				maxProgress = (int) (essentia.getCount() / SPEED_MOD);
				maxProgress += tier * cost * 5;
				if (maxProgress < 40) {
					maxProgress = 40;
				}
				if (outSlot.isEmpty()) {
					return updateStatus(0);
				} else if (outSlot.getItem() == target) {
					if (outSlot.getCount() < outSlot.getMaxCount()) {
						return updateStatus(0);
					} else {
						return updateStatus(1);
					}
				} else {
					return updateStatus(1);
				}
			}
		}
	}
	
	// Be sure to check canCraft() first!
	private void doCraft(RecipeSynthesis recipe) {
		ItemStack inSlot = items.get(0);
		ItemStack outSlot = items.get(1);
		ItemStack targetSlot = items.get(2);
		Ingredient container = recipe.getContainer();
		EssentiaStack essentia = recipe.getEssentia();
		int cost = recipe.getCost();
//		int xpCost = recipe.getXp(targetSlot);
		
		Item target = AoAHelper.getTarget(targetSlot);
		
		if (container != Ingredient.EMPTY || outSlot.isEmpty()) {
			items.set(1, new ItemStack(target));
		} else {
			outSlot.increment(1);
		}
		
		inSlot.decrement(cost);
		essentiaContainer.subtractEssentia(essentia);
//		this.addXp(-xpCost);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("xp", xp);
		tag.putInt("progress", progress);
		tag.putInt("max_progress", maxProgress);
		tag.putInt("status", status);
		tag.put("essentia", essentiaContainer.toTag());
		Inventories.toTag(tag, items);
		return super.toTag(tag);
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		Inventories.fromTag(tag, items);
		xp = tag.getInt("xp");
		progress = tag.getInt("progress");
		maxProgress = tag.getInt("max_progress");
		status = tag.getInt("status");
		essentiaContainer = new EssentiaContainer(tag.getCompound("essentia"));
	}

	@Override
	public DefaultedList<ItemStack> getItems() {
		return items;
	}
	
	@Override
	public boolean isValidInvStack(int slot, ItemStack stack) {
		if (slot == 1) {
			Tag<Item> tag = world.getTagManager().items().get(ArtOfAlchemy.id("containers"));
			if (tag == null) {
				return false;
			} else {
				return tag.contains(stack.getItem());
			}
		} else {
			return true;
		}
	}
	

	@Override
	public void tick() {
		boolean dirty = false;
		
		if (!world.isClient()) {
			ItemStack inSlot = items.get(0);
			ItemStack targetSlot = items.get(2);
			boolean isWorking = false;
			
			if (targetSlot.isEmpty()) {
				updateStatus(2);
			} else if (inSlot.isEmpty()) {
				updateStatus(3);
			} else {
				RecipeSynthesis recipe = world.getRecipeManager()
						.getFirstMatch(AoARecipes.SYNTHESIS, this, world).orElse(null);
				
				if (canCraft(recipe)) {
//					if (xp >= recipe.getXp(inSlot)) {
//						isWorking = true;
//					} else {
//						updateStatus(6);
//					}
					isWorking = true;
				}
			
				if (isWorking) {
					if (progress < maxProgress) {
						if (!lit) {
							world.setBlockState(pos, world.getBlockState(pos).with(BlockSynthesizer.LIT, true));
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
			
			if (!isWorking) {
				if (progress != 0) {
					progress = 0;
				}
				if (lit) {
					lit = false;
					world.setBlockState(pos, world.getBlockState(pos).with(BlockSynthesizer.LIT, false));
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
	public void fromClientTag(CompoundTag tag) {
		fromTag(world.getBlockState(pos), tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return toTag(tag);
	}
	
	@Override
	public void sync() {
		recipeSync();
		BlockEntityClientSerializable.super.sync();
	}
	
	public void recipeSync() {
		EssentiaStack requirements = getRequirements();
		AoANetworking.sendEssentiaPacketWithRequirements(world, pos, 0, essentiaContainer, requirements);
	}
	
	public EssentiaStack getRequirements() {
		RecipeSynthesis recipe = world.getRecipeManager()
				.getFirstMatch(AoARecipes.SYNTHESIS, this, world).orElse(null);
		if (recipe == null) {
			return new EssentiaStack();
		} else {
			return recipe.getEssentia();
		}
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
		if (slot == 1) {
			return world.isReceivingRedstonePower(pos) && isValidInvStack(slot, stack);
		} else {
			return isValidInvStack(slot, stack);
		}
	}

	@Override
	public boolean canExtractInvStack(int slot, ItemStack stack, Direction dir) {
		if (slot == 1) {
			return TagRegistry.item(ArtOfAlchemy.id("containers")).contains(stack.getItem());
		} else if (slot == 2) {
			return world.isReceivingRedstonePower(pos);
		} else {
			return true;
		}
	}
	
}
