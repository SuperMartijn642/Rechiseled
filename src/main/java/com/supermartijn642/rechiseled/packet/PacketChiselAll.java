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

    private boolean includeAllShapes;

    public PacketChiselAll(boolean includeAllShapes){
        this.includeAllShapes = includeAllShapes;
    }

    public PacketChiselAll(){
    }

    @Override
    public void write(FriendlyByteBuf buffer){
        buffer.writeBoolean(this.includeAllShapes);
    }

    @Override
    public void read(FriendlyByteBuf buffer){
        this.includeAllShapes = buffer.readBoolean();
    }

    @Override
    public void handle(PacketContext context){
        AbstractContainerMenu container = context.getPlayer().containerMenu;
        if(container instanceof BaseChiselingContainer)
            ((BaseChiselingContainer)container).chiselAll(this.includeAllShapes);
    }
}
