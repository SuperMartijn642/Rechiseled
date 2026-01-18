package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created 12/01/2026 by SuperMartijn642
 */
@Mixin(PlayerController.class)
public class PlayerControllerMixin {

    @Shadow
    private int destroyDelay;

    @Inject(
        method = "continueDestroyBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Direction;)Z",
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
