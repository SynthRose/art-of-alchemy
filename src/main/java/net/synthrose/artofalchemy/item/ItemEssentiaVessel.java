package net.synthrose.artofalchemy.item;

import java.util.HashSet;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.synthrose.artofalchemy.essentia.Essentia;
import net.synthrose.artofalchemy.essentia.EssentiaContainer;
import net.synthrose.artofalchemy.essentia.HasEssentia;

public class ItemEssentiaVessel extends Item {
	
	public final Essentia TYPE;
	private String translationKey;
	
	public ItemEssentiaVessel(Settings settings, Essentia type) {
		super(settings.maxCount(1));
		TYPE = type;
	}
	
	public ItemEssentiaVessel(Settings settings) {
		this(settings, null);
	}
	
	public boolean isEmpty() {
		return false;
	}
	
	public EssentiaContainer getContainer(ItemStack stack) {
		EssentiaContainer container;
		if (stack.hasTag() && stack.getTag().contains("contents")) {
			container = new EssentiaContainer(stack.getTag().getCompound("contents"));
		} else {
			container = new EssentiaContainer().setCapacity(4000);
		}
		if (TYPE != null) {
			container.whitelist(TYPE).setWhitelistEnabled(true);
		}
		return container;
	}
	
	public void setContainer(ItemStack stack, EssentiaContainer container) {
		if (TYPE != null) {
			container.setWhitelist(new HashSet<Essentia>()).whitelist(TYPE).setWhitelistEnabled(true);
		}
		CompoundTag tag;
		if (stack.hasTag()) {
			tag = stack.getTag();
		} else {
			tag = new CompoundTag();
		}
		tag.put("contents", container.toTag());
		stack.setTag(tag);
	}
	
	@Override
	public void onCraft(ItemStack stack, World world, PlayerEntity player) {
		EssentiaContainer container = new EssentiaContainer();
		setContainer(stack, container);
		super.onCraft(stack, world, player);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		EssentiaContainer container = getContainer(ctx.getStack());
		BlockEntity be = ctx.getWorld().getBlockEntity(ctx.getBlockPos());
		if (be != null && be instanceof HasEssentia) {
			HasEssentia target = (HasEssentia) be;
			int transferred = 0;
			for (int i = 0; i < target.getNumContainers() && transferred == 0; i++) {
				EssentiaContainer other = target.getContainer(i);
				int pulled = container.pullContents(other).getCount();
				transferred += pulled;
			}
			for (int i = 0; i < target.getNumContainers() && transferred == 0; i++) {
				EssentiaContainer other = target.getContainer(i);
				int pushed = container.pushContents(other).getCount();
				transferred += pushed;
			}
			setContainer(ctx.getStack(), container);
			if (transferred == 0) {
				return ActionResult.PASS;
			} else {
				be.markDirty();
				return ActionResult.SUCCESS;
			}
		} else {
			return ActionResult.PASS;
		}
	}
	
	@Override
	protected String getOrCreateTranslationKey() {
		if (translationKey == null) {
			translationKey = Util.createTranslationKey("item",
				Registry.ITEM.getId(AoAItems.ESSENTIA_VESSELS.get(null)));
		}

		return this.translationKey;
	}
	
	@Override
	public String getTranslationKey() {
		return getOrCreateTranslationKey();
	}

}
