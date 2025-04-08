package com.supermartijn642.rechiseled.mixin.dev;

import com.llamalad7.mixinextras.sugar.Local;
import com.supermartijn642.rechiseled.texture.TextureMappingTool;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created 08/04/2025 by SuperMartijn642
 */
@Mixin(DatagenModLoader.class)
public class DatagenModLoaderMixin {

    @Inject(
        method = "begin",
        at = @At(
            value = "INVOKE",
            target = "Lnet/neoforged/fml/ModContainer;acceptEvent(Lnet/neoforged/bus/api/Event;)V",
            shift = At.Shift.BEFORE
        )
    )
    private static void begin(CallbackInfo ci, @Local GatherDataEvent event){
        TextureMappingTool.RESOURCE_MANAGER = event.getResourceManager(PackType.CLIENT_RESOURCES);
    }
}
