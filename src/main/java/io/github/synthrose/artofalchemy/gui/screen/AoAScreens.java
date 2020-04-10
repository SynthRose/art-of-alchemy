package io.github.synthrose.artofalchemy.gui.screen;

import io.github.synthrose.artofalchemy.block.*;
import io.github.synthrose.artofalchemy.gui.controller.*;
import io.github.synthrose.artofalchemy.item.ItemJournal;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Hand;

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
		ScreenProviderRegistry.INSTANCE.registerFactory(BlockProjector.getId(), (syncId, id, player, buf) ->
				new ScreenProjector(new ControllerProjector(syncId, player.inventory,
						ScreenHandlerContext.create(player.world, buf.readBlockPos())), player));
		ScreenProviderRegistry.INSTANCE.registerFactory(ItemJournal.getId(), (syncId, id, player, buf) ->
				new ScreenJournal(new ControllerJournal(syncId, player.inventory,
						ScreenHandlerContext.create(player.world, buf.readBlockPos()), buf.readEnumConstant(Hand.class)), player));
	}

}
