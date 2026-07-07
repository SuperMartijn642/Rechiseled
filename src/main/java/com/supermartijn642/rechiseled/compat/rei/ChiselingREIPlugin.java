package com.supermartijn642.rechiseled.compat.rei;

import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipeManager;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;

/**
 * Created 29/07/2025 by SuperMartijn642
 */
@REIPluginClient
public class ChiselingREIPlugin implements REIClientPlugin {

    public static final CategoryIdentifier<ChiselingRecipeDisplay> CHISELING_CATEGORY = CategoryIdentifier.of(Rechiseled.MODID, "chiseling");

    @Override
    public void registerCategories(CategoryRegistry registry){
        registry.add(new ChiselingDisplayCategory());
        registry.addWorkstations(CHISELING_CATEGORY, EntryStacks.of(Rechiseled.chisel));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry){
        for(ChiselingRecipe recipe : ChiselingRecipeManager.get(true).getAllRecipes())
            registry.add(new ChiselingRecipeDisplay(recipe));
    }
}
