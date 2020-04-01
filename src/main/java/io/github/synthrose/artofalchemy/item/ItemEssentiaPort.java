package io.github.synthrose.artofalchemy.item;

import io.github.synthrose.artofalchemy.block.BlockPipe;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class ItemEssentiaPort extends Item {
    public final BlockPipe.IOFace IOFACE;

    public ItemEssentiaPort(Settings settings, BlockPipe.IOFace ioFace) {
        super(settings);
        IOFACE = ioFace;
    }

    public static Item getItem(BlockPipe.IOFace ioFace) {
        switch (ioFace) {
            case INSERTER:
                return AoAItems.ESSENTIA_INSERTER;
            case EXTRACTOR:
                return AoAItems.ESSENTIA_EXTRACTOR;
            case PASSIVE:
                return AoAItems.ESSENTIA_PORT;
            default:
                return Items.AIR;
        }
    }
}
