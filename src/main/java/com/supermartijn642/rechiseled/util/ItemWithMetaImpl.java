package com.supermartijn642.rechiseled.util;

import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.api.util.ItemWithMeta;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * Created 20/01/2026 by SuperMartijn642
 */
public class ItemWithMetaImpl implements ItemWithMeta {

    private final Item item;
    private final int meta;

    public ItemWithMetaImpl(Item item, int meta){
        this.item = item;
        this.meta = meta;
    }

    @Override
    public Item item(){
        return this.item;
    }

    @Override
    public int meta(){
        return this.meta;
    }

    @Override
    public String toString(){
        ResourceLocation identifier = Registries.ITEMS.getIdentifier(this.item);
        return this.hasSubtypes() ? "(" + identifier + "," + this.meta + ")" : identifier.toString();
    }

    @Override
    public final boolean equals(Object object){
        if(!(object instanceof ItemWithMetaImpl)) return false;

        ItemWithMetaImpl that = (ItemWithMetaImpl)object;
        return this.meta == that.meta && this.item.equals(that.item);
    }

    @Override
    public int hashCode(){
        int result = this.item.hashCode();
        result = 31 * result + this.meta;
        return result;
    }
}
