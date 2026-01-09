package com.supermartijn642.rechiseled.packet;

import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.screen.BaseChiselingContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;

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
    public void write(FriendlyByteBuf buffer){
        buffer.writeInt(this.index);
        buffer.writeEnum(this.shape);
        buffer.writeBoolean(this.connecting);
    }

    @Override
    public void read(FriendlyByteBuf buffer){
        this.index = buffer.readInt();
        this.shape = buffer.readEnum(ChiselingBlockShape.class);
        this.connecting = buffer.readBoolean();
    }

    @Override
    public boolean verify(PacketContext context){
        return this.index >= 0;
    }

    @Override
    public void handle(PacketContext context){
        AbstractContainerMenu container = context.getPlayer().containerMenu;
        if(container instanceof BaseChiselingContainer)
            ((BaseChiselingContainer)container).setCurrentEntry(this.index, this.shape, this.connecting);
    }
}
