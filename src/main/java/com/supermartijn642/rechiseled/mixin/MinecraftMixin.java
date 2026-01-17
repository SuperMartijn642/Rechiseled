package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeManagerImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Unique
    private boolean firstSetOverlay = true;

    @Inject(
        method = "clearLevel(Lnet/minecraft/client/gui/screen/Screen;)V",
        at = @At("TAIL")
    )
    private void clearLevel(Screen screen, CallbackInfo ci){
        ChiselingRecipeManagerImpl.get(true).clearRecipes();
    }

    @Inject(
        method = "setOverlay",
        at = @At("HEAD")
    )
    private void setOverlay(CallbackInfo ci){
        if(this.firstSetOverlay){
            ChiselingRecipeManagerImpl.finalizePlugins();
            this.firstSetOverlay = false;
        }
    }
}
