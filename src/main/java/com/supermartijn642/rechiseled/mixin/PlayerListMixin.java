package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipeManager;
import com.supermartijn642.rechiseled.chiseling.PacketUpdateChiselingRecipes;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
@Mixin(value = PlayerList.class, priority = 900) // This should have lower priority value than Fabric API's mixins
public class PlayerListMixin {

    @Inject(
        method = "placeNewPlayer",
        at = @At(
            value = "NEW",
            target = "net/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket",
            shift = At.Shift.BEFORE
        )
    )
    public void placeNewPlayer(Connection connection, ServerPlayer player, CallbackInfo ci){
        Rechiseled.CHANNEL.sendToPlayer(player, new PacketUpdateChiselingRecipes(ChiselingRecipeManager.get(false).getAllRecipes()));
    }

    @Inject(
        method = "reloadResources",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/game/ClientboundUpdateTagsPacket;<init>(Ljava/util/Map;)V",
            shift = At.Shift.BEFORE
        )
    )
    public void reloadResources(CallbackInfo ci){
        Rechiseled.CHANNEL.sendToAllPlayers(new PacketUpdateChiselingRecipes(ChiselingRecipeManager.get(false).getAllRecipes()));
    }
}
