package com.supermartijn642.rechiseled.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created 13/01/2026 by SuperMartijn642
 */
@Mixin(BushBlock.class)
public class BushBlockMixin {

    @Inject(
        method = "mayPlaceOn",
        at = @At("RETURN"),
        cancellable = true
    )
    private void mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> ci){
        // Prevent vegetation from being placed on blocks without a solid top face, i.e. bottom dirt slab and stairs
        if(ci.getReturnValue() && !state.isFaceSturdy(level, pos, Direction.UP))
            ci.setReturnValue(false);
    }
}
