package com.supermartijn642.rechiseled.chiseling.plugin;

import com.supermartijn642.rechiseled.api.chiseling.plugin.ChiselingRecipeMutationContext;
import com.supermartijn642.rechiseled.api.chiseling.plugin.MutableChiselingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public class ChiselingRecipeMutationContextImpl implements ChiselingRecipeMutationContext {

    private final Supplier<ResourceLocation> activePlugin;
    private final List<MutableChiselingRecipe> recipes = new ArrayList<>();
    private final Map<ResourceLocation,MutableChiselingRecipe> recipesByIdentifier = new HashMap<>();

    private boolean valid = true;

    public ChiselingRecipeMutationContextImpl(Supplier<ResourceLocation> activePlugin){
        this.activePlugin = activePlugin;
    }

    @Override
    public MutableChiselingRecipe getOrCreateRecipe(ResourceLocation identifier){
        this.checkValid();
        MutableChiselingRecipe recipe = this.recipesByIdentifier.get(identifier);
        if(recipe != null)
            return recipe;
        recipe = new MutableChiselingRecipeImpl(this.activePlugin, identifier);
        this.recipes.add(recipe);
        this.recipesByIdentifier.put(identifier, recipe);
        return recipe;
    }

    @Override
    public @Nullable MutableChiselingRecipe getRecipe(ResourceLocation identifier){
        this.checkValid();
        return this.recipesByIdentifier.get(identifier);
    }

    @Override
    public List<MutableChiselingRecipe> allRecipes(){
        this.checkValid();
        return Collections.unmodifiableList(this.recipes);
    }

    @Override
    public List<MutableChiselingRecipe> getRecipesContainingItem(ItemLike item){
        this.checkValid();
        return this.recipes.stream()
            .filter(recipe -> recipe.contains(item))
            .toList();
    }

    private void checkValid(){
        if(!this.valid)
            throw new IllegalStateException("Accessing recipe mutation context outside recipe mutation event!");
    }

    public void invalidate(){
        this.valid = false;
    }

    public List<MutableChiselingRecipe> getRecipesUnsafe(){
        return this.recipes;
    }
}
