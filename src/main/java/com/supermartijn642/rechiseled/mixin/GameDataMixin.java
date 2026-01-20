package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeManagerImpl;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.GameData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

/**
 * Created 20/01/2026 by SuperMartijn642
 */
@Mixin(GameData.class)
public class GameDataMixin {

    @Inject(
        method = "fireRegistryEvents(Ljava/util/function/Predicate;)V",
        at = @At("TAIL"),
        remap = false
    )
    private static void fireRegistryEvents(Predicate<ResourceLocation> filter, CallbackInfo ci){
        if(filter.test(GameData.RECIPES)){
            ChiselingRecipeManagerImpl.finalizePlugins();
            ChiselingRecipeManagerImpl.loadRecipes();
        }
    }
}
