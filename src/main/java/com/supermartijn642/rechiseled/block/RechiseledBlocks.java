package com.supermartijn642.rechiseled.block;

import com.supermartijn642.rechiseled.api.BaseChiselingRecipes;
import com.supermartijn642.rechiseled.texture.TextureType;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created 18/04/2023 by SuperMartijn642
 */
@SuppressWarnings("unused")
public class RechiseledBlocks {

    private static final List<RechiseledBlockType> ALL_BLOCKS_INTERNAL = new ArrayList<>();
    public static final List<RechiseledBlockType> ALL_BLOCKS = Collections.unmodifiableList(ALL_BLOCKS_INTERNAL);

    // Acacia planks
    public static final RechiseledBlockType ACACIA_PLANKS_BEAMS = create("acacia_planks_beams", "Acacia Plank Beams", () -> Blocks.ACACIA_PLANKS).recipes(BaseChiselingRecipes.ACACIA_PLANKS).build();
    public static final RechiseledBlockType ACACIA_PLANKS_BRICKS = create("acacia_planks_bricks", "Acacia Plank Bricks", () -> Blocks.ACACIA_PLANKS).recipes(BaseChiselingRecipes.ACACIA_PLANKS).build();
    public static final RechiseledBlockType ACACIA_PLANKS_CRATE = create("acacia_planks_crate", "Acacia Planks Crate", () -> Blocks.ACACIA_PLANKS).recipes(BaseChiselingRecipes.ACACIA_PLANKS).build();
    public static final RechiseledBlockType ACACIA_PLANKS_DIAGONAL_STRIPES = create("acacia_planks_diagonal_stripes", "Diagonal Acacia Plank Stripes", () -> Blocks.ACACIA_PLANKS).recipes(BaseChiselingRecipes.ACACIA_PLANKS).build();
    public static final RechiseledBlockType ACACIA_PLANKS_DIAGONAL_TILES = create("acacia_planks_diagonal_tiles", "Diagonal Acacia Plank Tiles", () -> Blocks.ACACIA_PLANKS).recipes(BaseChiselingRecipes.ACACIA_PLANKS).build();
    public static final RechiseledBlockType ACACIA_PLANKS_DOTTED = create("acacia_planks_dotted", "Dotted Acacia Planks", () -> Blocks.ACACIA_PLANKS).recipes(BaseChiselingRecipes.ACACIA_PLANKS).build();
    public static final RechiseledBlockType ACACIA_PLANKS_FLOORING = create("acacia_planks_flooring", "Acacia Plank Flooring", () -> Blocks.ACACIA_PLANKS).recipes(BaseChiselingRecipes.ACACIA_PLANKS).build();
    public static final RechiseledBlockType ACACIA_PLANKS_LARGE_TILES = create("acacia_planks_large_tiles", "Large Acacia Plank Tiles", () -> Blocks.ACACIA_PLANKS).recipes(BaseChiselingRecipes.ACACIA_PLANKS).build();
    public static final RechiseledBlockType ACACIA_PLANKS_PATTERN = create("acacia_planks_pattern", "Acacia Plank Pattern", () -> Blocks.ACACIA_PLANKS).recipes(BaseChiselingRecipes.ACACIA_PLANKS).build();
    public static final RechiseledBlockType ACACIA_PLANKS_SMALL_BRICKS = create("acacia_planks_small_bricks", "Small Acacia Plank Bricks", () -> Blocks.ACACIA_PLANKS).recipes(BaseChiselingRecipes.ACACIA_PLANKS).build();
    public static final RechiseledBlockType ACACIA_PLANKS_SMALL_TILES = create("acacia_planks_small_tiles", "Small Acacia Plank Tiles", () -> Blocks.ACACIA_PLANKS).recipes(BaseChiselingRecipes.ACACIA_PLANKS).build();
    public static final RechiseledBlockType ACACIA_PLANKS_SQUARES = create("acacia_planks_squares", "Acacia Plank Squares", () -> Blocks.ACACIA_PLANKS).recipes(BaseChiselingRecipes.ACACIA_PLANKS).build();
    public static final RechiseledBlockType ACACIA_PLANKS_TILES = create("acacia_planks_tiles", "Acacia Plank Tiles", () -> Blocks.ACACIA_PLANKS).recipes(BaseChiselingRecipes.ACACIA_PLANKS).build();
    public static final RechiseledBlockType ACACIA_PLANKS_WAVY = create("acacia_planks_wavy", "Wavy Acacia Planks", () -> Blocks.ACACIA_PLANKS).recipes(BaseChiselingRecipes.ACACIA_PLANKS).build();
    public static final RechiseledBlockType ACACIA_PLANKS_WOVEN = create("acacia_planks_woven", "Woven Acacia Planks", () -> Blocks.ACACIA_PLANKS).recipes(BaseChiselingRecipes.ACACIA_PLANKS).build();
    // Andesite
    public static final RechiseledBlockType ANDESITE_BRICKS = create("andesite_bricks", "Andesite Bricks", () -> Blocks.POLISHED_ANDESITE).recipes(BaseChiselingRecipes.ANDESITE).build();
    public static final RechiseledBlockType ANDESITE_DIAGONAL_BRICKS = create("andesite_diagonal_bricks", "Diagonal Andesite Bricks", () -> Blocks.POLISHED_ANDESITE).recipes(BaseChiselingRecipes.ANDESITE).build();
    public static final RechiseledBlockType ANDESITE_DOTTED = create("andesite_dotted", "Dotted Andesite", () -> Blocks.POLISHED_ANDESITE).recipes(BaseChiselingRecipes.ANDESITE).build();
    public static final RechiseledBlockType ANDESITE_PAVING = create("andesite_paving", "Andesite Paving", () -> Blocks.POLISHED_ANDESITE).recipes(BaseChiselingRecipes.ANDESITE).build();
    public static final RechiseledBlockType ANDESITE_POLISHED = create("andesite_polished", "Polished Andesite", () -> Blocks.POLISHED_ANDESITE).regularVariant(() -> Blocks.POLISHED_ANDESITE).recipes(BaseChiselingRecipes.ANDESITE).build();
    public static final RechiseledBlockType ANDESITE_ROTATED_BRICKS = create("andesite_rotated_bricks", "Rotated Andesite Bricks", () -> Blocks.POLISHED_ANDESITE).recipes(BaseChiselingRecipes.ANDESITE).build();
    public static final RechiseledBlockType ANDESITE_SQUARES = create("andesite_squares", "Andesite Squares", () -> Blocks.POLISHED_ANDESITE).recipes(BaseChiselingRecipes.ANDESITE).build();
    public static final RechiseledBlockType ANDESITE_TILES = create("andesite_tiles", "Andesite Tiles", () -> Blocks.POLISHED_ANDESITE).recipes(BaseChiselingRecipes.ANDESITE).build();
    public static final RechiseledBlockType ANDESITE_WAVY = create("andesite_wavy", "Wavy Andesite", () -> Blocks.POLISHED_ANDESITE).recipes(BaseChiselingRecipes.ANDESITE).build();
    // Birch planks
    public static final RechiseledBlockType BIRCH_PLANKS_BEAMS = create("birch_planks_beams", "Birch Plank Beams", () -> Blocks.BIRCH_PLANKS).recipes(BaseChiselingRecipes.BIRCH_PLANKS).build();
    public static final RechiseledBlockType BIRCH_PLANKS_BRICKS = create("birch_planks_bricks", "Birch Plank Bricks", () -> Blocks.BIRCH_PLANKS).recipes(BaseChiselingRecipes.BIRCH_PLANKS).build();
    public static final RechiseledBlockType BIRCH_PLANKS_CRATE = create("birch_planks_crate", "Birch Planks Crate", () -> Blocks.BIRCH_PLANKS).recipes(BaseChiselingRecipes.BIRCH_PLANKS).build();
    public static final RechiseledBlockType BIRCH_PLANKS_DIAGONAL_STRIPES = create("birch_planks_diagonal_stripes", "Diagonal Birch Plank Stripes", () -> Blocks.BIRCH_PLANKS).recipes(BaseChiselingRecipes.BIRCH_PLANKS).build();
    public static final RechiseledBlockType BIRCH_PLANKS_DIAGONAL_TILES = create("birch_planks_diagonal_tiles", "Diagonal Birch Plank Tiles", () -> Blocks.BIRCH_PLANKS).recipes(BaseChiselingRecipes.BIRCH_PLANKS).build();
    public static final RechiseledBlockType BIRCH_PLANKS_DOTTED = create("birch_planks_dotted", "Dotted Birch Planks", () -> Blocks.BIRCH_PLANKS).recipes(BaseChiselingRecipes.BIRCH_PLANKS).build();
    public static final RechiseledBlockType BIRCH_PLANKS_FLOORING = create("birch_planks_flooring", "Birch Plank Flooring", () -> Blocks.BIRCH_PLANKS).recipes(BaseChiselingRecipes.BIRCH_PLANKS).build();
    public static final RechiseledBlockType BIRCH_PLANKS_LARGE_TILES = create("birch_planks_large_tiles", "Large Birch Plank Tiles", () -> Blocks.BIRCH_PLANKS).recipes(BaseChiselingRecipes.BIRCH_PLANKS).build();
    public static final RechiseledBlockType BIRCH_PLANKS_PATTERN = create("birch_planks_pattern", "Birch Plank Pattern", () -> Blocks.BIRCH_PLANKS).recipes(BaseChiselingRecipes.BIRCH_PLANKS).build();
    public static final RechiseledBlockType BIRCH_PLANKS_SMALL_BRICKS = create("birch_planks_small_bricks", "Small Birch Plank Bricks", () -> Blocks.BIRCH_PLANKS).recipes(BaseChiselingRecipes.BIRCH_PLANKS).build();
    public static final RechiseledBlockType BIRCH_PLANKS_SMALL_TILES = create("birch_planks_small_tiles", "Small Birch Plank Tiles", () -> Blocks.BIRCH_PLANKS).recipes(BaseChiselingRecipes.BIRCH_PLANKS).build();
    public static final RechiseledBlockType BIRCH_PLANKS_SQUARES = create("birch_planks_squares", "Birch Plank Squares", () -> Blocks.BIRCH_PLANKS).recipes(BaseChiselingRecipes.BIRCH_PLANKS).build();
    public static final RechiseledBlockType BIRCH_PLANKS_TILES = create("birch_planks_tiles", "Birch Plank Tiles", () -> Blocks.BIRCH_PLANKS).recipes(BaseChiselingRecipes.BIRCH_PLANKS).build();
    public static final RechiseledBlockType BIRCH_PLANKS_WAVY = create("birch_planks_wavy", "Wavy Birch Planks", () -> Blocks.BIRCH_PLANKS).recipes(BaseChiselingRecipes.BIRCH_PLANKS).build();
    public static final RechiseledBlockType BIRCH_PLANKS_WOVEN = create("birch_planks_woven", "Woven Birch Planks", () -> Blocks.BIRCH_PLANKS).recipes(BaseChiselingRecipes.BIRCH_PLANKS).build();
    // Blackstone
    public static final RechiseledBlockType BLACKSTONE_DIAGONAL_BRICKS = create("blackstone_diagonal_bricks", "Diagonal Blackstone Bricks", () -> Blocks.POLISHED_BLACKSTONE).recipes(BaseChiselingRecipes.BLACKSTONE).build();
    public static final RechiseledBlockType BLACKSTONE_POLISHED = create("blackstone_polished", "Polished Blackstone", () -> Blocks.POLISHED_BLACKSTONE).regularVariant(() -> Blocks.POLISHED_BLACKSTONE).recipes(BaseChiselingRecipes.BLACKSTONE).build();
    public static final RechiseledBlockType BLACKSTONE_TILES = create("blackstone_tiles", "Blackstone Tiles", () -> Blocks.POLISHED_BLACKSTONE).recipes(BaseChiselingRecipes.BLACKSTONE).build();
    // Cobblestone
    public static final RechiseledBlockType COBBLESTONE_BEAMS = create("cobblestone_beams", "Cobblestone Beams", () -> Blocks.COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType COBBLESTONE_CROSSES = create("cobblestone_crosses", "Cobblestone Crosses", () -> Blocks.COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType COBBLESTONE_DENTED = create("cobblestone_dented", "Dented Cobblestone", () -> Blocks.COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType COBBLESTONE_INVERTED_DENTED = create("cobblestone_inverted_dented", "Inverted Dented Cobblestone", () -> Blocks.COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType COBBLESTONE_PAVING = create("cobblestone_paving", "Cobblestone Paving", () -> Blocks.COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType COBBLESTONE_PULVERIZED = create("cobblestone_pulverized", "Pulverized Cobblestone", () -> Blocks.COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType COBBLESTONE_ROTATED_BRICKS = create("cobblestone_rotated_bricks", "Rotated Cobblestone Bricks", () -> Blocks.COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType COBBLESTONE_SMALL_TILES = create("cobblestone_small_tiles", "Small Cobblestone Tiles", () -> Blocks.COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType COBBLESTONE_SQUARES = create("cobblestone_squares", "Cobblestone Squares", () -> Blocks.COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType COBBLESTONE_STRIPES = create("cobblestone_stripes", "Cobblestone Stripes", () -> Blocks.COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType COBBLESTONE_TILES = create("cobblestone_tiles", "Cobblestone Tiles", () -> Blocks.COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType COBBLESTONE_WORN_STRIPES = create("cobblestone_worn_stripes", "Weathered Cobblestone Stripes", () -> Blocks.COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    // Crimson planks
    public static final RechiseledBlockType CRIMSON_PLANKS_BEAMS = create("crimson_planks_beams", "Crimson Plank Beams", () -> Blocks.CRIMSON_PLANKS).recipes(BaseChiselingRecipes.CRIMSON_PLANKS).build();
    public static final RechiseledBlockType CRIMSON_PLANKS_BRICKS = create("crimson_planks_bricks", "Crimson Plank Bricks", () -> Blocks.CRIMSON_PLANKS).recipes(BaseChiselingRecipes.CRIMSON_PLANKS).build();
    public static final RechiseledBlockType CRIMSON_PLANKS_CRATE = create("crimson_planks_crate", "Crimson Planks Crate", () -> Blocks.CRIMSON_PLANKS).recipes(BaseChiselingRecipes.CRIMSON_PLANKS).build();
    public static final RechiseledBlockType CRIMSON_PLANKS_DIAGONAL_STRIPES = create("crimson_planks_diagonal_stripes", "Diagonal Crimson Plank Stripes", () -> Blocks.CRIMSON_PLANKS).recipes(BaseChiselingRecipes.CRIMSON_PLANKS).build();
    public static final RechiseledBlockType CRIMSON_PLANKS_DIAGONAL_TILES = create("crimson_planks_diagonal_tiles", "Diagonal Crimson Plank Tiles", () -> Blocks.CRIMSON_PLANKS).recipes(BaseChiselingRecipes.CRIMSON_PLANKS).build();
    public static final RechiseledBlockType CRIMSON_PLANKS_DOTTED = create("crimson_planks_dotted", "Dotted Crimson Planks", () -> Blocks.CRIMSON_PLANKS).recipes(BaseChiselingRecipes.CRIMSON_PLANKS).build();
    public static final RechiseledBlockType CRIMSON_PLANKS_FLOORING = create("crimson_planks_flooring", "Crimson Plank Flooring", () -> Blocks.CRIMSON_PLANKS).recipes(BaseChiselingRecipes.CRIMSON_PLANKS).build();
    public static final RechiseledBlockType CRIMSON_PLANKS_LARGE_TILES = create("crimson_planks_large_tiles", "Large Crimson Plank Tiles", () -> Blocks.CRIMSON_PLANKS).recipes(BaseChiselingRecipes.CRIMSON_PLANKS).build();
    public static final RechiseledBlockType CRIMSON_PLANKS_PATTERN = create("crimson_planks_pattern", "Crimson Plank Pattern", () -> Blocks.CRIMSON_PLANKS).recipes(BaseChiselingRecipes.CRIMSON_PLANKS).build();
    public static final RechiseledBlockType CRIMSON_PLANKS_SMALL_BRICKS = create("crimson_planks_small_bricks", "Small Crimson Plank Bricks", () -> Blocks.CRIMSON_PLANKS).recipes(BaseChiselingRecipes.CRIMSON_PLANKS).build();
    public static final RechiseledBlockType CRIMSON_PLANKS_SMALL_TILES = create("crimson_planks_small_tiles", "Small Crimson Plank Tiles", () -> Blocks.CRIMSON_PLANKS).recipes(BaseChiselingRecipes.CRIMSON_PLANKS).build();
    public static final RechiseledBlockType CRIMSON_PLANKS_SQUARES = create("crimson_planks_squares", "Crimson Plank Squares", () -> Blocks.CRIMSON_PLANKS).recipes(BaseChiselingRecipes.CRIMSON_PLANKS).build();
    public static final RechiseledBlockType CRIMSON_PLANKS_TILES = create("crimson_planks_tiles", "Crimson Plank Tiles", () -> Blocks.CRIMSON_PLANKS).recipes(BaseChiselingRecipes.CRIMSON_PLANKS).build();
    public static final RechiseledBlockType CRIMSON_PLANKS_WAVY = create("crimson_planks_wavy", "Wavy Crimson Planks", () -> Blocks.CRIMSON_PLANKS).recipes(BaseChiselingRecipes.CRIMSON_PLANKS).build();
    public static final RechiseledBlockType CRIMSON_PLANKS_WOVEN = create("crimson_planks_woven", "Woven Crimson Planks", () -> Blocks.CRIMSON_PLANKS).recipes(BaseChiselingRecipes.CRIMSON_PLANKS).build();
    // Dark oak planks
    public static final RechiseledBlockType DARK_OAK_PLANKS_BEAMS = create("dark_oak_planks_beams", "Dark Oak Plank Beams", () -> Blocks.DARK_OAK_PLANKS).recipes(BaseChiselingRecipes.DARK_OAK_PLANKS).build();
    public static final RechiseledBlockType DARK_OAK_PLANKS_BRICKS = create("dark_oak_planks_bricks", "Dark Oak Plank Bricks", () -> Blocks.DARK_OAK_PLANKS).recipes(BaseChiselingRecipes.DARK_OAK_PLANKS).build();
    public static final RechiseledBlockType DARK_OAK_PLANKS_CRATE = create("dark_oak_planks_crate", "Dark Oak Planks Crate", () -> Blocks.DARK_OAK_PLANKS).recipes(BaseChiselingRecipes.DARK_OAK_PLANKS).build();
    public static final RechiseledBlockType DARK_OAK_PLANKS_DIAGONAL_STRIPES = create("dark_oak_planks_diagonal_stripes", "Diagonal Dark Oak Plank Stripes", () -> Blocks.DARK_OAK_PLANKS).recipes(BaseChiselingRecipes.DARK_OAK_PLANKS).build();
    public static final RechiseledBlockType DARK_OAK_PLANKS_DIAGONAL_TILES = create("dark_oak_planks_diagonal_tiles", "Diagonal Dark Oak Plank Tiles", () -> Blocks.DARK_OAK_PLANKS).recipes(BaseChiselingRecipes.DARK_OAK_PLANKS).build();
    public static final RechiseledBlockType DARK_OAK_PLANKS_DOTTED = create("dark_oak_planks_dotted", "Dotted Dark Oak Planks", () -> Blocks.DARK_OAK_PLANKS).recipes(BaseChiselingRecipes.DARK_OAK_PLANKS).build();
    public static final RechiseledBlockType DARK_OAK_PLANKS_FLOORING = create("dark_oak_planks_flooring", "Dark Oak Plank Flooring", () -> Blocks.DARK_OAK_PLANKS).recipes(BaseChiselingRecipes.DARK_OAK_PLANKS).build();
    public static final RechiseledBlockType DARK_OAK_PLANKS_LARGE_TILES = create("dark_oak_planks_large_tiles", "Large Dark Oak Plank Tiles", () -> Blocks.DARK_OAK_PLANKS).recipes(BaseChiselingRecipes.DARK_OAK_PLANKS).build();
    public static final RechiseledBlockType DARK_OAK_PLANKS_PATTERN = create("dark_oak_planks_pattern", "Dark Oak Plank Pattern", () -> Blocks.DARK_OAK_PLANKS).recipes(BaseChiselingRecipes.DARK_OAK_PLANKS).build();
    public static final RechiseledBlockType DARK_OAK_PLANKS_SMALL_BRICKS = create("dark_oak_planks_small_bricks", "Small Dark Oak Plank Bricks", () -> Blocks.DARK_OAK_PLANKS).recipes(BaseChiselingRecipes.DARK_OAK_PLANKS).build();
    public static final RechiseledBlockType DARK_OAK_PLANKS_SMALL_TILES = create("dark_oak_planks_small_tiles", "Small Dark Oak Plank Tiles", () -> Blocks.DARK_OAK_PLANKS).recipes(BaseChiselingRecipes.DARK_OAK_PLANKS).build();
    public static final RechiseledBlockType DARK_OAK_PLANKS_SQUARES = create("dark_oak_planks_squares", "Dark Oak Plank Squares", () -> Blocks.DARK_OAK_PLANKS).recipes(BaseChiselingRecipes.DARK_OAK_PLANKS).build();
    public static final RechiseledBlockType DARK_OAK_PLANKS_TILES = create("dark_oak_planks_tiles", "Dark Oak Plank Tiles", () -> Blocks.DARK_OAK_PLANKS).recipes(BaseChiselingRecipes.DARK_OAK_PLANKS).build();
    public static final RechiseledBlockType DARK_OAK_PLANKS_WAVY = create("dark_oak_planks_wavy", "Wavy Dark Oak Planks", () -> Blocks.DARK_OAK_PLANKS).recipes(BaseChiselingRecipes.DARK_OAK_PLANKS).build();
    public static final RechiseledBlockType DARK_OAK_PLANKS_WOVEN = create("dark_oak_planks_woven", "Woven Dark Oak Planks", () -> Blocks.DARK_OAK_PLANKS).recipes(BaseChiselingRecipes.DARK_OAK_PLANKS).build();
    // Dark prismarine
    public static final RechiseledBlockType DARK_PRISMARINE_BEAMS = create("dark_prismarine_beams", "Dark Prismarine Beams", () -> Blocks.DARK_PRISMARINE).recipes(BaseChiselingRecipes.DARK_PRISMARINE).build();
    public static final RechiseledBlockType DARK_PRISMARINE_BRICKS = create("dark_prismarine_bricks", "Dark Prismarine Bricks", () -> Blocks.DARK_PRISMARINE).recipes(BaseChiselingRecipes.DARK_PRISMARINE).build();
    public static final RechiseledBlockType DARK_PRISMARINE_DOTTED = create("dark_prismarine_dotted", "Dotted Dark Prismarine", () -> Blocks.DARK_PRISMARINE).recipes(BaseChiselingRecipes.DARK_PRISMARINE).build();
    public static final RechiseledBlockType DARK_PRISMARINE_FABRIC = create("dark_prismarine_fabric", "Dark Prismarine Fabric", () -> Blocks.DARK_PRISMARINE).recipes(BaseChiselingRecipes.DARK_PRISMARINE).build();
    public static final RechiseledBlockType DARK_PRISMARINE_LARGE_TILES = create("dark_prismarine_large_tiles", "Large Dark Prismarine Tiles", () -> Blocks.DARK_PRISMARINE).recipes(BaseChiselingRecipes.DARK_PRISMARINE).build();
    public static final RechiseledBlockType DARK_PRISMARINE_ROWS = create("dark_prismarine_rows", "Dark Prismarine Rows", () -> Blocks.DARK_PRISMARINE).recipes(BaseChiselingRecipes.DARK_PRISMARINE).build();
    public static final RechiseledBlockType DARK_PRISMARINE_SQUARES = create("dark_prismarine_squares", "Dark Prismarine Squares", () -> Blocks.DARK_PRISMARINE).recipes(BaseChiselingRecipes.DARK_PRISMARINE).build();
    public static final RechiseledBlockType DARK_PRISMARINE_TILES = create("dark_prismarine_tiles", "Dark Prismarine Tiles", () -> Blocks.DARK_PRISMARINE).recipes(BaseChiselingRecipes.DARK_PRISMARINE).build();
    public static final RechiseledBlockType DARK_PRISMARINE_WAVY = create("dark_prismarine_wavy", "Wavy Dark Prismarine", () -> Blocks.DARK_PRISMARINE).recipes(BaseChiselingRecipes.DARK_PRISMARINE).build();
    public static final RechiseledBlockType DARK_PRISMARINE_WOVEN = create("dark_prismarine_woven", "Woven Dark Prismarine", () -> Blocks.DARK_PRISMARINE).recipes(BaseChiselingRecipes.DARK_PRISMARINE).build();
    // Diorite
    public static final RechiseledBlockType DIORITE_BRICKS = create("diorite_bricks", "Diorite Bricks", () -> Blocks.POLISHED_DIORITE).recipes(BaseChiselingRecipes.DIORITE).build();
    public static final RechiseledBlockType DIORITE_DIAGONAL_BRICKS = create("diorite_diagonal_bricks", "Diagonal Diorite Bricks", () -> Blocks.POLISHED_DIORITE).recipes(BaseChiselingRecipes.DIORITE).build();
    public static final RechiseledBlockType DIORITE_DOTTED = create("diorite_dotted", "Dotted Diorite", () -> Blocks.POLISHED_DIORITE).recipes(BaseChiselingRecipes.DIORITE).build();
    public static final RechiseledBlockType DIORITE_PAVING = create("diorite_paving", "Diorite Paving", () -> Blocks.POLISHED_DIORITE).recipes(BaseChiselingRecipes.DIORITE).build();
    public static final RechiseledBlockType DIORITE_POLISHED = create("diorite_polished", "Polished Diorite", () -> Blocks.POLISHED_DIORITE).regularVariant(() -> Blocks.POLISHED_DIORITE).recipes(BaseChiselingRecipes.DIORITE).build();
    public static final RechiseledBlockType DIORITE_ROTATED_BRICKS = create("diorite_rotated_bricks", "Rotated Diorite Bricks", () -> Blocks.POLISHED_DIORITE).recipes(BaseChiselingRecipes.DIORITE).build();
    public static final RechiseledBlockType DIORITE_SQUARES = create("diorite_squares", "Diorite Squares", () -> Blocks.POLISHED_DIORITE).recipes(BaseChiselingRecipes.DIORITE).build();
    public static final RechiseledBlockType DIORITE_TILES = create("diorite_tiles", "Diorite Tiles", () -> Blocks.POLISHED_DIORITE).recipes(BaseChiselingRecipes.DIORITE).build();
    public static final RechiseledBlockType DIORITE_WAVY = create("diorite_wavy", "Wavy Diorite", () -> Blocks.POLISHED_DIORITE).recipes(BaseChiselingRecipes.DIORITE).build();
    // Dirt
    public static final RechiseledBlockType DIRT_BLOBS = create("dirt_blobs", "Dirt Blobs", () -> Blocks.DIRT).recipes(BaseChiselingRecipes.DIRT).build();
    public static final RechiseledBlockType DIRT_BRICKS = create("dirt_bricks", "Dirt Bricks", () -> Blocks.DIRT).recipes(BaseChiselingRecipes.DIRT).build();
    public static final RechiseledBlockType DIRT_CHUNKS = create("dirt_chunks", "Dirt Chunks", () -> Blocks.DIRT).recipes(BaseChiselingRecipes.DIRT).build();
    public static final RechiseledBlockType DIRT_CLUMPS = create("dirt_clumps", "Dirt Clumps", () -> Blocks.DIRT).recipes(BaseChiselingRecipes.DIRT).build();
    public static final RechiseledBlockType DIRT_COBBLED = create("dirt_cobbled", "Cobbled Dirt", () -> Blocks.DIRT).regularVariantTextureType(TextureType.REGULAR).noConnectingVariant().recipes(BaseChiselingRecipes.DIRT).build();
    public static final RechiseledBlockType DIRT_GROOVES = create("dirt_grooves", "Dirt Grooves", () -> Blocks.DIRT).regularVariantTextureType(TextureType.REGULAR).noConnectingVariant().recipes(BaseChiselingRecipes.DIRT).build();
    public static final RechiseledBlockType DIRT_LARGE_TILES = create("dirt_large_tiles", "Large Dirt Tiles", () -> Blocks.DIRT).recipes(BaseChiselingRecipes.DIRT).build();
    public static final RechiseledBlockType DIRT_MUDDY = create("dirt_muddy", "Muddy Dirt", () -> Blocks.DIRT).regularVariantTextureType(TextureType.REGULAR).noConnectingVariant().recipes(BaseChiselingRecipes.DIRT).build();
    public static final RechiseledBlockType DIRT_SMALL_BRICKS = create("dirt_small_bricks", "Small Dirt Bricks", () -> Blocks.DIRT).recipes(BaseChiselingRecipes.DIRT).build();
    public static final RechiseledBlockType DIRT_SMALL_TILES = create("dirt_small_tiles", "Small Dirt Tiles", () -> Blocks.DIRT).recipes(BaseChiselingRecipes.DIRT).build();
    public static final RechiseledBlockType DIRT_SMOOTH_CLUMPS = create("dirt_smooth_clumps", "Smooth Dirt Clumps", () -> Blocks.DIRT).recipes(BaseChiselingRecipes.DIRT).build();
    public static final RechiseledBlockType DIRT_SOIL = create("dirt_soil", "Dirt Soil", () -> Blocks.DIRT).recipes(BaseChiselingRecipes.DIRT).build();
    public static final RechiseledBlockType DIRT_SQUARES = create("dirt_squares", "Dirt Squares", () -> Blocks.DIRT).recipes(BaseChiselingRecipes.DIRT).build();
    public static final RechiseledBlockType DIRT_TILES = create("dirt_tiles", "Dirt Tiles", () -> Blocks.DIRT).recipes(BaseChiselingRecipes.DIRT).build();
    public static final RechiseledBlockType DIRT_TILLED = create("dirt_tilled", "Tilled Dirt", () -> Blocks.DIRT).recipes(BaseChiselingRecipes.DIRT).build();
    // End stone
    public static final RechiseledBlockType END_STONE_BLOBS = create("end_stone_blobs", "End Stone Blobs", () -> Blocks.END_STONE).recipes(BaseChiselingRecipes.END_STONE).build();
    public static final RechiseledBlockType END_STONE_CHISELED = create("end_stone_chiseled", "Chiseled End Stone", () -> Blocks.END_STONE).recipes(BaseChiselingRecipes.END_STONE).build();
    public static final RechiseledBlockType END_STONE_CRUSHED = create("end_stone_crushed", "Crushed End Stone", () -> Blocks.END_STONE).recipes(BaseChiselingRecipes.END_STONE).build();
    public static final RechiseledBlockType END_STONE_DIAGONAL_BRICKS = create("end_stone_diagonal_bricks", "Diagonal End Stone Bricks", () -> Blocks.END_STONE_BRICKS).recipes(BaseChiselingRecipes.END_STONE).build();
    public static final RechiseledBlockType END_STONE_MESH = create("end_stone_mesh", "End Stone Mesh", () -> Blocks.END_STONE).recipes(BaseChiselingRecipes.END_STONE).build();
    public static final RechiseledBlockType END_STONE_PAVING = create("end_stone_paving", "End Stone Paving", () -> Blocks.END_STONE).recipes(BaseChiselingRecipes.END_STONE).build();
    public static final RechiseledBlockType END_STONE_POLISHED = create("end_stone_polished", "Polished End Stone", () -> Blocks.END_STONE_BRICKS).recipes(BaseChiselingRecipes.END_STONE).build();
    public static final RechiseledBlockType END_STONE_SCALES = create("end_stone_scales", "End Stone Scales", () -> Blocks.END_STONE).recipes(BaseChiselingRecipes.END_STONE).build();
    public static final RechiseledBlockType END_STONE_SMALL_TILES = create("end_stone_small_tiles", "Small End Stone Tiles", () -> Blocks.END_STONE).recipes(BaseChiselingRecipes.END_STONE).build();
    public static final RechiseledBlockType END_STONE_SPIRAL_PATTERN = create("end_stone_spiral_pattern", "Spiral End Stone Pattern", () -> Blocks.END_STONE).recipes(BaseChiselingRecipes.END_STONE).build();
    public static final RechiseledBlockType END_STONE_SQUARES = create("end_stone_squares", "End Stone Squares", () -> Blocks.END_STONE).recipes(BaseChiselingRecipes.END_STONE).build();
    public static final RechiseledBlockType END_STONE_TILES = create("end_stone_tiles", "End Stone Tiles", () -> Blocks.END_STONE_BRICKS).recipes(BaseChiselingRecipes.END_STONE).build();
    // Glowstone
    public static final RechiseledBlockType GLOWSTONE_BRICKS = create("glowstone_bricks", "Glowstone Bricks", () -> Blocks.GLOWSTONE).recipes(BaseChiselingRecipes.GLOWSTONE).build();
    public static final RechiseledBlockType GLOWSTONE_CRUSHED = create("glowstone_crushed", "Crushed Glowstone", () -> Blocks.GLOWSTONE).recipes(BaseChiselingRecipes.GLOWSTONE).build();
    public static final RechiseledBlockType GLOWSTONE_LARGE_TILES = create("glowstone_large_tiles", "Large Glowstone Tiles", () -> Blocks.GLOWSTONE).recipes(BaseChiselingRecipes.GLOWSTONE).build();
    public static final RechiseledBlockType GLOWSTONE_SMALL_TILES = create("glowstone_small_tiles", "Small Glowstone Tiles", () -> Blocks.GLOWSTONE).recipes(BaseChiselingRecipes.GLOWSTONE).build();
    public static final RechiseledBlockType GLOWSTONE_SMOOTH = create("glowstone_smooth", "Smooth Glowstone", () -> Blocks.GLOWSTONE).recipes(BaseChiselingRecipes.GLOWSTONE).build();
    public static final RechiseledBlockType GLOWSTONE_TILES = create("glowstone_tiles", "Glowstone Tiles", () -> Blocks.GLOWSTONE).recipes(BaseChiselingRecipes.GLOWSTONE).build();
    // Granite
    public static final RechiseledBlockType GRANITE_BRICKS = create("granite_bricks", "Granite Bricks", () -> Blocks.POLISHED_GRANITE).recipes(BaseChiselingRecipes.GRANITE).build();
    public static final RechiseledBlockType GRANITE_DIAGONAL_BRICKS = create("granite_diagonal_bricks", "Diagonal Granite Bricks", () -> Blocks.POLISHED_GRANITE).recipes(BaseChiselingRecipes.GRANITE).build();
    public static final RechiseledBlockType GRANITE_DOTTED = create("granite_dotted", "Dotted Granite", () -> Blocks.POLISHED_GRANITE).recipes(BaseChiselingRecipes.GRANITE).build();
    public static final RechiseledBlockType GRANITE_PAVING = create("granite_paving", "Granite Paving", () -> Blocks.POLISHED_GRANITE).recipes(BaseChiselingRecipes.GRANITE).build();
    public static final RechiseledBlockType GRANITE_POLISHED = create("granite_polished", "Polished Granite", () -> Blocks.POLISHED_GRANITE).regularVariant(() -> Blocks.POLISHED_GRANITE).recipes(BaseChiselingRecipes.GRANITE).build();
    public static final RechiseledBlockType GRANITE_ROTATED_BRICKS = create("granite_rotated_bricks", "Rotated Granite Bricks", () -> Blocks.POLISHED_GRANITE).recipes(BaseChiselingRecipes.GRANITE).build();
    public static final RechiseledBlockType GRANITE_SQUARES = create("granite_squares", "Granite Squares", () -> Blocks.POLISHED_GRANITE).recipes(BaseChiselingRecipes.GRANITE).build();
    public static final RechiseledBlockType GRANITE_TILES = create("granite_tiles", "Granite Tiles", () -> Blocks.POLISHED_GRANITE).recipes(BaseChiselingRecipes.GRANITE).build();
    public static final RechiseledBlockType GRANITE_WAVY = create("granite_wavy", "Wavy Granite", () -> Blocks.POLISHED_GRANITE).recipes(BaseChiselingRecipes.GRANITE).build();
    // Jungle planks
    public static final RechiseledBlockType JUNGLE_PLANKS_BEAMS = create("jungle_planks_beams", "Jungle Plank Beams", () -> Blocks.JUNGLE_PLANKS).recipes(BaseChiselingRecipes.JUNGLE_PLANKS).build();
    public static final RechiseledBlockType JUNGLE_PLANKS_BRICKS = create("jungle_planks_bricks", "Jungle Plank Bricks", () -> Blocks.JUNGLE_PLANKS).recipes(BaseChiselingRecipes.JUNGLE_PLANKS).build();
    public static final RechiseledBlockType JUNGLE_PLANKS_CRATE = create("jungle_planks_crate", "Jungle Planks Crate", () -> Blocks.JUNGLE_PLANKS).recipes(BaseChiselingRecipes.JUNGLE_PLANKS).build();
    public static final RechiseledBlockType JUNGLE_PLANKS_DIAGONAL_STRIPES = create("jungle_planks_diagonal_stripes", "Diagonal Jungle Plank Stripes", () -> Blocks.JUNGLE_PLANKS).recipes(BaseChiselingRecipes.JUNGLE_PLANKS).build();
    public static final RechiseledBlockType JUNGLE_PLANKS_DIAGONAL_TILES = create("jungle_planks_diagonal_tiles", "Diagonal Jungle Plank Tiles", () -> Blocks.JUNGLE_PLANKS).recipes(BaseChiselingRecipes.JUNGLE_PLANKS).build();
    public static final RechiseledBlockType JUNGLE_PLANKS_DOTTED = create("jungle_planks_dotted", "Dotted Jungle Planks", () -> Blocks.JUNGLE_PLANKS).recipes(BaseChiselingRecipes.JUNGLE_PLANKS).build();
    public static final RechiseledBlockType JUNGLE_PLANKS_FLOORING = create("jungle_planks_flooring", "Jungle Plank Flooring", () -> Blocks.JUNGLE_PLANKS).recipes(BaseChiselingRecipes.JUNGLE_PLANKS).build();
    public static final RechiseledBlockType JUNGLE_PLANKS_LARGE_TILES = create("jungle_planks_large_tiles", "Large Jungle Plank Tiles", () -> Blocks.JUNGLE_PLANKS).recipes(BaseChiselingRecipes.JUNGLE_PLANKS).build();
    public static final RechiseledBlockType JUNGLE_PLANKS_PATTERN = create("jungle_planks_pattern", "Jungle Plank Pattern", () -> Blocks.JUNGLE_PLANKS).recipes(BaseChiselingRecipes.JUNGLE_PLANKS).build();
    public static final RechiseledBlockType JUNGLE_PLANKS_SMALL_BRICKS = create("jungle_planks_small_bricks", "Small Jungle Plank Bricks", () -> Blocks.JUNGLE_PLANKS).recipes(BaseChiselingRecipes.JUNGLE_PLANKS).build();
    public static final RechiseledBlockType JUNGLE_PLANKS_SMALL_TILES = create("jungle_planks_small_tiles", "Small Jungle Plank Tiles", () -> Blocks.JUNGLE_PLANKS).recipes(BaseChiselingRecipes.JUNGLE_PLANKS).build();
    public static final RechiseledBlockType JUNGLE_PLANKS_SQUARES = create("jungle_planks_squares", "Jungle Plank Squares", () -> Blocks.JUNGLE_PLANKS).recipes(BaseChiselingRecipes.JUNGLE_PLANKS).build();
    public static final RechiseledBlockType JUNGLE_PLANKS_TILES = create("jungle_planks_tiles", "Jungle Plank Tiles", () -> Blocks.JUNGLE_PLANKS).recipes(BaseChiselingRecipes.JUNGLE_PLANKS).build();
    public static final RechiseledBlockType JUNGLE_PLANKS_WAVY = create("jungle_planks_wavy", "Wavy Jungle Planks", () -> Blocks.JUNGLE_PLANKS).recipes(BaseChiselingRecipes.JUNGLE_PLANKS).build();
    public static final RechiseledBlockType JUNGLE_PLANKS_WOVEN = create("jungle_planks_woven", "Woven Jungle Planks", () -> Blocks.JUNGLE_PLANKS).recipes(BaseChiselingRecipes.JUNGLE_PLANKS).build();
    // Mossy cobblestone
    public static final RechiseledBlockType MOSSY_COBBLESTONE_BEAMS = create("mossy_cobblestone_beams", "Mossy Cobblestone Beams", () -> Blocks.MOSSY_COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType MOSSY_COBBLESTONE_DENTED = create("mossy_cobblestone_dented", "Dented Mossy Cobblestone", () -> Blocks.MOSSY_COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType MOSSY_COBBLESTONE_INVERTED_DENTED = create("mossy_cobblestone_inverted_dented", "Inverted Dented Mossy Cobblestone", () -> Blocks.MOSSY_COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType MOSSY_COBBLESTONE_PAVING = create("mossy_cobblestone_paving", "Mossy Cobblestone Paving", () -> Blocks.MOSSY_COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType MOSSY_COBBLESTONE_SMALL_TILES = create("mossy_cobblestone_small_tiles", "Small Mossy Cobblestone Tiles", () -> Blocks.MOSSY_COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType MOSSY_COBBLESTONE_SQUARES = create("mossy_cobblestone_squares", "Mossy Cobblestone Squares", () -> Blocks.MOSSY_COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType MOSSY_COBBLESTONE_STRIPES = create("mossy_cobblestone_stripes", "Mossy Cobblestone Stripes", () -> Blocks.MOSSY_COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    public static final RechiseledBlockType MOSSY_COBBLESTONE_WORN_STRIPES = create("mossy_cobblestone_worn_stripes", "Weathered Mossy Cobblestone", () -> Blocks.MOSSY_COBBLESTONE).recipes(BaseChiselingRecipes.COBBLESTONE).build();
    // Netherrack
    public static final RechiseledBlockType NETHERRACK_BEAMS = create("netherrack_beams", "Netherrack Beams", () -> Blocks.NETHERRACK).recipes(BaseChiselingRecipes.NETHERRACK).build();
    public static final RechiseledBlockType NETHERRACK_BRICKS = create("netherrack_bricks", "Netherrack Bricks", () -> Blocks.NETHERRACK).recipes(BaseChiselingRecipes.NETHERRACK).build();
    public static final RechiseledBlockType NETHERRACK_DENTED = create("netherrack_dented", "Dented Netherrack", () -> Blocks.NETHERRACK).recipes(BaseChiselingRecipes.NETHERRACK).build();
    public static final RechiseledBlockType NETHERRACK_SMALL_TILES = create("netherrack_small_tiles", "Small Netherrack Tiles", () -> Blocks.NETHERRACK).recipes(BaseChiselingRecipes.NETHERRACK).build();
    public static final RechiseledBlockType NETHERRACK_STRIPES = create("netherrack_stripes", "Netherrack Stripes", () -> Blocks.NETHERRACK).recipes(BaseChiselingRecipes.NETHERRACK).build();
    public static final RechiseledBlockType NETHERRACK_TILES = create("netherrack_tiles", "Netherrack Tiles", () -> Blocks.NETHERRACK).recipes(BaseChiselingRecipes.NETHERRACK).build();
    // Nether bricks
    public static final RechiseledBlockType NETHER_BRICKS_BEAMS = create("nether_bricks_beams", "Nether Brick Beams", () -> Blocks.NETHER_BRICKS).recipes(BaseChiselingRecipes.NETHER_BRICKS).build();
    public static final RechiseledBlockType NETHER_BRICKS_CHISELED_SQUARES = create("nether_bricks_chiseled_squares", "Chiseled Nether Brick Squares", () -> Blocks.NETHER_BRICKS).recipes(BaseChiselingRecipes.NETHER_BRICKS).build();
    public static final RechiseledBlockType NETHER_BRICKS_DIAGONAL_BRICKS = create("nether_bricks_diagonal_bricks", "Diagonal Nether Bricks", () -> Blocks.NETHER_BRICKS).recipes(BaseChiselingRecipes.NETHER_BRICKS).build();
    public static final RechiseledBlockType NETHER_BRICKS_LARGE_BRICKS = create("nether_bricks_large_bricks", "Large Nether Bricks", () -> Blocks.NETHER_BRICKS).recipes(BaseChiselingRecipes.NETHER_BRICKS).build();
    public static final RechiseledBlockType NETHER_BRICKS_LARGE_TILES = create("nether_bricks_large_tiles", "Large Nether Brick Tiles", () -> Blocks.NETHER_BRICKS).recipes(BaseChiselingRecipes.NETHER_BRICKS).build();
    public static final RechiseledBlockType NETHER_BRICKS_ROTATED_BRICKS = create("nether_bricks_rotated_bricks", "Rotated Nether Bricks", () -> Blocks.NETHER_BRICKS).recipes(BaseChiselingRecipes.NETHER_BRICKS).build();
    public static final RechiseledBlockType NETHER_BRICKS_SMALL_TILES = create("nether_bricks_small_tiles", "Small Nether Brick Tiles", () -> Blocks.NETHER_BRICKS).recipes(BaseChiselingRecipes.NETHER_BRICKS).build();
    public static final RechiseledBlockType NETHER_BRICKS_SMOOTH = create("nether_bricks_smooth", "Nether Bricks Smooth", () -> Blocks.NETHER_BRICKS).recipes(BaseChiselingRecipes.NETHER_BRICKS).build();
    public static final RechiseledBlockType NETHER_BRICKS_SQUARES = create("nether_bricks_squares", "Nether Brick Squares", () -> Blocks.NETHER_BRICKS).recipes(BaseChiselingRecipes.NETHER_BRICKS).build();
    public static final RechiseledBlockType NETHER_BRICKS_TILES = create("nether_bricks_tiles", "Nether Brick Tiles", () -> Blocks.NETHER_BRICKS).recipes(BaseChiselingRecipes.NETHER_BRICKS).build();
    // Oak planks
    public static final RechiseledBlockType OAK_PLANKS_BEAMS = create("oak_planks_beams", "Oak Plank Beams", () -> Blocks.OAK_PLANKS).recipes(BaseChiselingRecipes.OAK_PLANKS).build();
    public static final RechiseledBlockType OAK_PLANKS_BRICKS = create("oak_planks_bricks", "Oak Plank Bricks", () -> Blocks.OAK_PLANKS).recipes(BaseChiselingRecipes.OAK_PLANKS).build();
    public static final RechiseledBlockType OAK_PLANKS_CRATE = create("oak_planks_crate", "Oak Planks Crate", () -> Blocks.OAK_PLANKS).recipes(BaseChiselingRecipes.OAK_PLANKS).build();
    public static final RechiseledBlockType OAK_PLANKS_DIAGONAL_STRIPES = create("oak_planks_diagonal_stripes", "Diagonal Oak Plank Stripes", () -> Blocks.OAK_PLANKS).recipes(BaseChiselingRecipes.OAK_PLANKS).build();
    public static final RechiseledBlockType OAK_PLANKS_DIAGONAL_TILES = create("oak_planks_diagonal_tiles", "Diagonal Oak Plank Tiles", () -> Blocks.OAK_PLANKS).recipes(BaseChiselingRecipes.OAK_PLANKS).build();
    public static final RechiseledBlockType OAK_PLANKS_DOTTED = create("oak_planks_dotted", "Dotted Oak Planks", () -> Blocks.OAK_PLANKS).recipes(BaseChiselingRecipes.OAK_PLANKS).build();
    public static final RechiseledBlockType OAK_PLANKS_FLOORING = create("oak_planks_flooring", "Oak Plank Flooring", () -> Blocks.OAK_PLANKS).recipes(BaseChiselingRecipes.OAK_PLANKS).build();
    public static final RechiseledBlockType OAK_PLANKS_LARGE_TILES = create("oak_planks_large_tiles", "Large Oak Plank Tiles", () -> Blocks.OAK_PLANKS).recipes(BaseChiselingRecipes.OAK_PLANKS).build();
    public static final RechiseledBlockType OAK_PLANKS_PATTERN = create("oak_planks_pattern", "Oak Plank Pattern", () -> Blocks.OAK_PLANKS).recipes(BaseChiselingRecipes.OAK_PLANKS).build();
    public static final RechiseledBlockType OAK_PLANKS_SMALL_BRICKS = create("oak_planks_small_bricks", "Small Oak Plank Bricks", () -> Blocks.OAK_PLANKS).recipes(BaseChiselingRecipes.OAK_PLANKS).build();
    public static final RechiseledBlockType OAK_PLANKS_SMALL_TILES = create("oak_planks_small_tiles", "Small Oak Plank Tiles", () -> Blocks.OAK_PLANKS).recipes(BaseChiselingRecipes.OAK_PLANKS).build();
    public static final RechiseledBlockType OAK_PLANKS_SQUARES = create("oak_planks_squares", "Oak Plank Squares", () -> Blocks.OAK_PLANKS).recipes(BaseChiselingRecipes.OAK_PLANKS).build();
    public static final RechiseledBlockType OAK_PLANKS_TILES = create("oak_planks_tiles", "Oak Plank Tiles", () -> Blocks.OAK_PLANKS).recipes(BaseChiselingRecipes.OAK_PLANKS).build();
    public static final RechiseledBlockType OAK_PLANKS_WAVY = create("oak_planks_wavy", "Wavy Oak Planks", () -> Blocks.OAK_PLANKS).recipes(BaseChiselingRecipes.OAK_PLANKS).build();
    public static final RechiseledBlockType OAK_PLANKS_WOVEN = create("oak_planks_woven", "Woven Oak Planks", () -> Blocks.OAK_PLANKS).recipes(BaseChiselingRecipes.OAK_PLANKS).build();
    // Obsidian
    public static final RechiseledBlockType OBSIDIAN_BORDERED = create("obsidian_bordered", "Bordered Obsidian", () -> Blocks.OBSIDIAN).recipes(BaseChiselingRecipes.OBSIDIAN).build();
    public static final RechiseledBlockType OBSIDIAN_BRICKS = create("obsidian_bricks", "Obsidian Bricks", () -> Blocks.OBSIDIAN).recipes(BaseChiselingRecipes.OBSIDIAN).build();
    public static final RechiseledBlockType OBSIDIAN_CHISELED = create("obsidian_chiseled", "Chiseled Obsidian", () -> Blocks.OBSIDIAN).recipes(BaseChiselingRecipes.OBSIDIAN).build();
    public static final RechiseledBlockType OBSIDIAN_CHISELED_CIRCLES = create("obsidian_chiseled_circles", "Chiseled Obsidian Circles", () -> Blocks.OBSIDIAN).recipes(BaseChiselingRecipes.OBSIDIAN).build();
    public static final RechiseledBlockType OBSIDIAN_CHISELED_CREEPER = create("obsidian_chiseled_creeper", "Chiseled Obsidian Creeper Face", () -> Blocks.OBSIDIAN).regularVariantTextureType(TextureType.REGULAR).noConnectingVariant().recipes(BaseChiselingRecipes.OBSIDIAN).build();
    public static final RechiseledBlockType OBSIDIAN_CHISELED_SKELETON = create("obsidian_chiseled_skeleton", "Chiseled Obsidian Skeleton Face", () -> Blocks.OBSIDIAN).regularVariantTextureType(TextureType.REGULAR).noConnectingVariant().recipes(BaseChiselingRecipes.OBSIDIAN).build();
    public static final RechiseledBlockType OBSIDIAN_DARK = create("obsidian_dark", "Dark Obsidian", () -> Blocks.OBSIDIAN).recipes(BaseChiselingRecipes.OBSIDIAN).build();
    public static final RechiseledBlockType OBSIDIAN_LARGE_TILES = create("obsidian_large_tiles", "Large Obsidian Tiles", () -> Blocks.OBSIDIAN).regularVariantTextureType(TextureType.REGULAR).noConnectingVariant().recipes(BaseChiselingRecipes.OBSIDIAN).build();
    public static final RechiseledBlockType OBSIDIAN_PILLARS = create("obsidian_pillars", "Obsidian Pillars", () -> Blocks.OBSIDIAN).recipes(BaseChiselingRecipes.OBSIDIAN).build();
    public static final RechiseledBlockType OBSIDIAN_SPOTS = create("obsidian_spots", "Spotted Obsidian", () -> Blocks.OBSIDIAN).recipes(BaseChiselingRecipes.OBSIDIAN).build();
    public static final RechiseledBlockType OBSIDIAN_SQUARES = create("obsidian_squares", "Obsidian Squares", () -> Blocks.OBSIDIAN).recipes(BaseChiselingRecipes.OBSIDIAN).build();
    public static final RechiseledBlockType OBSIDIAN_STRIPES = create("obsidian_stripes", "Obsidian Stripes", () -> Blocks.OBSIDIAN).recipes(BaseChiselingRecipes.OBSIDIAN).build();
    public static final RechiseledBlockType OBSIDIAN_TILES = create("obsidian_tiles", "Obsidian Tiles", () -> Blocks.OBSIDIAN).recipes(BaseChiselingRecipes.OBSIDIAN).build();
    // Prismarine
    public static final RechiseledBlockType PRISMARINE_BRICKS_BEAMS = create("prismarine_bricks_beams", "Prismarine Brick Beams", () -> Blocks.PRISMARINE_BRICKS).recipes(BaseChiselingRecipes.PRISMARINE_BRICKS).build();
    public static final RechiseledBlockType PRISMARINE_BRICKS_BRICKS = create("prismarine_bricks_bricks", "Prismarine Bricks", () -> Blocks.PRISMARINE_BRICKS).recipes(BaseChiselingRecipes.PRISMARINE_BRICKS).build();
    public static final RechiseledBlockType PRISMARINE_BRICKS_CHISELED_CIRCLES = create("prismarine_bricks_chiseled_circles", "Chiseled Prismarine Brick Circles", () -> Blocks.PRISMARINE_BRICKS).recipes(BaseChiselingRecipes.PRISMARINE_BRICKS).build();
    public static final RechiseledBlockType PRISMARINE_BRICKS_CHISELED_SQUARES = create("prismarine_bricks_chiseled_squares", "Chiseled Prismarine Brick Squares", () -> Blocks.PRISMARINE_BRICKS).recipes(BaseChiselingRecipes.PRISMARINE_BRICKS).build();
    public static final RechiseledBlockType PRISMARINE_BRICKS_DIAGONAL_BRICKS = create("prismarine_bricks_diagonal_bricks", "Diagonal Prismarine Bricks", () -> Blocks.PRISMARINE_BRICKS).recipes(BaseChiselingRecipes.PRISMARINE_BRICKS).build();
    public static final RechiseledBlockType PRISMARINE_BRICKS_DIAGONAL_TILES = create("prismarine_bricks_diagonal_tiles", "Diagonal Prismarine Brick Tiles", () -> Blocks.PRISMARINE_BRICKS).recipes(BaseChiselingRecipes.PRISMARINE_BRICKS).build();
    public static final RechiseledBlockType PRISMARINE_BRICKS_DOTTED = create("prismarine_bricks_dotted", "Dotted Prismarine Bricks", () -> Blocks.PRISMARINE_BRICKS).recipes(BaseChiselingRecipes.PRISMARINE_BRICKS).build();
    public static final RechiseledBlockType PRISMARINE_BRICKS_PILLARS = create("prismarine_bricks_pillars", "Prismarine Brick Pillars", () -> Blocks.PRISMARINE_BRICKS).recipes(BaseChiselingRecipes.PRISMARINE_BRICKS).build();
    public static final RechiseledBlockType PRISMARINE_BRICKS_POLISHED = create("prismarine_bricks_polished", "Polished Prismarine Bricks", () -> Blocks.PRISMARINE_BRICKS).recipes(BaseChiselingRecipes.PRISMARINE_BRICKS).build();
    public static final RechiseledBlockType PRISMARINE_BRICKS_ROWS = create("prismarine_bricks_rows", "Prismarine Brick Rows", () -> Blocks.PRISMARINE_BRICKS).recipes(BaseChiselingRecipes.PRISMARINE_BRICKS).build();
    public static final RechiseledBlockType PRISMARINE_BRICKS_SMALL_TILES = create("prismarine_bricks_small_tiles", "Small Prismarine Brick Tiles", () -> Blocks.PRISMARINE_BRICKS).recipes(BaseChiselingRecipes.PRISMARINE_BRICKS).build();
    public static final RechiseledBlockType PRISMARINE_BRICKS_SQUARES = create("prismarine_bricks_squares", "Prismarine Brick Squares", () -> Blocks.PRISMARINE_BRICKS).recipes(BaseChiselingRecipes.PRISMARINE_BRICKS).build();
    public static final RechiseledBlockType PRISMARINE_BRICKS_TILES = create("prismarine_bricks_tiles", "Prismarine Brick Tiles", () -> Blocks.PRISMARINE_BRICKS).recipes(BaseChiselingRecipes.PRISMARINE_BRICKS).build();
    public static final RechiseledBlockType PRISMARINE_BRICKS_WAVY = create("prismarine_bricks_wavy", "Wavy Prismarine Bricks", () -> Blocks.PRISMARINE_BRICKS).recipes(BaseChiselingRecipes.PRISMARINE_BRICKS).build();
    public static final RechiseledBlockType PRISMARINE_BRICKS_WOVEN = create("prismarine_bricks_woven", "Woven Prismarine Bricks", () -> Blocks.PRISMARINE_BRICKS).recipes(BaseChiselingRecipes.PRISMARINE_BRICKS).build();
    // Purpur
    public static final RechiseledBlockType PURPUR_BRICKS = create("purpur_bricks", "Purpur Bricks", () -> Blocks.PURPUR_BLOCK).recipes(BaseChiselingRecipes.PURPUR_BLOCK).build();
    public static final RechiseledBlockType PURPUR_DIAGONAL_BRICKS = create("purpur_diagonal_bricks", "Diagonal Purpur Bricks", () -> Blocks.PURPUR_BLOCK).recipes(BaseChiselingRecipes.PURPUR_BLOCK).build();
    public static final RechiseledBlockType PURPUR_DIAGONAL_TILES = create("purpur_diagonal_tiles", "Diagonal Purpur Tiles", () -> Blocks.PURPUR_BLOCK).recipes(BaseChiselingRecipes.PURPUR_BLOCK).build();
    public static final RechiseledBlockType PURPUR_DOTTED = create("purpur_dotted", "Dotted Purpur Block", () -> Blocks.PURPUR_BLOCK).recipes(BaseChiselingRecipes.PURPUR_BLOCK).build();
    public static final RechiseledBlockType PURPUR_FABRIC = create("purpur_fabric", "Purpur Fabric", () -> Blocks.PURPUR_BLOCK).recipes(BaseChiselingRecipes.PURPUR_BLOCK).build();
    public static final RechiseledBlockType PURPUR_JAGGED_PATTERN = create("purpur_jagged_pattern", "Jagged Purpur Pattern", () -> Blocks.PURPUR_BLOCK).recipes(BaseChiselingRecipes.PURPUR_BLOCK).build();
    public static final RechiseledBlockType PURPUR_LARGE_TILES = create("purpur_large_tiles", "Large Purpur Tiles", () -> Blocks.PURPUR_BLOCK).recipes(BaseChiselingRecipes.PURPUR_BLOCK).build();
    public static final RechiseledBlockType PURPUR_ORGANIC_PATTERN = create("purpur_organic_pattern", "Organic Purpur Pattern", () -> Blocks.PURPUR_BLOCK).recipes(BaseChiselingRecipes.PURPUR_BLOCK).build();
    public static final RechiseledBlockType PURPUR_PILLAR = create("purpur_pillar", "Sided Purpur Pillar", () -> Blocks.PURPUR_BLOCK).recipes(BaseChiselingRecipes.PURPUR_BLOCK).build();
    public static final RechiseledBlockType PURPUR_ROTATED_BRICKS = create("purpur_rotated_bricks", "Rotated Purpur Bricks", () -> Blocks.PURPUR_BLOCK).recipes(BaseChiselingRecipes.PURPUR_BLOCK).build();
    public static final RechiseledBlockType PURPUR_SLANTED_TILES = create("purpur_slanted_tiles", "Slanted Purpur Tiles", () -> Blocks.PURPUR_BLOCK).recipes(BaseChiselingRecipes.PURPUR_BLOCK).build();
    public static final RechiseledBlockType PURPUR_SMALL_TILES = create("purpur_small_tiles", "Small Purpur Tiles", () -> Blocks.PURPUR_BLOCK).recipes(BaseChiselingRecipes.PURPUR_BLOCK).build();
    public static final RechiseledBlockType PURPUR_SPIRAL_PATTERN = create("purpur_spiral_pattern", "Spiral Purpur Pattern", () -> Blocks.PURPUR_BLOCK).recipes(BaseChiselingRecipes.PURPUR_BLOCK).build();
    public static final RechiseledBlockType PURPUR_SQUARES = create("purpur_squares", "Purpur Squares", () -> Blocks.PURPUR_BLOCK).recipes(BaseChiselingRecipes.PURPUR_BLOCK).build();
    public static final RechiseledBlockType PURPUR_TILES = create("purpur_tiles", "Purpur Block", () -> Blocks.PURPUR_BLOCK).regularVariant(() -> Blocks.PURPUR_BLOCK).recipes(BaseChiselingRecipes.PURPUR_BLOCK).build();
    public static final RechiseledBlockType PURPUR_WOVEN = create("purpur_woven", "Woven Purpur Block", () -> Blocks.PURPUR_BLOCK).recipes(BaseChiselingRecipes.PURPUR_BLOCK).build();
    // Quartz block
    public static final RechiseledBlockType QUARTZ_BLOCK_BORDERED = create("quartz_block_bordered", "Bordered Quartz Block", () -> Blocks.QUARTZ_BLOCK).recipes(BaseChiselingRecipes.QUARTZ_BLOCK).build();
    public static final RechiseledBlockType QUARTZ_BLOCK_CHISELED_PILLAR = create("quartz_block_chiseled_pillar", "Chiseled Quartz Pillar", () -> Blocks.QUARTZ_BLOCK).recipes(BaseChiselingRecipes.QUARTZ_BLOCK).build();
    public static final RechiseledBlockType QUARTZ_BLOCK_CHISELED_SQUARES = create("quartz_block_chiseled_squares", "Chiseled Quartz Block Squares", () -> Blocks.QUARTZ_BLOCK).regularVariantTextureType(TextureType.REGULAR).noConnectingVariant().recipes(BaseChiselingRecipes.QUARTZ_BLOCK).build();
    public static final RechiseledBlockType QUARTZ_BLOCK_CONNECTING = create("quartz_block_connecting", "Quartz Block", () -> Blocks.QUARTZ_BLOCK).regularVariant(() -> Blocks.QUARTZ_BLOCK).recipes(BaseChiselingRecipes.QUARTZ_BLOCK).build();
    public static final RechiseledBlockType QUARTZ_BLOCK_CROSSES = create("quartz_block_crosses", "Quartz Block Crosses", () -> Blocks.QUARTZ_BLOCK).recipes(BaseChiselingRecipes.QUARTZ_BLOCK).build();
    public static final RechiseledBlockType QUARTZ_BLOCK_DIAGONAL_TILES = create("quartz_block_diagonal_tiles", "Quartz Block", () -> Blocks.QUARTZ_BLOCK).recipes(BaseChiselingRecipes.QUARTZ_BLOCK).build();
    public static final RechiseledBlockType QUARTZ_BLOCK_PATTERN = create("quartz_block_pattern", "Quartz Block Pattern", () -> Blocks.QUARTZ_BLOCK).recipes(BaseChiselingRecipes.QUARTZ_BLOCK).build();
    public static final RechiseledBlockType QUARTZ_BLOCK_ROWS = create("quartz_block_rows", "Quartz Block Rows", () -> Blocks.QUARTZ_BLOCK).recipes(BaseChiselingRecipes.QUARTZ_BLOCK).build();
    public static final RechiseledBlockType QUARTZ_BLOCK_SCALES = create("quartz_block_scales", "Quartz Block Scales", () -> Blocks.QUARTZ_BLOCK).recipes(BaseChiselingRecipes.QUARTZ_BLOCK).build();
    public static final RechiseledBlockType QUARTZ_BLOCK_SMALL_TILES = create("quartz_block_small_tiles", "Small Quartz Block Tiles", () -> Blocks.QUARTZ_BLOCK).recipes(BaseChiselingRecipes.QUARTZ_BLOCK).build();
    public static final RechiseledBlockType QUARTZ_BLOCK_SQUARES = create("quartz_block_squares", "Quartz Block Squares", () -> Blocks.QUARTZ_BLOCK).recipes(BaseChiselingRecipes.QUARTZ_BLOCK).build();
    public static final RechiseledBlockType QUARTZ_BLOCK_STRIPES = create("quartz_block_stripes", "Quartz Block Stripes", () -> Blocks.QUARTZ_BLOCK).recipes(BaseChiselingRecipes.QUARTZ_BLOCK).build();
    public static final RechiseledBlockType QUARTZ_BLOCK_TILES = create("quartz_block_tiles", "Quartz Block Tiles", () -> Blocks.QUARTZ_BLOCK).recipes(BaseChiselingRecipes.QUARTZ_BLOCK).build();
    // Red nether bricks
    public static final RechiseledBlockType RED_NETHER_BRICKS_BEAMS = create("red_nether_bricks_beams", "Nether Brick Beams", () -> Blocks.RED_NETHER_BRICKS).recipes(BaseChiselingRecipes.RED_NETHER_BRICKS).build();
    public static final RechiseledBlockType RED_NETHER_BRICKS_CHISELED_SQUARES = create("red_nether_bricks_chiseled_squares", "Chiseled Nether Brick Squares", () -> Blocks.RED_NETHER_BRICKS).recipes(BaseChiselingRecipes.RED_NETHER_BRICKS).build();
    public static final RechiseledBlockType RED_NETHER_BRICKS_DIAGONAL_BRICKS = create("red_nether_bricks_diagonal_bricks", "Diagonal Nether Bricks", () -> Blocks.RED_NETHER_BRICKS).recipes(BaseChiselingRecipes.RED_NETHER_BRICKS).build();
    public static final RechiseledBlockType RED_NETHER_BRICKS_LARGE_BRICKS = create("red_nether_bricks_large_bricks", "Large Red Nether Bricks", () -> Blocks.RED_NETHER_BRICKS).recipes(BaseChiselingRecipes.RED_NETHER_BRICKS).build();
    public static final RechiseledBlockType RED_NETHER_BRICKS_LARGE_TILES = create("red_nether_bricks_large_tiles", "Large Red Nether Brick Tiles", () -> Blocks.RED_NETHER_BRICKS).recipes(BaseChiselingRecipes.RED_NETHER_BRICKS).build();
    public static final RechiseledBlockType RED_NETHER_BRICKS_ROTATED_BRICKS = create("red_nether_bricks_rotated_bricks", "Rotated Nether Bricks", () -> Blocks.RED_NETHER_BRICKS).recipes(BaseChiselingRecipes.RED_NETHER_BRICKS).build();
    public static final RechiseledBlockType RED_NETHER_BRICKS_SMALL_TILES = create("red_nether_bricks_small_tiles", "Small Red Nether Brick Tiles", () -> Blocks.RED_NETHER_BRICKS).recipes(BaseChiselingRecipes.RED_NETHER_BRICKS).build();
    public static final RechiseledBlockType RED_NETHER_BRICKS_SMOOTH = create("red_nether_bricks_smooth", "Nether Bricks Smooth", () -> Blocks.RED_NETHER_BRICKS).recipes(BaseChiselingRecipes.RED_NETHER_BRICKS).build();
    public static final RechiseledBlockType RED_NETHER_BRICKS_SQUARES = create("red_nether_bricks_squares", "Nether Brick Squares", () -> Blocks.RED_NETHER_BRICKS).recipes(BaseChiselingRecipes.RED_NETHER_BRICKS).build();
    public static final RechiseledBlockType RED_NETHER_BRICKS_TILES = create("red_nether_bricks_tiles", "Red Nether Brick Tiles", () -> Blocks.RED_NETHER_BRICKS).recipes(BaseChiselingRecipes.RED_NETHER_BRICKS).build();
    // Red sandstone
    public static final RechiseledBlockType RED_SANDSTONE_BRICKS = create("red_sandstone_bricks", "Red Sandstone Bricks", () -> Blocks.RED_SANDSTONE).recipes(BaseChiselingRecipes.RED_SANDSTONE).build();
    public static final RechiseledBlockType RED_SANDSTONE_DIAGONAL_BRICKS = create("red_sandstone_diagonal_bricks", "Diagonal Red Sandstone Bricks", () -> Blocks.RED_SANDSTONE).recipes(BaseChiselingRecipes.RED_SANDSTONE).build();
    public static final RechiseledBlockType RED_SANDSTONE_LARGE_TILES = create("red_sandstone_large_tiles", "Large Red Sandstone Tiles", () -> Blocks.RED_SANDSTONE).recipes(BaseChiselingRecipes.RED_SANDSTONE).build();
    public static final RechiseledBlockType RED_SANDSTONE_POLISHED = create("red_sandstone_polished", "Polished Red Sandstone", () -> Blocks.RED_SANDSTONE).recipes(BaseChiselingRecipes.RED_SANDSTONE).build();
    public static final RechiseledBlockType RED_SANDSTONE_TILES = create("red_sandstone_tiles", "Red Sandstone Tiles", () -> Blocks.RED_SANDSTONE).recipes(BaseChiselingRecipes.RED_SANDSTONE).build();
    // Sandstone
    public static final RechiseledBlockType SANDSTONE_BRICKS = create("sandstone_bricks", "Sandstone Bricks", () -> Blocks.SANDSTONE).recipes(BaseChiselingRecipes.SANDSTONE).build();
    public static final RechiseledBlockType SANDSTONE_DIAGONAL_BRICKS = create("sandstone_diagonal_bricks", "Diagonal Sandstone Bricks", () -> Blocks.SANDSTONE).recipes(BaseChiselingRecipes.SANDSTONE).build();
    public static final RechiseledBlockType SANDSTONE_LARGE_TILES = create("sandstone_large_tiles", "Large Sandstone Tiles", () -> Blocks.SANDSTONE).recipes(BaseChiselingRecipes.SANDSTONE).build();
    public static final RechiseledBlockType SANDSTONE_POLISHED = create("sandstone_polished", "Polished Sandstone", () -> Blocks.SANDSTONE).recipes(BaseChiselingRecipes.SANDSTONE).build();
    public static final RechiseledBlockType SANDSTONE_TILES = create("sandstone_tiles", "Sandstone Tiles", () -> Blocks.SANDSTONE).recipes(BaseChiselingRecipes.SANDSTONE).build();
    // Spruce planks
    public static final RechiseledBlockType SPRUCE_PLANKS_BEAMS = create("spruce_planks_beams", "Spruce Plank Beams", () -> Blocks.SPRUCE_PLANKS).recipes(BaseChiselingRecipes.SPRUCE_PLANKS).build();
    public static final RechiseledBlockType SPRUCE_PLANKS_BRICKS = create("spruce_planks_bricks", "Spruce Plank Bricks", () -> Blocks.SPRUCE_PLANKS).recipes(BaseChiselingRecipes.SPRUCE_PLANKS).build();
    public static final RechiseledBlockType SPRUCE_PLANKS_CRATE = create("spruce_planks_crate", "Spruce Planks Crate", () -> Blocks.SPRUCE_PLANKS).recipes(BaseChiselingRecipes.SPRUCE_PLANKS).build();
    public static final RechiseledBlockType SPRUCE_PLANKS_DIAGONAL_STRIPES = create("spruce_planks_diagonal_stripes", "Diagonal Spruce Plank Stripes", () -> Blocks.SPRUCE_PLANKS).recipes(BaseChiselingRecipes.SPRUCE_PLANKS).build();
    public static final RechiseledBlockType SPRUCE_PLANKS_DIAGONAL_TILES = create("spruce_planks_diagonal_tiles", "Diagonal Spruce Plank Tiles", () -> Blocks.SPRUCE_PLANKS).recipes(BaseChiselingRecipes.SPRUCE_PLANKS).build();
    public static final RechiseledBlockType SPRUCE_PLANKS_DOTTED = create("spruce_planks_dotted", "Dotted Spruce Planks", () -> Blocks.SPRUCE_PLANKS).recipes(BaseChiselingRecipes.SPRUCE_PLANKS).build();
    public static final RechiseledBlockType SPRUCE_PLANKS_FLOORING = create("spruce_planks_flooring", "Spruce Plank Flooring", () -> Blocks.SPRUCE_PLANKS).recipes(BaseChiselingRecipes.SPRUCE_PLANKS).build();
    public static final RechiseledBlockType SPRUCE_PLANKS_LARGE_TILES = create("spruce_planks_large_tiles", "Large Spruce Plank Tiles", () -> Blocks.SPRUCE_PLANKS).recipes(BaseChiselingRecipes.SPRUCE_PLANKS).build();
    public static final RechiseledBlockType SPRUCE_PLANKS_PATTERN = create("spruce_planks_pattern", "Spruce Plank Pattern", () -> Blocks.SPRUCE_PLANKS).recipes(BaseChiselingRecipes.SPRUCE_PLANKS).build();
    public static final RechiseledBlockType SPRUCE_PLANKS_SMALL_BRICKS = create("spruce_planks_small_bricks", "Small Spruce Plank Bricks", () -> Blocks.SPRUCE_PLANKS).recipes(BaseChiselingRecipes.SPRUCE_PLANKS).build();
    public static final RechiseledBlockType SPRUCE_PLANKS_SMALL_TILES = create("spruce_planks_small_tiles", "Small Spruce Plank Tiles", () -> Blocks.SPRUCE_PLANKS).recipes(BaseChiselingRecipes.SPRUCE_PLANKS).build();
    public static final RechiseledBlockType SPRUCE_PLANKS_SQUARES = create("spruce_planks_squares", "Spruce Plank Squares", () -> Blocks.SPRUCE_PLANKS).recipes(BaseChiselingRecipes.SPRUCE_PLANKS).build();
    public static final RechiseledBlockType SPRUCE_PLANKS_TILES = create("spruce_planks_tiles", "Spruce Plank Tiles", () -> Blocks.SPRUCE_PLANKS).recipes(BaseChiselingRecipes.SPRUCE_PLANKS).build();
    public static final RechiseledBlockType SPRUCE_PLANKS_WAVY = create("spruce_planks_wavy", "Wavy Spruce Planks", () -> Blocks.SPRUCE_PLANKS).recipes(BaseChiselingRecipes.SPRUCE_PLANKS).build();
    public static final RechiseledBlockType SPRUCE_PLANKS_WOVEN = create("spruce_planks_woven", "Woven Spruce Planks", () -> Blocks.SPRUCE_PLANKS).recipes(BaseChiselingRecipes.SPRUCE_PLANKS).build();
    // Stone
    public static final RechiseledBlockType STONE_BIG_TILES = create("stone_big_tiles", "Large Stone Tiles", () -> Blocks.STONE).recipes(BaseChiselingRecipes.STONE).build();
    public static final RechiseledBlockType STONE_BORDERED = create("stone_bordered", "Bordered Stone", () -> Blocks.STONE).recipes(BaseChiselingRecipes.STONE).build();
    public static final RechiseledBlockType STONE_CHISELED_BRICKS = create("stone_chiseled_bricks", "Chiseled Stone Bricks", () -> Blocks.CHISELED_STONE_BRICKS).regularVariant(() -> Blocks.CHISELED_STONE_BRICKS).recipes(BaseChiselingRecipes.STONE).build();
    public static final RechiseledBlockType STONE_CRUSHED = create("stone_crushed", "Crushed Stone", () -> Blocks.STONE).recipes(BaseChiselingRecipes.STONE).build();
    public static final RechiseledBlockType STONE_DIAGONAL_BRICKS = create("stone_diagonal_bricks", "Diagonal Stone Bricks", () -> Blocks.STONE).recipes(BaseChiselingRecipes.STONE).build();
    public static final RechiseledBlockType STONE_PATH = create("stone_path", "Stone Path", () -> Blocks.STONE).recipes(BaseChiselingRecipes.STONE).build();
    public static final RechiseledBlockType STONE_ROTATED_BRICKS = create("stone_rotated_bricks", "Rotated Stone Bricks", () -> Blocks.STONE).recipes(BaseChiselingRecipes.STONE).build();
    public static final RechiseledBlockType STONE_SMALL_BRICKS = create("stone_small_bricks", "Small Stone Bricks", () -> Blocks.STONE_BRICKS).recipes(BaseChiselingRecipes.STONE).build();
    public static final RechiseledBlockType STONE_SMALL_TILES = create("stone_small_tiles", "Small Stone Tiles", () -> Blocks.STONE).recipes(BaseChiselingRecipes.STONE).build();
    public static final RechiseledBlockType STONE_SMOOTH = create("stone_smooth", "Smooth Stone", () -> Blocks.SMOOTH_STONE).regularVariant(() -> Blocks.SMOOTH_STONE).recipes(BaseChiselingRecipes.STONE).build();
    public static final RechiseledBlockType STONE_SMOOTH_LARGE_TILES = create("stone_smooth_large_tiles", "Large Smooth Stone Tiles", () -> Blocks.SMOOTH_STONE).recipes(BaseChiselingRecipes.STONE).build();
    public static final RechiseledBlockType STONE_SMOOTH_TILES = create("stone_smooth_tiles", "Smooth Stone Tiles", () -> Blocks.SMOOTH_STONE).recipes(BaseChiselingRecipes.STONE).build();
    public static final RechiseledBlockType STONE_SQUARES = create("stone_squares", "Stone Squares", () -> Blocks.STONE).recipes(BaseChiselingRecipes.STONE).build();
    public static final RechiseledBlockType STONE_TILES = create("stone_tiles", "Stone Tiles", () -> Blocks.STONE).recipes(BaseChiselingRecipes.STONE).build();
    public static final RechiseledBlockType STONE_WAVES = create("stone_waves", "Stone Waves", () -> Blocks.STONE).recipes(BaseChiselingRecipes.STONE).build();
    // Warped planks
    public static final RechiseledBlockType WARPED_PLANKS_BEAMS = create("warped_planks_beams", "Warped Plank Beams", () -> Blocks.WARPED_PLANKS).recipes(BaseChiselingRecipes.WARPED_PLANKS).build();
    public static final RechiseledBlockType WARPED_PLANKS_BRICKS = create("warped_planks_bricks", "Warped Plank Bricks", () -> Blocks.WARPED_PLANKS).recipes(BaseChiselingRecipes.WARPED_PLANKS).build();
    public static final RechiseledBlockType WARPED_PLANKS_CRATE = create("warped_planks_crate", "Warped Planks Crate", () -> Blocks.WARPED_PLANKS).recipes(BaseChiselingRecipes.WARPED_PLANKS).build();
    public static final RechiseledBlockType WARPED_PLANKS_DIAGONAL_STRIPES = create("warped_planks_diagonal_stripes", "Diagonal Warped Plank Stripes", () -> Blocks.WARPED_PLANKS).recipes(BaseChiselingRecipes.WARPED_PLANKS).build();
    public static final RechiseledBlockType WARPED_PLANKS_DIAGONAL_TILES = create("warped_planks_diagonal_tiles", "Diagonal Warped Plank Tiles", () -> Blocks.WARPED_PLANKS).recipes(BaseChiselingRecipes.WARPED_PLANKS).build();
    public static final RechiseledBlockType WARPED_PLANKS_DOTTED = create("warped_planks_dotted", "Dotted Warped Planks", () -> Blocks.WARPED_PLANKS).recipes(BaseChiselingRecipes.WARPED_PLANKS).build();
    public static final RechiseledBlockType WARPED_PLANKS_FLOORING = create("warped_planks_flooring", "Warped Plank Flooring", () -> Blocks.WARPED_PLANKS).recipes(BaseChiselingRecipes.WARPED_PLANKS).build();
    public static final RechiseledBlockType WARPED_PLANKS_LARGE_TILES = create("warped_planks_large_tiles", "Large Warped Plank Tiles", () -> Blocks.WARPED_PLANKS).recipes(BaseChiselingRecipes.WARPED_PLANKS).build();
    public static final RechiseledBlockType WARPED_PLANKS_PATTERN = create("warped_planks_pattern", "Warped Plank Pattern", () -> Blocks.WARPED_PLANKS).recipes(BaseChiselingRecipes.WARPED_PLANKS).build();
    public static final RechiseledBlockType WARPED_PLANKS_SMALL_BRICKS = create("warped_planks_small_bricks", "Small Warped Plank Bricks", () -> Blocks.WARPED_PLANKS).recipes(BaseChiselingRecipes.WARPED_PLANKS).build();
    public static final RechiseledBlockType WARPED_PLANKS_SMALL_TILES = create("warped_planks_small_tiles", "Small Warped Plank Tiles", () -> Blocks.WARPED_PLANKS).recipes(BaseChiselingRecipes.WARPED_PLANKS).build();
    public static final RechiseledBlockType WARPED_PLANKS_SQUARES = create("warped_planks_squares", "Warped Plank Squares", () -> Blocks.WARPED_PLANKS).recipes(BaseChiselingRecipes.WARPED_PLANKS).build();
    public static final RechiseledBlockType WARPED_PLANKS_TILES = create("warped_planks_tiles", "Warped Plank Tiles", () -> Blocks.WARPED_PLANKS).recipes(BaseChiselingRecipes.WARPED_PLANKS).build();
    public static final RechiseledBlockType WARPED_PLANKS_WAVY = create("warped_planks_wavy", "Wavy Warped Planks", () -> Blocks.WARPED_PLANKS).recipes(BaseChiselingRecipes.WARPED_PLANKS).build();
    public static final RechiseledBlockType WARPED_PLANKS_WOVEN = create("warped_planks_woven", "Woven Warped Planks", () -> Blocks.WARPED_PLANKS).recipes(BaseChiselingRecipes.WARPED_PLANKS).build();

    private static RechiseledBlockBuilder create(String identifier, String translation){
        return new RechiseledBlockBuilder("rechiseled", identifier).translation(translation);
    }

    private static RechiseledBlockBuilder create(String identifier, String translation, Supplier<Block> parent){
        return new RechiseledBlockBuilder("rechiseled", identifier).translation(translation).copyProperties(parent).properties(properties -> properties.lootTableFrom(null)).blockTagsFrom(parent);
    }

    public static void init(){
        // Cause this class to be initialized
    }

    static{
        try{
            for(Field field : RechiseledBlocks.class.getDeclaredFields())
                if(Modifier.isStatic(field.getModifiers()) && field.getType().equals(RechiseledBlockType.class))
                    ALL_BLOCKS_INTERNAL.add((RechiseledBlockType)field.get(null));
        }catch(IllegalAccessException e){
            throw new RuntimeException(e);
        }
    }
}
