package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created 12/01/2026 by SuperMartijn642
 */
@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin {

    @Shadow
    private int blockHitDelay;

    @Inject(
        method = "onPlayerDamageBlock",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelChiselLeftClickBlock(BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> cir){
        if(this.blockHitDelay > 0)
            return;
        if(ClientUtils.getPlayer().getHeldItemMainhand().getItem() == Rechiseled.chisel)
            cir.setReturnValue(false);
    }
}
