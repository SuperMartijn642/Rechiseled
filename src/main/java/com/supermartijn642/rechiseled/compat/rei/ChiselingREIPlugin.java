package com.supermartijn642.rechiseled.compat.rei;

import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipes;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;

/**
 * Created 29/07/2025 by SuperMartijn642
 */
public class ChiselingREIPlugin implements REIClientPlugin {

    public static final CategoryIdentifier<ChiselingRecipeDisplay> CHISELING_CATEGORY = CategoryIdentifier.of("rechiseled", "chiseling");

    @Override
    public void registerCategories(CategoryRegistry registry){
        registry.add(new ChiselingDisplayCategory());
        registry.addWorkstations(CHISELING_CATEGORY, EntryStacks.of(Rechiseled.chisel));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry){
        for(ChiselingRecipe recipe : ChiselingRecipes.getAllRecipes())
            registry.add(new ChiselingRecipeDisplay(recipe));
    }
}
