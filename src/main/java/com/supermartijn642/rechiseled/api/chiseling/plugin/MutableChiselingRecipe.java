package com.supermartijn642.rechiseled.api.chiseling.plugin;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.Iterator;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public interface MutableChiselingRecipe extends ChiselingRecipe {

    @Nullable
    ResourceLocation identifier();

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
        EntryBuilder regularItem(ChiselingBlockShape shape, ItemLike item);

        EntryBuilder connectingItem(ChiselingBlockShape shape, ItemLike item);

        void submit();
    }
}
