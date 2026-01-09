package com.supermartijn642.rechiseled.api.util;

import com.supermartijn642.rechiseled.util.ItemWithMetaImpl;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created 20/01/2026 by SuperMartijn642
 */
public interface ItemWithMeta {

    static ItemWithMeta of(Item item, int meta){
        if(meta != 0 && !item.getHasSubtypes())
            throw new IllegalArgumentException("Cannot set metadata for item without sub-types!");
        return new ItemWithMetaImpl(item, meta);
    }

    static ItemWithMeta of(Item item){
        return of(item, 0);
    }

    static ItemWithMeta fromStack(ItemStack stack){
        if(!stack.getHasSubtypes())
            return of(stack.getItem());
        return of(stack.getItem(), stack.getMetadata());
    }

    static ItemWithMeta of(Block block, int meta){
        return of(Item.getItemFromBlock(block), meta);
    }

    static ItemWithMeta of(Block block){
        return of(block, 0);
    }

    Item item();

    int meta();

    default boolean hasSubtypes(){
        return this.item().getHasSubtypes();
    }

    default ItemStack toStack(int count){
        return new ItemStack(this.item(), count, this.meta());
    }

    default ItemStack toStack(){
        return this.toStack(1);
    }

    default boolean matches(ItemStack stack){
        if(stack.getItem() != this.item())
            return false;
        return !this.hasSubtypes() || stack.getMetadata() == this.meta();
    }
}
