package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeManagerImpl;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

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

    @ModifyVariable(
        method = "updateSelectedPacks",
        at = @At("STORE"),
        ordinal = 0
    )
    private CompletableFuture<Unit> updateSelectedPacks(CompletableFuture<Unit> future){
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
