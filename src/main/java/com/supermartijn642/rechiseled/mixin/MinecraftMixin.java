package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeManagerImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Unique
    private boolean firstSetOverlay = true;

    @Inject(
        method = "clearLevel(Lnet/minecraft/client/gui/screen/Screen;)V",
        at = @At("TAIL")
    )
    private void clearLevel(Screen screen, CallbackInfo ci){
        ChiselingRecipeManagerImpl.get(true).clearRecipes();
    }

    @Inject(
        method = "setOverlay",
        at = @At("HEAD")
    )
    private void setOverlay(CallbackInfo ci){
        if(this.firstSetOverlay){
            ChiselingRecipeManagerImpl.finalizePlugins();
            this.firstSetOverlay = false;
        }
    }

    @Inject(
        method = "startAttack()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/PlayerController;startDestroyBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Direction;)Z",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private void cancelChiselLeftClickBlock(CallbackInfo ci){
        PlayerEntity player = ClientUtils.getPlayer();
        if(player.isSpectator())
            return;
        ItemStack stack = player.getMainHandItem();
        if(stack.getItem() == Rechiseled.chisel){
            BlockRayTraceResult result = (BlockRayTraceResult)ClientUtils.getMinecraft().hitResult;
            assert result != null;
            if(!ClientUtils.getWorld().getWorldBorder().isWithinBounds(result.getBlockPos()))
                return;
            boolean isShiftDown = ClientUtils.getMinecraft().options.keyShift.isDown();
            if(Rechiseled.chisel.leftClickBlock(player, stack, result.getBlockPos(), result.getDirection(), isShiftDown))
                player.swing(Hand.MAIN_HAND);
            ci.cancel();
        }
    }
}
