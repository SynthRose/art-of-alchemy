package net.synthrose.artofalchemy.gui;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.screen.BlockContext;
import net.synthrose.artofalchemy.block.BlockCalcinator;
import net.synthrose.artofalchemy.block.BlockDissolver;
import net.synthrose.artofalchemy.block.BlockSynthesizer;

public class AoAContainers {

	public static void registerContainers() {
		ContainerProviderRegistry.INSTANCE.registerFactory(BlockCalcinator.getId(), (syncId, id, player, buf) ->
    		new ControllerCalcinator(syncId, player.inventory,
    		BlockContext.create(player.world, buf.readBlockPos())));
		ContainerProviderRegistry.INSTANCE.registerFactory(BlockDissolver.getId(), (syncId, id, player, buf) ->
			new ControllerDissolver(syncId, player.inventory,
			BlockContext.create(player.world, buf.readBlockPos())));
		ContainerProviderRegistry.INSTANCE.registerFactory(BlockSynthesizer.getId(), (syncId, id, player, buf) ->
			new ControllerSynthesizer(syncId, player.inventory,
			BlockContext.create(player.world, buf.readBlockPos())));
	}

}
