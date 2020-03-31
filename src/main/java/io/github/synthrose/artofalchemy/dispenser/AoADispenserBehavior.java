package io.github.synthrose.artofalchemy.dispenser;

import io.github.synthrose.artofalchemy.transport.HasAlkahest;
import io.github.synthrose.artofalchemy.item.AoAItems;
import io.github.synthrose.artofalchemy.item.ItemEssentiaVessel;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Direction;

public class AoADispenserBehavior {
	
	protected static final DispenserBehavior VESSEL_BEHAVIOR = (pointer, stack) -> {
		Direction facing = pointer.getBlockState().get(DispenserBlock.FACING);
		BlockEntity be = pointer.getWorld().getBlockEntity(pointer.getBlockPos().offset(facing));
		ItemEssentiaVessel.useStackOnBE(stack, be);
		return stack;
	};
	
	protected static final DispenserBehavior ALKAHEST_BEHAVIOR = (pointer, stack) -> {
		Direction facing = pointer.getBlockState().get(DispenserBlock.FACING);
		BlockEntity be = pointer.getWorld().getBlockEntity(pointer.getBlockPos().offset(facing));
		if (be instanceof HasAlkahest) {
			boolean successful = ((HasAlkahest) be).addAlkahest(1000);
			if (successful) {
				be.getWorld().playSound(null, be.getPos(), SoundEvents.ITEM_BUCKET_EMPTY,
						SoundCategory.BLOCKS, 1.0F, 1.0F);
				return new ItemStack(Items.BUCKET);
			}
		}
		return stack;
	};

	public static void registerDispenserBehavior() {
		AoAItems.ESSENTIA_VESSELS.forEach((essentia, item) -> DispenserBlock.registerBehavior(item, VESSEL_BEHAVIOR));
		DispenserBlock.registerBehavior(AoAItems.ALKAHEST_BUCKET, ALKAHEST_BEHAVIOR);
	}
	
}
