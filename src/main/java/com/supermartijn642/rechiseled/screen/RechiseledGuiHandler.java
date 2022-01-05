package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * Created 04/01/2022 by SuperMartijn642
 */
public class RechiseledGuiHandler implements IGuiHandler {

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        return new ChiselContainer(player, ID == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        return new BaseChiselingContainerScreen<>(new ChiselContainer(player, ID == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND), TextComponents.item(Rechiseled.chisel).get());
    }
}
