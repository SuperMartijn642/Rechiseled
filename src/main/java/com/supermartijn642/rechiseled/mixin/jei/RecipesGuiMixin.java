package com.supermartijn642.rechiseled.mixin.jei;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.compat.jei.ChiselingRecipeCategory;
import com.supermartijn642.rechiseled.compat.jei.JEIFieldAccess;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.gui.recipes.RecipeGuiLayouts;
import mezz.jei.gui.recipes.RecipeLayoutWithButtons;
import mezz.jei.gui.recipes.RecipesGui;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.renderer.Rect2i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created 17/01/2026 by SuperMartijn642
 */
@Pseudo
@Mixin(RecipesGui.class)
public abstract class RecipesGuiMixin implements ContainerEventHandler {

    @Final
    @Shadow
    private RecipeGuiLayouts layouts;

    @Inject(
        method = "mouseScrolled",
        at = @At("HEAD"),
        cancellable = true
    )
    private void mouseScrolled(double mouseX, double mouseY, double verticalScroll, CallbackInfoReturnable<Boolean> ci){
        for(RecipeLayoutWithButtons<?> layoutWithButtons : JEIFieldAccess.getRecipeGuiGuiLayoutsRecipeLayoutsWithButtons(this.layouts)){
            IRecipeLayoutDrawable<?> layout = layoutWithButtons.recipeLayout();
            if(!layout.isMouseOver(mouseX, mouseY))
                continue;
            if(!(layout.getRecipeCategory() instanceof ChiselingRecipeCategory))
                continue;
            Rect2i recipeArea = layout.getRect();
            mouseX -= recipeArea.getX();
            mouseY -= recipeArea.getY();
            if(((ChiselingRecipeCategory)layout.getRecipeCategory()).mouseScrolled((ChiselingRecipe)layout.getRecipe(), mouseX, mouseY, verticalScroll))
                ci.setReturnValue(true);
            break;
        }
    }

    @Inject(
        method = "mouseClicked",
        at = @At("HEAD"),
        cancellable = true
    )
    private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> ci){
        for(RecipeLayoutWithButtons<?> layoutWithButtons : JEIFieldAccess.getRecipeGuiGuiLayoutsRecipeLayoutsWithButtons(this.layouts)){
            IRecipeLayoutDrawable<?> layout = layoutWithButtons.recipeLayout();
            if(!(layout.getRecipeCategory() instanceof ChiselingRecipeCategory))
                continue;
            Rect2i recipeArea = layout.getRect();
            if(((ChiselingRecipeCategory)layout.getRecipeCategory()).mousePressed((ChiselingRecipe)layout.getRecipe(), mouseX - recipeArea.getX(), mouseY - recipeArea.getY(), button))
                ci.setReturnValue(true);
        }
    }

    @Inject(
        method = "mouseReleased",
        at = @At("HEAD")
    )
    private void mouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> ci){
        for(RecipeLayoutWithButtons<?> layoutWithButtons : JEIFieldAccess.getRecipeGuiGuiLayoutsRecipeLayoutsWithButtons(this.layouts)){
            IRecipeLayoutDrawable<?> layout = layoutWithButtons.recipeLayout();
            if(!(layout.getRecipeCategory() instanceof ChiselingRecipeCategory))
                continue;
            Rect2i recipeArea = layout.getRect();
            ((ChiselingRecipeCategory)layout.getRecipeCategory()).mouseReleased((ChiselingRecipe)layout.getRecipe(), mouseX - recipeArea.getX(), mouseY - recipeArea.getY(), button);
        }
    }
}
