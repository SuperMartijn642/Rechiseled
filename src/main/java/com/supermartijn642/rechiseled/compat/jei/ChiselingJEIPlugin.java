package com.supermartijn642.rechiseled.compat.jei;

import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

/**
 * Created 28/12/2021 by SuperMartijn642
 */
@JEIPlugin
public class ChiselingJEIPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration){
        registration.addRecipeCategories(new ChiselingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(IModRegistry registry){
        registry.addRecipes(ChiselingRecipes.getAllRecipes(), "rechiseled:chiseling");
        registry.handleRecipes(ChiselingRecipe.class, ChiselingRecipeCategory.ChiselingRecipeWrapper::new, "rechiseled:chiseling");
        registry.addRecipeCatalyst(new ItemStack(Rechiseled.chisel), "rechiseled:chiseling");
    }
}
