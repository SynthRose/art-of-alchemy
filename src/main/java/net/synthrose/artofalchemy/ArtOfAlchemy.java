package net.synthrose.artofalchemy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.synthrose.artofalchemy.block.Blocks;
import net.synthrose.artofalchemy.blockentity.BlockEntities;
import net.synthrose.artofalchemy.gui.Guis;
import net.synthrose.artofalchemy.item.Items;
import net.synthrose.artofalchemy.recipe.Recipes;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArtOfAlchemy implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "artofalchemy";
    public static final String MOD_NAME = "Art of Alchemy";
    
    public static final ItemGroup ALCHEMY_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "alchemy"))
    		.icon(() -> new ItemStack(Items.ICON_ITEM)).build();

    @Override
    public void onInitialize() {
        log(Level.INFO, "Humankind cannot gain anything without first giving something in return. To obtain, something of equal value must be lost.");
        
        Items.registerItems();
        Blocks.registerBlocks();
        BlockEntities.registerBlockEntities();
        Guis.registerContainers();
        Recipes.registerRecipes();
        
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}