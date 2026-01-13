package com.supermartijn642.rechiseled.api.blocks;

import com.supermartijn642.rechiseled.api.registration.RechiseledRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Used to configure and build new blocks for use with Rechiseled.
 * See {@link RechiseledRegistration} to get new block builders.
 * <p>
 * Created 26/04/2023 by SuperMartijn642
 */
public interface RechiseledBlockBuilder extends RechiseledCommonBlockBuilder<RechiseledBlockBuilder> {

    /**
     * Copies the properties from the given block
     * @deprecated do not use this. Specify properties explicitly through {@link #properties(Consumer)}.
     */
    @Deprecated
    RechiseledBlockBuilder copyProperties(Supplier<Block> block);

    /**
     * Sets the type of block to be constructed
     */
    RechiseledBlockBuilder specification(BlockSpecification specification);

    /**
     * Sets the regular variant for this block to the given block.
     * This is only relevant for the chiseling recipes.
     */
    RechiseledBlockBuilder regularVariant(Supplier<Block> block, Supplier<Block> stairs, Supplier<Block> slab);

    /**
     * Sets the connecting variant for this block to the given block, stairs, and slab.
     * This is only relevant for the chiseling recipes.
     */
    RechiseledBlockBuilder connectingVariant(Supplier<Block> block, Supplier<Block> stairs, Supplier<Block> slab);

    /**
     * Creates stairs for this block.<br>
     * Stairs will inherit properties from this block unless overwritten on their builder.
     * @see #withStairs(Consumer) with stair configuration
     */
    RechiseledBlockBuilder withStairs();

    /**
     * Creates stairs for this block configure through the given consumer.<br>
     * Stairs will inherit properties from this block unless overwritten on their builder.
     */
    RechiseledBlockBuilder withStairs(Consumer<RechiseledStairsBuilder> builder);

    /**
     * Creates slabs for this block configure through the returned builder.<br>
     * Slabs will inherit properties from this block unless overwritten on their builder.
     * @see #withSlabs(Consumer) with slab configuration
     */
    RechiseledBlockBuilder withSlabs();

    /**
     * Creates slabs for this block configure through the given consumer.<br>
     * Slabs will inherit properties from this block unless overwritten on their builder.
     */
    RechiseledBlockBuilder withSlabs(Consumer<RechiseledSlabBuilder> builder);

    /**
     * Sets the chiseling recipe which this block should be added to.
     */
    RechiseledBlockBuilder recipe(ResourceLocation location);

    /**
     * Sets a different model type to be generated.
     * By default, the model type from the block specification will be used.
     */
    RechiseledBlockBuilder model(BlockModelType modelType);

    /**
     * Adds the constructed block to the given block tag.
     * @param includeStairsAndSlabs whether to add the stairs and slabs of this block type to the tag
     */
    RechiseledBlockBuilder blockTag(ResourceLocation identifier, boolean includeStairsAndSlabs);

    /**
     * Adds the constructed block to the given block tag.
     */
    default RechiseledBlockBuilder blockTag(String namespace, String identifier){
        return this.blockTag(new ResourceLocation(namespace, identifier));
    }

    /**
     * Adds the constructed block to the given block tag.
     * @param includeStairsAndSlabs whether to add the stairs and slabs of this block type to the tag
     */
    RechiseledBlockBuilder itemTag(ResourceLocation identifier, boolean includeStairsAndSlabs);

    /**
     * Adds the constructed block to the given block tag.
     * @param includeStairsAndSlabs whether to add the stairs and slabs of this block type to the tag
     */
    default RechiseledBlockBuilder itemAndBlockTag(ResourceLocation identifier, boolean includeStairsAndSlabs){
        return this.blockTag(identifier, includeStairsAndSlabs).itemTag(identifier, includeStairsAndSlabs);
    }

    /**
     * Applies the given consumer to this builder.
     */
    RechiseledBlockBuilder configure(Consumer<RechiseledBlockBuilder> configurer);

    /**
     * Completes this block builder and returns an {@link RechiseledBlockType} containing the constructed blocks.
     */
    RechiseledBlockType build();
}
