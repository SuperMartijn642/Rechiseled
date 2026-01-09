package com.supermartijn642.rechiseled.chiseling;

import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;
import com.supermartijn642.rechiseled.api.util.ItemWithMeta;

/**
 * Created 09/01/2026 by SuperMartijn642
 */
public class ItemWithWorthImpl implements ItemWithWorth {

    public static ItemWithWorth of(ItemWithMeta item, float worth){
        if(item == null)
            throw new NullPointerException("Item cannot be null!");
        if(worth <= 0)
            throw new IllegalArgumentException("Worth must be positive!");
        return new ItemWithWorthImpl(item, worth);
    }

    public static ItemWithWorth defaultWorth(ItemWithMeta item){
        return of(item, 1);
    }

    private final ItemWithMeta item;
    private final float worth;

    private ItemWithWorthImpl(ItemWithMeta item, float worth){
        this.item = item;
        this.worth = worth;
    }

    @Override
    public ItemWithMeta item(){
        return this.item;
    }

    @Override
    public float worth(){
        return this.worth;
    }
}
