package io.github.synthrose.artofalchemy.gui;

import io.github.synthrose.artofalchemy.block.BlockAnalyzer;
import io.github.synthrose.artofalchemy.block.BlockCalcinator;
import io.github.synthrose.artofalchemy.block.BlockDissolver;
import io.github.synthrose.artofalchemy.block.BlockSynthesizer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.screen.ScreenHandlerContext;

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
		ScreenProviderRegistry.INSTANCE.registerFactory(BlockAnalyzer.getId(), (syncId, id, player, buf) ->
			new ScreenAnalyzer(new ControllerAnalyzer(syncId, player.inventory,
			ScreenHandlerContext.create(player.world, buf.readBlockPos())), player));
	}

}
