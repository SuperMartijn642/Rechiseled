package com.supermartijn642.rechiseled.api.chiseling;

import com.supermartijn642.rechiseled.api.util.ItemWithMeta;

import java.util.List;

/**
 * A chiseling recipe is a collection of {@link ChiselingEntry}s.
 * <p>
 * Created 07/01/2026 by SuperMartijn642
 */
public interface ChiselingRecipe {

    List<ChiselingEntry> entries();

    /**
     * Whether any entry in this recipe contains the given item.
     */
    boolean contains(ItemWithMeta item);

    /**
     * Number of items that this item is worth relative to other items in this recipe.
     * Value is always greater than 0 if the recipe contains the item, otherwise it is {@code -1}.
     */
    float getWorth(ItemWithMeta item);
}
