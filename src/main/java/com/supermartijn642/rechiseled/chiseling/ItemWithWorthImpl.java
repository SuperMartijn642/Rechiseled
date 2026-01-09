package com.supermartijn642.rechiseled.chiseling;

import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;
import net.minecraft.world.item.Item;

/**
 * Created 09/01/2026 by SuperMartijn642
 */
public record ItemWithWorthImpl(Item item, float worth) implements ItemWithWorth {

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
}
