package net.synthrose.artofalchemy.blockentity;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.MathHelper;
import net.synthrose.artofalchemy.FuelHelper;
import net.synthrose.artofalchemy.ImplementedInventory;
import net.synthrose.artofalchemy.block.BlockCalcinator;
import net.synthrose.artofalchemy.item.Items;

public class BlockEntityCalcinator extends BlockEntity
	implements ImplementedInventory, Tickable, PropertyDelegateHolder {
	
	private int OPERATION_TIME = 20;
	
	private int fuel = 0;
	private int maxFuel = 20;
	private int progress = 0;
	private int maxProgress = OPERATION_TIME;
	
	protected final DefaultedList<ItemStack> items = DefaultedList.ofSize(3, ItemStack.EMPTY);
	protected final PropertyDelegate delegate = new PropertyDelegate() {
		
		@Override
		public int size() {
			return 4;
		}
		
		@Override
		public void set(int index, int value) {
			switch(index) {
			case 0:
				fuel = value;
				break;
			case 1:
				maxFuel = value;
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
				return fuel;
			case 1:
				return maxFuel;
			case 2:
				return progress;
			case 3:
				return maxProgress;
			default:
				return 0;
			}
		}
		
	};
	
	public BlockEntityCalcinator() {
		super(BlockEntities.CALCINATION_FURNACE);
	}
	
	private boolean isActive() {
		return fuel > 0;
	}
	
	private boolean canCraft() {
		ItemStack inSlot = items.get(0);
		ItemStack outSlot = items.get(2);
		ItemStack outStack = new ItemStack(Items.MATERIA_F, 1);
		
		if (inSlot.isEmpty()) {
			return false;
		} else if (outSlot.isEmpty()) {
			return true;
		} else if (outSlot.getItem() == outStack.getItem()) {
			if (outSlot.getCount() <= outSlot.getMaxCount() - outStack.getCount()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private void doCraft() {
		ItemStack inSlot = items.get(0);
		ItemStack outSlot = items.get(2);
		ItemStack outStack = new ItemStack(Items.MATERIA_F, 1);
		
		if (!canCraft()) {
			return;
		} else {
			inSlot.decrement(1);
			if (outSlot.isEmpty()) {
				items.set(2, outStack.copy());
			} else {
				outSlot.increment(outStack.getCount());
			}
		}
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("fuel", fuel);
		tag.putInt("progress", progress);
		Inventories.toTag(tag, items);
		return super.toTag(tag);
	}
	
	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		Inventories.fromTag(tag, items);
		fuel = tag.getInt("fuel");
		progress = tag.getInt("progress");
		maxFuel = FuelHelper.fuelTime(items.get(1));
		maxProgress = OPERATION_TIME;
	}

	@Override
	public DefaultedList<ItemStack> getItems() {
		return items;
	}
	
	@Override
	public boolean isValidInvStack(int slot, ItemStack stack) {
		if (slot == 2) {
			return false;
		} else if (slot == 1) {
			return FuelHelper.isFuel(stack);
		} else {
			return true;
		}
	}
	

	@Override
	public void tick() {
		boolean wasActive = isActive();
		boolean dirty = false;
		
		if (wasActive) {
			fuel = MathHelper.clamp(fuel - 2, 0, maxFuel);
			dirty = true;
		}
		
		if (!world.isClient()) {
			ItemStack fuelSlot = items.get(1);
					
			if (!isActive()) {
				if (!fuelSlot.isEmpty() && FuelHelper.isFuel(fuelSlot) && canCraft()) {
					maxFuel = FuelHelper.fuelTime(fuelSlot);
					fuel += maxFuel;
					fuelSlot.decrement(1);
				} else if (progress != 0) {
					progress = 0;
				}
			}
			
			if (isActive() && canCraft()) {
				if (progress < maxProgress) {
					progress++;
				}
				if (progress >= maxProgress) {
					progress -= maxProgress;
					doCraft();
				}
			} else if (progress != 0) {
				progress = 0;
			}
			
			if (isActive() != wasActive) {
				dirty = true;
				world.setBlockState(pos, world.getBlockState(pos).with(BlockCalcinator.LIT, isActive()));
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
	
}
