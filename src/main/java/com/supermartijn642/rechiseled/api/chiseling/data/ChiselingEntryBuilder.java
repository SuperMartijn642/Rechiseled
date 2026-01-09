package com.supermartijn642.rechiseled.api.chiseling.data;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import net.minecraft.world.level.ItemLike;

/**
 * Created 10/01/2026 by SuperMartijn642
 */
public interface ChiselingEntryBuilder {
    /**
     * Sets whether the recipe may ignore the entry's items when they are not present.
     * Useful for adding compatibility with mods that may not always be present.
     */
    ChiselingEntryBuilder optional(boolean optional);

    /**
     * Sets that the recipe may ignore the entry's items when they are not present.
     * Useful for adding compatibility with mods that may not always be present.
     */
    ChiselingEntryBuilder optional();

    /**
     * Sets the regular item for the given shape.
     * <p>
     * Typical worth of a block is {@code 1}, of a stair is {@code 1}, and of a slab is {@code 0.5f}.
     * @param worth number of items that this item is worth relative to other items in the recipe
     */
    ChiselingEntryBuilder regularItem(ChiselingBlockShape shape, ItemLike item, float worth);

    /**
     * Sets the regular item for the given shape with a worth of {@code 1}.
     */
    ChiselingEntryBuilder regularItem(ChiselingBlockShape shape, ItemLike item);

    /**
     * Sets the regular item for the given shape.
     * <p>
     * Typical worth of a block is {@code 1}, of a stair is {@code 1}, and of a slab is {@code 0.5f}.
     * @param worth number of items that this item is worth relative to other items in the recipe
     */
    ChiselingEntryBuilder connectingItem(ChiselingBlockShape shape, ItemLike item, float worth);

    /**
     * Sets the connecting item for the given shape with a worth of {@code 1}.
     */
    ChiselingEntryBuilder connectingItem(ChiselingBlockShape shape, ItemLike item);

    /**
     * Sets the regular block.
     * <p>
     * Typical worth of a block is {@code 1}.
     * @param worth number of items that this item is worth relative to other items in the recipe
     */
    ChiselingEntryBuilder regularBlock(ItemLike item, float worth);

    /**
     * Sets the regular block with a worth of {@code 1}.
     */
    ChiselingEntryBuilder regularBlock(ItemLike item);

    /**
     * Sets the regular stairs.
     * <p>
     * Typical worth of a stair is {@code 1}.
     * @param worth number of items that this item is worth relative to other items in the recipe
     */
    ChiselingEntryBuilder regularStairs(ItemLike item, float worth);

    /**
     * Sets the regular stairs with a worth of {@code 1}.
     */
    ChiselingEntryBuilder regularStairs(ItemLike item);

    /**
     * Sets the regular slab.
     * <p>
     * Typical worth of a slab is {@code 0.5f}.
     * @param worth number of items that this item is worth relative to other items in the recipe
     */
    ChiselingEntryBuilder regularSlab(ItemLike item, float worth);

    /**
     * Sets the regular slab with a worth of {@code 0.5f}.
     */
    ChiselingEntryBuilder regularSlab(ItemLike item);

    /**
     * Sets the connecting block.
     * <p>
     * Typical worth of a block is {@code 1}.
     * @param worth number of items that this item is worth relative to other items in the recipe
     */
    ChiselingEntryBuilder connectingBlock(ItemLike item, float worth);

    /**
     * Sets the connecting block with a worth of {@code 1}.
     */
    ChiselingEntryBuilder connectingBlock(ItemLike item);

    /**
     * Sets the connecting stairs.
     * <p>
     * Typical worth of a stair is {@code 1}.
     * @param worth number of items that this item is worth relative to other items in the recipe
     */
    ChiselingEntryBuilder connectingStairs(ItemLike item, float worth);

    /**
     * Sets the connecting stairs with a worth of {@code 1}.
     */
    ChiselingEntryBuilder connectingStairs(ItemLike item);

    /**
     * Sets the connecting slab.
     * <p>
     * Typical worth of a slab is {@code 0.5f}.
     * @param worth number of items that this item is worth relative to other items in the recipe
     */
    ChiselingEntryBuilder connectingSlab(ItemLike item, float worth);

    /**
     * Sets the connecting slab with a worth of {@code 0.5f}.
     */
    ChiselingEntryBuilder connectingSlab(ItemLike item);
}
