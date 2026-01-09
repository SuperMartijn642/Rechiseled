package com.supermartijn642.rechiseled.packet;

import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.screen.BaseChiselingContainer;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class PacketSelectEntry implements BasePacket {

    private int index;
    private ChiselingBlockShape shape;
    private boolean connecting;

    public PacketSelectEntry(int index, ChiselingBlockShape shape, boolean connecting){
        this.index = index;
        this.shape = shape;
        this.connecting = connecting;
    }

    public PacketSelectEntry(){
    }

    @Override
    public void write(PacketBuffer buffer){
        buffer.writeInt(this.index);
        buffer.writeEnumValue(this.shape);
        buffer.writeBoolean(this.connecting);
    }

    @Override
    public void read(PacketBuffer buffer){
        this.index = buffer.readInt();
        this.shape = buffer.readEnumValue(ChiselingBlockShape.class);
        this.connecting = buffer.readBoolean();
    }

    @Override
    public boolean verify(PacketContext context){
        return this.index >= 0;
    }

    @Override
    public void handle(PacketContext context){
        Container container = context.getPlayer().openContainer;
        if(container instanceof BaseChiselingContainer)
            ((BaseChiselingContainer)container).setCurrentEntry(this.index, this.shape, this.connecting);
    }
}
