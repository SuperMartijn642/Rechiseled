package com.supermartijn642.rechiseled.api.chiseling;

import net.minecraft.world.level.ItemLike;

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
    boolean contains(ItemLike item);
}
