package com.supermartijn642.rechiseled;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Locale;
import java.util.function.Supplier;

import static com.supermartijn642.rechiseled.RechiseledTagGroups.*;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public enum RechiseledBlockType {

    ACACIA_PLANKS_BEAMS("Acacia Plank Beams", () -> Blocks.ACACIA_PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_BRICKS("Acacia Plank Bricks", () -> Blocks.ACACIA_PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_CRATE("Acacia Planks Crate", () -> Blocks.ACACIA_PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_DIAGONAL_STRIPES("Diagonal Acacia Plank Stripes", () -> Blocks.ACACIA_PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_DIAGONAL_TILES("Diagonal Acacia Plank Tiles", () -> Blocks.ACACIA_PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_DOTTED("Dotted Acacia Planks", () -> Blocks.ACACIA_PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_FLOORING("Acacia Plank Flooring", () -> Blocks.ACACIA_PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_LARGE_TILES("Large Acacia Plank Tiles", () -> Blocks.ACACIA_PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_PATTERN("Acacia Plank Pattern", () -> Blocks.ACACIA_PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_SMALL_BRICKS("Small Acacia Plank Bricks", () -> Blocks.ACACIA_PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_SMALL_TILES("Small Acacia Plank Tiles", () -> Blocks.ACACIA_PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_SQUARES("Acacia Plank Squares", () -> Blocks.ACACIA_PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_TILES("Acacia Plank Tiles", () -> Blocks.ACACIA_PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_WAVY("Wavy Acacia Planks", () -> Blocks.ACACIA_PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_WOVEN("Woven Acacia Planks", () -> Blocks.ACACIA_PLANKS, false, ACACIA_PLANKS),
    ANDESITE_BRICKS("Andesite Bricks", () -> Blocks.POLISHED_ANDESITE, false, ANDESITE),
    ANDESITE_DIAGONAL_BRICKS("Diagonal Andesite Bricks", () -> Blocks.POLISHED_ANDESITE, false, ANDESITE),
    ANDESITE_DOTTED("Dotted Andesite", () -> Blocks.POLISHED_ANDESITE, false, ANDESITE),
    ANDESITE_PAVING("Andesite Paving", () -> Blocks.POLISHED_ANDESITE, false, ANDESITE),
    ANDESITE_POLISHED("Polished Andesite", () -> Blocks.POLISHED_ANDESITE, true, ANDESITE),
    ANDESITE_ROTATED_BRICKS("Rotated Andesite Bricks", () -> Blocks.POLISHED_ANDESITE, false, ANDESITE),
    ANDESITE_SQUARES("Andesite Squares", () -> Blocks.POLISHED_ANDESITE, false, ANDESITE),
    ANDESITE_TILES("Andesite Tiles", () -> Blocks.POLISHED_ANDESITE, false, ANDESITE),
    ANDESITE_WAVY("Wavy Andesite", () -> Blocks.POLISHED_ANDESITE, false, ANDESITE),
    BIRCH_PLANKS_BEAMS("Birch Plank Beams", () -> Blocks.BIRCH_PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_BRICKS("Birch Plank Bricks", () -> Blocks.BIRCH_PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_CRATE("Birch Planks Crate", () -> Blocks.BIRCH_PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_DIAGONAL_STRIPES("Diagonal Birch Plank Stripes", () -> Blocks.BIRCH_PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_DIAGONAL_TILES("Diagonal Birch Plank Tiles", () -> Blocks.BIRCH_PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_DOTTED("Dotted Birch Planks", () -> Blocks.BIRCH_PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_FLOORING("Birch Plank Flooring", () -> Blocks.BIRCH_PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_LARGE_TILES("Large Birch Plank Tiles", () -> Blocks.BIRCH_PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_PATTERN("Birch Plank Pattern", () -> Blocks.BIRCH_PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_SMALL_BRICKS("Small Birch Plank Bricks", () -> Blocks.BIRCH_PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_SMALL_TILES("Small Birch Plank Tiles", () -> Blocks.BIRCH_PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_SQUARES("Birch Plank Squares", () -> Blocks.BIRCH_PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_TILES("Birch Plank Tiles", () -> Blocks.BIRCH_PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_WAVY("Wavy Birch Planks", () -> Blocks.BIRCH_PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_WOVEN("Woven Birch Planks", () -> Blocks.BIRCH_PLANKS, false, BIRCH_PLANKS),
    BLACKSTONE_DIAGONAL_BRICKS("Diagonal Blackstone Bricks", () -> Blocks.POLISHED_BLACKSTONE, false, BLACKSTONE),
    BLACKSTONE_POLISHED("Polished Blackstone", () -> Blocks.POLISHED_BLACKSTONE, true, BLACKSTONE),
    BLACKSTONE_TILES("Blackstone Tiles", () -> Blocks.POLISHED_BLACKSTONE, false, BLACKSTONE),
    COBBLED_DEEPSLATE_BEAMS("Cobbled Deepslate Beams", () -> Blocks.COBBLED_DEEPSLATE, false, COBBLED_DEEPSLATE),
    COBBLED_DEEPSLATE_BRICKS("Cobbled Deepslate Bricks", () -> Blocks.COBBLED_DEEPSLATE, false, COBBLED_DEEPSLATE),
    COBBLED_DEEPSLATE_LARGE_TILES("Large Cobbled Deepslate Tiles", () -> Blocks.COBBLED_DEEPSLATE, false, COBBLED_DEEPSLATE),
    COBBLED_DEEPSLATE_PAVING("Cobbled Deepslate Paving", () -> Blocks.COBBLED_DEEPSLATE, false, COBBLED_DEEPSLATE),
    COBBLED_DEEPSLATE_PULVERIZED("Pulverized Cobbled Deepslate", () -> Blocks.COBBLED_DEEPSLATE, false, COBBLED_DEEPSLATE),
    COBBLED_DEEPSLATE_ROTATED_BRICKS("Rotated Cobbled Deepslate Bricks", () -> Blocks.COBBLED_DEEPSLATE, false, COBBLED_DEEPSLATE),
    COBBLED_DEEPSLATE_SMALL_TILES("Small Cobbled Deepslate Tiles", () -> Blocks.COBBLED_DEEPSLATE, false, COBBLED_DEEPSLATE),
    COBBLED_DEEPSLATE_SQUARES("Cobbled Deepslate Squares", () -> Blocks.COBBLED_DEEPSLATE, false, COBBLED_DEEPSLATE),
    COBBLED_DEEPSLATE_STRIPES("Cobbled Deepslate Stripes", () -> Blocks.COBBLED_DEEPSLATE, false, COBBLED_DEEPSLATE),
    COBBLED_DEEPSLATE_TILES("Cobbled Deepslate Tiles", () -> Blocks.COBBLED_DEEPSLATE, false, COBBLED_DEEPSLATE),
    COBBLED_DEEPSLATE_WORN_STRIPES("Weathered Cobbled Deepslate Stripes", () -> Blocks.COBBLED_DEEPSLATE, false, COBBLED_DEEPSLATE),
    COBBLESTONE_BEAMS("Cobblestone Beams", () -> Blocks.COBBLESTONE, false, COBBLESTONE),
    COBBLESTONE_CROSSES("Cobblestone Crosses", () -> Blocks.COBBLESTONE, false, COBBLESTONE),
    COBBLESTONE_DENTED("Dented Cobblestone", () -> Blocks.COBBLESTONE, false, COBBLESTONE),
    COBBLESTONE_INVERTED_DENTED("Inverted Dented Cobblestone", () -> Blocks.COBBLESTONE, false, COBBLESTONE),
    COBBLESTONE_PAVING("Cobblestone Paving", () -> Blocks.COBBLESTONE, false, COBBLESTONE),
    COBBLESTONE_PULVERIZED("Pulverized Cobblestone", () -> Blocks.COBBLESTONE, false, COBBLESTONE),
    COBBLESTONE_ROTATED_BRICKS("Rotated Cobblestone Bricks", () -> Blocks.COBBLESTONE, false, COBBLESTONE),
    COBBLESTONE_SMALL_TILES("Small Cobblestone Tiles", () -> Blocks.COBBLESTONE, false, COBBLESTONE),
    COBBLESTONE_SQUARES("Cobblestone Squares", () -> Blocks.COBBLESTONE, false, COBBLESTONE),
    COBBLESTONE_STRIPES("Cobblestone Stripes", () -> Blocks.COBBLESTONE, false, COBBLESTONE),
    COBBLESTONE_TILES("Cobblestone Tiles", () -> Blocks.COBBLESTONE, false, COBBLESTONE),
    COBBLESTONE_WORN_STRIPES("Weathered Cobblestone Stripes", () -> Blocks.COBBLESTONE, false, COBBLESTONE),
    CRIMSON_PLANKS_BEAMS("Crimson Plank Beams", () -> Blocks.CRIMSON_PLANKS, false, CRIMSON_PLANKS),
    CRIMSON_PLANKS_BRICKS("Crimson Plank Bricks", () -> Blocks.CRIMSON_PLANKS, false, CRIMSON_PLANKS),
    CRIMSON_PLANKS_CRATE("Crimson Planks Crate", () -> Blocks.CRIMSON_PLANKS, false, CRIMSON_PLANKS),
    CRIMSON_PLANKS_DIAGONAL_STRIPES("Diagonal Crimson Plank Stripes", () -> Blocks.CRIMSON_PLANKS, false, CRIMSON_PLANKS),
    CRIMSON_PLANKS_DIAGONAL_TILES("Diagonal Crimson Plank Tiles", () -> Blocks.CRIMSON_PLANKS, false, CRIMSON_PLANKS),
    CRIMSON_PLANKS_DOTTED("Dotted Crimson Planks", () -> Blocks.CRIMSON_PLANKS, false, CRIMSON_PLANKS),
    CRIMSON_PLANKS_FLOORING("Crimson Plank Flooring", () -> Blocks.CRIMSON_PLANKS, false, CRIMSON_PLANKS),
    CRIMSON_PLANKS_LARGE_TILES("Large Crimson Plank Tiles", () -> Blocks.CRIMSON_PLANKS, false, CRIMSON_PLANKS),
    CRIMSON_PLANKS_PATTERN("Crimson Plank Pattern", () -> Blocks.CRIMSON_PLANKS, false, CRIMSON_PLANKS),
    CRIMSON_PLANKS_SMALL_BRICKS("Small Crimson Plank Bricks", () -> Blocks.CRIMSON_PLANKS, false, CRIMSON_PLANKS),
    CRIMSON_PLANKS_SMALL_TILES("Small Crimson Plank Tiles", () -> Blocks.CRIMSON_PLANKS, false, CRIMSON_PLANKS),
    CRIMSON_PLANKS_SQUARES("Crimson Plank Squares", () -> Blocks.CRIMSON_PLANKS, false, CRIMSON_PLANKS),
    CRIMSON_PLANKS_TILES("Crimson Plank Tiles", () -> Blocks.CRIMSON_PLANKS, false, CRIMSON_PLANKS),
    CRIMSON_PLANKS_WAVY("Wavy Crimson Planks", () -> Blocks.CRIMSON_PLANKS, false, CRIMSON_PLANKS),
    CRIMSON_PLANKS_WOVEN("Woven Crimson Planks", () -> Blocks.CRIMSON_PLANKS, false, CRIMSON_PLANKS),
    DARK_OAK_PLANKS_BEAMS("Dark Oak Plank Beams", () -> Blocks.DARK_OAK_PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_BRICKS("Dark Oak Plank Bricks", () -> Blocks.DARK_OAK_PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_CRATE("Dark Oak Planks Crate", () -> Blocks.DARK_OAK_PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_DIAGONAL_STRIPES("Diagonal Dark Oak Plank Stripes", () -> Blocks.DARK_OAK_PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_DIAGONAL_TILES("Diagonal Dark Oak Plank Tiles", () -> Blocks.DARK_OAK_PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_DOTTED("Dotted Dark Oak Planks", () -> Blocks.DARK_OAK_PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_FLOORING("Dark Oak Plank Flooring", () -> Blocks.DARK_OAK_PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_LARGE_TILES("Large Dark Oak Plank Tiles", () -> Blocks.DARK_OAK_PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_PATTERN("Dark Oak Plank Pattern", () -> Blocks.DARK_OAK_PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_SMALL_BRICKS("Small Dark Oak Plank Bricks", () -> Blocks.DARK_OAK_PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_SMALL_TILES("Small Dark Oak Plank Tiles", () -> Blocks.DARK_OAK_PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_SQUARES("Dark Oak Plank Squares", () -> Blocks.DARK_OAK_PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_TILES("Dark Oak Plank Tiles", () -> Blocks.DARK_OAK_PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_WAVY("Wavy Dark Oak Planks", () -> Blocks.DARK_OAK_PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_WOVEN("Woven Dark Oak Planks", () -> Blocks.DARK_OAK_PLANKS, false, DARK_OAK_PLANKS),
    DIORITE_BRICKS("Diorite Bricks", () -> Blocks.POLISHED_DIORITE, false, DIORITE),
    DIORITE_DIAGONAL_BRICKS("Diagonal Diorite Bricks", () -> Blocks.POLISHED_DIORITE, false, DIORITE),
    DIORITE_DOTTED("Dotted Diorite", () -> Blocks.POLISHED_DIORITE, false, DIORITE),
    DIORITE_PAVING("Diorite Paving", () -> Blocks.POLISHED_DIORITE, false, DIORITE),
    DIORITE_POLISHED("Polished Diorite", () -> Blocks.POLISHED_DIORITE, true, DIORITE),
    DIORITE_ROTATED_BRICKS("Rotated Diorite Bricks", () -> Blocks.POLISHED_DIORITE, false, DIORITE),
    DIORITE_SQUARES("Diorite Squares", () -> Blocks.POLISHED_DIORITE, false, DIORITE),
    DIORITE_TILES("Diorite Tiles", () -> Blocks.POLISHED_DIORITE, false, DIORITE),
    DIORITE_WAVY("Wavy Diorite", () -> Blocks.POLISHED_DIORITE, false, DIORITE),
    END_STONE_DIAGONAL_BRICKS("Diagonal End Stone Bricks", () -> Blocks.END_STONE_BRICKS, false, END_STONE),
    END_STONE_POLISHED("Polished End Stone", () -> Blocks.END_STONE_BRICKS, false, END_STONE),
    END_STONE_TILES("End Stone Tiles", () -> Blocks.END_STONE_BRICKS, false, END_STONE),
    GLOWSTONE_BRICKS("Glowstone Bricks", () -> Blocks.GLOWSTONE, false, GLOWSTONE),
    GLOWSTONE_CRUSHED("Crushed Glowstone", () -> Blocks.GLOWSTONE, false, GLOWSTONE),
    GLOWSTONE_LARGE_TILES("Large Glowstone Tiles", () -> Blocks.GLOWSTONE, false, GLOWSTONE),
    GLOWSTONE_SMALL_TILES("Small Glowstone Tiles", () -> Blocks.GLOWSTONE, false, GLOWSTONE),
    GLOWSTONE_SMOOTH("Smooth Glowstone", () -> Blocks.GLOWSTONE, false, GLOWSTONE),
    GLOWSTONE_TILES("Glowstone Tiles", () -> Blocks.GLOWSTONE, false, GLOWSTONE),
    GRANITE_BRICKS("Granite Bricks", () -> Blocks.POLISHED_GRANITE, false, GRANITE),
    GRANITE_DIAGONAL_BRICKS("Diagonal Granite Bricks", () -> Blocks.POLISHED_GRANITE, false, GRANITE),
    GRANITE_DOTTED("Dotted Granite", () -> Blocks.POLISHED_GRANITE, false, GRANITE),
    GRANITE_PAVING("Granite Paving", () -> Blocks.POLISHED_GRANITE, false, GRANITE),
    GRANITE_POLISHED("Polished Granite", () -> Blocks.POLISHED_GRANITE, true, GRANITE),
    GRANITE_ROTATED_BRICKS("Rotated Granite Bricks", () -> Blocks.POLISHED_GRANITE, false, GRANITE),
    GRANITE_SQUARES("Granite Squares", () -> Blocks.POLISHED_GRANITE, false, GRANITE),
    GRANITE_TILES("Granite Tiles", () -> Blocks.POLISHED_GRANITE, false, GRANITE),
    GRANITE_WAVY("Wavy Granite", () -> Blocks.POLISHED_GRANITE, false, GRANITE),
    JUNGLE_PLANKS_BEAMS("Jungle Plank Beams", () -> Blocks.JUNGLE_PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_BRICKS("Jungle Plank Bricks", () -> Blocks.JUNGLE_PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_CRATE("Jungle Planks Crate", () -> Blocks.JUNGLE_PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_DIAGONAL_STRIPES("Diagonal Jungle Plank Stripes", () -> Blocks.JUNGLE_PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_DIAGONAL_TILES("Diagonal Jungle Plank Tiles", () -> Blocks.JUNGLE_PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_DOTTED("Dotted Jungle Planks", () -> Blocks.JUNGLE_PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_FLOORING("Jungle Plank Flooring", () -> Blocks.JUNGLE_PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_LARGE_TILES("Large Jungle Plank Tiles", () -> Blocks.JUNGLE_PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_PATTERN("Jungle Plank Pattern", () -> Blocks.JUNGLE_PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_SMALL_BRICKS("Small Jungle Plank Bricks", () -> Blocks.JUNGLE_PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_SMALL_TILES("Small Jungle Plank Tiles", () -> Blocks.JUNGLE_PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_SQUARES("Jungle Plank Squares", () -> Blocks.JUNGLE_PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_TILES("Jungle Plank Tiles", () -> Blocks.JUNGLE_PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_WAVY("Wavy Jungle Planks", () -> Blocks.JUNGLE_PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_WOVEN("Woven Jungle Planks", () -> Blocks.JUNGLE_PLANKS, false, JUNGLE_PLANKS),
    MOSSY_COBBLESTONE_BEAMS("Mossy Cobblestone Beams", () -> Blocks.MOSSY_COBBLESTONE, false, COBBLESTONE),
    MOSSY_COBBLESTONE_DENTED("Dented Mossy Cobblestone", () -> Blocks.MOSSY_COBBLESTONE, false, COBBLESTONE),
    MOSSY_COBBLESTONE_INVERTED_DENTED("Inverted Dented Mossy Cobblestone", () -> Blocks.MOSSY_COBBLESTONE, false, COBBLESTONE),
    MOSSY_COBBLESTONE_PAVING("Mossy Cobblestone Paving", () -> Blocks.MOSSY_COBBLESTONE, false, COBBLESTONE),
    MOSSY_COBBLESTONE_SMALL_TILES("Small Mossy Cobblestone Tiles", () -> Blocks.MOSSY_COBBLESTONE, false, COBBLESTONE),
    MOSSY_COBBLESTONE_SQUARES("Mossy Cobblestone Squares", () -> Blocks.MOSSY_COBBLESTONE, false, COBBLESTONE),
    MOSSY_COBBLESTONE_STRIPES("Mossy Cobblestone Stripes", () -> Blocks.MOSSY_COBBLESTONE, false, COBBLESTONE),
    MOSSY_COBBLESTONE_WORN_STRIPES("Weathered Mossy Cobblestone", () -> Blocks.MOSSY_COBBLESTONE, false, COBBLESTONE),
    NETHERRACK_BEAMS("Netherrack Beams", () -> Blocks.NETHERRACK, false, NETHERRACK),
    NETHERRACK_BRICKS("Netherrack Bricks", () -> Blocks.NETHERRACK, false, NETHERRACK),
    NETHERRACK_DENTED("Dented Netherrack", () -> Blocks.NETHERRACK, false, NETHERRACK),
    NETHERRACK_SMALL_TILES("Small Netherrack Tiles", () -> Blocks.NETHERRACK, false, NETHERRACK),
    NETHERRACK_STRIPES("Netherrack Stripes", () -> Blocks.NETHERRACK, false, NETHERRACK),
    NETHERRACK_TILES("Netherrack Tiles", () -> Blocks.NETHERRACK, false, NETHERRACK),
    NETHER_BRICKS_LARGE_BRICKS("Large Nether Bricks", () -> Blocks.NETHER_BRICKS, false, NETHER_BRICKS),
    NETHER_BRICKS_LARGE_TILES("Large Nether Brick Tiles", () -> Blocks.NETHER_BRICKS, false, NETHER_BRICKS),
    NETHER_BRICKS_SMALL_TILES("Small Nether Brick Tiles", () -> Blocks.NETHER_BRICKS, false, NETHER_BRICKS),
    NETHER_BRICKS_TILES("Nether Brick Tiles", () -> Blocks.NETHER_BRICKS, false, NETHER_BRICKS),
    OAK_PLANKS_BEAMS("Oak Plank Beams", () -> Blocks.OAK_PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_BRICKS("Oak Plank Bricks", () -> Blocks.OAK_PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_CRATE("Oak Planks Crate", () -> Blocks.OAK_PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_DIAGONAL_STRIPES("Diagonal Oak Plank Stripes", () -> Blocks.OAK_PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_DIAGONAL_TILES("Diagonal Oak Plank Tiles", () -> Blocks.OAK_PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_DOTTED("Dotted Oak Planks", () -> Blocks.OAK_PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_FLOORING("Oak Plank Flooring", () -> Blocks.OAK_PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_LARGE_TILES("Large Oak Plank Tiles", () -> Blocks.OAK_PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_PATTERN("Oak Plank Pattern", () -> Blocks.OAK_PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_SMALL_BRICKS("Small Oak Plank Bricks", () -> Blocks.OAK_PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_SMALL_TILES("Small Oak Plank Tiles", () -> Blocks.OAK_PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_SQUARES("Oak Plank Squares", () -> Blocks.OAK_PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_TILES("Oak Plank Tiles", () -> Blocks.OAK_PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_WAVY("Wavy Oak Planks", () -> Blocks.OAK_PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_WOVEN("Woven Oak Planks", () -> Blocks.OAK_PLANKS, false, OAK_PLANKS),
    PRISMARINE_BRICKS_BRICKS("Prismarine Bricks", () -> Blocks.PRISMARINE_BRICKS, false, PRISMARINE_BRICKS),
    PRISMARINE_BRICKS_DIAGONAL_BRICKS("Diagonal Prismarine Bricks", () -> Blocks.PRISMARINE_BRICKS, false, PRISMARINE_BRICKS),
    PRISMARINE_BRICKS_POLISHED("Polished Prismarine", () -> Blocks.PRISMARINE_BRICKS, false, PRISMARINE_BRICKS),
    PRISMARINE_BRICKS_TILES("Prismarine Tiles", () -> Blocks.PRISMARINE_BRICKS, false, PRISMARINE_BRICKS),
    RED_NETHER_BRICKS_LARGE_BRICKS("Large Red Nether Bricks", () -> Blocks.RED_NETHER_BRICKS, false, RED_NETHER_BRICKS),
    RED_NETHER_BRICKS_LARGE_TILES("Large Red Nether Brick Tiles", () -> Blocks.RED_NETHER_BRICKS, false, RED_NETHER_BRICKS),
    RED_NETHER_BRICKS_SMALL_TILES("Small Red Nether Brick Tiles", () -> Blocks.RED_NETHER_BRICKS, false, RED_NETHER_BRICKS),
    RED_NETHER_BRICKS_TILES("Red Nether Brick Tiles", () -> Blocks.RED_NETHER_BRICKS, false, RED_NETHER_BRICKS),
    RED_SANDSTONE_BRICKS("Red Sandstone Bricks", () -> Blocks.RED_SANDSTONE, false, RED_SANDSTONE),
    RED_SANDSTONE_DIAGONAL_BRICKS("Diagonal Red Sandstone Bricks", () -> Blocks.RED_SANDSTONE, false, RED_SANDSTONE),
    RED_SANDSTONE_LARGE_TILES("Large Red Sandstone Tiles", () -> Blocks.RED_SANDSTONE, false, RED_SANDSTONE),
    RED_SANDSTONE_POLISHED("Polished Red Sandstone", () -> Blocks.RED_SANDSTONE, false, RED_SANDSTONE),
    RED_SANDSTONE_TILES("Red Sandstone Tiles", () -> Blocks.RED_SANDSTONE, false, RED_SANDSTONE),
    SANDSTONE_BRICKS("Sandstone Bricks", () -> Blocks.SANDSTONE, false, SANDSTONE),
    SANDSTONE_DIAGONAL_BRICKS("Diagonal Sandstone Bricks", () -> Blocks.SANDSTONE, false, SANDSTONE),
    SANDSTONE_LARGE_TILES("Large Sandstone Tiles", () -> Blocks.SANDSTONE, false, SANDSTONE),
    SANDSTONE_POLISHED("Polished Sandstone", () -> Blocks.SANDSTONE, false, SANDSTONE),
    SANDSTONE_TILES("Sandstone Tiles", () -> Blocks.SANDSTONE, false, SANDSTONE),
    SPRUCE_PLANKS_BEAMS("Spruce Plank Beams", () -> Blocks.SPRUCE_PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_BRICKS("Spruce Plank Bricks", () -> Blocks.SPRUCE_PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_CRATE("Spruce Planks Crate", () -> Blocks.SPRUCE_PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_DIAGONAL_STRIPES("Diagonal Spruce Plank Stripes", () -> Blocks.SPRUCE_PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_DIAGONAL_TILES("Diagonal Spruce Plank Tiles", () -> Blocks.SPRUCE_PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_DOTTED("Dotted Spruce Planks", () -> Blocks.SPRUCE_PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_FLOORING("Spruce Plank Flooring", () -> Blocks.SPRUCE_PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_LARGE_TILES("Large Spruce Plank Tiles", () -> Blocks.SPRUCE_PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_PATTERN("Spruce Plank Pattern", () -> Blocks.SPRUCE_PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_SMALL_BRICKS("Small Spruce Plank Bricks", () -> Blocks.SPRUCE_PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_SMALL_TILES("Small Spruce Plank Tiles", () -> Blocks.SPRUCE_PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_SQUARES("Spruce Plank Squares", () -> Blocks.SPRUCE_PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_TILES("Spruce Plank Tiles", () -> Blocks.SPRUCE_PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_WAVY("Wavy Spruce Planks", () -> Blocks.SPRUCE_PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_WOVEN("Woven Spruce Planks", () -> Blocks.SPRUCE_PLANKS, false, SPRUCE_PLANKS),
    STONE_BIG_TILES("Large Stone Tiles", () -> Blocks.STONE, false, STONE),
    STONE_BORDERED("Bordered Stone", () -> Blocks.STONE, false, STONE),
    STONE_CHISELED_BRICKS("Chiseled Stone Bricks", () -> Blocks.CHISELED_STONE_BRICKS, true, STONE),
    STONE_DIAGONAL_BRICKS("Diagonal Stone Bricks", () -> Blocks.STONE, false, STONE),
    STONE_PATH("Stone Path", () -> Blocks.STONE, false, STONE),
    STONE_SMALL_BRICKS("Small Stone Bricks", () -> Blocks.STONE_BRICKS, false, STONE),
    STONE_SMOOTH("Smooth Stone", () -> Blocks.SMOOTH_STONE, true, STONE),
    STONE_TILES("Stone Tiles", () -> Blocks.STONE, false, STONE),
    WARPED_PLANKS_BEAMS("Warped Plank Beams", () -> Blocks.WARPED_PLANKS, false, WARPED_PLANKS),
    WARPED_PLANKS_BRICKS("Warped Plank Bricks", () -> Blocks.WARPED_PLANKS, false, WARPED_PLANKS),
    WARPED_PLANKS_CRATE("Warped Planks Crate", () -> Blocks.WARPED_PLANKS, false, WARPED_PLANKS),
    WARPED_PLANKS_DIAGONAL_STRIPES("Diagonal Warped Plank Stripes", () -> Blocks.WARPED_PLANKS, false, WARPED_PLANKS),
    WARPED_PLANKS_DIAGONAL_TILES("Diagonal Warped Plank Tiles", () -> Blocks.WARPED_PLANKS, false, WARPED_PLANKS),
    WARPED_PLANKS_DOTTED("Dotted Warped Planks", () -> Blocks.WARPED_PLANKS, false, WARPED_PLANKS),
    WARPED_PLANKS_FLOORING("Warped Plank Flooring", () -> Blocks.WARPED_PLANKS, false, WARPED_PLANKS),
    WARPED_PLANKS_LARGE_TILES("Large Warped Plank Tiles", () -> Blocks.WARPED_PLANKS, false, WARPED_PLANKS),
    WARPED_PLANKS_PATTERN("Warped Plank Pattern", () -> Blocks.WARPED_PLANKS, false, WARPED_PLANKS),
    WARPED_PLANKS_SMALL_BRICKS("Small Warped Plank Bricks", () -> Blocks.WARPED_PLANKS, false, WARPED_PLANKS),
    WARPED_PLANKS_SMALL_TILES("Small Warped Plank Tiles", () -> Blocks.WARPED_PLANKS, false, WARPED_PLANKS),
    WARPED_PLANKS_SQUARES("Warped Plank Squares", () -> Blocks.WARPED_PLANKS, false, WARPED_PLANKS),
    WARPED_PLANKS_TILES("Warped Plank Tiles", () -> Blocks.WARPED_PLANKS, false, WARPED_PLANKS),
    WARPED_PLANKS_WAVY("Wavy Warped Planks", () -> Blocks.WARPED_PLANKS, false, WARPED_PLANKS),
    WARPED_PLANKS_WOVEN("Woven Warped Planks", () -> Blocks.WARPED_PLANKS, false, WARPED_PLANKS);

    public final String regularRegistryName;
    public final String connectingRegistryName;
    public final String englishTranslation;
    public final Supplier<Block> parentBlock;
    public final boolean useParent;
    public final String[] tags;

    private Block regularBlock;
    private Block connectingBlock;
    private BlockItem regularItem;
    private BlockItem connectingItem;

    /**
     * @param useParent whether to use the parent block as the non-connecting variant
     */
    RechiseledBlockType(String englishTranslation, Supplier<Block> parentBlock, boolean useParent, String... tags){
        this.regularRegistryName = this.name().toLowerCase(Locale.ROOT);
        this.connectingRegistryName = this.name().toLowerCase(Locale.ROOT) + "_connecting";
        this.englishTranslation = englishTranslation;
        this.parentBlock = parentBlock;
        this.useParent = useParent;
        this.tags = tags;
    }

    public void createBlocks(){
        if(this.regularBlock != null || this.connectingBlock != null)
            throw new IllegalStateException("Blocks for '" + this.regularRegistryName + "' have already been created!");

        if(!this.useParent)
            this.regularBlock = new InheritingRechiseledBlock(this.regularRegistryName, false, this.parentBlock.get());
        this.connectingBlock = new InheritingRechiseledBlock(this.connectingRegistryName, true, this.parentBlock.get());
    }

    public void createItems(){
        if(this.connectingBlock == null)
            throw new IllegalStateException("Blocks for '" + this.regularRegistryName + "' must be created before the item!");
        if(this.regularItem != null || this.connectingItem != null)
            throw new IllegalStateException("Items for '" + this.regularRegistryName + "' have already been created!");

        if(!this.useParent){
            this.regularItem = new BlockItem(this.regularBlock, new Item.Properties().tab(Rechiseled.GROUP));
            this.regularItem.setRegistryName(this.regularRegistryName);
        }
        this.connectingItem = new BlockItem(this.connectingBlock, new Item.Properties().tab(Rechiseled.GROUP));
        this.connectingItem.setRegistryName(this.connectingRegistryName);
    }

    public Block getRegularBlock(){
        return this.regularBlock;
    }

    public Block getConnectingBlock(){
        return this.connectingBlock;
    }

    public BlockItem getRegularItem(){
        return this.regularItem;
    }

    public BlockItem getConnectingItem(){
        return this.connectingItem;
    }
}
