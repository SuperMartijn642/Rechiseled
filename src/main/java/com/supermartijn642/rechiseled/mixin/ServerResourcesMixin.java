package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeManagerImpl;
import net.minecraft.server.ServerResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

/**
 * Created 12/01/2026 by SuperMartijn642
 */
@Mixin(ServerResources.class)
public class ServerResourcesMixin {

    @Inject(
        method = "loadResources",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void loadResources(CallbackInfoReturnable<CompletableFuture<ServerResources>> ci){
        ci.setReturnValue(ci.getReturnValue().whenComplete((resources, throwable) -> {
            if(throwable != null)
                return;
            try{
                ChiselingRecipeManagerImpl.loadRecipes();
            }catch(Exception e){
                Rechiseled.LOGGER.error("Failed to load recipes!", e);
            }
        }));
    }
}
