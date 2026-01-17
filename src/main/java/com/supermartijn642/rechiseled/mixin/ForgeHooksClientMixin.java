package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeManagerImpl;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created 18/01/2026 by SuperMartijn642
 */
@Mixin(ForgeHooksClient.class)
public class ForgeHooksClientMixin {

    @Inject(
        method = "initClientHooks",
        at = @At("TAIL"),
        remap = false
    )
    private static void initClientHooks(CallbackInfo ci){
        ChiselingRecipeManagerImpl.finalizePlugins();
    }
}
