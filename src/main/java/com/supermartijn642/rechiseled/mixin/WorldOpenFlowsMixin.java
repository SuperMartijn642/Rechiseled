package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeManagerImpl;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created 08/01/2026 by SuperMartijn642
 */
@Mixin(WorldOpenFlows.class)
public class WorldOpenFlowsMixin {

    @Inject(
        method = "loadWorldDataBlocking",
        at = @At("TAIL")
    )
    private void loadWorldDataBlocking(CallbackInfoReturnable<?> ci){
        ChiselingRecipeManagerImpl.loadRecipes();
    }
}
