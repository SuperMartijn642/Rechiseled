package com.supermartijn642.rechiseled.chiseling;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created 24/12/2021 by SuperMartijn642
 */
public class ChiselingEntry {

    private final Item regularItem;
    private final int regularItemData;
    private final Item connectingItem;
    private final int connectingItemData;

    public ChiselingEntry(Item regularItem, int regularItemData, Item connectingItem, int connectingItemData){
        this.regularItem = regularItem;
        this.regularItemData = regularItemData;
        this.connectingItem = connectingItem;
        this.connectingItemData = connectingItemData;
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

    public int getRegularItemData(){
        return this.regularItemData;
    }

    public int getConnectingItemData(){
        return this.connectingItemData;
    }

    public ItemStack getRegularItemStack(){
        return new ItemStack(this.regularItem, 1, this.regularItemData);
    }

    public ItemStack getConnectingItemStack(){
        return new ItemStack(this.connectingItem, 1, this.connectingItemData);
    }
}
