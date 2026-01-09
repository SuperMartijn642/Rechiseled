package com.supermartijn642.rechiseled.api.chiseling.data;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.util.ItemWithMeta;

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
    ChiselingEntryBuilder regularItem(ChiselingBlockShape shape, ItemWithMeta item, float worth);

    /**
     * Sets the regular item for the given shape with a worth of {@code 1}.
     */
    ChiselingEntryBuilder regularItem(ChiselingBlockShape shape, ItemWithMeta item);

    /**
     * Sets the regular item for the given shape.
     * <p>
     * Typical worth of a block is {@code 1}, of a stair is {@code 1}, and of a slab is {@code 0.5f}.
     * @param worth number of items that this item is worth relative to other items in the recipe
     */
    ChiselingEntryBuilder connectingItem(ChiselingBlockShape shape, ItemWithMeta item, float worth);

    /**
     * Sets the connecting item for the given shape with a worth of {@code 1}.
     */
    ChiselingEntryBuilder connectingItem(ChiselingBlockShape shape, ItemWithMeta item);

    /**
     * Sets the regular block.
     * <p>
     * Typical worth of a block is {@code 1}.
     * @param worth number of items that this item is worth relative to other items in the recipe
     */
    ChiselingEntryBuilder regularBlock(ItemWithMeta item, float worth);

    /**
     * Sets the regular block with a worth of {@code 1}.
     */
    ChiselingEntryBuilder regularBlock(ItemWithMeta item);

    /**
     * Sets the regular stairs.
     * <p>
     * Typical worth of a stair is {@code 1}.
     * @param worth number of items that this item is worth relative to other items in the recipe
     */
    ChiselingEntryBuilder regularStairs(ItemWithMeta item, float worth);

    /**
     * Sets the regular stairs with a worth of {@code 1}.
     */
    ChiselingEntryBuilder regularStairs(ItemWithMeta item);

    /**
     * Sets the regular slab.
     * <p>
     * Typical worth of a slab is {@code 0.5f}.
     * @param worth number of items that this item is worth relative to other items in the recipe
     */
    ChiselingEntryBuilder regularSlab(ItemWithMeta item, float worth);

    /**
     * Sets the regular slab with a worth of {@code 0.5f}.
     */
    ChiselingEntryBuilder regularSlab(ItemWithMeta item);

    /**
     * Sets the connecting block.
     * <p>
     * Typical worth of a block is {@code 1}.
     * @param worth number of items that this item is worth relative to other items in the recipe
     */
    ChiselingEntryBuilder connectingBlock(ItemWithMeta item, float worth);

    /**
     * Sets the connecting block with a worth of {@code 1}.
     */
    ChiselingEntryBuilder connectingBlock(ItemWithMeta item);

    /**
     * Sets the connecting stairs.
     * <p>
     * Typical worth of a stair is {@code 1}.
     * @param worth number of items that this item is worth relative to other items in the recipe
     */
    ChiselingEntryBuilder connectingStairs(ItemWithMeta item, float worth);

    /**
     * Sets the connecting stairs with a worth of {@code 1}.
     */
    ChiselingEntryBuilder connectingStairs(ItemWithMeta item);

    /**
     * Sets the connecting slab.
     * <p>
     * Typical worth of a slab is {@code 0.5f}.
     * @param worth number of items that this item is worth relative to other items in the recipe
     */
    ChiselingEntryBuilder connectingSlab(ItemWithMeta item, float worth);

    /**
     * Sets the connecting slab with a worth of {@code 0.5f}.
     */
    ChiselingEntryBuilder connectingSlab(ItemWithMeta item);
}
