package com.supermartijn642.rechiseled.mixin.jei;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.compat.jei.ChiselingRecipeCategory;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
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

import java.util.List;

/**
 * Created 17/01/2026 by SuperMartijn642
 */
@Pseudo
@Mixin(RecipesGui.class)
public abstract class RecipesGuiMixin implements ContainerEventHandler {

    @Final
    @Shadow
    private List<IRecipeLayoutDrawable<?>> recipeLayouts;

    @Inject(
        method = "mouseScrolled",
        at = @At("HEAD"),
        cancellable = true
    )
    private void mouseScrolled(double mouseX, double mouseY, double horizontalScroll, double verticalScroll, CallbackInfoReturnable<Boolean> ci){
        for(IRecipeLayoutDrawable<?> layout : this.recipeLayouts){
            if(!layout.isMouseOver(mouseX, mouseY))
                continue;
            if(!(layout.getRecipeCategory() instanceof ChiselingRecipeCategory))
                continue;
            Rect2i recipeArea = layout.getRect();
            mouseX -= recipeArea.getX();
            mouseY -= recipeArea.getY();
            if(((ChiselingRecipeCategory)layout.getRecipeCategory()).mouseScrolled((ChiselingRecipe)layout.getRecipe(), mouseX, mouseY, horizontalScroll, verticalScroll))
                ci.setReturnValue(true);
            break;
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button){
        for(IRecipeLayoutDrawable<?> layout : this.recipeLayouts){
            if(!(layout.getRecipeCategory() instanceof ChiselingRecipeCategory))
                continue;
            Rect2i recipeArea = layout.getRect();
            ((ChiselingRecipeCategory)layout.getRecipeCategory()).mouseReleased((ChiselingRecipe)layout.getRecipe(), mouseX - recipeArea.getX(), mouseY - recipeArea.getY(), button);
        }
        return false;
    }
}
