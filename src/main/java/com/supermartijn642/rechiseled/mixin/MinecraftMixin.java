package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeManagerImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
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
        method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V",
        at = @At("TAIL")
    )
    private void loadWorld(WorldClient level, String s, CallbackInfo ci){
        if(level == null)
            ChiselingRecipeManagerImpl.get(true).clearRecipes();
    }

    @Inject(
        method = "clickMouse()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;clickBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private void cancelChiselLeftClickBlock(CallbackInfo ci){
        EntityPlayer player = ClientUtils.getPlayer();
        if(player.isSpectator())
            return;
        ItemStack stack = player.getHeldItemMainhand();
        if(stack.getItem() == Rechiseled.chisel){
            RayTraceResult result = ClientUtils.getMinecraft().objectMouseOver;
            assert result != null;
            if(!ClientUtils.getWorld().getWorldBorder().contains(result.getBlockPos()))
                return;
            boolean isShiftDown = ClientUtils.getMinecraft().gameSettings.keyBindSneak.isKeyDown();
            if(Rechiseled.chisel.leftClickBlock(player, stack, result.getBlockPos(), result.sideHit, isShiftDown))
                player.swingArm(EnumHand.MAIN_HAND);
            ci.cancel();
        }
    }
}
