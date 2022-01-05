package com.supermartijn642.rechiseled;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;

import java.util.Locale;
import java.util.function.Supplier;

import static com.supermartijn642.rechiseled.RechiseledTagGroups.*;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public enum RechiseledBlockType {

    ACACIA_PLANKS_BEAMS("Acacia Plank Beams", () -> Blocks.PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_BRICKS("Acacia Plank Bricks", () -> Blocks.PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_CRATE("Acacia Planks Crate", () -> Blocks.PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_DIAGONAL_STRIPES("Diagonal Acacia Plank Stripes", () -> Blocks.PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_DIAGONAL_TILES("Diagonal Acacia Plank Tiles", () -> Blocks.PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_DOTTED("Dotted Acacia Planks", () -> Blocks.PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_FLOORING("Acacia Plank Flooring", () -> Blocks.PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_LARGE_TILES("Large Acacia Plank Tiles", () -> Blocks.PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_PATTERN("Acacia Plank Pattern", () -> Blocks.PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_SMALL_BRICKS("Small Acacia Plank Bricks", () -> Blocks.PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_SMALL_TILES("Small Acacia Plank Tiles", () -> Blocks.PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_SQUARES("Acacia Plank Squares", () -> Blocks.PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_TILES("Acacia Plank Tiles", () -> Blocks.PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_WAVY("Wavy Acacia Planks", () -> Blocks.PLANKS, false, ACACIA_PLANKS),
    ACACIA_PLANKS_WOVEN("Woven Acacia Planks", () -> Blocks.PLANKS, false, ACACIA_PLANKS),
    ANDESITE_BRICKS("Andesite Bricks", () -> Blocks.STONE, false, ANDESITE),
    ANDESITE_DIAGONAL_BRICKS("Diagonal Andesite Bricks", () -> Blocks.STONE, false, ANDESITE),
    ANDESITE_DOTTED("Dotted Andesite", () -> Blocks.STONE, false, ANDESITE),
    ANDESITE_PAVING("Andesite Paving", () -> Blocks.STONE, false, ANDESITE),
    ANDESITE_POLISHED("Polished Andesite", () -> Blocks.STONE, true, ANDESITE),
    ANDESITE_ROTATED_BRICKS("Rotated Andesite Bricks", () -> Blocks.STONE, false, ANDESITE),
    ANDESITE_SQUARES("Andesite Squares", () -> Blocks.STONE, false, ANDESITE),
    ANDESITE_TILES("Andesite Tiles", () -> Blocks.STONE, false, ANDESITE),
    ANDESITE_WAVY("Wavy Andesite", () -> Blocks.STONE, false, ANDESITE),
    BIRCH_PLANKS_BEAMS("Birch Plank Beams", () -> Blocks.PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_BRICKS("Birch Plank Bricks", () -> Blocks.PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_CRATE("Birch Planks Crate", () -> Blocks.PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_DIAGONAL_STRIPES("Diagonal Birch Plank Stripes", () -> Blocks.PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_DIAGONAL_TILES("Diagonal Birch Plank Tiles", () -> Blocks.PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_DOTTED("Dotted Birch Planks", () -> Blocks.PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_FLOORING("Birch Plank Flooring", () -> Blocks.PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_LARGE_TILES("Large Birch Plank Tiles", () -> Blocks.PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_PATTERN("Birch Plank Pattern", () -> Blocks.PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_SMALL_BRICKS("Small Birch Plank Bricks", () -> Blocks.PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_SMALL_TILES("Small Birch Plank Tiles", () -> Blocks.PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_SQUARES("Birch Plank Squares", () -> Blocks.PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_TILES("Birch Plank Tiles", () -> Blocks.PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_WAVY("Wavy Birch Planks", () -> Blocks.PLANKS, false, BIRCH_PLANKS),
    BIRCH_PLANKS_WOVEN("Woven Birch Planks", () -> Blocks.PLANKS, false, BIRCH_PLANKS),
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
    DARK_OAK_PLANKS_BEAMS("Dark Oak Plank Beams", () -> Blocks.PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_BRICKS("Dark Oak Plank Bricks", () -> Blocks.PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_CRATE("Dark Oak Planks Crate", () -> Blocks.PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_DIAGONAL_STRIPES("Diagonal Dark Oak Plank Stripes", () -> Blocks.PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_DIAGONAL_TILES("Diagonal Dark Oak Plank Tiles", () -> Blocks.PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_DOTTED("Dotted Dark Oak Planks", () -> Blocks.PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_FLOORING("Dark Oak Plank Flooring", () -> Blocks.PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_LARGE_TILES("Large Dark Oak Plank Tiles", () -> Blocks.PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_PATTERN("Dark Oak Plank Pattern", () -> Blocks.PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_SMALL_BRICKS("Small Dark Oak Plank Bricks", () -> Blocks.PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_SMALL_TILES("Small Dark Oak Plank Tiles", () -> Blocks.PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_SQUARES("Dark Oak Plank Squares", () -> Blocks.PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_TILES("Dark Oak Plank Tiles", () -> Blocks.PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_WAVY("Wavy Dark Oak Planks", () -> Blocks.PLANKS, false, DARK_OAK_PLANKS),
    DARK_OAK_PLANKS_WOVEN("Woven Dark Oak Planks", () -> Blocks.PLANKS, false, DARK_OAK_PLANKS),
    DIORITE_BRICKS("Diorite Bricks", () -> Blocks.STONE, false, DIORITE),
    DIORITE_DIAGONAL_BRICKS("Diagonal Diorite Bricks", () -> Blocks.STONE, false, DIORITE),
    DIORITE_DOTTED("Dotted Diorite", () -> Blocks.STONE, false, DIORITE),
    DIORITE_PAVING("Diorite Paving", () -> Blocks.STONE, false, DIORITE),
    DIORITE_POLISHED("Polished Diorite", () -> Blocks.STONE, true, DIORITE),
    DIORITE_ROTATED_BRICKS("Rotated Diorite Bricks", () -> Blocks.STONE, false, DIORITE),
    DIORITE_SQUARES("Diorite Squares", () -> Blocks.STONE, false, DIORITE),
    DIORITE_TILES("Diorite Tiles", () -> Blocks.STONE, false, DIORITE),
    DIORITE_WAVY("Wavy Diorite", () -> Blocks.STONE, false, DIORITE),
    END_STONE_DIAGONAL_BRICKS("Diagonal End Stone Bricks", () -> Blocks.END_BRICKS, false, END_STONE),
    END_STONE_POLISHED("Polished End Stone", () -> Blocks.END_BRICKS, false, END_STONE),
    END_STONE_TILES("End Stone Tiles", () -> Blocks.END_BRICKS, false, END_STONE),
    GLOWSTONE_BRICKS("Glowstone Bricks", () -> Blocks.GLOWSTONE, false, GLOWSTONE),
    GLOWSTONE_CRUSHED("Crushed Glowstone", () -> Blocks.GLOWSTONE, false, GLOWSTONE),
    GLOWSTONE_LARGE_TILES("Large Glowstone Tiles", () -> Blocks.GLOWSTONE, false, GLOWSTONE),
    GLOWSTONE_SMALL_TILES("Small Glowstone Tiles", () -> Blocks.GLOWSTONE, false, GLOWSTONE),
    GLOWSTONE_SMOOTH("Smooth Glowstone", () -> Blocks.GLOWSTONE, false, GLOWSTONE),
    GLOWSTONE_TILES("Glowstone Tiles", () -> Blocks.GLOWSTONE, false, GLOWSTONE),
    GRANITE_BRICKS("Granite Bricks", () -> Blocks.STONE, false, GRANITE),
    GRANITE_DIAGONAL_BRICKS("Diagonal Granite Bricks", () -> Blocks.STONE, false, GRANITE),
    GRANITE_DOTTED("Dotted Granite", () -> Blocks.STONE, false, GRANITE),
    GRANITE_PAVING("Granite Paving", () -> Blocks.STONE, false, GRANITE),
    GRANITE_POLISHED("Polished Granite", () -> Blocks.STONE, true, GRANITE),
    GRANITE_ROTATED_BRICKS("Rotated Granite Bricks", () -> Blocks.STONE, false, GRANITE),
    GRANITE_SQUARES("Granite Squares", () -> Blocks.STONE, false, GRANITE),
    GRANITE_TILES("Granite Tiles", () -> Blocks.STONE, false, GRANITE),
    GRANITE_WAVY("Wavy Granite", () -> Blocks.STONE, false, GRANITE),
    JUNGLE_PLANKS_BEAMS("Jungle Plank Beams", () -> Blocks.PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_BRICKS("Jungle Plank Bricks", () -> Blocks.PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_CRATE("Jungle Planks Crate", () -> Blocks.PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_DIAGONAL_STRIPES("Diagonal Jungle Plank Stripes", () -> Blocks.PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_DIAGONAL_TILES("Diagonal Jungle Plank Tiles", () -> Blocks.PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_DOTTED("Dotted Jungle Planks", () -> Blocks.PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_FLOORING("Jungle Plank Flooring", () -> Blocks.PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_LARGE_TILES("Large Jungle Plank Tiles", () -> Blocks.PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_PATTERN("Jungle Plank Pattern", () -> Blocks.PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_SMALL_BRICKS("Small Jungle Plank Bricks", () -> Blocks.PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_SMALL_TILES("Small Jungle Plank Tiles", () -> Blocks.PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_SQUARES("Jungle Plank Squares", () -> Blocks.PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_TILES("Jungle Plank Tiles", () -> Blocks.PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_WAVY("Wavy Jungle Planks", () -> Blocks.PLANKS, false, JUNGLE_PLANKS),
    JUNGLE_PLANKS_WOVEN("Woven Jungle Planks", () -> Blocks.PLANKS, false, JUNGLE_PLANKS),
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
    NETHER_BRICKS_LARGE_BRICKS("Large Nether Bricks", () -> Blocks.NETHER_BRICK, false, NETHER_BRICKS),
    NETHER_BRICKS_LARGE_TILES("Large Nether Brick Tiles", () -> Blocks.NETHER_BRICK, false, NETHER_BRICKS),
    NETHER_BRICKS_SMALL_TILES("Small Nether Brick Tiles", () -> Blocks.NETHER_BRICK, false, NETHER_BRICKS),
    NETHER_BRICKS_TILES("Nether Brick Tiles", () -> Blocks.NETHER_BRICK, false, NETHER_BRICKS),
    OAK_PLANKS_BEAMS("Oak Plank Beams", () -> Blocks.PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_BRICKS("Oak Plank Bricks", () -> Blocks.PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_CRATE("Oak Planks Crate", () -> Blocks.PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_DIAGONAL_STRIPES("Diagonal Oak Plank Stripes", () -> Blocks.PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_DIAGONAL_TILES("Diagonal Oak Plank Tiles", () -> Blocks.PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_DOTTED("Dotted Oak Planks", () -> Blocks.PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_FLOORING("Oak Plank Flooring", () -> Blocks.PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_LARGE_TILES("Large Oak Plank Tiles", () -> Blocks.PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_PATTERN("Oak Plank Pattern", () -> Blocks.PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_SMALL_BRICKS("Small Oak Plank Bricks", () -> Blocks.PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_SMALL_TILES("Small Oak Plank Tiles", () -> Blocks.PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_SQUARES("Oak Plank Squares", () -> Blocks.PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_TILES("Oak Plank Tiles", () -> Blocks.PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_WAVY("Wavy Oak Planks", () -> Blocks.PLANKS, false, OAK_PLANKS),
    OAK_PLANKS_WOVEN("Woven Oak Planks", () -> Blocks.PLANKS, false, OAK_PLANKS),
    PRISMARINE_BRICKS_BRICKS("Prismarine Bricks", () -> Blocks.PRISMARINE, false, PRISMARINE_BRICKS),
    PRISMARINE_BRICKS_DIAGONAL_BRICKS("Diagonal Prismarine Bricks", () -> Blocks.PRISMARINE, false, PRISMARINE_BRICKS),
    PRISMARINE_BRICKS_POLISHED("Polished Prismarine", () -> Blocks.PRISMARINE, false, PRISMARINE_BRICKS),
    PRISMARINE_BRICKS_TILES("Prismarine Tiles", () -> Blocks.PRISMARINE, false, PRISMARINE_BRICKS),
    RED_NETHER_BRICKS_LARGE_BRICKS("Large Red Nether Bricks", () -> Blocks.RED_NETHER_BRICK, false, RED_NETHER_BRICKS),
    RED_NETHER_BRICKS_LARGE_TILES("Large Red Nether Brick Tiles", () -> Blocks.RED_NETHER_BRICK, false, RED_NETHER_BRICKS),
    RED_NETHER_BRICKS_SMALL_TILES("Small Red Nether Brick Tiles", () -> Blocks.RED_NETHER_BRICK, false, RED_NETHER_BRICKS),
    RED_NETHER_BRICKS_TILES("Red Nether Brick Tiles", () -> Blocks.RED_NETHER_BRICK, false, RED_NETHER_BRICKS),
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
    SPRUCE_PLANKS_BEAMS("Spruce Plank Beams", () -> Blocks.PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_BRICKS("Spruce Plank Bricks", () -> Blocks.PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_CRATE("Spruce Planks Crate", () -> Blocks.PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_DIAGONAL_STRIPES("Diagonal Spruce Plank Stripes", () -> Blocks.PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_DIAGONAL_TILES("Diagonal Spruce Plank Tiles", () -> Blocks.PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_DOTTED("Dotted Spruce Planks", () -> Blocks.PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_FLOORING("Spruce Plank Flooring", () -> Blocks.PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_LARGE_TILES("Large Spruce Plank Tiles", () -> Blocks.PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_PATTERN("Spruce Plank Pattern", () -> Blocks.PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_SMALL_BRICKS("Small Spruce Plank Bricks", () -> Blocks.PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_SMALL_TILES("Small Spruce Plank Tiles", () -> Blocks.PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_SQUARES("Spruce Plank Squares", () -> Blocks.PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_TILES("Spruce Plank Tiles", () -> Blocks.PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_WAVY("Wavy Spruce Planks", () -> Blocks.PLANKS, false, SPRUCE_PLANKS),
    SPRUCE_PLANKS_WOVEN("Woven Spruce Planks", () -> Blocks.PLANKS, false, SPRUCE_PLANKS),
    STONE_BIG_TILES("Large Stone Tiles", () -> Blocks.STONE, false, STONE),
    STONE_BORDERED("Bordered Stone", () -> Blocks.STONE, false, STONE),
    STONE_CHISELED_BRICKS("Chiseled Stone Bricks", () -> Blocks.STONE, true, STONE),
    STONE_DIAGONAL_BRICKS("Diagonal Stone Bricks", () -> Blocks.STONE, false, STONE),
    STONE_PATH("Stone Path", () -> Blocks.STONE, false, STONE),
    STONE_SMALL_BRICKS("Small Stone Bricks", () -> Blocks.STONE, false, STONE),
    STONE_SMOOTH("Smooth Stone", () -> Blocks.STONE, true, STONE),
    STONE_TILES("Stone Tiles", () -> Blocks.STONE, false, STONE);

    public final String regularRegistryName;
    public final String connectingRegistryName;
    public final String englishTranslation;
    public final Supplier<Block> parentBlock;
    public final boolean useParent;
    public final String[] tags;

    private Block regularBlock;
    private Block connectingBlock;
    private ItemBlock regularItem;
    private ItemBlock connectingItem;

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
            this.regularItem = new ItemBlock(this.regularBlock);
            this.regularItem.setCreativeTab(Rechiseled.GROUP);
            this.regularItem.setRegistryName(this.regularRegistryName);
        }
        this.connectingItem = new ItemBlock(this.connectingBlock);
        this.connectingItem.setCreativeTab(Rechiseled.GROUP);
        this.connectingItem.setRegistryName(this.connectingRegistryName);
    }

    public Block getRegularBlock(){
        return this.regularBlock;
    }

    public Block getConnectingBlock(){
        return this.connectingBlock;
    }

    public ItemBlock getRegularItem(){
        return this.regularItem;
    }

    public ItemBlock getConnectingItem(){
        return this.connectingItem;
    }
}
