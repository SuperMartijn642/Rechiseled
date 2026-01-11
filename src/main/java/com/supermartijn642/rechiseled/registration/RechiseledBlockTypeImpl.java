package com.supermartijn642.rechiseled.registration;

import com.supermartijn642.rechiseled.api.blocks.RechiseledBlockType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import java.util.function.Supplier;

/**
 * Created 18/04/2023 by SuperMartijn642
 */
public class RechiseledBlockTypeImpl implements RechiseledBlockType {

    private final boolean hasRegularVariant, hasConnectingVariant;
    private final Supplier<Block> regularBlock, connectingBlock;
    private final Supplier<ItemBlock> regularItem, connectingItem;
    private final boolean hasStairs, hasSlabs;
    private final Supplier<Block> regularStairs, connectingStairs, regularSlab, connectingSlab;
    private final Supplier<ItemBlock> regularStairsItem, connectingStairsItem, regularSlabItem, connectingSlabItem;

    public RechiseledBlockTypeImpl(boolean hasRegularVariant, boolean hasConnectingVariant, Supplier<Block> regularBlock, Supplier<Block> connectingBlock, Supplier<ItemBlock> regularItem, Supplier<ItemBlock> connectingItem, boolean hasStairs, boolean hasSlabs, Supplier<Block> regularStairs, Supplier<Block> connectingStairs, Supplier<Block> regularSlab, Supplier<Block> connectingSlab, Supplier<ItemBlock> regularStairsItem, Supplier<ItemBlock> connectingStairsItem, Supplier<ItemBlock> regularSlabItem, Supplier<ItemBlock> connectingSlabItem){
        this.hasRegularVariant = hasRegularVariant;
        this.hasConnectingVariant = hasConnectingVariant;
        this.regularBlock = regularBlock;
        this.connectingBlock = connectingBlock;
        this.regularItem = regularItem;
        this.connectingItem = connectingItem;
        this.hasStairs = hasStairs;
        this.hasSlabs = hasSlabs;
        this.regularStairs = regularStairs;
        this.connectingStairs = connectingStairs;
        this.regularSlab = regularSlab;
        this.connectingSlab = connectingSlab;
        this.regularStairsItem = regularStairsItem;
        this.connectingStairsItem = connectingStairsItem;
        this.regularSlabItem = regularSlabItem;
        this.connectingSlabItem = connectingSlabItem;
    }

    @Override
    public boolean hasRegularVariant(){
        return this.hasRegularVariant;
    }

    @Override
    public Block getRegularBlock(){
        return this.regularBlock == null ? null : this.regularBlock.get();
    }

    @Override
    public Block getConnectingBlock(){
        return this.connectingBlock == null ? null : this.connectingBlock.get();
    }

    @Override
    public ItemBlock getRegularItem(){
        return this.regularItem == null ? null : this.regularItem.get();
    }

    @Override
    public boolean hasConnectingVariant(){
        return this.hasConnectingVariant;
    }

    @Override
    public ItemBlock getConnectingItem(){
        return this.connectingItem == null ? null : this.connectingItem.get();
    }

    @Override
    public boolean hasStairs(){
        return this.hasStairs;
    }

    @Override
    public boolean hasRegularStairs(){
        return this.regularStairs != null;
    }

    @Override
    public Block getRegularStairs(){
        return this.regularStairs == null ? null : this.regularStairs.get();
    }

    @Override
    public ItemBlock getRegularStairsItem(){
        return this.regularStairsItem == null ? null : this.regularStairsItem.get();
    }

    @Override
    public boolean hasConnectingStairs(){
        return this.connectingStairs != null;
    }

    @Override
    public Block getConnectingStairs(){
        return this.connectingStairs == null ? null : this.connectingStairs.get();
    }

    @Override
    public ItemBlock getConnectingStairsItem(){
        return this.connectingStairsItem == null ? null : this.connectingStairsItem.get();
    }

    @Override
    public boolean hasSlabs(){
        return this.hasSlabs;
    }

    @Override
    public boolean hasRegularSlab(){
        return this.regularSlab != null;
    }

    @Override
    public Block getRegularSlab(){
        return this.regularSlab == null ? null : this.regularSlab.get();
    }

    @Override
    public ItemBlock getRegularSlabItem(){
        return this.regularSlabItem == null ? null : this.regularSlabItem.get();
    }

    @Override
    public boolean hasConnectingSlab(){
        return this.connectingSlab != null;
    }

    @Override
    public Block getConnectingSlab(){
        return this.connectingSlab == null ? null : this.connectingSlab.get();
    }

    @Override
    public ItemBlock getConnectingSlabItem(){
        return this.connectingSlabItem == null ? null : this.connectingSlabItem.get();
    }
}
