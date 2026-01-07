package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipeManager;
import com.supermartijn642.rechiseled.chiseling.PacketUpdateChiselingRecipes;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
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
            target = "Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;<init>(Ljava/util/Map;Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;)V",
            shift = At.Shift.BEFORE
        )
    )
    public void placeNewPlayer(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        Rechiseled.CHANNEL.sendToPlayer(player, new PacketUpdateChiselingRecipes(ChiselingRecipeManager.get(false).getAllRecipes()));
    }

    @Inject(
        method = "reloadResources",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/common/ClientboundUpdateTagsPacket;<init>(Ljava/util/Map;)V",
            shift = At.Shift.BEFORE
        )
    )
    public void reloadResources(CallbackInfo ci) {
        Rechiseled.CHANNEL.sendToAllPlayers(new PacketUpdateChiselingRecipes(ChiselingRecipeManager.get(false).getAllRecipes()));
    }
}
