package com.supermartijn642.rechiseled.api.chiseling.plugin;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public interface ChiselingRecipePlugin {

    /**
     * Can be used to modify existing chiseling recipes or add new ones.
     * After all plugins have been called, recipes with overlapping items will be merged automatically.
     */
    default void mutateRecipes(ChiselingRecipeMutationContext context){
    }

    /**
     * Called whenever the chiseling recipes are updated.
     * Server-side, recipes are updated when data is reloaded.
     * Client-side, recipes are updated when receiving a recipes packet from the server.
     * <p>
     * Use {@link ChiselingRecipesLoadedContext#getRecipeManager()} to access the chiseling recipes.
     */
    default void onRecipesLoaded(ChiselingRecipesLoadedContext context){
    }
}
