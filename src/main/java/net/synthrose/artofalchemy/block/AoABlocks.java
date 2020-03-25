package net.synthrose.artofalchemy.block;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.synthrose.artofalchemy.ArtOfAlchemy;
import net.synthrose.artofalchemy.essentia.Essentia;
import net.synthrose.artofalchemy.essentia.RegistryEssentia;
import net.synthrose.artofalchemy.item.AoAItems;

public class AoABlocks {
	
	public static final Block CALCINATOR = new BlockCalcinator();
	public static final Block DISSOLVER = new BlockDissolver();
	public static final Block SYNTHESIZER = new BlockSynthesizer();
	public static final Block PIPE = new BlockPipe();
	
	public static final Block ALKAHEST = new BlockAlkahest();
	public static Map<Essentia, Block> ESSENTIA = new HashMap<>();
	
	public static void registerBlocks() {
		register("calcination_furnace", CALCINATOR);
		register("dissolution_chamber", DISSOLVER);
		register("synthesis_table", SYNTHESIZER);
		register("essentia_pipe", PIPE);
		
		registerItemless("alkahest", ALKAHEST);
		
		// Register essentia fluid blocks; add-on essentia fluids will be registered to THEIR namespace
		RegistryEssentia.INSTANCE.forEach((Essentia essentia, Identifier id) -> {
			Identifier blockId = new Identifier(id.getNamespace(), "essentia_" + id.getPath());
			ESSENTIA.put(essentia, registerItemless(blockId, new BlockEssentia(essentia)));
		});
	}
	
	public static Block register(String name, Block block) {
		return register(name, block, Rarity.COMMON);
	}
	
	public static Block register(String name, Block block, Rarity rarity) {
		AoAItems.register(name, new BlockItem(block, AoAItems.defaults(rarity)));
		return registerItemless(name, block);
	}
	
	public static Block registerItemless(String name, Block block) {
		return registerItemless(ArtOfAlchemy.id(name), block);
	}
	
	public static Block registerItemless(Identifier id, Block block) {
		return Registry.register(Registry.BLOCK, id, block);
	}
	
}
