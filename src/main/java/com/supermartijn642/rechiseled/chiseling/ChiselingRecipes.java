package com.supermartijn642.rechiseled.chiseling;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;

import java.util.Collection;
import java.util.List;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselingRecipes {

    static final IRecipeType<ChiselingRecipe> CHISELING = IRecipeType.register("chiseling");

    public static ChiselingRecipe getRecipe(RecipeManager recipeManager, ItemStack stack){
        List<ChiselingRecipe> recipes = recipeManager.getAllRecipesFor(CHISELING);
        for(ChiselingRecipe recipe : recipes){
            if(recipe.contains(stack))
                return recipe;
        }
        return null;
    }

    public static Collection<ChiselingRecipe> getAllRecipes(RecipeManager recipeManager){
        return recipeManager.getAllRecipesFor(CHISELING);
    }
}
