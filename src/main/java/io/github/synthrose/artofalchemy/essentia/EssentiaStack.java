package io.github.synthrose.artofalchemy.essentia;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.synthrose.artofalchemy.AoAHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.*;

@SuppressWarnings("serial")
public class EssentiaStack extends HashMap<Essentia, Integer> {
	
	public EssentiaStack() {
		super();
	}
	
	public EssentiaStack(JsonObject obj) {
		obj.entrySet().forEach((entry) -> {
			Essentia essentia = RegistryEssentia.INSTANCE.get(new Identifier(entry.getKey()));
			if (essentia != null) {
				put(essentia, entry.getValue().getAsInt());
			} else {
				throw new JsonSyntaxException("Unknown essentia '" + entry.getKey() + "'");
			}
		});
	}
	
	public EssentiaStack(CompoundTag tag) {
		if (tag != null) {
			tag.getKeys().forEach((key) -> {
				Essentia essentia = RegistryEssentia.INSTANCE.get(new Identifier(key));
				if (essentia != null) {
					put(essentia, tag.getInt(key));
				}
			});
		}
	}

	public int getCount() {
		int sum = 0;
		for (int amount : values()) {
			sum += amount;
		}
		return sum;
	}
	
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		for (Essentia essentia : keySet()) {
			tag.putInt(RegistryEssentia.INSTANCE.getId(essentia).toString(), get(essentia));
		}
		return tag;
	}
	
	public List<Essentia> sortedList() {
		List<Essentia> list = new ArrayList<>();
		for (Essentia key : keySet()) {
			if (get(key) > 0) {
				list.add(key);
			}
		}
		list.sort((item1, item2) -> get(item2) - get(item1));
		return list;
	}
	
	// Mutating scalar multiplication for a single essentia type. Can go negative - try not to break things.
	public void multiply(Essentia essentia, int scalar) {
		this.put(essentia, this.getOrDefault(essentia, 0) * scalar);
	}
	public void multiply(Essentia essentia, double scalar) {
		this.put(essentia, (int) (this.getOrDefault(essentia, 0) * scalar));
	}
	
	// Mutating scalar multiplication. Can go negative - try not to break things.
	public void multiply(int scalar) {
		this.forEach((essentia, __) -> multiply(essentia, scalar));
	}
	public void multiply(double scalar) {
		this.forEach((essentia, __) -> multiply(essentia, scalar));
	}
	
	// Non-mutating scalar multiplication. Can go negative - try not to break things.
	public static EssentiaStack multiply(EssentiaStack inStack, int scalar) {
		EssentiaStack outStack = new EssentiaStack();
		inStack.forEach((essentia, amount) -> outStack.put(essentia, amount * scalar));
		return outStack;
	}

	// Non-mutating scalar multiplication. Can go negative - try not to break things.
	public static EssentiaStack multiply(EssentiaStack inStack, double scalar) {
		EssentiaStack outStack = new EssentiaStack();
		inStack.forEach((essentia, amount) -> outStack.put(essentia, (int) (amount * scalar)));
		return outStack;
	}

	// Non-mutating scalar multiplication. Can go negative - try not to break things.
	public static EssentiaStack multiplyCeil(EssentiaStack inStack, double scalar) {
		EssentiaStack outStack = new EssentiaStack();
		inStack.forEach((essentia, amount) -> outStack.put(essentia, (int) Math.ceil(amount * scalar)));
		return outStack;
	}
	
	// Mutating addition for a single essentia type.
	public void add(Essentia essentia, int amount) {
		this.put(essentia, this.getOrDefault(essentia, 0) + amount);
	}
	
	// Mutating addition.
	public void add(EssentiaStack other) {
		other.forEach(this::add);
	}
	
	// Non-mutating addition.
	public static EssentiaStack add(EssentiaStack stack1, EssentiaStack stack2) {
		EssentiaStack outStack = new EssentiaStack();
		Set<Essentia> union = new HashSet<>();
		union.addAll(stack1.keySet());
		union.addAll(stack2.keySet());
		union.forEach((essentia) -> outStack.put(essentia, stack1.getOrDefault(essentia, 0) + stack2.getOrDefault(essentia, 0)));
		return outStack;
	}
	
	// Mutating subtraction for a single essentia type.
	public void subtract(Essentia essentia, int amount) {
		this.put(essentia, Math.max(0, this.getOrDefault(essentia, 0) - amount));
	}
	
	// Mutating subtraction.
	public void subtract(EssentiaStack other) {
		other.forEach(this::subtract);
	}
	
	// Non-mutating subtraction.
	public static EssentiaStack subtract(EssentiaStack stack1, EssentiaStack stack2) {
		EssentiaStack outStack = new EssentiaStack();
		Set<Essentia> union = stack1.keySet();
		union.addAll(stack2.keySet());
		union.forEach((essentia) -> {
			int amount = Math.min(0, stack1.getOrDefault(essentia, 0) - stack2.getOrDefault(essentia, 0));
			outStack.put(essentia, amount);
		});
		return outStack;
	}
	
	// Returns true if this stack contains at least as much essentia of all types as the argument.
	public boolean contains(EssentiaStack other) {
		Set<Essentia> union = new HashSet<>();
		union.addAll(this.keySet());
		union.addAll(other.keySet());
		for (Essentia essentia : union) {
			if (this.getOrDefault(essentia, 0) < other.getOrDefault(essentia, 0)) {
				return false;
			}
		}
		return true;
	}

	public int getColor() {
		Vec3d colorSum = new Vec3d(0, 0, 0);
		double count = getCount();
		for (Essentia essentia : keySet()) {
			Vec3d color = AoAHelper.decimalColor(essentia.getColor());
			color = color.multiply(get(essentia) / count);
			colorSum = colorSum.add(color);
		}
		return AoAHelper.combineColor(colorSum);
	}
	
}
