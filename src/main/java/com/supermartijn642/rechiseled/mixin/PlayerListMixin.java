package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipeManager;
import com.supermartijn642.rechiseled.chiseling.PacketUpdateChiselingRecipes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.management.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Inject(
        method = "initializeConnectionToPlayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/play/server/SPacketHeldItemChange;<init>(I)V",
            shift = At.Shift.BEFORE
        )
    )
    private void initializeConnectionToPlayer(NetworkManager networkManager, EntityPlayerMP player, NetHandlerPlayServer netHandlerPlayServer, CallbackInfo ci){
        Rechiseled.CHANNEL.sendToPlayer(player, new PacketUpdateChiselingRecipes(ChiselingRecipeManager.get(false).getAllRecipes()));
    }
}
