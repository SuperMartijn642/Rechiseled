package com.supermartijn642.rechiseled.api;

import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.resources.Identifier;

/**
 * Created 19/01/2022 by SuperMartijn642
 * <p>
 * All default recipe locations in rechiseled.
 */
public final class BaseChiselingRecipes {

    public static final Identifier AMETHYST_BLOCK = location("amethyst_block");
    public static final Identifier ACACIA_PLANKS = location("acacia_planks");
    public static final Identifier ANDESITE = location("andesite");
    public static final Identifier BASALT = location("basalt");
    public static final Identifier BAMBOO_PLANKS = location("bamboo_planks");
    public static final Identifier BIRCH_PLANKS = location("birch_planks");
    public static final Identifier BLACKSTONE = location("blackstone");
    public static final Identifier BLUE_ICE = location("blue_ice");
    public static final Identifier BONE_BLOCK = location("bone_block");
    public static final Identifier CHERRY_PLANKS = location("cherry_planks");
    public static final Identifier COAL_BLOCK = location("coal_block");
    public static final Identifier COBBLED_DEEPSLATE = location("cobbled_deepslate");
    public static final Identifier COBBLESTONE = location("cobblestone");
    public static final Identifier COPPER_BLOCK = location("copper_block");
    public static final Identifier CRIMSON_PLANKS = location("crimson_planks");
    public static final Identifier DARK_OAK_PLANKS = location("dark_oak_planks");
    public static final Identifier DARK_PRISMARINE = location("dark_prismarine");
    public static final Identifier DIAMOND_BLOCK = location("diamond_block");
    public static final Identifier DIORITE = location("diorite");
    public static final Identifier DIRT = location("dirt");
    public static final Identifier EMERALD_BLOCK = location("emerald_block");
    public static final Identifier END_STONE = location("end_stone");
    public static final Identifier GLOWSTONE = location("glowstone");
    public static final Identifier GOLD_BLOCK = location("gold_block");
    public static final Identifier GRANITE = location("granite");
    public static final Identifier IRON_BLOCK = location("iron_block");
    public static final Identifier JUNGLE_PLANKS = location("jungle_planks");
    public static final Identifier LAPIS_BLOCK = location("lapis_block");
    public static final Identifier MANGROVE_PLANKS = location("mangrove_planks");
    public static final Identifier NETHERRACK = location("netherrack");
    public static final Identifier NETHER_BRICKS = location("nether_bricks");
    public static final Identifier NETHERITE_BLOCK = location("netherite_block");
    public static final Identifier OAK_PLANKS = location("oak_planks");
    public static final Identifier OBSIDIAN = location("obsidian");
    public static final Identifier PALE_OAK_PLANKS = location("pale_oak_planks");
    public static final Identifier PRISMARINE_BRICKS = location("prismarine_bricks");
    public static final Identifier PURPUR_BLOCK = location("purpur_block");
    public static final Identifier QUARTZ_BLOCK = location("quartz_block");
    public static final Identifier RED_NETHER_BRICKS = location("red_nether_bricks");
    public static final Identifier RED_SANDSTONE = location("red_sandstone");
    public static final Identifier REDSTONE_BLOCK = location("redstone_block");
    public static final Identifier SANDSTONE = location("sandstone");
    public static final Identifier SPRUCE_PLANKS = location("spruce_planks");
    public static final Identifier STONE = location("stone");
    public static final Identifier WARPED_PLANKS = location("warped_planks");

    private static Identifier location(String name){
        return Rechiseled.identifier(name);
    }
}
