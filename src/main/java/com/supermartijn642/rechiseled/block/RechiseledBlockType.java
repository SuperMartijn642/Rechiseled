package com.supermartijn642.rechiseled.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * Created 18/04/2023 by SuperMartijn642
 */
public class RechiseledBlockType {

    private final ResourceLocation identifier;
    private final Supplier<Block> regularBlock, connectingBlock;
    private final Supplier<Item> regularItem, connectingItem;

    public RechiseledBlockType(ResourceLocation identifier, Supplier<Block> regularBlock, Supplier<Block> connectingBlock, Supplier<Item> regularItem, Supplier<Item> connectingItem){
        this.identifier = identifier;
        this.regularBlock = regularBlock;
        this.connectingBlock = connectingBlock;
        this.regularItem = regularItem;
        this.connectingItem = connectingItem;
    }

    public ResourceLocation getIdentifier(){
        return this.identifier;
    }

    public Block getRegularBlock(){
        return this.regularBlock == null ? null : this.regularBlock.get();
    }

    public Block getConnectingBlock(){
        return this.connectingBlock == null ? null : this.connectingBlock.get();
    }

    public Item getRegularItem(){
        return this.regularItem == null ? null : this.regularItem.get();
    }

    public Item getConnectingItem(){
        return this.connectingItem == null ? null : this.connectingItem.get();
    }
}
