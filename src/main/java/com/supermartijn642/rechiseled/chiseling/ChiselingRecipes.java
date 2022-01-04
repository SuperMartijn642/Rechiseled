package com.supermartijn642.rechiseled.chiseling;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselingRecipes {

    private static final Method byType = ObfuscationReflectionHelper.findMethod(RecipeManager.class, "func_215366_a", IRecipeType.class);

    static final IRecipeType<ChiselingRecipe> CHISELING = IRecipeType.register("chiseling");

    public static ChiselingRecipe getRecipe(RecipeManager recipeManager, ItemStack stack){
        Collection<ChiselingRecipe> recipes = getAllRecipes(recipeManager);
        for(ChiselingRecipe recipe : recipes){
            if(recipe.contains(stack))
                return recipe;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Collection<ChiselingRecipe> getAllRecipes(RecipeManager recipeManager){
        try{
            byType.setAccessible(true);
            return ((Map<ResourceLocation,ChiselingRecipe>)byType.invoke(recipeManager, CHISELING)).values();
        }catch(Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
