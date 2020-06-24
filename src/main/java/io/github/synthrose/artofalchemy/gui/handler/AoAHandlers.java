package io.github.synthrose.artofalchemy.gui.handler;

import io.github.synthrose.artofalchemy.block.*;
import io.github.synthrose.artofalchemy.item.ItemJournal;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Hand;

import java.lang.reflect.InvocationTargetException;

public class AoAHandlers {

	public static ScreenHandlerType<HandlerCalcinator>  CALCINATOR;
	public static ScreenHandlerType<HandlerDissolver>   DISSOLVER;
	public static ScreenHandlerType<HandlerSynthesizer> SYNTHESIZER;
	public static ScreenHandlerType<HandlerAnalyzer>    ANALYZER;
	public static ScreenHandlerType<HandlerProjector>   PROJECTOR;
	public static ScreenHandlerType<HandlerJournal>     JOURNAL;

	public static void registerHandlers() {
		CALCINATOR = ScreenHandlerRegistry.registerExtended(BlockCalcinator.getId(), defaultFactory(HandlerCalcinator.class));
		DISSOLVER = ScreenHandlerRegistry.registerExtended(BlockDissolver.getId(), defaultFactory(HandlerDissolver.class));
		SYNTHESIZER = ScreenHandlerRegistry.registerExtended(BlockSynthesizer.getId(), defaultFactory(HandlerSynthesizer.class));
		PROJECTOR = ScreenHandlerRegistry.registerExtended(BlockProjector.getId(), defaultFactory(HandlerProjector.class));
		ANALYZER = ScreenHandlerRegistry.registerExtended(BlockAnalyzer.getId(),
				(syncId, inventory, buf) -> new HandlerAnalyzer(syncId, inventory, ScreenHandlerContext.EMPTY));
		JOURNAL = ScreenHandlerRegistry.registerExtended(ItemJournal.getId(),
				(syncId, inventory, buf) -> new HandlerJournal(syncId, inventory, ScreenHandlerContext.EMPTY,
						buf.readEnumConstant(Hand.class)));
	}

	// I'm going to forget what this does in 2 weeks and then my eyes will glaze over when I try to fix some really dumb bug
	// I am a pile of garbage pretending to be a programmer and I should not be allowed near computers
	private static <T extends ScreenHandler> ScreenHandlerRegistry.ExtendedClientHandlerFactory<T> defaultFactory(Class<T> klass) {
		return (syncId, inventory, buf) -> {
			try {
				return klass.getDeclaredConstructor(int.class, PlayerInventory.class, ScreenHandlerContext.class)
						.newInstance(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos()));
			} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
		};
	}

}
