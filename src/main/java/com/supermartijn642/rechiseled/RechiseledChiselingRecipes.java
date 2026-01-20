package com.supermartijn642.rechiseled;

import com.supermartijn642.rechiseled.api.util.ItemWithMeta;
import net.minecraft.init.Blocks;

import static com.supermartijn642.rechiseled.Rechiseled.REGISTRATION;
import static com.supermartijn642.rechiseled.api.BaseChiselingRecipes.*;

/**
 * Created 05/05/2023 by SuperMartijn642
 */
public class RechiseledChiselingRecipes {

    public static void init(){
        // Acacia planks
        REGISTRATION.chiselingEntry(ACACIA_PLANKS, () -> ItemWithMeta.of(Blocks.PLANKS, 4), () -> ItemWithMeta.of(Blocks.ACACIA_STAIRS), () -> ItemWithMeta.of(Blocks.WOODEN_SLAB, 4), null, null, null);
        // Andesite
        REGISTRATION.chiselingEntry(ANDESITE, () -> ItemWithMeta.of(Blocks.STONE, 5), null);
        // Birch planks
        REGISTRATION.chiselingEntry(BIRCH_PLANKS, () -> ItemWithMeta.of(Blocks.PLANKS, 2), () -> ItemWithMeta.of(Blocks.BIRCH_STAIRS), () -> ItemWithMeta.of(Blocks.WOODEN_SLAB, 2), null, null, null);
        // Coal block
        REGISTRATION.chiselingEntry(COAL_BLOCK, () -> ItemWithMeta.of(Blocks.COAL_BLOCK), null);
        // Cobblestone
        REGISTRATION.chiselingEntry(COBBLESTONE, () -> ItemWithMeta.of(Blocks.COBBLESTONE), () -> ItemWithMeta.of(Blocks.STONE_STAIRS), () -> ItemWithMeta.of(Blocks.STONE_SLAB, 3), null, null, null);
        REGISTRATION.chiselingEntry(COBBLESTONE, () -> ItemWithMeta.of(Blocks.MOSSY_COBBLESTONE), null);
        // Dark oak planks
        REGISTRATION.chiselingEntry(DARK_OAK_PLANKS, () -> ItemWithMeta.of(Blocks.PLANKS, 5), () -> ItemWithMeta.of(Blocks.DARK_OAK_STAIRS), () -> ItemWithMeta.of(Blocks.WOODEN_SLAB, 5), null, null, null);
        // Dark prismarine
        REGISTRATION.chiselingEntry(DARK_PRISMARINE, () -> ItemWithMeta.of(Blocks.PRISMARINE, 2), null);
        // Diorite
        REGISTRATION.chiselingEntry(DIORITE, () -> ItemWithMeta.of(Blocks.STONE, 3), null);
        // Dirt
        REGISTRATION.chiselingEntry(DIRT, () -> ItemWithMeta.of(Blocks.DIRT), null);
        // Emerald block
        REGISTRATION.chiselingEntry(EMERALD_BLOCK, () -> ItemWithMeta.of(Blocks.EMERALD_BLOCK), null);
        // End stone
        REGISTRATION.chiselingEntry(END_STONE, () -> ItemWithMeta.of(Blocks.END_STONE), null);
        REGISTRATION.chiselingEntry(END_STONE, () -> ItemWithMeta.of(Blocks.END_BRICKS), null);
        // Glowstone
        REGISTRATION.chiselingEntry(GLOWSTONE, () -> ItemWithMeta.of(Blocks.GLOWSTONE), null);
        // Gold block
        REGISTRATION.chiselingEntry(GOLD_BLOCK, () -> ItemWithMeta.of(Blocks.GOLD_BLOCK), null);
        // Granite
        REGISTRATION.chiselingEntry(GRANITE, () -> ItemWithMeta.of(Blocks.STONE, 1), null);
        // Jungle planks
        REGISTRATION.chiselingEntry(JUNGLE_PLANKS, () -> ItemWithMeta.of(Blocks.PLANKS, 3), () -> ItemWithMeta.of(Blocks.JUNGLE_STAIRS), () -> ItemWithMeta.of(Blocks.WOODEN_SLAB, 3), null, null, null);
        // Netherrack
        REGISTRATION.chiselingEntry(NETHERRACK, () -> ItemWithMeta.of(Blocks.NETHERRACK), null);
        // Nether bricks
        REGISTRATION.chiselingEntry(NETHER_BRICKS, () -> ItemWithMeta.of(Blocks.NETHER_BRICK), () -> ItemWithMeta.of(Blocks.NETHER_BRICK_STAIRS), () -> ItemWithMeta.of(Blocks.STONE_SLAB, 6), null, null, null);
        // Oak planks
        REGISTRATION.chiselingEntry(OAK_PLANKS, () -> ItemWithMeta.of(Blocks.PLANKS, 0), () -> ItemWithMeta.of(Blocks.OAK_STAIRS), () -> ItemWithMeta.of(Blocks.WOODEN_SLAB, 0), null, null, null);
        // Obsidian
        REGISTRATION.chiselingEntry(OBSIDIAN, () -> ItemWithMeta.of(Blocks.OBSIDIAN), null);
        // Prismarine bricks
        REGISTRATION.chiselingEntry(PRISMARINE_BRICKS, () -> ItemWithMeta.of(Blocks.PRISMARINE, 1), null);
        // Purpur block
        REGISTRATION.chiselingEntry(PURPUR_BLOCK, () -> ItemWithMeta.of(Blocks.PURPUR_PILLAR), null);
        // Quartz block
        REGISTRATION.chiselingEntry(QUARTZ_BLOCK, () -> ItemWithMeta.of(Blocks.QUARTZ_BLOCK, 1), null);
        REGISTRATION.chiselingEntry(QUARTZ_BLOCK, () -> ItemWithMeta.of(Blocks.QUARTZ_BLOCK, 2), null);
        // Red nether bricks
        REGISTRATION.chiselingEntry(RED_NETHER_BRICKS, () -> ItemWithMeta.of(Blocks.RED_NETHER_BRICK), null);
        // Red sandstone
        REGISTRATION.chiselingEntry(RED_SANDSTONE, () -> ItemWithMeta.of(Blocks.RED_SANDSTONE, 0), () -> ItemWithMeta.of(Blocks.RED_SANDSTONE_STAIRS), () -> ItemWithMeta.of(Blocks.STONE_SLAB2, 0), null, null, null);
        REGISTRATION.chiselingEntry(RED_SANDSTONE, () -> ItemWithMeta.of(Blocks.RED_SANDSTONE, 1), null);
        REGISTRATION.chiselingEntry(RED_SANDSTONE, () -> ItemWithMeta.of(Blocks.RED_SANDSTONE, 2), null);
        // Redstone block
        REGISTRATION.chiselingEntry(REDSTONE_BLOCK, () -> ItemWithMeta.of(Blocks.REDSTONE_BLOCK), null);
        // Sandstone
        REGISTRATION.chiselingEntry(SANDSTONE, () -> ItemWithMeta.of(Blocks.SANDSTONE, 0), () -> ItemWithMeta.of(Blocks.SANDSTONE_STAIRS), () -> ItemWithMeta.of(Blocks.STONE_SLAB, 1), null, null, null);
        REGISTRATION.chiselingEntry(SANDSTONE, () -> ItemWithMeta.of(Blocks.SANDSTONE, 1), null);
        REGISTRATION.chiselingEntry(SANDSTONE, () -> ItemWithMeta.of(Blocks.SANDSTONE, 2), null);
        // Spruce planks
        REGISTRATION.chiselingEntry(SPRUCE_PLANKS, () -> ItemWithMeta.of(Blocks.PLANKS, 1), null);
        // Stone
        REGISTRATION.chiselingEntry(STONE, () -> ItemWithMeta.of(Blocks.STONE, 0), null);
        REGISTRATION.chiselingEntry(STONE, () -> ItemWithMeta.of(Blocks.STONEBRICK, 0), null);
        REGISTRATION.chiselingEntry(STONE, () -> ItemWithMeta.of(Blocks.STONEBRICK, 1), null);
        REGISTRATION.chiselingEntry(STONE, () -> ItemWithMeta.of(Blocks.STONEBRICK, 2), null);
    }
}
