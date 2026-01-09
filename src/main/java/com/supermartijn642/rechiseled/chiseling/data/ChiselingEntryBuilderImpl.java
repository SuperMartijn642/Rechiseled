package com.supermartijn642.rechiseled.chiseling.data;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;
import com.supermartijn642.rechiseled.api.chiseling.data.ChiselingEntryBuilder;
import com.supermartijn642.rechiseled.chiseling.ItemWithWorthImpl;
import net.minecraft.world.level.ItemLike;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created 10/01/2026 by SuperMartijn642
 */
public class ChiselingEntryBuilderImpl implements ChiselingEntryBuilder {

    public final Map<ChiselingBlockShape,ItemWithWorth> items = new EnumMap<>(ChiselingBlockShape.class);
    public final Map<ChiselingBlockShape,ItemWithWorth> connectingItems = new EnumMap<>(ChiselingBlockShape.class);
    public boolean optional = false;

    public ChiselingEntryBuilderImpl(){
    }

    @Override
    public ChiselingEntryBuilder optional(boolean optional){
        this.optional = optional;
        return this;
    }

    @Override
    public ChiselingEntryBuilder optional(){
        return this.optional(true);
    }

    @Override
    public ChiselingEntryBuilder regularItem(ChiselingBlockShape shape, ItemLike item, float worth){
        if(item == null)
            throw new IllegalArgumentException("Item must not be null!");
        if(worth <= 0)
            throw new IllegalArgumentException("Worth must be greater than zero!");

        this.items.put(shape, ItemWithWorthImpl.of(item.asItem(), worth));
        return this;
    }

    @Override
    public ChiselingEntryBuilder regularItem(ChiselingBlockShape shape, ItemLike item){
        return this.regularItem(shape, item, 1);
    }

    @Override
    public ChiselingEntryBuilder connectingItem(ChiselingBlockShape shape, ItemLike item, float worth){
        if(item == null)
            throw new IllegalArgumentException("Item must not be null!");
        if(worth <= 0)
            throw new IllegalArgumentException("Worth must be greater than zero!");

        this.connectingItems.put(shape, ItemWithWorthImpl.of(item.asItem(), worth));
        return this;
    }

    @Override
    public ChiselingEntryBuilder connectingItem(ChiselingBlockShape shape, ItemLike item){
        return this.connectingItem(shape, item, 1);
    }

    @Override
    public ChiselingEntryBuilder regularBlock(ItemLike item, float worth){
        return this.regularItem(ChiselingBlockShape.BLOCK, item, worth);
    }

    @Override
    public ChiselingEntryBuilder regularBlock(ItemLike item){
        return this.regularBlock(item, 1);
    }

    @Override
    public ChiselingEntryBuilder regularStairs(ItemLike item, float worth){
        return this.regularItem(ChiselingBlockShape.STAIRS, item, worth);
    }

    @Override
    public ChiselingEntryBuilder regularStairs(ItemLike item){
        return this.regularStairs(item, 1);
    }

    @Override
    public ChiselingEntryBuilder regularSlab(ItemLike item, float worth){
        return this.regularItem(ChiselingBlockShape.SLAB, item, worth);
    }

    @Override
    public ChiselingEntryBuilder regularSlab(ItemLike item){
        return this.regularSlab(item, 0.5f);
    }

    @Override
    public ChiselingEntryBuilder connectingBlock(ItemLike item, float worth){
        return this.connectingItem(ChiselingBlockShape.BLOCK, item, worth);
    }

    @Override
    public ChiselingEntryBuilder connectingBlock(ItemLike item){
        return this.connectingBlock(item, 1);
    }

    @Override
    public ChiselingEntryBuilder connectingStairs(ItemLike item, float worth){
        return this.connectingItem(ChiselingBlockShape.STAIRS, item, worth);
    }

    @Override
    public ChiselingEntryBuilder connectingStairs(ItemLike item){
        return this.connectingStairs(item, 1);
    }

    @Override
    public ChiselingEntryBuilder connectingSlab(ItemLike item, float worth){
        return this.connectingItem(ChiselingBlockShape.SLAB, item, worth);
    }

    @Override
    public ChiselingEntryBuilder connectingSlab(ItemLike item){
        return this.connectingSlab(item, 0.5f);
    }
}
