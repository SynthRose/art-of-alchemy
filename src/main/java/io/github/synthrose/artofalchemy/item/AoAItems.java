package io.github.synthrose.artofalchemy.item;

import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import io.github.synthrose.artofalchemy.MateriaRank;
import io.github.synthrose.artofalchemy.essentia.Essentia;
import io.github.synthrose.artofalchemy.essentia.RegistryEssentia;
import io.github.synthrose.artofalchemy.fluid.AoAFluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class AoAItems {

	public static final Item ICON_ITEM = new Item(new Item.Settings());	
	public static final Item AZOTH = new Item(defaults());
	public static final Item ALCHEMY_FORMULA = new ItemAlchemyFormula(defaults().maxCount(1));

	public static final Map<MateriaRank, Item> MATERIA_DUSTS = new HashMap<>();
	
	public static final Item ALKAHEST_BUCKET = new BucketItem(AoAFluids.ALKAHEST, defaults().maxCount(1));
	public static final Map<Essentia, Item> ESSENTIA_BUCKETS = new HashMap<>();
	public static final Map<Essentia, Item> ESSENTIA_VESSELS = new HashMap<>();
	
	public static void registerItems() {
		register("icon_item", ICON_ITEM);
		register("azoth", AZOTH);
		
		register("alchemy_formula", ALCHEMY_FORMULA);

		// Register materia dusts
		for (MateriaRank rank : MateriaRank.values()) {
			String name = "materia_" + rank.toString().toLowerCase();
			MATERIA_DUSTS.put(rank, register(name, new ItemMateria(defaults(), rank)));
		}
		
		register("alkahest_bucket", ALKAHEST_BUCKET);
		
		// Register essentia buckets; add-on essentia buckets will be registered to THEIR namespace
		RegistryEssentia.INSTANCE.forEach((Essentia essentia, Identifier id) -> {
			Identifier itemId = new Identifier(id.getNamespace(), "essentia_bucket_" + id.getPath());
			ESSENTIA_BUCKETS.put(essentia, register(itemId,
					new BucketItem(AoAFluids.ESSENTIA_FLUIDS.get(essentia), defaults().maxCount(1))));
		});
		
		// Register essentia vessels; add-on essentia buckets will be registered to THEIR namespace
		ESSENTIA_VESSELS.put(null, register(ArtOfAlchemy.id("essentia_vessel"),
				new ItemEssentiaVessel(defaults(), null)));
		RegistryEssentia.INSTANCE.forEach((Essentia essentia, Identifier id) -> {
			Identifier itemId = new Identifier(id.getNamespace(), "essentia_vessel_" + id.getPath());
			ESSENTIA_VESSELS.put(essentia, register(itemId, new ItemEssentiaVessel(defaults(), essentia)));
		});
	}
	
	public static Item register(String name, Item item) {
		return register(ArtOfAlchemy.id(name), item);
	}

	public static Item register(Identifier id, Item item) {
		return Registry.register(Registry.ITEM, id, item);
	}

	public static Settings defaults() {
		return new Item.Settings().group(ArtOfAlchemy.ALCHEMY_GROUP);
	}
}
