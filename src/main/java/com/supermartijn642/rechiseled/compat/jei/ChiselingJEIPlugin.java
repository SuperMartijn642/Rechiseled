package com.supermartijn642.rechiseled.compat.jei;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipeManager;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created 28/12/2021 by SuperMartijn642
 */
@JeiPlugin
public class ChiselingJEIPlugin implements IModPlugin {

    private static final boolean isREIPresent = CommonUtils.isModLoaded("roughlyenoughitems");

    @Override
    public ResourceLocation getPluginUid(){
        return Rechiseled.identifier("chiseling_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration){
        if(!isREIPresent)
            registration.addRecipeCategories(new ChiselingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration){
        if(!isREIPresent)
            registration.addRecipes(ChiselingRecipeManager.get(true).getAllRecipes(), Rechiseled.identifier("chiseling"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration){
        if(!isREIPresent)
            registration.addRecipeCatalyst(new ItemStack(Rechiseled.chisel), Rechiseled.identifier("chiseling"));
    }
}
