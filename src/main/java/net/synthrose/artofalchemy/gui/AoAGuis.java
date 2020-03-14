package net.synthrose.artofalchemy.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.screen.BlockContext;
import net.synthrose.artofalchemy.block.BlockCalcinator;
import net.synthrose.artofalchemy.block.BlockDissolver;

public class AoAGuis {

	public static void registerContainers() {
		ContainerProviderRegistry.INSTANCE.registerFactory(BlockCalcinator.getId(), (syncId, id, player, buf) ->
    		new ControllerCalcinator(syncId, player.inventory,
    		BlockContext.create(player.world, buf.readBlockPos())));
		ContainerProviderRegistry.INSTANCE.registerFactory(BlockDissolver.getId(), (syncId, id, player, buf) ->
			new ControllerDissolver(syncId, player.inventory,
			BlockContext.create(player.world, buf.readBlockPos())));
	}
	
	@Environment(EnvType.CLIENT)
	public static void registerScreens() {
        ScreenProviderRegistry.INSTANCE.registerFactory(BlockCalcinator.getId(), (syncId, id, player, buf) ->
    		new ScreenCalcinator(new ControllerCalcinator(syncId, player.inventory,
    		BlockContext.create(player.world, buf.readBlockPos())), player));
        ScreenProviderRegistry.INSTANCE.registerFactory(BlockDissolver.getId(), (syncId, id, player, buf) ->
			new ScreenDissolver(new ControllerDissolver(syncId, player.inventory,
			BlockContext.create(player.world, buf.readBlockPos())), player));
	}

}
