package io.github.synthrose.artofalchemy;

import io.github.synthrose.artofalchemy.block.AoABlocks;
import io.github.synthrose.artofalchemy.blockentity.AoABlockEntities;
import io.github.synthrose.artofalchemy.dispenser.AoADispenserBehavior;
import io.github.synthrose.artofalchemy.essentia.AoAEssentia;
import io.github.synthrose.artofalchemy.fluid.AoAFluids;
import io.github.synthrose.artofalchemy.gui.AoAContainers;
import io.github.synthrose.artofalchemy.item.AoAItems;
import io.github.synthrose.artofalchemy.network.AoANetworking;
import io.github.synthrose.artofalchemy.recipe.AoARecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArtOfAlchemy implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "artofalchemy";
    public static final String MOD_NAME = "Art of Alchemy";
    
    public static final ItemGroup ALCHEMY_GROUP = FabricItemGroupBuilder.create(ArtOfAlchemy.id("alchemy"))
    		.icon(() -> new ItemStack(AoAItems.ICON_ITEM)).build();

    @Override
    public void onInitialize() {
        log(Level.INFO, "Humankind cannot gain anything without first giving something in return. "
        		+ "To obtain, something of equal value must be lost.");
        
        AoAEssentia.registerEssentia();
        AoAFluids.registerFluids();
        AoABlocks.registerBlocks();
        AoAItems.registerItems();
        AoABlockEntities.registerBlockEntities();
        AoAContainers.registerContainers();
        AoARecipes.registerRecipes();
        AoADispenserBehavior.registerDispenserBehavior();
        AoANetworking.initializeNetworking();
    }
    
    public static Identifier id(String name) {
    	return new Identifier(MOD_ID, name);
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}