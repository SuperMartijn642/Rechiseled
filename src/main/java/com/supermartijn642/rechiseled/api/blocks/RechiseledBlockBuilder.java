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
 * Created 26/04/2023 by SuperMartijn642
 */
public interface RechiseledBlockBuilder {

    /**
     * Sets the properties for the constructed blocks
     */
    RechiseledBlockBuilder properties(BlockProperties properties);

    /**
     * Copies the properties from the given block
     */
    RechiseledBlockBuilder copyProperties(Supplier<Block> block);

    /**
     * Allows configuration of block properties
     */
    RechiseledBlockBuilder properties(Consumer<BlockProperties> configurer);

    /**
     * Sets the item groups the constructed blocks will be added to
     */
    RechiseledBlockBuilder itemGroups(CreativeModeTab group, CreativeModeTab... groups);

    /**
     * Sets the type of block to be constructed
     */
    RechiseledBlockBuilder specification(BlockSpecification specification);

    /**
     * Indicates that this block does not have a non-connecting variant
     */
    RechiseledBlockBuilder noRegularVariant();

    /**
     * Indicates that this block does not have a connecting variant
     */
    RechiseledBlockBuilder noConnectingVariant();

    /**
     * Sets the regular variant for this block to the given block.
     * This is only relevant for the chiseling recipes.
     */
    RechiseledBlockBuilder regularVariant(Supplier<Block> blockSupplier);

    /**
     * Sets the connecting variant for this block to the given block.
     * This is only relevant for the chiseling recipes.
     */
    RechiseledBlockBuilder connectingVariant(Supplier<Block> blockSupplier);

    /**
     * Sets the chiseling recipe which this block should be added to.
     */
    RechiseledBlockBuilder recipe(ResourceLocation location);

    /**
     * Adds the constructed block to the given tag.
     */
    RechiseledBlockBuilder blockTag(String namespace, String identifier);

    /**
     * Copies the mining tags from the given block.
     */
    RechiseledBlockBuilder miningTagsFrom(Supplier<Block> blockSupplier);

    /**
     * Sets the translation for the constructed block.
     */
    RechiseledBlockBuilder translation(String translation);

    /**
     * Sets a different model type to be generated.
     * By default, the model type from the block specification will be used.
     */
    RechiseledBlockBuilder model(BlockModelType modelType);

    /**
     * Completes this block builder and returns an {@link RechiseledBlockType} containing the constructed blocks.
     */
    RechiseledBlockType build();
}
