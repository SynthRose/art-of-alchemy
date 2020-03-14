package net.synthrose.artofalchemy.block;

import java.util.HashMap;
import java.util.Map;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.synthrose.artofalchemy.ArtOfAlchemy;
import net.synthrose.artofalchemy.EssentiaType;
import net.synthrose.artofalchemy.item.AoAItems;

public class AoABlocks {
	
	public static final Block CALCINATOR = new BlockCalcinator();
	public static final Block DISSOLVER = new BlockDissolver();
	public static final Block ALKAHEST = new BlockAlkahest();
	public static Map<EssentiaType, Block> ESSENTIA = new HashMap<>();
	
	public static void registerBlocks() {
		register("calcination_furnace", CALCINATOR);
		register("dissolution_chamber", DISSOLVER);
		
		registerItemless("alkahest", ALKAHEST);
		for (EssentiaType essentia : EssentiaType.values()) {
			ESSENTIA.put(essentia, registerItemless("essentia_" + essentia.getName(), new BlockEssentia(essentia)));
		}
		
		ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) ->
			0xAA0077, DISSOLVER);
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
