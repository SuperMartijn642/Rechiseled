package com.supermartijn642.rechiseled.chiseling;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Collection;
import java.util.List;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselingRecipes {

    static final RecipeType<ChiselingRecipe> CHISELING = RecipeType.register("chiseling");

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
