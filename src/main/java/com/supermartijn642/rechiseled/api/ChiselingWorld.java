package com.supermartijn642.rechiseled.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ChiselingWorld {
    public static BlockItem pickBlockItemFromWorld(){
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        double blockReach = player.getBlockReach();
        HitResult hitBlock = player.pick(blockReach, 0f, false);
        if(hitBlock.getType() == HitResult.Type.BLOCK){
            Level level = player.level();

            BlockPos blockPos = ((BlockHitResult)hitBlock).getBlockPos();
            Item worldItem = level.getBlockState(blockPos).getBlock().asItem();
            if(worldItem instanceof BlockItem blockItem) return blockItem;
        }

        return null;
    }
}
