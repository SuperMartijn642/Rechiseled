package com.supermartijn642.rechiseled.mixin.jei;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.compat.jei.ChiselingRecipeCategory;
import mezz.jei.common.gui.recipes.RecipesGui;
import mezz.jei.common.gui.recipes.layout.RecipeLayout;
import net.minecraft.client.gui.screens.Screen;
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
public abstract class RecipesGuiMixin extends Screen {

    @Final
    @Shadow
    private List<RecipeLayout<?>> recipeLayouts;

    private RecipesGuiMixin(){
        super(null);
    }

    @Inject(
        method = "mouseScrolled",
        at = @At("HEAD"),
        cancellable = true
    )
    private void mouseScrolled(double mouseX, double mouseY, double verticalScroll, CallbackInfoReturnable<Boolean> ci){
        for(RecipeLayout<?> layout : this.recipeLayouts){
            if(!layout.isMouseOver(mouseX, mouseY))
                continue;
            if(!(layout.getRecipeCategory() instanceof ChiselingRecipeCategory))
                continue;
            mouseX -= layout.getPosX();
            mouseY -= layout.getPosY();
            if(((ChiselingRecipeCategory)layout.getRecipeCategory()).mouseScrolled((ChiselingRecipe)layout.getRecipe(), mouseX, mouseY, verticalScroll))
                ci.setReturnValue(true);
            break;
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button){
        for(RecipeLayout<?> layout : this.recipeLayouts){
            if(!(layout.getRecipeCategory() instanceof ChiselingRecipeCategory))
                continue;
            ((ChiselingRecipeCategory)layout.getRecipeCategory()).mouseReleased((ChiselingRecipe)layout.getRecipe(), mouseX - layout.getPosX(), mouseY - layout.getPosY(), button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
