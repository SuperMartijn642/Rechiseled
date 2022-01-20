package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeLoader;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.management.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created 20/01/2022 by SuperMartijn642
 * <p>
 * This class injects a function call into {@link PlayerList#reloadResources()}
 * and into {@link PlayerList#placeNewPlayer(NetworkManager, ServerPlayerEntity)}
 * in order to sync custom data for the chiseling recipes.
 */
@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Inject(method = "reloadResources", at = @At("HEAD"))
    public void reloadResources(CallbackInfo ci){
        ChiselingRecipeLoader.onDataPackSync(null);
    }

    @Inject(
        method = "placeNewPlayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;getRecipeManager()Lnet/minecraft/item/crafting/RecipeManager;"
        )
    )
    public void placeNewPlayer(NetworkManager networkManager, ServerPlayerEntity player, CallbackInfo ci){
        ChiselingRecipeLoader.onDataPackSync(player);
    }
}
