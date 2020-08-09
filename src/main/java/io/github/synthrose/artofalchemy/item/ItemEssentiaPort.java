package io.github.synthrose.artofalchemy.item;

import io.github.synthrose.artofalchemy.block.AoABlocks;
import io.github.synthrose.artofalchemy.blockentity.BlockEntityPipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;

public class ItemEssentiaPort extends Item {
    public final BlockEntityPipe.IOFace IOFACE;

    public ItemEssentiaPort(Settings settings, BlockEntityPipe.IOFace ioFace) {
        super(settings);
        IOFACE = ioFace;
    }

    public static Item getItem(BlockEntityPipe.IOFace ioFace) {
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

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() == AoABlocks.PIPE) {
            return context.getWorld().getBlockState(context.getBlockPos()).onUse(context.getWorld(), context.getPlayer(), context.getHand(),
                    new BlockHitResult(context.getHitPos(), context.getSide(), context.getBlockPos(), context.hitsInsideBlock()));
        } else {
            return super.useOnBlock(context);
        }
    }
}
