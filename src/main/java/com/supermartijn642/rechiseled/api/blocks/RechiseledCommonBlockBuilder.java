package com.supermartijn642.rechiseled.api.blocks;

import com.supermartijn642.core.block.BlockProperties;
import com.supermartijn642.rechiseled.api.registration.RechiseledRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Used to configure and build new blocks for use with Rechiseled.
 * See {@link RechiseledRegistration} to get new block builders.
 * <p>
 * Created 11/01/2026 by SuperMartijn642
 */
public interface RechiseledCommonBlockBuilder<T extends RechiseledCommonBlockBuilder<?>> {

    /**
     * Sets the properties for the constructed blocks
     */
    T properties(BlockProperties properties);

    /**
     * Sets the properties for the constructed blocks
     */
    T properties(Supplier<BlockProperties> properties);

    /**
     * Allows configuration of block properties
     */
    T properties(Consumer<BlockProperties> configurer);

    /**
     * Sets the item groups the constructed blocks will be added to
     */
    T itemGroups(CreativeModeTab group, CreativeModeTab... groups);

    /**
     * Sets the item groups the constructed blocks will be added to, to none
     */
    T noItemGroups();

    /**
     * Indicates that this block does not have a non-connecting variant
     */
    T noRegularVariant();

    /**
     * Indicates that this block does not have a connecting variant
     */
    T noConnectingVariant();

    /**
     * Sets the regular variant for this block to the given block, stairs, and slab.
     * This is only relevant for the chiseling recipes.
     */
    T regularVariant(Supplier<Block> block);

    /**
     * Sets the connecting variant for this block to the given block.
     * This is only relevant for the chiseling recipes.
     */
    T connectingVariant(Supplier<Block> block);

    /**
     * Adds the constructed block to the given block tag.
     */
    T blockTag(ResourceLocation identifier);

    /**
     * Adds the constructed block to the given block tag.
     */
    T itemTag(ResourceLocation identifier);

    /**
     * Adds the constructed block to the given block tag.
     */
    default T itemAndBlockTag(ResourceLocation identifier){
        this.blockTag(identifier);
        return this.itemTag(identifier);
    }

    /**
     * Copies the mining tags from the given block.
     */
    T miningTagsFrom(Supplier<Block> block);

    /**
     * Sets the translation for the constructed block.
     */
    T translation(String translation);
}
