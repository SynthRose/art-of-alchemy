package net.synthrose.artofalchemy.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.synthrose.artofalchemy.ArtOfAlchemy;
import net.synthrose.artofalchemy.item.AoAItems;

public class AoABlocks {
	
	public static final Block CALCINATOR = new BlockCalcinator();
	public static Block ALKAHEST = new BlockAlkahest();
	
	public static void registerBlocks() {
		register("calcination_furnace", CALCINATOR);
		registerItemless("alkahest", ALKAHEST);
	}
	
	public static Block register(String name, Block block) {
		return register(name, block, Rarity.COMMON);
	}
	
	public static Block register(String name, Block block, Rarity rarity) {
		AoAItems.register(name, new BlockItem(block, AoAItems.defaults(rarity)));
		return registerItemless(name, block);
	}
	
	public static Block registerItemless(String name, Block block) {
		return Registry.register(Registry.BLOCK, new Identifier(ArtOfAlchemy.MOD_ID, name), block);
	}
	
}
