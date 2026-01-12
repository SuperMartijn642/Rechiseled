package com.supermartijn642.rechiseled.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeManagerImpl;
import net.minecraft.server.ReloadableServerResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.CompletableFuture;

/**
 * Created 12/01/2026 by SuperMartijn642
 */
@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin {

    @ModifyReturnValue(
        method = "loadResources",
        at = @At("RETURN")
    )
    private static CompletableFuture<ReloadableServerResources> loadResources(CompletableFuture<ReloadableServerResources> future){
        return future.whenComplete((resources, throwable) -> {
            if(throwable != null)
                return;
            try{
                ChiselingRecipeManagerImpl.loadRecipes();
            }catch(Exception e){
                Rechiseled.LOGGER.error("Failed to load recipes!", e);
            }
        });
    }
}
