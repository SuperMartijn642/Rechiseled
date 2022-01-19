package com.supermartijn642.rechiseled.chiseling;

import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselingRecipes {

    private static List<ChiselingRecipe> chiselingRecipes;

    static synchronized void setRecipes(List<ChiselingRecipe> recipes){
        chiselingRecipes = Collections.unmodifiableList(recipes);
    }

    public static synchronized ChiselingRecipe getRecipe(ItemStack stack){
        for(ChiselingRecipe recipe : chiselingRecipes){
            if(recipe.contains(stack))
                return recipe;
        }
        return null;
    }

    public static List<ChiselingRecipe> getAllRecipes(){
        return chiselingRecipes;
    }
}
