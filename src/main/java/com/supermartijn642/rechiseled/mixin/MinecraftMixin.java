package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeManagerImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;setOverlay(Lnet/minecraft/client/gui/screens/Overlay;)V",
            shift = At.Shift.AFTER
        )
    )
    private void init(CallbackInfo ci){
        ChiselingRecipeManagerImpl.finalizePlugins();
    }

    @Inject(
        method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V",
        at = @At("TAIL")
    )
    private void clearClientLevel(Screen screen, CallbackInfo ci){
        ChiselingRecipeManagerImpl.get(true).clearRecipes();
    }
}
