package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeManagerImpl;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
@Mixin(DedicatedServer.class)
public class DedicatedServerMixin {

    @Inject(
        method = "initServer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/dedicated/DedicatedServer;loadLevel(Ljava/lang/String;Ljava/lang/String;JLnet/minecraft/world/WorldType;Lcom/google/gson/JsonElement;)V"
        )
    )
    private static void finalizePlugins(CallbackInfo ci){
        ChiselingRecipeManagerImpl.finalizePlugins();
    }
}
