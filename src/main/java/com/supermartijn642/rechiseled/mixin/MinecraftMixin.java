package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeManagerImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(
        method = "onGameLoadFinished",
        at = @At("HEAD")
    )
    private void onGameLoadFinished(CallbackInfo ci){
        ChiselingRecipeManagerImpl.finalizePlugins();
    }

    @Inject(
        method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V",
        at = @At("TAIL")
    )
    private void clearLevel(Screen screen, CallbackInfo ci){
        ChiselingRecipeManagerImpl.get(true).clearRecipes();
    }

    @Inject(
        method = "startAttack()Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;startDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private void cancelChiselLeftClickBlock(CallbackInfoReturnable<Boolean> cir){
        Player player = ClientUtils.getPlayer();
        if(player.isSpectator())
            return;
        ItemStack stack = player.getMainHandItem();
        if(stack.getItem() == Rechiseled.chisel){
            BlockHitResult result = (BlockHitResult)ClientUtils.getMinecraft().hitResult;
            assert result != null;
            if(!ClientUtils.getWorld().getWorldBorder().isWithinBounds(result.getBlockPos()))
                return;
            boolean isShiftDown = ClientUtils.getMinecraft().options.keyShift.isDown();
            if(Rechiseled.chisel.leftClickBlock(player, stack, result.getBlockPos(), result.getDirection(), isShiftDown))
                player.swing(InteractionHand.MAIN_HAND);
            cir.setReturnValue(true);
        }
    }
}
