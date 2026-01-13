package com.supermartijn642.rechiseled.api.chiseling.plugin;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipeManager;

/**
 * Base plugin class for mods to add or modify chiseling recipes and listen chiseling recipe updates.
 * <p>
 * On Forge and NeoForge, plugins will automatically be registered if annotated with {@link RechiseledChiselingRecipePlugin}.<br>
 * On Fabric, plugins should be listed as a 'rechiseled-chiseling-recipe-plugin' entry point in the <i>fabric.mod.json</i> properties.
 * <p>
 * Created 07/01/2026 by SuperMartijn642
 * @see RechiseledChiselingRecipePlugin annotation to register plugins automatically
 * @see ChiselingRecipeManager for manually registering plugins
 */
public interface ChiselingRecipePlugin {

    int DEFAULT_PLUGIN_PRIORITY = 100;

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
