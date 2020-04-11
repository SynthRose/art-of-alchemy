package io.github.synthrose.artofalchemy.block;

import io.github.synthrose.artofalchemy.util.MateriaRank;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.MathHelper;

public class BlockMateria extends Block {

    private final MateriaRank rank;

    public BlockMateria(MateriaRank rank) {
        super(makeSettings(rank.tier));
        this.rank = rank;
    }

    private static Settings makeSettings(int tier) {
        return FabricBlockSettings.of(Material.SAND)
                .sounds(BlockSoundGroup.BASALT)
                .lightLevel(MathHelper.clamp(tier * 5 - 15, 0, 15))
                .strength(tier * 0.25f + 0.5f, tier * 0.25f + 0.5f)
                .breakByTool(FabricToolTags.SHOVELS).build();
    }

    public MateriaRank getRank() {
        return rank;
    }

    public int getTier() {
        if (rank == null) {
            return 0;
        } else {
            return rank.tier;
        }
    }

}
