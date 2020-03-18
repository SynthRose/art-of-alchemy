package net.synthrose.artofalchemy.blockentity;

import java.util.Collection;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.tag.RegistryTagContainer;
import net.minecraft.tag.Tag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.synthrose.artofalchemy.ArtOfAlchemy;
import net.synthrose.artofalchemy.ImplementedInventory;
import net.synthrose.artofalchemy.block.BlockSynthesizer;
import net.synthrose.artofalchemy.essentia.EssentiaContainer;
import net.synthrose.artofalchemy.essentia.HasEssentia;
import net.synthrose.artofalchemy.item.ItemMateria;
import net.synthrose.artofalchemy.network.AoANetworking;
import net.synthrose.artofalchemy.essentia.EssentiaStack;
import net.synthrose.artofalchemy.recipe.RecipeSynthesis;
import net.synthrose.artofalchemy.recipe.AoARecipes;

public class BlockEntitySynthesizer extends BlockEntity implements ImplementedInventory,
	Tickable, PropertyDelegateHolder, BlockEntityClientSerializable, HasEssentia {
	
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
				} else if (outSlot.getItem() == targetSlot.getItem()) {
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
		
		if (container != Ingredient.EMPTY || outSlot.isEmpty()) {
			items.set(1, new ItemStack(targetSlot.getItem()));
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
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
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
						AoANetworking.sendEssentiaPacket(world, pos, 0, essentiaContainer);
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
		fromTag(tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return toTag(tag);
	}
	
}
