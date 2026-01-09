package com.supermartijn642.rechiseled.api.chiseling.plugin;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public interface MutableChiselingRecipe extends ChiselingRecipe {

    @Nullable ResourceLocation identifier();

    /**
     * Removes all entries from this recipe.
     */
    void clear();

    /**
     * Provides a builder for a new entry. {@link EntryBuilder#submit()} must be called for the entry to be added to the recipe.
     */
    EntryBuilder newEntry();

    /**
     * Provides an iterator that allows chiseling entries to be removed.
     */
    Iterator<ChiselingEntry> iterator();

    interface EntryBuilder {

        /**
         * Sets the regular item for the given shape.
         * <p>
         * Typical worth of a block is {@code 1}, of a stair is {@code 1}, and of a slab is {@code 0.5f}.
         * @param worth number of items that this item is worth relative to other items in the recipe.
         */
        EntryBuilder regularItem(ChiselingBlockShape shape, ItemLike item, float worth);

        /**
         * Sets the connecting item for the given shape.
         * <p>
         * Typical worth of a block is {@code 1}, of a stair is {@code 1}, and of a slab is {@code 0.5f}.
         * @param worth number of items that this item is worth relative to other items in the recipe.
         */
        EntryBuilder connectingItem(ChiselingBlockShape shape, ItemLike item, float worth);

        /**
         * Sets the given item with a worth of {@code 1}.
         */
        EntryBuilder regularItem(ChiselingBlockShape shape, ItemLike item);

        /**
         * Sets the given item with a worth of {@code 1}.
         */
        EntryBuilder connectingItem(ChiselingBlockShape shape, ItemLike item);

        void submit();
    }
}
