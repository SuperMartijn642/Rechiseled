package com.supermartijn642.rechiseled.packet;

import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.rechiseled.screen.BaseChiselingContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;

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
    public void write(PacketBuffer buffer){
        buffer.writeBoolean(this.includeAllShapes);
    }

    @Override
    public void read(PacketBuffer buffer){
        this.includeAllShapes = buffer.readBoolean();
    }

    @Override
    public void handle(PacketContext context){
        Container container = context.getPlayer().containerMenu;
        if(container instanceof BaseChiselingContainer)
            ((BaseChiselingContainer)container).chiselAll(this.includeAllShapes);
    }
}
