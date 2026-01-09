package com.supermartijn642.rechiseled.chiseling;

import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;
import net.minecraft.item.Item;

/**
 * Created 09/01/2026 by SuperMartijn642
 */
public class ItemWithWorthImpl implements ItemWithWorth {

    public static ItemWithWorth of(Item item, float worth){
        if(item == null)
            throw new NullPointerException("Item cannot be null!");
        if(worth <= 0)
            throw new IllegalArgumentException("Worth must be positive!");
        return new ItemWithWorthImpl(item, worth);
    }

    public static ItemWithWorth defaultWorth(Item item){
        return of(item, 1);
    }

    private final Item item;
    private final float worth;

    private ItemWithWorthImpl(Item item, float worth){
        this.item = item;
        this.worth = worth;
    }

    @Override
    public Item item(){
        return this.item;
    }

    @Override
    public float worth(){
        return this.worth;
    }
}
