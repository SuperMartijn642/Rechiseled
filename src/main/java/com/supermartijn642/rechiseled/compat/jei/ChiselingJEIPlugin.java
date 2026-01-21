package com.supermartijn642.rechiseled.compat.jei;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipeManager;
import com.supermartijn642.rechiseled.api.util.ItemWithMeta;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeImpl;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created 28/12/2021 by SuperMartijn642
 */
@JEIPlugin
public class ChiselingJEIPlugin implements IModPlugin {

    private static final ChiselingRecipeCategory.ChiselingRecipeWrapper DUMMY_RECIPE = new ChiselingRecipeCategory.ChiselingRecipeWrapper(new ChiselingRecipeImpl(Collections.emptyList()));

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration){
        registration.addRecipeCategories(new ChiselingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(IModRegistry registry){
        registry.addRecipeRegistryPlugin(new IRecipeRegistryPlugin() {
            @Override
            public <V> List<String> getRecipeCategoryUids(IFocus<V> focus){
                ItemWithMeta item = this.focusedItem(focus);
                return item != null && ChiselingRecipeManager.get(true).getRecipeForItem(item) != null ?
                    Collections.singletonList(ChiselingRecipeCategory.IDENTIFIER) :
                    Collections.emptyList();
            }

            @Override
            public <T extends IRecipeWrapper, V> List<T> getRecipeWrappers(IRecipeCategory<T> recipeCategory, IFocus<V> focus){
                if(!(recipeCategory instanceof ChiselingRecipeCategory))
                    return Collections.emptyList();
                ItemWithMeta item = this.focusedItem(focus);
                if(item.item() == Rechiseled.chisel)
                    return this.getRecipeWrappers(recipeCategory);
                ChiselingRecipe recipe = ChiselingRecipeManager.get(true).getRecipeForItem(item);
                //noinspection unchecked
                return recipe != null ? (List<T>)Collections.singletonList(new ChiselingRecipeCategory.ChiselingRecipeWrapper(recipe)) : Collections.emptyList();
            }

            @Override
            public <T extends IRecipeWrapper> List<T> getRecipeWrappers(IRecipeCategory<T> recipeCategory){
                if(!(recipeCategory instanceof ChiselingRecipeCategory))
                    return Collections.emptyList();
                if(ClientUtils.getMinecraft().world == null)
                    //noinspection unchecked
                    return Collections.singletonList((T)DUMMY_RECIPE);
                List<ChiselingRecipe> allRecipes = ChiselingRecipeManager.get(true).getAllRecipes();
                List<ChiselingRecipeCategory.ChiselingRecipeWrapper> wrappers = new ArrayList<>(allRecipes.size());
                for(ChiselingRecipe recipe : allRecipes)
                    wrappers.add(new ChiselingRecipeCategory.ChiselingRecipeWrapper(recipe));
                //noinspection unchecked
                return (List<T>)wrappers;
            }

            private ItemWithMeta focusedItem(IFocus<?> focus){
                Object value = focus.getValue();
                if(value instanceof Item)
                    return ItemWithMeta.of((Item)value);
                else if(value instanceof ItemStack)
                    return ItemWithMeta.fromStack((ItemStack)value);
                return null;
            }
        });
        registry.addRecipeCatalyst(new ItemStack(Rechiseled.chisel), ChiselingRecipeCategory.IDENTIFIER);
    }
}
