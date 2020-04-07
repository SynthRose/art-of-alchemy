package io.github.synthrose.artofalchemy.gui.controller;

import io.github.synthrose.artofalchemy.block.BlockAnalyzer;
import io.github.synthrose.artofalchemy.block.BlockCalcinator;
import io.github.synthrose.artofalchemy.block.BlockDissolver;
import io.github.synthrose.artofalchemy.block.BlockSynthesizer;
import io.github.synthrose.artofalchemy.item.ItemJournal;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Hand;

public class AoAContainers {

	public static void registerContainers() {
		ContainerProviderRegistry.INSTANCE.registerFactory(BlockCalcinator.getId(), (syncId, id, player, buf) ->
				new ControllerCalcinator(syncId, player.inventory,
						ScreenHandlerContext.create(player.world, buf.readBlockPos())));
		ContainerProviderRegistry.INSTANCE.registerFactory(BlockDissolver.getId(), (syncId, id, player, buf) ->
				new ControllerDissolver(syncId, player.inventory,
						ScreenHandlerContext.create(player.world, buf.readBlockPos())));
		ContainerProviderRegistry.INSTANCE.registerFactory(BlockSynthesizer.getId(), (syncId, id, player, buf) ->
				new ControllerSynthesizer(syncId, player.inventory,
						ScreenHandlerContext.create(player.world, buf.readBlockPos())));
		ContainerProviderRegistry.INSTANCE.registerFactory(BlockAnalyzer.getId(), (syncId, id, player, buf) ->
				new ControllerAnalyzer(syncId, player.inventory,
						ScreenHandlerContext.create(player.world, buf.readBlockPos())));
		ContainerProviderRegistry.INSTANCE.registerFactory(ItemJournal.getId(), (syncId, id, player, buf) ->
				new ControllerJournal(syncId, player.inventory,
						ScreenHandlerContext.create(player.world, buf.readBlockPos()), buf.readEnumConstant(Hand.class)));
	}

}
