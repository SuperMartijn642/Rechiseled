package com.supermartijn642.rechiseled.mixin.jei;

import com.supermartijn642.rechiseled.compat.jei.JEIOutOfBoundsInputListener;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.gui.inputs.IJeiInputHandler;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * Created 12/02/2026 by SuperMartijn642
 */
@Pseudo
@Mixin(targets = "mezz.jei.gui.recipes.RecipeLayoutWithButtons$RecipeLayoutUserInputHandler")
public class RecipeLayoutUserInputHandlerMixin {

    @Final
    @Shadow
    private IRecipeLayoutDrawable<?> recipeLayout;

    @Inject(
        method = "handleUserInput",
        at = @At("HEAD")
    )
    private void handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings, CallbackInfoReturnable<Optional<IUserInputHandler>> ci){
        IJeiInputHandler inputHandler = this.recipeLayout.getInputHandler();
        if(!(inputHandler instanceof JEIOutOfBoundsInputListener))
            return;

        final double mouseX = input.getMouseX();
        final double mouseY = input.getMouseY();
        if(this.recipeLayout.isMouseOver(mouseX, mouseY))
            return;

        ((JEIOutOfBoundsInputListener)inputHandler).rechiseledOutOfBoundsInput(mouseX, mouseY, input);
    }
}
