package com.supermartijn642.rechiseled.data;

import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraftforge.common.Tags;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.function.Consumer;

/**
 * Created 26/12/2021 by SuperMartijn642
 */
public class RechiseledRecipeProvider extends RecipeProvider {

    public RechiseledRecipeProvider(GatherDataEvent e){
        super(e.getGenerator());
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> recipeConsumer){
        ShapedRecipeBuilder.shaped(Rechiseled.chisel)
            .pattern(" A")
            .pattern("B ")
            .define('A', Tags.Items.INGOTS_IRON)
            .define('B', Tags.Items.RODS_WOODEN)
            .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
            .save(recipeConsumer);
    }
}
