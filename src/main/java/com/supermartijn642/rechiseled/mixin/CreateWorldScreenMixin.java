package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeManagerImpl;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created 08/01/2026 by SuperMartijn642
 */
@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {

    @Inject(
        method = "openCreateWorldScreen",
        at = @At("TAIL")
    )
    private static void openCreateWorldScreen(CallbackInfo ci){
        ChiselingRecipeManagerImpl.loadRecipes();
    }
}
