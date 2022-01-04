package com.supermartijn642.rechiseled.data;

import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.function.Consumer;

/**
 * Created 26/12/2021 by SuperMartijn642
 */
public class RechiseledRecipeProvider extends RecipeProvider {

    public RechiseledRecipeProvider(GatherDataEvent e){
        super(e.getGenerator());
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> recipeConsumer){
        ShapedRecipeBuilder.shaped(Rechiseled.chisel)
            .pattern(" A")
            .pattern("B ")
            .define('A', Tags.Items.INGOTS_IRON)
            .define('B', Tags.Items.RODS_WOODEN)
            .unlocks("has_iron", this.has(Tags.Items.INGOTS_IRON))
            .save(recipeConsumer);
    }
}
