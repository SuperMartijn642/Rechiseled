package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipeManager;
import com.supermartijn642.rechiseled.chiseling.PacketUpdateChiselingRecipes;
import net.minecraft.entity.player.ServerPlayerEntity;
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
        method = "placeNewPlayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/play/server/SUpdateRecipesPacket;<init>(Ljava/util/Collection;)V",
            shift = At.Shift.BEFORE
        )
    )
    public void placeNewPlayer(NetworkManager connection, ServerPlayerEntity player, CallbackInfo ci){
        Rechiseled.CHANNEL.sendToPlayer(player, new PacketUpdateChiselingRecipes(ChiselingRecipeManager.get(false).getAllRecipes()));
    }

    @Inject(
        method = "reloadResources",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/play/server/STagsListPacket;<init>(Lnet/minecraft/tags/NetworkTagManager;)V",
            shift = At.Shift.BEFORE
        )
    )
    public void reloadResources(CallbackInfo ci){
        Rechiseled.CHANNEL.sendToAllPlayers(new PacketUpdateChiselingRecipes(ChiselingRecipeManager.get(false).getAllRecipes()));
    }
}
