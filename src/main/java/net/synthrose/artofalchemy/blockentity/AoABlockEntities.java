package net.synthrose.artofalchemy.blockentity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import net.synthrose.artofalchemy.ArtOfAlchemy;
import net.synthrose.artofalchemy.block.AoABlocks;

public class AoABlockEntities {
	
	public static final BlockEntityType<?> CALCINATOR = BlockEntityType.Builder.create(
		BlockEntityCalcinator::new, AoABlocks.CALCINATOR).build(null);
	public static final BlockEntityType<?> DISSOLVER = BlockEntityType.Builder.create(
		BlockEntityDissolver::new, AoABlocks.DISSOLVER).build(null);
	public static final BlockEntityType<?> SYNTHESIZER = BlockEntityType.Builder.create(
		BlockEntitySynthesizer::new, AoABlocks.SYNTHESIZER).build(null);

	public static void registerBlockEntities() {
		register("calcination_furnace", CALCINATOR);
		register("dissolution_chamber", DISSOLVER);
		register("synthesis_table", SYNTHESIZER);
	}
	
	public static void register(String name, BlockEntityType<? extends BlockEntity> blockEntity) {
		Registry.register(Registry.BLOCK_ENTITY_TYPE, ArtOfAlchemy.id(name), blockEntity);
	}

}
