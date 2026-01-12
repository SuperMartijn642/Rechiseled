package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeManagerImpl;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(
        method = "stopServer",
        at = @At("TAIL")
    )
    private void stopServer(CallbackInfo ci){
        ChiselingRecipeManagerImpl.get(false).clearRecipes();
    }
}
