package io.github.synthrose.artofalchemy.block;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class BlockAnalyzer extends Block {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public static final Settings SETTINGS = Settings
        .of(Material.STONE)
        .strength(5.0f, 6.0f)
        .nonOpaque();

    public BlockAnalyzer() {
        super(SETTINGS);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public static Identifier getId() {
        return Registry.BLOCK.getId(AoABlocks.ANALYZER);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                              BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        ContainerProviderRegistry.INSTANCE.openContainer(getId(), player,
            (packetByteBuf -> packetByteBuf.writeBlockPos(pos)));

        return ActionResult.SUCCESS;
    }

}
