package com.supermartijn642.rechiseled.compat.jei;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 28/12/2021 by SuperMartijn642
 */
public class ChiselingRecipeCategory implements IRecipeCategory<ChiselingRecipe> {

    private final IDrawable backgrounds;
    private final IDrawable icon;

    public ChiselingRecipeCategory(IGuiHelper guiHelper){
        this.backgrounds = guiHelper.createDrawable(new ResourceLocation("rechiseled", "textures/screen/jei_category_background.png"), 0, 0, 174, 72);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Rechiseled.chisel));
    }

    @Override
    public RecipeType<ChiselingRecipe> getRecipeType(){
        return ChiselingJEIPlugin.CHISELING_RECIPE_TYPE;
    }

    @Override
    public Component getTitle(){
        return TextComponents.translation("rechiseled.jei_category.title").get();
    }

    @Override
    public IDrawable getBackground(){
        return this.backgrounds;
    }

    @Override
    public IDrawable getIcon(){
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, ChiselingRecipe recipe, IFocusGroup focusGroup){
        List<ItemStack> inputs = new ArrayList<>();
        List<List<ItemStack>> outputs = new ArrayList<>();

        for(ChiselingEntry entry : recipe.getEntries()){
            List<ItemStack> output = new ArrayList<>();
            if(entry.hasRegularItem()){
                inputs.add(new ItemStack(entry.getRegularItem()));
                output.add(new ItemStack(entry.getRegularItem()));
            }
            if(entry.hasConnectingItem()){
                inputs.add(new ItemStack(entry.getConnectingItem()));
                output.add(new ItemStack(entry.getConnectingItem()));
            }
            outputs.add(output);
        }

        // Input slot
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 1, 28).addItemStacks(inputs);

        // Output slots
        for(int i = 0; i < outputs.size(); i++){
            int x = 49 + 18 * (i % 7);
            int y = 1 + 18 * (i / 7);
            recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStacks(outputs.get(i));
        }
    }
}
