package com.supermartijn642.rechiseled.compat.jei;

import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Created 28/12/2021 by SuperMartijn642
 */
@JeiPlugin
public class ChiselingJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid(){
        return new ResourceLocation("rechiseled", "chiseling_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration){
        registration.addRecipeCategories(new ChiselingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration){
        registration.addRecipes(ChiselingRecipes.getAllRecipes(), new ResourceLocation("rechiseled", "chiseling"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration){
        registration.addRecipeCatalyst(new ItemStack(Rechiseled.chisel), new ResourceLocation("rechiseled", "chiseling"));
    }
}
