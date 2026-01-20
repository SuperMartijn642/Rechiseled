package com.supermartijn642.rechiseled.api.chiseling.plugin;

import com.supermartijn642.rechiseled.api.util.ItemWithMeta;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public interface ChiselingRecipeMutationContext {

    /**
     * Retrieves the recipe with the given identifier. If there is no recipe for the given identifier, a new recipe is created.
     * Entries can be added to or removed from the returned instance.
     */
    MutableChiselingRecipe getOrCreateRecipe(ResourceLocation identifier);

    /**
     * Retrieves the recipe with the given identifier.
     * Entries can be added to or removed from the returned instance.
     */
    @Nullable
    MutableChiselingRecipe getRecipe(ResourceLocation identifier);

    List<MutableChiselingRecipe> allRecipes();

    List<MutableChiselingRecipe> getRecipesContainingItem(ItemWithMeta item);
}
