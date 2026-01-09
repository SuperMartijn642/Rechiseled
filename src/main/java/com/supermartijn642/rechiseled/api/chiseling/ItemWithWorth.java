package com.supermartijn642.rechiseled.api.chiseling;

import net.minecraft.world.item.Item;

/**
 * Created 09/01/2026 by SuperMartijn642
 */
public interface ItemWithWorth {

    Item item();

    /**
     * Number of items that this item is worth relative to other items in the recipe.
     * Value is always greater than 0.
     */
    float worth();
}
