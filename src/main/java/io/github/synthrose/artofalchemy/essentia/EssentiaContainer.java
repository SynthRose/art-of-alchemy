package io.github.synthrose.artofalchemy.essentia;

import blue.endless.jankson.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class EssentiaContainer {

	private final EssentiaStack contents = new EssentiaStack();
	private final Set<Essentia> whitelist = new HashSet<>();
	private Integer capacity = null;
	private boolean input = true;
	private boolean output = true;
	private boolean infinite = false;
	private boolean whitelistEnabled = false;
	
	public EssentiaContainer() {
		
	}
	
	public EssentiaContainer(CompoundTag tag) {
		if (tag != null) {
			if (tag.contains("essentia")) {
				CompoundTag essentiaTag = tag.getCompound("essentia");
				this.setContents(new EssentiaStack(essentiaTag));
			}
			if (tag.contains("whitelist")) {
				whitelistEnabled = true;
				ListTag list = tag.getList("whitelist", 8);
				for (int i = 0; i < list.size(); i++) {
					Identifier id = new Identifier(list.getString(i));
					this.whitelist(RegistryEssentia.INSTANCE.get(id));
				}
			}
			if (tag.contains("capacity")) {
				if (tag.getString("capacity").equals("unlimited")) {
					this.setUnlimitedCapacity();
				} else {
					this.setCapacity(tag.getInt("capacity"));
				}
				
			}
			if (tag.contains("infinite")) {
				this.setInfinite(tag.getBoolean("infinite"));
			}
			if (tag.contains("whitelist_enabled")) {
				this.setWhitelistEnabled(tag.getBoolean("whitelist_enabled"));
			}
			if (tag.contains("input")) {
				this.setInput(tag.getBoolean("input"));
			}
			if (tag.contains("output")) {
				this.setOutput(tag.getBoolean("output"));
			}	
		}
	}
	
	@Nullable
	public static EssentiaContainer of(ItemStack item) {
		EssentiaContainer container;
		if (item.hasTag() && item.getTag().contains("contents")) {
			container = new EssentiaContainer(item.getTag().getCompound("contents"));
		} else {
			container = null;
		}
		return container;
	}
	
	public ItemStack in(ItemStack item) {
		CompoundTag tag;
		if (item.hasTag()) {
			tag = item.getTag();
		} else {
			tag = new CompoundTag();
		}
		tag.put("contents", toTag());
		item.setTag(tag);
		return item;
	}
	
	public EssentiaStack getContents() {
		return contents;
	}

	public EssentiaContainer setContents(EssentiaStack essentia) {
		this.contents.clear();
		if (essentia != null) {
			this.contents.putAll(essentia);
		}
		return this;
	}
	
	public Set<Essentia> getWhitelist() {
		return whitelist;
	}
	
	public EssentiaContainer setWhitelist(Set<Essentia> whitelist) {
		this.whitelist.clear();
		if (whitelist != null) {
			this.whitelist.addAll(whitelist);
		}
		return this;
	}
	
	public int getCapacity() {
		return capacity;
	}

	public EssentiaContainer setCapacity(int capacity) {
		this.capacity = capacity;
		return this;
	}
	
	public EssentiaContainer setUnlimitedCapacity() {
		this.capacity = null;
		return this;
	}
	
	public boolean hasUnlimitedCapacity() {
		return this.capacity == null;
	}

	public boolean isInput() {
		return input;
	}
	
	public EssentiaContainer setInput(boolean input) {
		this.input = input;
		return this;
	}
	
	public boolean isOutput() {
		return output;
	}
	
	public EssentiaContainer setOutput(boolean output) {
		this.output = output;
		return this;
	}
	
	public boolean isInfinite() {
		return infinite;
	}
	
	public EssentiaContainer setInfinite(boolean infinite) {
		this.infinite = infinite;
		return this;
	}
	
	public boolean isWhitelistEnabled() {
		return whitelistEnabled;
	}
	
	public EssentiaContainer setWhitelistEnabled(boolean whitelistEnabled) {
		this.whitelistEnabled = whitelistEnabled;
		return this;
	}
	
	public EssentiaContainer whitelist(Essentia essentia) {
		whitelist.add(essentia);
		return this;
	}
	
	public EssentiaContainer blacklist(Essentia essentia) {
		whitelist.remove(essentia);
		return this;
	}
	
	// Clears any essentia in violation of the whitelist; returns true if any essentia was deleted
	public boolean enforceWhitelist() {
		if (whitelistEnabled) {
			boolean removed = false;
			for (Essentia key : contents.keySet()) {
				if (!whitelist.contains(key)) {
					contents.remove(key);
					removed = true;
				}
			}
			return removed;
		} else {
			return false;
		}
	}
	
	public int getCount(Essentia essentia) {
		if (essentia != null) {
			return contents.getOrDefault(essentia, 0);
		} else {
			return 0;
		}
	}
	
	public int getCount() {
		return contents.getCount();
	}
	
	public boolean isEmpty() {
		for (int amount : contents.values()) {
			if (amount != 0) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isFull() {
		if (capacity != null) {
			return getCount() >= capacity;
		} else {
			return false;
		}
	}
	
	public boolean contains(EssentiaStack query) {
		return contents.contains(query);
	}
	
	public boolean emptyContents() {
		if (!isEmpty()) {
			this.contents.clear();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean whitelisted(Essentia essentia) {
		return (!whitelistEnabled || whitelist.contains(essentia));
	}
	
	public boolean canAcceptIgnoreIO(EssentiaStack query) {
		if (whitelistEnabled) {
			for (Essentia key : query.keySet()) {
				if (query.getOrDefault(key, 0) != 0 && !whitelisted(key)) {
					return false;
				}
			}
		}
		if (capacity == null) {
			return true;
		} else {
			return (this.getCount() + query.getCount()) <= capacity;
		}
	}
	
	public boolean canAccept(EssentiaStack query) {
		if (!input) {
			return false;
		} else {
			return canAcceptIgnoreIO(query);
		}
	}
	
	public boolean canProvideIgnoreIO(EssentiaStack query) {
		if (whitelistEnabled) {
			for (Essentia key : query.keySet()) {
				if (query.getOrDefault(key, 0) != 0 && !whitelisted(key)) {
					return false;
				}
			}
		}
		if (infinite) {
			return true;
		} else {
			return contains(query);
		}
	}
	
	public boolean canProvide(EssentiaStack query) {
		if (!output) {
			return false;
		} else {
			return canProvideIgnoreIO(query);
		}
	}
	
	public boolean addEssentia(EssentiaStack stack) {
		if (canAcceptIgnoreIO(stack)) {
			contents.add(stack);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean subtractEssentia(EssentiaStack stack) {
		if (canProvideIgnoreIO(stack)) {
			contents.subtract(stack);
			return true;
		} else {
			return false;
		}
	}
	
	// Push an entire stack to another container, failing if any essentia couldn't transfer
	public boolean pushEntireStack(EssentiaContainer other, EssentiaStack stack) {
		if (this.canProvide(stack) && other.canAccept(stack)) {
			if (!this.infinite) {
				this.contents.subtract(stack);
			}
			if (!other.infinite) {
				other.contents.add(stack);
			}
			return true;
		} else {
			return false;
		}
	}
	
	// Pull an entire stack from another container, failing if any essentia couldn't transfer
	public boolean pullEntireStack(EssentiaContainer other, EssentiaStack stack) {
		return other.pushEntireStack(this, stack);
	}
	
	// Push the entire contents of this container, failing if any essentia couldn't transfer
	public boolean pushEntireContents(EssentiaContainer other) {
		return pushEntireStack(other, this.contents);
	}
	
	// Pull the entire contents of another container, failing if any essentia couldn't transfer
	public boolean pullEntireContents(EssentiaContainer other) {
		return pullEntireStack(other, other.contents);
	}
	
	// Push as much as possible of a stack to another container, returning the essentia transferred
	public EssentiaStack pushStack(EssentiaContainer other, EssentiaStack stack, boolean force) {
		if (!force && (!this.output || !other.input)) {
			return new EssentiaStack();
		} else {
			EssentiaStack transferred = new EssentiaStack();
			for (Entry<Essentia, Integer> entry : stack.entrySet()) {
				Essentia key = entry.getKey();
				int value = entry.getValue();
				if (this.whitelisted(key) && other.whitelisted(key)) {
					int transferAmt = value;
					if (other.capacity != null) {
						transferAmt = Math.min(transferAmt, other.getCapacity() - other.getCount());
					}
					if (!this.infinite) {
						transferAmt = Math.min(transferAmt, this.getCount(key));
						this.contents.subtract(key, transferAmt);
					}
					if (!other.infinite) {
						other.contents.add(key, transferAmt);
					}
					transferred.put(key, transferAmt);
				}
			}
			return transferred;
		}
	}
	
	// Pull as much as possible of a stack from another container, returning the essentia transferred
	public EssentiaStack pullStack(EssentiaContainer other, EssentiaStack stack, boolean force) {
		return other.pushStack(this, stack, force);
	}
	
	// Push as much as possible of this container's contents to another, returning the essentia transferred
	public EssentiaStack pushContents(EssentiaContainer other, boolean force) {
		return pushStack(other, this.contents, force);
	}
	
	// Pull as much as possible of another container's contents, returning the essentia transferred
	public EssentiaStack pullContents(EssentiaContainer other, boolean force) {
		return pullStack(other, other.contents, force);
	}
	
	public EssentiaStack pushStack(EssentiaContainer other, EssentiaStack stack) {
		return pushStack(other, stack, false);
	}
	
	public EssentiaStack pullStack(EssentiaContainer other, EssentiaStack stack) {
		return pullStack(other, stack, false);
	}
	
	public EssentiaStack pushContents(EssentiaContainer other) {
		return pushContents(other, false);
	}
	
	public EssentiaStack pullContents(EssentiaContainer other) {
		return pullContents(other, false);
	}
	
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.put("essentia", getContents().toTag());
		ListTag list = new ListTag();
		for (Essentia essentia : getWhitelist()) {
			list.add(StringTag.of(RegistryEssentia.INSTANCE.getId(essentia).toString()));
		}
		tag.put("whitelist", list);
		tag.putBoolean("whitelist_enabled", isWhitelistEnabled());
		tag.putBoolean("infinite", isInfinite());
		if (capacity == null) {
			tag.putString("capacity", "unlimited");
		} else {
			tag.putInt("capacity", getCapacity());
		}
		tag.putBoolean("input", isInput());
		tag.putBoolean("output", isOutput());
		return tag;
	}
	
}
