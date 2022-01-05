package com.supermartijn642.rechiseled.chiseling;

import com.google.common.collect.Streams;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselingRecipes {

    private static Collection<ChiselingRecipe> recipes = Collections.emptyList();

    public static ChiselingRecipe getRecipe(ItemStack stack){
        Collection<ChiselingRecipe> recipes = getAllRecipes();
        for(ChiselingRecipe recipe : recipes){
            if(recipe.contains(stack))
                return recipe;
        }
        return null;
    }

    public static Collection<ChiselingRecipe> getAllRecipes(){
        return recipes;
    }

    public static void collectRecipes(){
        recipes = Streams.stream(CraftingManager.REGISTRY)
            .filter(ChiselingRecipe.class::isInstance)
            .map(ChiselingRecipe.class::cast)
            .collect(Collectors.toList());
    }
}
