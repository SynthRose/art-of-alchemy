package net.synthrose.artofalchemy;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import net.minecraft.nbt.CompoundTag;

public class EssentiaSerializer {

	public static Map<EssentiaType, Integer> jsonToMap(JsonObject obj) {
		Map<EssentiaType, Integer> map = new HashMap<>();
		for (EssentiaType essentiaType : EssentiaType.values()) {
			String essentiaName = essentiaType.getName();
			if (obj.has(essentiaName)) {
				int essentiaValue = obj.get(essentiaName).getAsInt();
				map.put(essentiaType, essentiaValue);
			}
		}
		return map;
	}
	
	public static Map<EssentiaType, Integer> tagToMap(CompoundTag tag) {
		Map<EssentiaType, Integer> map = new HashMap<>();
		for (EssentiaType essentiaType : EssentiaType.values()) {
			String essentiaName = essentiaType.getName();
			if (tag.contains(essentiaName)) {
				int essentiaValue = tag.getInt(essentiaName);
				map.put(essentiaType, essentiaValue);
			}
		}
		return map;
	}
	
	public static CompoundTag mapToTag(Map<EssentiaType, Integer> map) {
		CompoundTag tag = new CompoundTag();
		for (EssentiaType essentia : map.keySet()) {
			tag.putInt(essentia.getName(), map.get(essentia));
		}
		return tag;
	}

}
