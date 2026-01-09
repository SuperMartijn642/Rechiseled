package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.util.ItemWithMeta;

public class DisplayEntry {

    /**
     * Index of the entry in the chiseling recipe
     */
    private final int entryIndex;
    private final ChiselingEntry entry;
    private final ChiselingBlockShape shape;

    public DisplayEntry(int entryIndex, ChiselingEntry entry, ChiselingBlockShape shape){
        this.entryIndex = entryIndex;
        this.entry = entry;
        this.shape = shape;
    }

    public int entryIndex(){
        return this.entryIndex;
    }

    public ChiselingEntry entry(){
        return this.entry;
    }

    public ChiselingBlockShape shape(){
        return this.shape;
    }

    public ItemWithMeta getItem(boolean connecting){
        return (connecting && this.entry.hasConnectingItem(this.shape)) || !this.entry.hasRegularItem(this.shape) ?
            this.entry.getConnectingItem(this.shape) : this.entry.getRegularItem(this.shape);
    }

    public boolean hasItem(boolean connecting){
        return connecting ? this.entry.hasConnectingItem(this.shape) : this.entry.hasRegularItem(this.shape);
    }
}
