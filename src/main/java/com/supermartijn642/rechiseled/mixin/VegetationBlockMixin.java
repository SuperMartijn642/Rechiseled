package com.supermartijn642.rechiseled.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Created 13/01/2026 by SuperMartijn642
 */
@Mixin(VegetationBlock.class)
public class VegetationBlockMixin {

    @ModifyReturnValue(
        method = "mayPlaceOn",
        at = @At("RETURN")
    )
    private boolean mayPlaceOn(boolean allowed, BlockState state, BlockGetter level, BlockPos pos) {
        // Prevent vegetation from being placed on blocks without a solid top face, i.e. bottom dirt slab and stairs
        return allowed && state.isFaceSturdy(level, pos, Direction.UP);
    }
}
