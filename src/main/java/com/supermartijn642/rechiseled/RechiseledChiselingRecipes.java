package com.supermartijn642.rechiseled;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

import static com.supermartijn642.rechiseled.Rechiseled.REGISTRATION;
import static com.supermartijn642.rechiseled.api.BaseChiselingRecipes.*;

/**
 * Created 05/05/2023 by SuperMartijn642
 */
public class RechiseledChiselingRecipes {

    public static void init(){
        // Amethyst block
        regularBlockOnly(AMETHYST_BLOCK, () -> Items.AMETHYST_BLOCK);
        // Acacia planks
        regularSet(ACACIA_PLANKS, () -> Items.ACACIA_PLANKS, () -> Items.ACACIA_STAIRS, () -> Items.ACACIA_SLAB);
        // Andesite
        regularSet(ANDESITE, () -> Items.ANDESITE, () -> Items.ANDESITE_STAIRS, () -> Items.ANDESITE_SLAB);
        // Basalt
        regularBlockOnly(BASALT, () -> Items.BASALT);
        regularBlockOnly(BASALT, () -> Items.POLISHED_BASALT);
        regularBlockOnly(BASALT, () -> Items.SMOOTH_BASALT);
        // Bamboo planks
        regularSet(BAMBOO_PLANKS, () -> Items.BAMBOO_PLANKS, () -> Items.BAMBOO_STAIRS, () -> Items.BAMBOO_SLAB);
        regularBlockOnly(BAMBOO_PLANKS, () -> Items.BAMBOO_MOSAIC);
        // Birch planks
        regularSet(BIRCH_PLANKS, () -> Items.BIRCH_PLANKS, () -> Items.BIRCH_STAIRS, () -> Items.BIRCH_SLAB);
        // Blackstone
        regularSet(BLACKSTONE, () -> Items.BLACKSTONE, () -> Items.BLACKSTONE_STAIRS, () -> Items.BLACKSTONE_SLAB);
        regularSet(BLACKSTONE, () -> Items.POLISHED_BLACKSTONE_BRICKS, () -> Items.POLISHED_BLACKSTONE_BRICK_STAIRS, () -> Items.POLISHED_BLACKSTONE_BRICK_SLAB);
        regularBlockOnly(BLACKSTONE, () -> Items.CRACKED_POLISHED_BLACKSTONE_BRICKS);
        regularBlockOnly(BLACKSTONE, () -> Items.CHISELED_POLISHED_BLACKSTONE);
        // Blue ice
        regularBlockOnly(BLUE_ICE, () -> Items.BLUE_ICE);
        // Cherry planks
        regularSet(CHERRY_PLANKS, () -> Items.CHERRY_PLANKS, () -> Items.CHERRY_STAIRS, () -> Items.CHERRY_SLAB);
        // Coal block
        regularBlockOnly(COAL_BLOCK, () -> Items.COAL_BLOCK);
        // Cobbled deepslate
        regularSet(COBBLED_DEEPSLATE, () -> Items.COBBLED_DEEPSLATE, () -> Items.COBBLED_DEEPSLATE_STAIRS, () -> Items.COBBLED_DEEPSLATE_SLAB);
        regularSet(COBBLED_DEEPSLATE, () -> Items.POLISHED_DEEPSLATE, () -> Items.POLISHED_DEEPSLATE_STAIRS, () -> Items.POLISHED_DEEPSLATE_SLAB);
        regularSet(COBBLED_DEEPSLATE, () -> Items.DEEPSLATE_BRICKS, () -> Items.DEEPSLATE_BRICK_STAIRS, () -> Items.DEEPSLATE_BRICK_SLAB);
        regularBlockOnly(COBBLED_DEEPSLATE, () -> Items.CRACKED_DEEPSLATE_BRICKS);
        regularSet(COBBLED_DEEPSLATE, () -> Items.DEEPSLATE_TILES, () -> Items.DEEPSLATE_TILE_STAIRS, () -> Items.DEEPSLATE_TILE_SLAB);
        regularBlockOnly(COBBLED_DEEPSLATE, () -> Items.CRACKED_DEEPSLATE_TILES);
        regularBlockOnly(COBBLED_DEEPSLATE, () -> Items.CHISELED_DEEPSLATE);
        // Cobblestone
        regularSet(COBBLESTONE, () -> Items.COBBLESTONE, () -> Items.COBBLESTONE_STAIRS, () -> Items.COBBLESTONE_SLAB);
        regularSet(COBBLESTONE, () -> Items.MOSSY_COBBLESTONE, () -> Items.MOSSY_COBBLESTONE_STAIRS, () -> Items.MOSSY_COBBLESTONE_SLAB);
        // Copper block
        regularBlockOnly(COPPER_BLOCK, () -> Items.COPPER_BLOCK);
        // Crimson planks
        regularSet(CRIMSON_PLANKS, () -> Items.CRIMSON_PLANKS, () -> Items.CRIMSON_STAIRS, () -> Items.CRIMSON_SLAB);
        // Dark oak planks
        regularSet(DARK_OAK_PLANKS, () -> Items.DARK_OAK_PLANKS, () -> Items.DARK_OAK_STAIRS, () -> Items.DARK_OAK_SLAB);
        // Dark prismarine
        regularBlockOnly(DARK_PRISMARINE, () -> Items.DARK_PRISMARINE);
        // Diorite
        regularSet(DIORITE, () -> Items.DIORITE, () -> Items.DIORITE_STAIRS, () -> Items.DIORITE_SLAB);
        // Dirt
        regularBlockOnly(DIRT, () -> Items.DIRT);
        // Emerald block
        regularBlockOnly(EMERALD_BLOCK, () -> Items.EMERALD_BLOCK);
        // End stone
        regularBlockOnly(END_STONE, () -> Items.END_STONE);
        regularSet(END_STONE, () -> Items.END_STONE_BRICKS, () -> Items.END_STONE_BRICK_STAIRS, () -> Items.END_STONE_BRICK_SLAB);
        // Glowstone
        regularBlockOnly(GLOWSTONE, () -> Items.GLOWSTONE);
        // Gold block
        regularBlockOnly(GOLD_BLOCK, () -> Items.GOLD_BLOCK);
        // Granite
        regularSet(GRANITE, () -> Items.GRANITE, () -> Items.GRANITE_STAIRS, () -> Items.GRANITE_SLAB);
        // Jungle planks
        regularSet(JUNGLE_PLANKS, () -> Items.JUNGLE_PLANKS, () -> Items.JUNGLE_STAIRS, () -> Items.JUNGLE_SLAB);
        // Mangrove planks
        regularSet(MANGROVE_PLANKS, () -> Items.MANGROVE_PLANKS, () -> Items.MANGROVE_STAIRS, () -> Items.MANGROVE_SLAB);
        // Netherrack
        regularBlockOnly(NETHERRACK, () -> Items.NETHERRACK);
        // Nether bricks
        regularSet(NETHER_BRICKS, () -> Items.NETHER_BRICKS, () -> Items.NETHER_BRICK_STAIRS, () -> Items.NETHER_BRICK_SLAB);
        regularBlockOnly(NETHER_BRICKS, () -> Items.CHISELED_NETHER_BRICKS);
        regularBlockOnly(NETHER_BRICKS, () -> Items.CRACKED_NETHER_BRICKS);
        // Netherite
        regularBlockOnly(NETHERITE_BLOCK, () -> Items.NETHERITE_BLOCK);
        // Oak planks
        regularSet(OAK_PLANKS, () -> Items.OAK_PLANKS, () -> Items.OAK_STAIRS, () -> Items.OAK_SLAB);
        // Obsidian
        regularBlockOnly(OBSIDIAN, () -> Items.OBSIDIAN);
        // Prismarine bricks
        regularSet(PRISMARINE_BRICKS, () -> Items.PRISMARINE_BRICKS, () -> Items.PRISMARINE_BRICK_STAIRS, () -> Items.PRISMARINE_BRICK_SLAB);
        // Purpur block
        regularBlockOnly(PURPUR_BLOCK, () -> Items.PURPUR_PILLAR);
        // Quartz block
        regularBlockOnly(QUARTZ_BLOCK, () -> Items.QUARTZ_BRICKS);
        regularBlockOnly(QUARTZ_BLOCK, () -> Items.QUARTZ_PILLAR);
        regularBlockOnly(QUARTZ_BLOCK, () -> Items.CHISELED_QUARTZ_BLOCK);
        regularSet(QUARTZ_BLOCK, () -> Items.SMOOTH_QUARTZ, () -> Items.SMOOTH_QUARTZ_STAIRS, () -> Items.SMOOTH_QUARTZ_SLAB);
        // Red nether bricks
        regularSet(RED_NETHER_BRICKS, () -> Items.RED_NETHER_BRICKS, () -> Items.RED_NETHER_BRICK_STAIRS, () -> Items.RED_NETHER_BRICK_SLAB);
        // Red sandstone
        regularSet(RED_SANDSTONE, () -> Items.RED_SANDSTONE, () -> Items.RED_SANDSTONE_STAIRS, () -> Items.RED_SANDSTONE_SLAB);
        regularBlockOnly(RED_SANDSTONE, () -> Items.CHISELED_RED_SANDSTONE);
        regularSet(RED_SANDSTONE, () -> Items.CUT_RED_SANDSTONE, null, () -> Items.CUT_RED_SANDSTONE_SLAB);
        regularSet(RED_SANDSTONE, () -> Items.SMOOTH_RED_SANDSTONE, () -> Items.SMOOTH_RED_SANDSTONE_STAIRS, () -> Items.SMOOTH_RED_SANDSTONE_SLAB);
        // Redstone block
        regularBlockOnly(REDSTONE_BLOCK, () -> Items.REDSTONE_BLOCK);
        // Sandstone
        regularSet(SANDSTONE, () -> Items.SANDSTONE, () -> Items.SANDSTONE_STAIRS, () -> Items.SANDSTONE_SLAB);
        regularBlockOnly(SANDSTONE, () -> Items.CHISELED_SANDSTONE);
        regularSet(SANDSTONE, () -> Items.CUT_SANDSTONE, null, () -> Items.CUT_STANDSTONE_SLAB);
        regularSet(SANDSTONE, () -> Items.SMOOTH_SANDSTONE, () -> Items.SMOOTH_SANDSTONE_STAIRS, () -> Items.SMOOTH_SANDSTONE_SLAB);
        // Spruce planks
        regularSet(SPRUCE_PLANKS, () -> Items.SPRUCE_PLANKS, () -> Items.SPRUCE_STAIRS, () -> Items.SPRUCE_SLAB);
        // Stone
        regularSet(STONE, () -> Items.STONE, () -> Items.STONE_STAIRS, () -> Items.STONE_SLAB);
        regularSet(STONE, () -> Items.STONE_BRICKS, () -> Items.STONE_BRICK_STAIRS, () -> Items.STONE_BRICK_SLAB);
        regularSet(STONE, () -> Items.MOSSY_STONE_BRICKS, () -> Items.MOSSY_STONE_BRICK_STAIRS, () -> Items.MOSSY_STONE_BRICK_SLAB);
        regularBlockOnly(STONE, () -> Items.CRACKED_STONE_BRICKS);
        // Warped planks
        regularSet(WARPED_PLANKS, () -> Items.WARPED_PLANKS, () -> Items.WARPED_STAIRS, () -> Items.WARPED_SLAB);
    }

    private static void regularBlockOnly(ResourceLocation recipe, Supplier<ItemLike> block){
        REGISTRATION.chiselingEntry(recipe, entry -> entry.regularBlock(block.get()));
    }

    private static void regularSet(ResourceLocation recipe, Supplier<ItemLike> block, Supplier<ItemLike> stairs, Supplier<ItemLike> slab){
        REGISTRATION.chiselingEntry(recipe, entry -> {
            if(block != null)
                entry.regularBlock(block.get());
            if(stairs != null)
                entry.regularStairs(stairs.get());
            if(slab != null)
                entry.regularSlab(slab.get());
        });
    }
}
