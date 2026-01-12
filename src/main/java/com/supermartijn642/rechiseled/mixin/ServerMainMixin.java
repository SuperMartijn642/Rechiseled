package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeManagerImpl;
import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
@Mixin(Main.class)
public class ServerMainMixin {

    @Inject(
        method = "main",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/LevelStorageSource;createDefault(Ljava/nio/file/Path;)Lnet/minecraft/world/level/storage/LevelStorageSource;"
        )
    )
    private static void finalizePlugins(CallbackInfo ci){
        ChiselingRecipeManagerImpl.finalizePlugins();
    }
}
