package com.supermartijn642.rechiseled.mixin.jei;

import com.supermartijn642.rechiseled.compat.jei.JEIOutOfBoundsInputListener;
import mezz.jei.api.gui.inputs.IJeiInputHandler;
import mezz.jei.api.gui.inputs.IJeiUserInput;
import mezz.jei.common.util.MathUtil;
import mezz.jei.library.gui.recipes.RecipeLayout;
import mezz.jei.library.gui.recipes.RecipeLayoutInputHandler;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
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
 * Created 12/02/2026 by SuperMartijn642
 */
@Pseudo
@Mixin(RecipeLayoutInputHandler.class)
public class RecipeLayoutInputHandlerMixin implements JEIOutOfBoundsInputListener {

    @Final
    @Shadow
    private RecipeLayout<?> recipeLayout;
    @Final
    @Shadow
    private List<IJeiInputHandler> inputHandlers;

    @Override
    public void rechiseledOutOfBoundsInput(double mouseX, double mouseY, IJeiUserInput userInput){
        boolean isMouseOverLayout = this.recipeLayout.isMouseOver(mouseX, mouseY);
        Rect2i area = this.recipeLayout.getRect();
        final double recipeMouseX = mouseX - area.getX();
        final double recipeMouseY = mouseY - area.getY();

        for(IJeiInputHandler inputHandler : this.inputHandlers){
            if(!(inputHandler instanceof JEIOutOfBoundsInputListener))
                continue;
            ScreenRectangle widgetArea = inputHandler.getArea();
            if(!isMouseOverLayout || !MathUtil.contains(widgetArea, recipeMouseX, recipeMouseY)){
                ScreenPosition position = widgetArea.position();
                double relativeMouseX = recipeMouseX - position.x();
                double relativeMouseY = recipeMouseY - position.y();
                ((JEIOutOfBoundsInputListener)inputHandler).rechiseledOutOfBoundsInput(relativeMouseX, relativeMouseY, userInput);
                inputHandler.handleInput(relativeMouseX, relativeMouseY, userInput);
            }
        }
    }

    @Inject(
        method = "handleInput",
        at = @At("HEAD"),
        remap = false
    )
    private void handleInput(double mouseX, double mouseY, IJeiUserInput userInput, CallbackInfoReturnable<Boolean> ci){
        this.rechiseledOutOfBoundsInput(mouseX, mouseY, userInput);
    }
}
