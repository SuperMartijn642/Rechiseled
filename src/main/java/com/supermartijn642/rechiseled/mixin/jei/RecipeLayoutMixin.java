package com.supermartijn642.rechiseled.mixin.jei;

import com.supermartijn642.rechiseled.compat.jei.ChiselingRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.gui.recipes.RecipeLayout;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created 21/01/2026 by SuperMartijn642
 */
@Pseudo
@Mixin(RecipeLayout.class)
public class RecipeLayoutMixin {

    @Final
    @Shadow
    private IRecipeCategory<?> recipeCategory;
    @Final
    @Shadow
    private IRecipeWrapper recipeWrapper;
    @Shadow
    private int posX;
    @Shadow
    private int posY;

    @Inject(
        method = "drawRecipe",
        at = @At(
            value = "INVOKE",
            target = "Lmezz/jei/api/recipe/IRecipeCategory;drawExtras(Lnet/minecraft/client/Minecraft;)V"
        ),
        remap = false
    )
    public void drawRecipe(Minecraft minecraft, int mouseX, int mouseY, CallbackInfo ci){
        if(this.recipeCategory instanceof ChiselingRecipeCategory){
            mouseX -= this.posX;
            mouseY -= this.posY;
            ((ChiselingRecipeCategory)this.recipeCategory).draw((ChiselingRecipeCategory.ChiselingRecipeWrapper)this.recipeWrapper, mouseX, mouseY);
        }
    }
}
