package com.supermartijn642.rechiseled.packet;

import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.rechiseled.screen.BaseChiselingContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * Created 26/12/2021 by SuperMartijn642
 */
public class PacketChiselAll implements BasePacket {

    @Override
    public void write(FriendlyByteBuf buffer){
    }

    @Override
    public void read(FriendlyByteBuf buffer){
    }

    @Override
    public void handle(PacketContext context){
        AbstractContainerMenu container = context.getSendingPlayer().containerMenu;
        if(container instanceof BaseChiselingContainer)
            ((BaseChiselingContainer)container).chiselAll();
    }
}
