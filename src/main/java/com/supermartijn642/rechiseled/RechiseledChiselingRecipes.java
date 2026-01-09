package com.supermartijn642.rechiseled;

import com.supermartijn642.rechiseled.api.util.ItemWithMeta;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

import static com.supermartijn642.rechiseled.Rechiseled.REGISTRATION;
import static com.supermartijn642.rechiseled.api.BaseChiselingRecipes.*;

/**
 * Created 05/05/2023 by SuperMartijn642
 */
public class RechiseledChiselingRecipes {

    public static void init(){
        // Acacia planks
        regularSet(ACACIA_PLANKS, () -> ItemWithMeta.of(Blocks.PLANKS, 4), () -> ItemWithMeta.of(Blocks.ACACIA_STAIRS), () -> ItemWithMeta.of(Blocks.WOODEN_SLAB, 4));
        // Andesite
        regularBlockOnly(ANDESITE, () -> ItemWithMeta.of(Blocks.STONE, 5));
        // Birch planks
        regularSet(BIRCH_PLANKS, () -> ItemWithMeta.of(Blocks.PLANKS, 2), () -> ItemWithMeta.of(Blocks.BIRCH_STAIRS), () -> ItemWithMeta.of(Blocks.WOODEN_SLAB, 2));
        // Coal block
        regularBlockOnly(COAL_BLOCK, () -> ItemWithMeta.of(Blocks.COAL_BLOCK));
        // Cobblestone
        regularSet(COBBLESTONE, () -> ItemWithMeta.of(Blocks.COBBLESTONE), () -> ItemWithMeta.of(Blocks.STONE_STAIRS), () -> ItemWithMeta.of(Blocks.STONE_SLAB, 3));
        regularBlockOnly(COBBLESTONE, () -> ItemWithMeta.of(Blocks.MOSSY_COBBLESTONE));
        // Dark oak planks
        regularSet(DARK_OAK_PLANKS, () -> ItemWithMeta.of(Blocks.PLANKS, 5), () -> ItemWithMeta.of(Blocks.DARK_OAK_STAIRS), () -> ItemWithMeta.of(Blocks.WOODEN_SLAB, 5));
        // Dark prismarine
        regularBlockOnly(DARK_PRISMARINE, () -> ItemWithMeta.of(Blocks.PRISMARINE, 2));
        // Diorite
        regularBlockOnly(DIORITE, () -> ItemWithMeta.of(Blocks.STONE, 3));
        // Dirt
        regularBlockOnly(DIRT, () -> ItemWithMeta.of(Blocks.DIRT));
        // Emerald block
        regularBlockOnly(EMERALD_BLOCK, () -> ItemWithMeta.of(Blocks.EMERALD_BLOCK));
        // End stone
        regularBlockOnly(END_STONE, () -> ItemWithMeta.of(Blocks.END_STONE));
        regularBlockOnly(END_STONE, () -> ItemWithMeta.of(Blocks.END_BRICKS));
        // Glowstone
        regularBlockOnly(GLOWSTONE, () -> ItemWithMeta.of(Blocks.GLOWSTONE));
        // Gold block
        regularBlockOnly(GOLD_BLOCK, () -> ItemWithMeta.of(Blocks.GOLD_BLOCK));
        // Granite
        regularBlockOnly(GRANITE, () -> ItemWithMeta.of(Blocks.STONE, 1));
        // Jungle planks
        regularSet(JUNGLE_PLANKS, () -> ItemWithMeta.of(Blocks.PLANKS, 3), () -> ItemWithMeta.of(Blocks.JUNGLE_STAIRS), () -> ItemWithMeta.of(Blocks.WOODEN_SLAB, 3));
        // Netherrack
        regularBlockOnly(NETHERRACK, () -> ItemWithMeta.of(Blocks.NETHERRACK));
        // Nether bricks
        regularSet(NETHER_BRICKS, () -> ItemWithMeta.of(Blocks.NETHER_BRICK), () -> ItemWithMeta.of(Blocks.NETHER_BRICK_STAIRS), () -> ItemWithMeta.of(Blocks.STONE_SLAB, 6));
        // Oak planks
        regularSet(OAK_PLANKS, () -> ItemWithMeta.of(Blocks.PLANKS, 0), () -> ItemWithMeta.of(Blocks.OAK_STAIRS), () -> ItemWithMeta.of(Blocks.WOODEN_SLAB, 0));
        // Obsidian
        regularBlockOnly(OBSIDIAN, () -> ItemWithMeta.of(Blocks.OBSIDIAN));
        // Prismarine bricks
        regularBlockOnly(PRISMARINE_BRICKS, () -> ItemWithMeta.of(Blocks.PRISMARINE, 1));
        // Purpur block
        regularBlockOnly(PURPUR_BLOCK, () -> ItemWithMeta.of(Blocks.PURPUR_PILLAR));
        // Quartz block
        regularBlockOnly(QUARTZ_BLOCK, () -> ItemWithMeta.of(Blocks.QUARTZ_BLOCK, 1));
        regularBlockOnly(QUARTZ_BLOCK, () -> ItemWithMeta.of(Blocks.QUARTZ_BLOCK, 2));
        // Red nether bricks
        regularBlockOnly(RED_NETHER_BRICKS, () -> ItemWithMeta.of(Blocks.RED_NETHER_BRICK));
        // Red sandstone
        regularSet(RED_SANDSTONE, () -> ItemWithMeta.of(Blocks.RED_SANDSTONE, 0), () -> ItemWithMeta.of(Blocks.RED_SANDSTONE_STAIRS), () -> ItemWithMeta.of(Blocks.STONE_SLAB2, 0));
        regularBlockOnly(RED_SANDSTONE, () -> ItemWithMeta.of(Blocks.RED_SANDSTONE, 1));
        regularBlockOnly(RED_SANDSTONE, () -> ItemWithMeta.of(Blocks.RED_SANDSTONE, 2));
        // Redstone block
        regularBlockOnly(REDSTONE_BLOCK, () -> ItemWithMeta.of(Blocks.REDSTONE_BLOCK));
        // Sandstone
        regularSet(SANDSTONE, () -> ItemWithMeta.of(Blocks.SANDSTONE, 0), () -> ItemWithMeta.of(Blocks.SANDSTONE_STAIRS), () -> ItemWithMeta.of(Blocks.STONE_SLAB, 1));
        regularBlockOnly(SANDSTONE, () -> ItemWithMeta.of(Blocks.SANDSTONE, 1));
        regularBlockOnly(SANDSTONE, () -> ItemWithMeta.of(Blocks.SANDSTONE, 2));
        // Spruce planks
        regularSet(SPRUCE_PLANKS, () -> ItemWithMeta.of(Blocks.PLANKS, 1), () -> ItemWithMeta.of(Blocks.SPRUCE_STAIRS), () -> ItemWithMeta.of(Blocks.WOODEN_SLAB, 1));
        // Stone
        regularBlockOnly(STONE, () -> ItemWithMeta.of(Blocks.STONE, 0));
        regularBlockOnly(STONE, () -> ItemWithMeta.of(Blocks.STONEBRICK, 0));
        regularBlockOnly(STONE, () -> ItemWithMeta.of(Blocks.STONEBRICK, 1));
        regularBlockOnly(STONE, () -> ItemWithMeta.of(Blocks.STONEBRICK, 2));
    }

    private static void regularBlockOnly(ResourceLocation recipe, Supplier<ItemWithMeta> block){
        REGISTRATION.chiselingEntry(recipe, entry -> entry.regularBlock(block.get()));
    }

    private static void regularSet(ResourceLocation recipe, Supplier<ItemWithMeta> block, Supplier<ItemWithMeta> stairs, Supplier<ItemWithMeta> slab){
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
