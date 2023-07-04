package com.supermartijn642.rechiseled.chiseling;

import net.minecraft.world.item.Item;

import java.util.Objects;

/**
 * Created 24/12/2021 by SuperMartijn642
 */
public class ChiselingEntry {

    private final Item regularItem;
    private final Item connectingItem;

    public ChiselingEntry(Item regularItem, Item connectingItem){
        this.regularItem = regularItem;
        this.connectingItem = connectingItem;
    }

    public boolean hasRegularItem(){
        return this.regularItem != null;
    }

    public boolean hasConnectingItem(){
        return this.connectingItem != null;
    }

    public Item getRegularItem(){
        return this.regularItem;
    }

    public Item getConnectingItem(){
        return this.connectingItem;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || this.getClass() != o.getClass()) return false;

        ChiselingEntry that = (ChiselingEntry)o;

        if(!Objects.equals(this.regularItem, that.regularItem)) return false;
        return Objects.equals(this.connectingItem, that.connectingItem);
    }

    @Override
    public int hashCode(){
        int result = this.regularItem != null ? this.regularItem.hashCode() : 0;
        result = 31 * result + (this.connectingItem != null ? this.connectingItem.hashCode() : 0);
        return result;
    }
}
