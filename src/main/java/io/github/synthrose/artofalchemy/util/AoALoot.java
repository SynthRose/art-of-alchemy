package io.github.synthrose.artofalchemy.util;

import io.github.synthrose.artofalchemy.AoAConfig;
import io.github.synthrose.artofalchemy.item.AoAItems;
import io.github.synthrose.artofalchemy.item.ItemAlchemyFormula;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;

public class AoALoot {

    public static final Identifier[] LOOT_TABLES = new Identifier[]{
            LootTables.SIMPLE_DUNGEON_CHEST,
            LootTables.END_CITY_TREASURE_CHEST,
            LootTables.NETHER_BRIDGE_CHEST,
            LootTables.ABANDONED_MINESHAFT_CHEST,
            LootTables.SHIPWRECK_TREASURE_CHEST,
            LootTables.DESERT_PYRAMID_CHEST,
            LootTables.JUNGLE_TEMPLE_CHEST,
            LootTables.STRONGHOLD_LIBRARY_CHEST,
            LootTables.PILLAGER_OUTPOST_CHEST,
            LootTables.WOODLAND_MANSION_CHEST,
            LootTables.BURIED_TREASURE_CHEST,
            LootTables.FISHING_TREASURE_GAMEPLAY
    };

    public static void initialize() {
        if (AoAConfig.get().formulaLoot) {
            // Thanks, TheBrokenRail!
            LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
                if (isSelectedLootTable(id)) {
                    FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                            .withRolls(new ConstantLootTableRange(1))
                            .withEntry(ItemEntry.builder(AoAItems.ALCHEMY_FORMULA))
                            .withFunction((stack, ctx) -> {
                                ItemAlchemyFormula.setFormula(stack, AoAItems.PHILOSOPHERS_STONE);
                                return stack;
                            });
                    supplier.withPool(poolBuilder);
                }
            });
        }
    }

    private static boolean isSelectedLootTable(Identifier lootTable) {
        for (Identifier id : LOOT_TABLES) {
            if (id.equals(lootTable)) {
                return true;
            }
        }
        return false;
    }

}
