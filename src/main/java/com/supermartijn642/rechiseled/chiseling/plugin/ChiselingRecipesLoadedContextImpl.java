package com.supermartijn642.rechiseled.chiseling.plugin;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipeManager;
import com.supermartijn642.rechiseled.api.chiseling.plugin.ChiselingRecipesLoadedContext;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public class ChiselingRecipesLoadedContextImpl implements ChiselingRecipesLoadedContext {

    private final ChiselingRecipeManager recipeManager;
    private final boolean isClient;

    public ChiselingRecipesLoadedContextImpl(ChiselingRecipeManager recipeManager, boolean isClient){
        this.recipeManager = recipeManager;
        this.isClient = isClient;
    }

    @Override
    public boolean isClient(){
        return this.isClient;
    }

    @Override
    public boolean isServer(){
        return !this.isClient;
    }

    @Override
    public ChiselingRecipeManager getRecipeManager(){
        return this.recipeManager;
    }
}
