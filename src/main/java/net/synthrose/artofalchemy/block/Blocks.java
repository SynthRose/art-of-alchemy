package net.synthrose.artofalchemy.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.synthrose.artofalchemy.ArtOfAlchemy;

public class Blocks {
	
	public static final Block CALCINATOR = new BlockCalcinator(FabricBlockSettings
			.of(Material.STONE)
			.hardness(5.0f).resistance(6.0f)
			.build());
	
	public static void registerBlocks() {
		register("calcination_furnace", CALCINATOR);
	}
	
	public static void register(String name, Block block) {
		Registry.register(Registry.BLOCK, new Identifier(ArtOfAlchemy.MOD_ID, name), block);
		Registry.register(Registry.ITEM, new Identifier(ArtOfAlchemy.MOD_ID, name),
				new BlockItem(block, itemDefaults()));
	}
	
	public static void register(String name, Block block, Rarity rarity) {
		Registry.register(Registry.BLOCK, new Identifier(ArtOfAlchemy.MOD_ID, name), block);
		Registry.register(Registry.ITEM, new Identifier(ArtOfAlchemy.MOD_ID, name),
				new BlockItem(block, itemDefaults(rarity)));
	}
	
	public static Settings itemDefaults() {
		return new Item.Settings().group(ArtOfAlchemy.ALCHEMY_GROUP);
	}
	
	public static Settings itemDefaults(Rarity rarity) {
		return new Item.Settings().rarity(rarity).group(ArtOfAlchemy.ALCHEMY_GROUP);
	}
}
