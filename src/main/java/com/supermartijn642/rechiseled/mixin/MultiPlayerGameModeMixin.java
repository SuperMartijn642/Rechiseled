package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created 12/01/2026 by SuperMartijn642
 */
@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Shadow
    private int destroyDelay;

    @Inject(
        method = "continueDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelChiselLeftClickBlock(BlockPos pos, Direction side, CallbackInfoReturnable<Boolean> cir){
        if(this.destroyDelay > 0)
            return;
        if(ClientUtils.getPlayer().getMainHandItem().getItem() == Rechiseled.chisel)
            cir.setReturnValue(false);
    }
}
