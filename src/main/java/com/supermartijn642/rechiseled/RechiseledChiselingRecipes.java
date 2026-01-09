package com.supermartijn642.rechiseled;

import net.minecraft.item.Items;

import static com.supermartijn642.rechiseled.Rechiseled.REGISTRATION;
import static com.supermartijn642.rechiseled.api.BaseChiselingRecipes.*;

/**
 * Created 05/05/2023 by SuperMartijn642
 */
public class RechiseledChiselingRecipes {

    public static void init(){
        // Acacia planks
        REGISTRATION.chiselingEntry(ACACIA_PLANKS, () -> Items.ACACIA_PLANKS, () -> Items.ACACIA_STAIRS, () -> Items.ACACIA_SLAB, null, null, null);
        // Andesite
        REGISTRATION.chiselingEntry(ANDESITE, () -> Items.ANDESITE, () -> Items.ANDESITE_STAIRS, () -> Items.ANDESITE_SLAB, null, null, null);
        // Basalt
        REGISTRATION.chiselingEntry(BASALT, () -> Items.BASALT, null);
        REGISTRATION.chiselingEntry(BASALT, () -> Items.POLISHED_BASALT, null);
        // Birch planks
        REGISTRATION.chiselingEntry(BIRCH_PLANKS, () -> Items.BIRCH_PLANKS, () -> Items.BIRCH_STAIRS, () -> Items.BIRCH_SLAB, null, null, null);
        // Blackstone
        REGISTRATION.chiselingEntry(BLACKSTONE, () -> Items.BLACKSTONE, () -> Items.BLACKSTONE_STAIRS, () -> Items.BLACKSTONE_SLAB, null, null, null);
        REGISTRATION.chiselingEntry(BLACKSTONE, () -> Items.POLISHED_BLACKSTONE_BRICKS, () -> Items.POLISHED_BLACKSTONE_BRICK_STAIRS, () -> Items.POLISHED_BLACKSTONE_BRICK_SLAB, null, null, null);
        REGISTRATION.chiselingEntry(BLACKSTONE, () -> Items.CRACKED_POLISHED_BLACKSTONE_BRICKS, null);
        REGISTRATION.chiselingEntry(BLACKSTONE, () -> Items.CHISELED_POLISHED_BLACKSTONE, null);
        // Blue ice
        REGISTRATION.chiselingEntry(BLUE_ICE, () -> Items.BLUE_ICE, null);
        // Coal block
        REGISTRATION.chiselingEntry(COAL_BLOCK, () -> Items.COAL_BLOCK, null);
        // Cobblestone
        REGISTRATION.chiselingEntry(COBBLESTONE, () -> Items.COBBLESTONE, () -> Items.COBBLESTONE_STAIRS, () -> Items.COBBLESTONE_SLAB, null, null, null);
        REGISTRATION.chiselingEntry(COBBLESTONE, () -> Items.MOSSY_COBBLESTONE, () -> Items.MOSSY_COBBLESTONE_STAIRS, () -> Items.MOSSY_COBBLESTONE_SLAB, null, null, null);
        // Crimson planks
        REGISTRATION.chiselingEntry(CRIMSON_PLANKS, () -> Items.CRIMSON_PLANKS, () -> Items.CRIMSON_STAIRS, () -> Items.CRIMSON_SLAB, null, null, null);
        // Dark oak planks
        REGISTRATION.chiselingEntry(DARK_OAK_PLANKS, () -> Items.DARK_OAK_PLANKS, () -> Items.DARK_OAK_STAIRS, () -> Items.DARK_OAK_SLAB, null, null, null);
        // Dark prismarine
        REGISTRATION.chiselingEntry(DARK_PRISMARINE, () -> Items.DARK_PRISMARINE, null);
        // Diorite
        REGISTRATION.chiselingEntry(DIORITE, () -> Items.DIORITE, () -> Items.DIORITE_STAIRS, () -> Items.DIORITE_SLAB, null, null, null);
        // Dirt
        REGISTRATION.chiselingEntry(DIRT, () -> Items.DIRT, null);
        // Emerald block
        REGISTRATION.chiselingEntry(EMERALD_BLOCK, () -> Items.EMERALD_BLOCK, null);
        // End stone
        REGISTRATION.chiselingEntry(END_STONE, () -> Items.END_STONE, null);
        REGISTRATION.chiselingEntry(END_STONE, () -> Items.END_STONE_BRICKS, () -> Items.END_STONE_BRICK_STAIRS, () -> Items.END_STONE_BRICK_SLAB, null, null, null);
        // Glowstone
        REGISTRATION.chiselingEntry(GLOWSTONE, () -> Items.GLOWSTONE, null);
        // Gold block
        REGISTRATION.chiselingEntry(GOLD_BLOCK, () -> Items.GOLD_BLOCK, null);
        // Granite
        REGISTRATION.chiselingEntry(GRANITE, () -> Items.GRANITE, () -> Items.GRANITE_STAIRS, () -> Items.GRANITE_SLAB, null, null, null);
        // Jungle planks
        REGISTRATION.chiselingEntry(JUNGLE_PLANKS, () -> Items.JUNGLE_PLANKS, () -> Items.JUNGLE_STAIRS, () -> Items.JUNGLE_SLAB, null, null, null);
        // Netherrack
        REGISTRATION.chiselingEntry(NETHERRACK, () -> Items.NETHERRACK, null);
        // Nether bricks
        REGISTRATION.chiselingEntry(NETHER_BRICKS, () -> Items.NETHER_BRICKS, () -> Items.NETHER_BRICK_STAIRS, () -> Items.NETHER_BRICK_SLAB, null, null, null);
        REGISTRATION.chiselingEntry(NETHER_BRICKS, () -> Items.CHISELED_NETHER_BRICKS, null);
        REGISTRATION.chiselingEntry(NETHER_BRICKS, () -> Items.CRACKED_NETHER_BRICKS, null);
        // Netherite
        REGISTRATION.chiselingEntry(NETHERITE_BLOCK, () -> Items.NETHERITE_BLOCK, null);
        // Oak planks
        REGISTRATION.chiselingEntry(OAK_PLANKS, () -> Items.OAK_PLANKS, () -> Items.OAK_STAIRS, () -> Items.OAK_SLAB, null, null, null);
        // Obsidian
        REGISTRATION.chiselingEntry(OBSIDIAN, () -> Items.OBSIDIAN, null);
        // Prismarine bricks
        REGISTRATION.chiselingEntry(PRISMARINE_BRICKS, () -> Items.PRISMARINE_BRICKS, () -> Items.PRISMARINE_BRICK_STAIRS, () -> Items.PRISMARINE_BRICK_SLAB, null, null, null);
        // Purpur block
        REGISTRATION.chiselingEntry(PURPUR_BLOCK, () -> Items.PURPUR_PILLAR, null);
        // Quartz block
        REGISTRATION.chiselingEntry(QUARTZ_BLOCK, () -> Items.QUARTZ_BRICKS, null);
        REGISTRATION.chiselingEntry(QUARTZ_BLOCK, () -> Items.QUARTZ_PILLAR, null);
        REGISTRATION.chiselingEntry(QUARTZ_BLOCK, () -> Items.CHISELED_QUARTZ_BLOCK, null);
        REGISTRATION.chiselingEntry(QUARTZ_BLOCK, () -> Items.SMOOTH_QUARTZ, () -> Items.SMOOTH_QUARTZ_STAIRS, () -> Items.SMOOTH_QUARTZ_SLAB, null, null, null);
        // Red nether bricks
        REGISTRATION.chiselingEntry(RED_NETHER_BRICKS, () -> Items.RED_NETHER_BRICKS, () -> Items.RED_NETHER_BRICK_STAIRS, () -> Items.RED_NETHER_BRICK_SLAB, null, null, null);
        // Red sandstone
        REGISTRATION.chiselingEntry(RED_SANDSTONE, () -> Items.RED_SANDSTONE, () -> Items.RED_SANDSTONE_STAIRS, () -> Items.RED_SANDSTONE_SLAB, null, null, null);
        REGISTRATION.chiselingEntry(RED_SANDSTONE, () -> Items.CHISELED_RED_SANDSTONE, null);
        REGISTRATION.chiselingEntry(RED_SANDSTONE, () -> Items.CUT_RED_SANDSTONE, null, () -> Items.CUT_RED_SANDSTONE_SLAB, null, null, null);
        REGISTRATION.chiselingEntry(RED_SANDSTONE, () -> Items.SMOOTH_RED_SANDSTONE, () -> Items.SMOOTH_RED_SANDSTONE_STAIRS, () -> Items.SMOOTH_RED_SANDSTONE_SLAB, null, null, null);
        // Redstone block
        REGISTRATION.chiselingEntry(REDSTONE_BLOCK, () -> Items.REDSTONE_BLOCK, null);
        // Sandstone
        REGISTRATION.chiselingEntry(SANDSTONE, () -> Items.SANDSTONE, () -> Items.SANDSTONE_STAIRS, () -> Items.SANDSTONE_SLAB, null, null, null);
        REGISTRATION.chiselingEntry(SANDSTONE, () -> Items.CHISELED_SANDSTONE, null);
        REGISTRATION.chiselingEntry(SANDSTONE, () -> Items.CUT_SANDSTONE, null, null, null, null, null);
        REGISTRATION.chiselingEntry(SANDSTONE, () -> Items.SMOOTH_SANDSTONE, () -> Items.SMOOTH_SANDSTONE_STAIRS, () -> Items.SMOOTH_SANDSTONE_SLAB, null, null, null);
        // Spruce planks
        REGISTRATION.chiselingEntry(SPRUCE_PLANKS, () -> Items.SPRUCE_PLANKS, () -> Items.SPRUCE_STAIRS, () -> Items.SPRUCE_SLAB, null, null, null);
        // Stone
        REGISTRATION.chiselingEntry(STONE, () -> Items.STONE, () -> Items.STONE_STAIRS, () -> Items.STONE_SLAB, null, null, null);
        REGISTRATION.chiselingEntry(STONE, () -> Items.STONE_BRICKS, () -> Items.STONE_BRICK_STAIRS, () -> Items.STONE_BRICK_SLAB, null, null, null);
        REGISTRATION.chiselingEntry(STONE, () -> Items.MOSSY_STONE_BRICKS, () -> Items.MOSSY_STONE_BRICK_STAIRS, () -> Items.MOSSY_STONE_BRICK_SLAB, null, null, null);
        REGISTRATION.chiselingEntry(STONE, () -> Items.CRACKED_STONE_BRICKS, null);
        // Warped planks
        REGISTRATION.chiselingEntry(WARPED_PLANKS, () -> Items.WARPED_PLANKS, () -> Items.WARPED_STAIRS, () -> Items.WARPED_SLAB, null, null, null);
    }
}
