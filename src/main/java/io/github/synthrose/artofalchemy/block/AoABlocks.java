package io.github.synthrose.artofalchemy.block;

import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import io.github.synthrose.artofalchemy.MateriaRank;
import io.github.synthrose.artofalchemy.essentia.Essentia;
import io.github.synthrose.artofalchemy.essentia.RegistryEssentia;
import io.github.synthrose.artofalchemy.item.AoAItems;
import io.github.synthrose.artofalchemy.item.BlockItemMateria;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class AoABlocks {
	
	public static final Block CALCINATOR = new BlockCalcinator();
	public static final Block DISSOLVER = new BlockDissolver();
	public static final Block SYNTHESIZER = new BlockSynthesizer();
	public static final Block ANALYZER = new BlockAnalyzer();
	public static final Block PIPE = new BlockPipe();

	public static final Map<MateriaRank, Block> MATERIA_BLOCKS = new HashMap<>();

	public static final Block ALKAHEST = new BlockAlkahest();
	public static final Map<Essentia, Block> ESSENTIA = new HashMap<>();
	
	public static void registerBlocks() {
		register("calcination_furnace", CALCINATOR);
		register("dissolution_chamber", DISSOLVER);
		register("synthesis_table", SYNTHESIZER);
		register("analysis_desk", ANALYZER);
		register("essentia_pipe", PIPE);
		
		registerItemless("alkahest", ALKAHEST);

		// Register materia dusts
		for (MateriaRank rank : MateriaRank.values()) {
			String name = "materia_block_" + rank.toString().toLowerCase();
			BlockMateria block = new BlockMateria(rank);
			MATERIA_BLOCKS.put(rank, registerItemless(name, block));
			AoAItems.register(name, new BlockItemMateria(block, AoAItems.defaults()));
		}
		
		// Register essentia fluid blocks; add-on essentia fluids will be registered to THEIR namespace
		RegistryEssentia.INSTANCE.forEach((Essentia essentia, Identifier id) -> {
			Identifier blockId = new Identifier(id.getNamespace(), "essentia_" + id.getPath());
			ESSENTIA.put(essentia, registerItemless(blockId, new BlockEssentia(essentia)));
		});
	}

	
	public static Block register(String name, Block block) {
		AoAItems.register(name, new BlockItem(block, AoAItems.defaults()));
		return registerItemless(name, block);
	}
	
	public static Block registerItemless(String name, Block block) {
		return registerItemless(ArtOfAlchemy.id(name), block);
	}
	
	public static Block registerItemless(Identifier id, Block block) {
		return Registry.register(Registry.BLOCK, id, block);
	}
	
}
