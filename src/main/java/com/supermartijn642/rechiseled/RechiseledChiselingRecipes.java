package com.supermartijn642.rechiseled;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import static com.supermartijn642.rechiseled.Rechiseled.REGISTRATION;
import static com.supermartijn642.rechiseled.api.BaseChiselingRecipes.*;

/**
 * Created 05/05/2023 by SuperMartijn642
 */
public class RechiseledChiselingRecipes {

    public static void init(){
        // Acacia planks
        REGISTRATION.chiselingEntry(ACACIA_PLANKS, () -> Item.getItemFromBlock(Blocks.PLANKS), 4, null, 0);
        // Andesite
        REGISTRATION.chiselingEntry(ANDESITE, () -> Item.getItemFromBlock(Blocks.STONE), 5, null, 0);
        // Birch planks
        REGISTRATION.chiselingEntry(BIRCH_PLANKS, () -> Item.getItemFromBlock(Blocks.PLANKS), 2, null, 0);
        // Cobblestone
        REGISTRATION.chiselingEntry(COBBLESTONE, () -> Item.getItemFromBlock(Blocks.COBBLESTONE), 0, null, 0);
        REGISTRATION.chiselingEntry(COBBLESTONE, () -> Item.getItemFromBlock(Blocks.MOSSY_COBBLESTONE), 0, null, 0);
        // Dark oak planks
        REGISTRATION.chiselingEntry(DARK_OAK_PLANKS, () -> Item.getItemFromBlock(Blocks.PLANKS), 5, null, 0);
        // Dark prismarine
        REGISTRATION.chiselingEntry(DARK_PRISMARINE, () -> Item.getItemFromBlock(Blocks.PRISMARINE), 2, null, 0);
        // Diorite
        REGISTRATION.chiselingEntry(DIORITE, () -> Item.getItemFromBlock(Blocks.STONE), 3, null, 0);
        // Dirt
        REGISTRATION.chiselingEntry(DIRT, () -> Item.getItemFromBlock(Blocks.DIRT), 0, null, 0);
        // End stone
        REGISTRATION.chiselingEntry(END_STONE, () -> Item.getItemFromBlock(Blocks.END_STONE), 0, null, 0);
        REGISTRATION.chiselingEntry(END_STONE, () -> Item.getItemFromBlock(Blocks.END_BRICKS), 0, null, 0);
        // Glowstone
        REGISTRATION.chiselingEntry(GLOWSTONE, () -> Item.getItemFromBlock(Blocks.GLOWSTONE), 0, null, 0);
        // Granite
        REGISTRATION.chiselingEntry(GRANITE, () -> Item.getItemFromBlock(Blocks.STONE), 1, null, 0);
        // Jungle planks
        REGISTRATION.chiselingEntry(JUNGLE_PLANKS, () -> Item.getItemFromBlock(Blocks.PLANKS), 3, null, 0);
        // Netherrack
        REGISTRATION.chiselingEntry(NETHERRACK, () -> Item.getItemFromBlock(Blocks.NETHERRACK), 0, null, 0);
        // Nether bricks
        REGISTRATION.chiselingEntry(NETHER_BRICKS, () -> Item.getItemFromBlock(Blocks.NETHER_BRICK), 0, null, 0);
        // Oak planks
        REGISTRATION.chiselingEntry(OAK_PLANKS, () -> Item.getItemFromBlock(Blocks.PLANKS), 0, null, 0);
        // Obsidian
        REGISTRATION.chiselingEntry(OBSIDIAN, () -> Item.getItemFromBlock(Blocks.OBSIDIAN), 0, null, 0);
        // Prismarine bricks
        REGISTRATION.chiselingEntry(PRISMARINE_BRICKS, () -> Item.getItemFromBlock(Blocks.PRISMARINE), 1, null, 0);
        // Purpur block
        REGISTRATION.chiselingEntry(PURPUR_BLOCK, () -> Item.getItemFromBlock(Blocks.PURPUR_PILLAR), 0, null, 0);
        // Quartz block
        REGISTRATION.chiselingEntry(QUARTZ_BLOCK, () -> Item.getItemFromBlock(Blocks.QUARTZ_BLOCK), 1, null, 0);
        REGISTRATION.chiselingEntry(QUARTZ_BLOCK, () -> Item.getItemFromBlock(Blocks.QUARTZ_BLOCK), 2, null, 0);
        // Red nether bricks
        REGISTRATION.chiselingEntry(RED_NETHER_BRICKS, () -> Item.getItemFromBlock(Blocks.RED_NETHER_BRICK), 0, null, 0);
        // Red sandstone
        REGISTRATION.chiselingEntry(RED_SANDSTONE, () -> Item.getItemFromBlock(Blocks.RED_SANDSTONE), 0, null, 0);
        REGISTRATION.chiselingEntry(RED_SANDSTONE, () -> Item.getItemFromBlock(Blocks.RED_SANDSTONE), 1, null, 0);
        REGISTRATION.chiselingEntry(RED_SANDSTONE, () -> Item.getItemFromBlock(Blocks.RED_SANDSTONE), 2, null, 0);
        // Sandstone
        REGISTRATION.chiselingEntry(SANDSTONE, () -> Item.getItemFromBlock(Blocks.SANDSTONE), 0, null, 0);
        REGISTRATION.chiselingEntry(SANDSTONE, () -> Item.getItemFromBlock(Blocks.SANDSTONE), 1, null, 0);
        REGISTRATION.chiselingEntry(SANDSTONE, () -> Item.getItemFromBlock(Blocks.SANDSTONE), 2, null, 0);
        // Spruce planks
        REGISTRATION.chiselingEntry(SPRUCE_PLANKS, () -> Item.getItemFromBlock(Blocks.PLANKS), 1, null, 0);
        // Stone
        REGISTRATION.chiselingEntry(STONE, () -> Item.getItemFromBlock(Blocks.STONE), 0, null, 0);
        REGISTRATION.chiselingEntry(STONE, () -> Item.getItemFromBlock(Blocks.STONEBRICK), 0, null, 0);
        REGISTRATION.chiselingEntry(STONE, () -> Item.getItemFromBlock(Blocks.STONEBRICK), 1, null, 0);
        REGISTRATION.chiselingEntry(STONE, () -> Item.getItemFromBlock(Blocks.STONEBRICK), 2, null, 0);
    }
}
