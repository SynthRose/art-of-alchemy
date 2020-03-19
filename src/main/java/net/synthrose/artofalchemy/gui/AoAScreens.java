package net.synthrose.artofalchemy.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.screen.ScreenHandlerContext;
import net.synthrose.artofalchemy.block.BlockCalcinator;
import net.synthrose.artofalchemy.block.BlockDissolver;
import net.synthrose.artofalchemy.block.BlockSynthesizer;

@Environment(EnvType.CLIENT)
public class AoAScreens {

	public static void registerScreens() {
        ScreenProviderRegistry.INSTANCE.registerFactory(BlockCalcinator.getId(), (syncId, id, player, buf) ->
    		new ScreenCalcinator(new ControllerCalcinator(syncId, player.inventory,
    		ScreenHandlerContext.create(player.world, buf.readBlockPos())), player));
        ScreenProviderRegistry.INSTANCE.registerFactory(BlockDissolver.getId(), (syncId, id, player, buf) ->
			new ScreenDissolver(new ControllerDissolver(syncId, player.inventory,
			ScreenHandlerContext.create(player.world, buf.readBlockPos())), player));
        ScreenProviderRegistry.INSTANCE.registerFactory(BlockSynthesizer.getId(), (syncId, id, player, buf) ->
			new ScreenSynthesizer(new ControllerSynthesizer(syncId, player.inventory,
			ScreenHandlerContext.create(player.world, buf.readBlockPos())), player));
	}

}
