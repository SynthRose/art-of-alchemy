package io.github.synthrose.artofalchemy.block;

import io.github.synthrose.artofalchemy.blockentity.BlockEntityDissolverPlus;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public class BlockDissolverPlus extends BlockDissolver {

	public static final Settings SETTINGS = Settings
		.of(Material.METAL)
		.strength(5.0f, 6.0f)
		.lightLevel((state) -> state.get(LIT) ? 15 : 0)
		.nonOpaque();

	public static Identifier getId() {
		return Registry.BLOCK.getId(AoABlocks.DISSOLVER_PLUS);
	}

	public BlockDissolverPlus() {
		super(SETTINGS);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new BlockEntityDissolverPlus();
	}

}
