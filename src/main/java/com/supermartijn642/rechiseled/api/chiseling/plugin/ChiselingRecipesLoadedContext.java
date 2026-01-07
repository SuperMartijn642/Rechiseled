package com.supermartijn642.rechiseled.api.chiseling.plugin;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipeManager;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public interface ChiselingRecipesLoadedContext {

    /**
     * Whether this is being called on the logical client.
     */
    boolean isClient();

    /**
     * Whether this is being called on the logical server.
     */
    boolean isServer();

    ChiselingRecipeManager getRecipeManager();
}
