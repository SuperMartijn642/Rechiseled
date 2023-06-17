package com.supermartijn642.rechiseled;

import net.minecraft.world.item.Items;

import static com.supermartijn642.rechiseled.Rechiseled.REGISTRATION;
import static com.supermartijn642.rechiseled.api.BaseChiselingRecipes.*;

/**
 * Created 05/05/2023 by SuperMartijn642
 */
public class RechiseledChiselingRecipes {

    public static void init(){
        // Amethyst block
        REGISTRATION.chiselingEntry(AMETHYST_BLOCK, () -> Items.AMETHYST_BLOCK, null);
        // Acacia planks
        REGISTRATION.chiselingEntry(ACACIA_PLANKS, () -> Items.ACACIA_PLANKS, null);
        // Andesite
        REGISTRATION.chiselingEntry(ANDESITE, () -> Items.ANDESITE, null);
        // Basalt
        REGISTRATION.chiselingEntry(BASALT, () -> Items.BASALT, null);
        REGISTRATION.chiselingEntry(BASALT, () -> Items.POLISHED_BASALT, null);
        REGISTRATION.chiselingEntry(BASALT, () -> Items.SMOOTH_BASALT, null);
        // Bamboo planks
        REGISTRATION.chiselingEntry(BAMBOO_PLANKS, () -> Items.BAMBOO_PLANKS, null);
        REGISTRATION.chiselingEntry(BAMBOO_PLANKS, () -> Items.BAMBOO_MOSAIC, null);
        // Birch planks
        REGISTRATION.chiselingEntry(BIRCH_PLANKS, () -> Items.BIRCH_PLANKS, null);
        // Blackstone
        REGISTRATION.chiselingEntry(BLACKSTONE, () -> Items.BLACKSTONE, null);
        REGISTRATION.chiselingEntry(BLACKSTONE, () -> Items.POLISHED_BLACKSTONE_BRICKS, null);
        REGISTRATION.chiselingEntry(BLACKSTONE, () -> Items.CRACKED_POLISHED_BLACKSTONE_BRICKS, null);
        REGISTRATION.chiselingEntry(BLACKSTONE, () -> Items.CHISELED_POLISHED_BLACKSTONE, null);
        // Cherry planks
        REGISTRATION.chiselingEntry(CHERRY_PLANKS, () -> Items.CHERRY_PLANKS, null);
        // Coal block
        REGISTRATION.chiselingEntry(COAL_BLOCK, () -> Items.COAL_BLOCK, null);
        // Cobbled deepslate
        REGISTRATION.chiselingEntry(COBBLED_DEEPSLATE, () -> Items.COBBLED_DEEPSLATE, null);
        REGISTRATION.chiselingEntry(COBBLED_DEEPSLATE, () -> Items.POLISHED_DEEPSLATE, null);
        REGISTRATION.chiselingEntry(COBBLED_DEEPSLATE, () -> Items.DEEPSLATE_BRICKS, null);
        REGISTRATION.chiselingEntry(COBBLED_DEEPSLATE, () -> Items.CRACKED_DEEPSLATE_BRICKS, null);
        REGISTRATION.chiselingEntry(COBBLED_DEEPSLATE, () -> Items.DEEPSLATE_TILES, null);
        REGISTRATION.chiselingEntry(COBBLED_DEEPSLATE, () -> Items.CRACKED_DEEPSLATE_TILES, null);
        REGISTRATION.chiselingEntry(COBBLED_DEEPSLATE, () -> Items.CHISELED_DEEPSLATE, null);
        // Cobblestone
        REGISTRATION.chiselingEntry(COBBLESTONE, () -> Items.COBBLESTONE, null);
        REGISTRATION.chiselingEntry(COBBLESTONE, () -> Items.MOSSY_COBBLESTONE, null);
        // Copper block
        REGISTRATION.chiselingEntry(COPPER_BLOCK, () -> Items.COPPER_BLOCK, null);
        REGISTRATION.chiselingEntry(COPPER_BLOCK, () -> Items.CUT_COPPER, null);
        // Crimson planks
        REGISTRATION.chiselingEntry(CRIMSON_PLANKS, () -> Items.CRIMSON_PLANKS, null);
        // Dark oak planks
        REGISTRATION.chiselingEntry(DARK_OAK_PLANKS, () -> Items.DARK_OAK_PLANKS, null);
        // Dark prismarine
        REGISTRATION.chiselingEntry(DARK_PRISMARINE, () -> Items.DARK_PRISMARINE, null);
        // Diorite
        REGISTRATION.chiselingEntry(DIORITE, () -> Items.DIORITE, null);
        // Dirt
        REGISTRATION.chiselingEntry(DIRT, () -> Items.DIRT, null);
        // Emerald block
        REGISTRATION.chiselingEntry(EMERALD_BLOCK, () -> Items.EMERALD_BLOCK, null);
        // End stone
        REGISTRATION.chiselingEntry(END_STONE, () -> Items.END_STONE, null);
        REGISTRATION.chiselingEntry(END_STONE, () -> Items.END_STONE_BRICKS, null);
        // Glowstone
        REGISTRATION.chiselingEntry(GLOWSTONE, () -> Items.GLOWSTONE, null);
        // Gold block
        REGISTRATION.chiselingEntry(GOLD_BLOCK, () -> Items.GOLD_BLOCK, null);
        // Granite
        REGISTRATION.chiselingEntry(GRANITE, () -> Items.GRANITE, null);
        // Jungle planks
        REGISTRATION.chiselingEntry(JUNGLE_PLANKS, () -> Items.JUNGLE_PLANKS, null);
        // Mangrove planks
        REGISTRATION.chiselingEntry(MANGROVE_PLANKS, () -> Items.MANGROVE_PLANKS, null);
        // Netherrack
        REGISTRATION.chiselingEntry(NETHERRACK, () -> Items.NETHERRACK, null);
        // Nether bricks
        REGISTRATION.chiselingEntry(NETHER_BRICKS, () -> Items.NETHER_BRICKS, null);
        REGISTRATION.chiselingEntry(NETHER_BRICKS, () -> Items.CHISELED_NETHER_BRICKS, null);
        REGISTRATION.chiselingEntry(NETHER_BRICKS, () -> Items.CRACKED_NETHER_BRICKS, null);
        // Oak planks
        REGISTRATION.chiselingEntry(OAK_PLANKS, () -> Items.OAK_PLANKS, null);
        // Obsidian
        REGISTRATION.chiselingEntry(OBSIDIAN, () -> Items.OBSIDIAN, null);
        // Prismarine bricks
        REGISTRATION.chiselingEntry(PRISMARINE_BRICKS, () -> Items.PRISMARINE_BRICKS, null);
        // Purpur block
        REGISTRATION.chiselingEntry(PURPUR_BLOCK, () -> Items.PURPUR_PILLAR, null);
        // Quartz block
        REGISTRATION.chiselingEntry(QUARTZ_BLOCK, () -> Items.QUARTZ_BRICKS, null);
        REGISTRATION.chiselingEntry(QUARTZ_BLOCK, () -> Items.QUARTZ_PILLAR, null);
        REGISTRATION.chiselingEntry(QUARTZ_BLOCK, () -> Items.CHISELED_QUARTZ_BLOCK, null);
        REGISTRATION.chiselingEntry(QUARTZ_BLOCK, () -> Items.SMOOTH_QUARTZ, null);
        // Red nether bricks
        REGISTRATION.chiselingEntry(RED_NETHER_BRICKS, () -> Items.RED_NETHER_BRICKS, null);
        // Red sandstone
        REGISTRATION.chiselingEntry(RED_SANDSTONE, () -> Items.RED_SANDSTONE, null);
        REGISTRATION.chiselingEntry(RED_SANDSTONE, () -> Items.CHISELED_RED_SANDSTONE, null);
        REGISTRATION.chiselingEntry(RED_SANDSTONE, () -> Items.CUT_RED_SANDSTONE, null);
        REGISTRATION.chiselingEntry(RED_SANDSTONE, () -> Items.SMOOTH_RED_SANDSTONE, null);
        // Sandstone
        REGISTRATION.chiselingEntry(SANDSTONE, () -> Items.SANDSTONE, null);
        REGISTRATION.chiselingEntry(SANDSTONE, () -> Items.CHISELED_SANDSTONE, null);
        REGISTRATION.chiselingEntry(SANDSTONE, () -> Items.CUT_SANDSTONE, null);
        REGISTRATION.chiselingEntry(SANDSTONE, () -> Items.SMOOTH_SANDSTONE, null);
        // Spruce planks
        REGISTRATION.chiselingEntry(SPRUCE_PLANKS, () -> Items.SPRUCE_PLANKS, null);
        // Stone
        REGISTRATION.chiselingEntry(STONE, () -> Items.STONE, null);
        REGISTRATION.chiselingEntry(STONE, () -> Items.STONE_BRICKS, null);
        REGISTRATION.chiselingEntry(STONE, () -> Items.MOSSY_STONE_BRICKS, null);
        REGISTRATION.chiselingEntry(STONE, () -> Items.CRACKED_STONE_BRICKS, null);
        // Warped planks
        REGISTRATION.chiselingEntry(WARPED_PLANKS, () -> Items.WARPED_PLANKS, null);
    }
}
