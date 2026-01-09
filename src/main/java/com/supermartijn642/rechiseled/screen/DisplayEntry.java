package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;

/**
 * @param entryIndex index of the entry in the chiseling recipe
 */
public record DisplayEntry(int entryIndex, ChiselingEntry entry, ChiselingBlockShape shape) {

    public ItemWithWorth getItem(boolean connecting){
        return (connecting && this.entry.hasConnectingItem(this.shape)) || !this.entry.hasRegularItem(this.shape) ?
            this.entry.getConnectingItem(this.shape) : this.entry.getRegularItem(this.shape);
    }

    public boolean hasItem(boolean connecting){
        return connecting ? this.entry.hasConnectingItem(this.shape) : this.entry.hasRegularItem(this.shape);
    }
}
