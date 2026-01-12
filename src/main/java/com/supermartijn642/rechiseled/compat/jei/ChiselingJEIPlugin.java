package com.supermartijn642.rechiseled.compat.jei;

import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipeManager;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

/**
 * Created 28/12/2021 by SuperMartijn642
 */
@JeiPlugin
public class ChiselingJEIPlugin implements IModPlugin {

    public static final RecipeType<ChiselingRecipe> CHISELING_RECIPE_TYPE = RecipeType.create(Rechiseled.MODID, "chiseling", ChiselingRecipe.class);

    @Override
    public ResourceLocation getPluginUid(){
        return Rechiseled.identifier("chiseling_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration){
        registration.addRecipeCategories(new ChiselingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration){
        registration.addRecipes(CHISELING_RECIPE_TYPE, ChiselingRecipeManager.get(true).getAllRecipes());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration){
        registration.addRecipeCatalyst(Rechiseled.chisel, CHISELING_RECIPE_TYPE);
    }
}
