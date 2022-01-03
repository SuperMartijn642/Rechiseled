package com.supermartijn642.rechiseled.packet;

import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.rechiseled.screen.BaseChiselingContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class PacketSelectEntry implements BasePacket {

    private int index;

    public PacketSelectEntry(int index){
        this.index = index;
    }

    public PacketSelectEntry(){
    }

    @Override
    public void write(FriendlyByteBuf buffer){
        buffer.writeInt(this.index);
    }

    @Override
    public void read(FriendlyByteBuf buffer){
        this.index = buffer.readInt();
    }

    @Override
    public boolean verify(PacketContext context){
        return this.index >= 0;
    }

    @Override
    public void handle(PacketContext context){
        AbstractContainerMenu container = context.getSendingPlayer().containerMenu;
        if(container instanceof BaseChiselingContainer)
            ((BaseChiselingContainer)container).setCurrentEntry(this.index);
    }
}
