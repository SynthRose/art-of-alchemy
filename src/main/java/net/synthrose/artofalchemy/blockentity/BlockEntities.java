package net.synthrose.artofalchemy.blockentity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.synthrose.artofalchemy.ArtOfAlchemy;
import net.synthrose.artofalchemy.block.Blocks;

public class BlockEntities {
	
	public static final BlockEntityType<?> CALCINATION_FURNACE = BlockEntityType.Builder.create(
			BlockEntityCalcinator::new, Blocks.CALCINATOR).build(null);

	public static void registerBlockEntities() {
		register("calcination_furnace", CALCINATION_FURNACE);
	}
	
	public static void register(String name, BlockEntityType<? extends BlockEntity> blockEntity) {
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(ArtOfAlchemy.MOD_ID, name), blockEntity);
	}

}
