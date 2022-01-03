package com.supermartijn642.rechiseled.chiseling;

import net.minecraft.world.item.Item;

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
}
