package com.supermartijn642.rechiseled.api;

import net.minecraft.resources.ResourceLocation;

/**
 * Created 19/01/2022 by SuperMartijn642
 * <p>
 * All default recipe locations in rechiseled.
 * Can be used for the parent argument in {@link ChiselingRecipeProvider}.
 */
public class BaseChiselingRecipes {

    public static final ResourceLocation AMETHYST_BLOCK = location("amethyst_block");
    public static final ResourceLocation ACACIA_PLANKS = location("acacia_planks");
    public static final ResourceLocation ANDESITE = location("andesite");
    public static final ResourceLocation BASALT = location("basalt");
    public static final ResourceLocation BAMBOO_PLANKS = location("bamboo_planks");
    public static final ResourceLocation BIRCH_PLANKS = location("birch_planks");
    public static final ResourceLocation BLACKSTONE = location("blackstone");
    public static final ResourceLocation CHERRY_PLANKS = location("cherry_planks");
    public static final ResourceLocation COAL_BLOCK = location("coal_block");
    public static final ResourceLocation COBBLED_DEEPSLATE = location("cobbled_deepslate");
    public static final ResourceLocation COBBLESTONE = location("cobblestone");
    public static final ResourceLocation COPPER_BLOCK = location("copper_block");
    public static final ResourceLocation CRIMSON_PLANKS = location("crimson_planks");
    public static final ResourceLocation DARK_OAK_PLANKS = location("dark_oak_planks");
    public static final ResourceLocation DARK_PRISMARINE = location("dark_prismarine");
    public static final ResourceLocation DIAMOND_BLOCK = location("diamond_block");
    public static final ResourceLocation DIORITE = location("diorite");
    public static final ResourceLocation DIRT = location("dirt");
    public static final ResourceLocation EMERALD_BLOCK = location("emerald_block");
    public static final ResourceLocation END_STONE = location("end_stone");
    public static final ResourceLocation GLOWSTONE = location("glowstone");
    public static final ResourceLocation GOLD_BLOCK = location("gold_block");
    public static final ResourceLocation GRANITE = location("granite");
    public static final ResourceLocation JUNGLE_PLANKS = location("jungle_planks");
    public static final ResourceLocation MANGROVE_PLANKS = location("mangrove_planks");
    public static final ResourceLocation NETHERRACK = location("netherrack");
    public static final ResourceLocation NETHER_BRICKS = location("nether_bricks");
    public static final ResourceLocation OAK_PLANKS = location("oak_planks");
    public static final ResourceLocation OBSIDIAN = location("obsidian");
    public static final ResourceLocation PRISMARINE_BRICKS = location("prismarine_bricks");
    public static final ResourceLocation PURPUR_BLOCK = location("purpur_block");
    public static final ResourceLocation QUARTZ_BLOCK = location("quartz_block");
    public static final ResourceLocation RED_NETHER_BRICKS = location("red_nether_bricks");
    public static final ResourceLocation RED_SANDSTONE = location("red_sandstone");
    public static final ResourceLocation SANDSTONE = location("sandstone");
    public static final ResourceLocation SPRUCE_PLANKS = location("spruce_planks");
    public static final ResourceLocation STONE = location("stone");
    public static final ResourceLocation WARPED_PLANKS = location("warped_planks");

    private static ResourceLocation location(String name){
        return new ResourceLocation("rechiseled", name);
    }
}
