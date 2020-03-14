package net.synthrose.artofalchemy.item;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.synthrose.artofalchemy.ArtOfAlchemy;
import net.synthrose.artofalchemy.EssentiaType;
import net.synthrose.artofalchemy.MateriaRank;
import net.synthrose.artofalchemy.fluid.AoAFluids;

public class AoAItems {

	public static final Item ICON_ITEM = new Item(new Item.Settings());	
	public static final Item AZOTH = new Item(defaults());
	
	public static final Item MATERIA_OMEGA = new ItemMateria(defaults(Rarity.RARE), MateriaRank.OMEGA);
	public static final Item MATERIA_S = new ItemMateria(defaults(Rarity.UNCOMMON), MateriaRank.S);
	public static final Item MATERIA_A = new ItemMateria(defaults(), MateriaRank.A);
	public static final Item MATERIA_B = new ItemMateria(defaults(), MateriaRank.B);
	public static final Item MATERIA_C = new ItemMateria(defaults(), MateriaRank.C);
	public static final Item MATERIA_D = new ItemMateria(defaults(), MateriaRank.D);
	public static final Item MATERIA_E = new ItemMateria(defaults(), MateriaRank.E);
	public static final Item MATERIA_F = new ItemMateria(defaults(), MateriaRank.F);
	
	public static Item ALKAHEST_BUCKET = new BucketItem(AoAFluids.ALKAHEST, defaults().maxCount(1));
	public static Map<EssentiaType, Item> ESSENTIA_BUCKETS = new HashMap<>();
	
	public static void registerItems() {
		register("icon_item", ICON_ITEM);
		register("azoth", AZOTH);
		
		register("materia_omega", MATERIA_OMEGA);
		register("materia_s", MATERIA_S);
		register("materia_a", MATERIA_A);
		register("materia_b", MATERIA_B);
		register("materia_c", MATERIA_C);
		register("materia_d", MATERIA_D);
		register("materia_e", MATERIA_E);
		register("materia_f", MATERIA_F);
		
		register("alkahest_bucket", ALKAHEST_BUCKET);
		for (EssentiaType essentia : EssentiaType.values()) {
			ESSENTIA_BUCKETS.put(essentia, register("essentia_bucket_" + essentia.getName(),
					new BucketItem(AoAFluids.ESSENTIA_FLUIDS.get(essentia), defaults().maxCount(1))));
		}
	}
	
	public static Item register(String name, Item item) {
		return Registry.register(Registry.ITEM, new Identifier(ArtOfAlchemy.MOD_ID, name), item);
	}
	
	public static Settings defaults() {
		return defaults(Rarity.COMMON);
	}
	
	public static Settings defaults(Rarity rarity) {
		return new Item.Settings().rarity(rarity).group(ArtOfAlchemy.ALCHEMY_GROUP);
	}
}
