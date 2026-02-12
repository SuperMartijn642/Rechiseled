package com.supermartijn642.rechiseled.mixin.jei;

import com.supermartijn642.rechiseled.compat.jei.ChiselingRecipeCategory;
import com.supermartijn642.rechiseled.compat.jei.JEIFieldAccess;
import mezz.jei.gui.recipes.RecipeLayout;
import mezz.jei.gui.recipes.RecipesGui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Created 17/01/2026 by SuperMartijn642
 */
@Pseudo
@Mixin(RecipesGui.class)
public abstract class RecipesGuiMixin extends GuiScreen {

    @Final
    @Shadow
    private List<RecipeLayout> recipeLayouts;

    @Inject(
        method = "handleMouseInput",
        at = @At("HEAD"),
        cancellable = true
    )
    public void interceptMouseScroll(CallbackInfo ci){
        int scroll = Mouse.getEventDWheel() / 120;
        if(scroll == 0)
            return;
        int mouseX = (int)((double)Mouse.getEventX() * (double)this.width / (double)this.mc.displayWidth);
        int mouseY = (int)((double)this.height - (double)Mouse.getEventY() * (double)this.height / (double)this.mc.displayHeight - (double)1.0F);
        for(RecipeLayout layout : this.recipeLayouts){
            if(!layout.isMouseOver(mouseX, mouseY))
                continue;
            if(!(layout.getRecipeCategory() instanceof ChiselingRecipeCategory))
                continue;
            mouseX -= layout.getPosX();
            mouseY -= layout.getPosY();
            Object recipe = JEIFieldAccess.getRecipeLayoutRecipeWrapper(layout);
            if(((ChiselingRecipeCategory)layout.getRecipeCategory()).mouseScrolled((ChiselingRecipeCategory.ChiselingRecipeWrapper)recipe, mouseX, mouseY, scroll))
                ci.cancel();
            break;
        }
    }

    @Inject(
        method = "mouseClicked",
        at = @At("HEAD"),
        cancellable = true
    )
    private void interceptMouseClick(int mouseX, int mouseY, int button, CallbackInfo ci){
        for(RecipeLayout layout : this.recipeLayouts){
            if(!(layout.getRecipeCategory() instanceof ChiselingRecipeCategory))
                continue;
            Object recipe = JEIFieldAccess.getRecipeLayoutRecipeWrapper(layout);
            if(((ChiselingRecipeCategory)layout.getRecipeCategory()).mouseClicked((ChiselingRecipeCategory.ChiselingRecipeWrapper)recipe, mouseX - layout.getPosX(), mouseY - layout.getPosY(), button))
                ci.cancel();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button){
        for(RecipeLayout layout : this.recipeLayouts){
            if(!(layout.getRecipeCategory() instanceof ChiselingRecipeCategory))
                continue;
            Object recipe = JEIFieldAccess.getRecipeLayoutRecipeWrapper(layout);
            ((ChiselingRecipeCategory)layout.getRecipeCategory()).mouseReleased((ChiselingRecipeCategory.ChiselingRecipeWrapper)recipe, mouseX - layout.getPosX(), mouseY - layout.getPosY(), button);
        }
        super.mouseReleased(mouseX, mouseY, button);
    }
}
