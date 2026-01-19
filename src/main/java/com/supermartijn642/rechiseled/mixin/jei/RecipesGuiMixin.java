package com.supermartijn642.rechiseled.mixin.jei;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.compat.jei.ChiselingRecipeCategory;
import com.supermartijn642.rechiseled.compat.jei.JEIFieldAccess;
import mezz.jei.gui.recipes.RecipeLayout;
import mezz.jei.gui.recipes.RecipesGui;
import net.minecraft.client.gui.IGuiEventListener;
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
public abstract class RecipesGuiMixin implements IGuiEventListener {

    @Final
    @Shadow
    private List<RecipeLayout> recipeLayouts;

    @Inject(
        method = "mouseScrolled",
        at = @At("HEAD"),
        cancellable = true
    )
    private void mouseScrolled(double mouseX, double mouseY, double verticalScroll, CallbackInfoReturnable<Boolean> ci){
        for(RecipeLayout layout : this.recipeLayouts){
            if(!layout.isMouseOver(mouseX, mouseY))
                continue;
            if(!(layout.getRecipeCategory() instanceof ChiselingRecipeCategory))
                continue;
            mouseX -= layout.getPosX();
            mouseY -= layout.getPosY();
            Object recipe = JEIFieldAccess.getRecipeLayoutRecipe(layout);
            if(((ChiselingRecipeCategory)layout.getRecipeCategory()).mouseScrolled((ChiselingRecipe)recipe, mouseX, mouseY, verticalScroll))
                ci.setReturnValue(true);
            break;
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button){
        for(RecipeLayout layout : this.recipeLayouts){
            if(!(layout.getRecipeCategory() instanceof ChiselingRecipeCategory))
                continue;
            Object recipe = JEIFieldAccess.getRecipeLayoutRecipe(layout);
            ((ChiselingRecipeCategory)layout.getRecipeCategory()).mouseReleased((ChiselingRecipe)recipe, mouseX - layout.getPosX(), mouseY - layout.getPosY(), button);
        }
        return false;
    }
}
