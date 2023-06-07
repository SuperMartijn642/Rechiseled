package com.supermartijn642.rechiseled.blocks;

import com.supermartijn642.rechiseled.api.blocks.BlockSpecification;
import com.supermartijn642.rechiseled.api.blocks.RechiseledBlockType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

/**
 * Created 18/04/2023 by SuperMartijn642
 */
public class RechiseledBlockTypeImpl implements RechiseledBlockType {

    private final ResourceLocation identifier;
    private final BlockSpecification specification;
    private final boolean hasRegularVariant, hasConnectingVariant;
    private final Supplier<Block> regularBlock, connectingBlock;
    private final Supplier<ItemBlock> regularItem, connectingItem;

    public RechiseledBlockTypeImpl(ResourceLocation identifier, BlockSpecification specification, boolean hasRegularVariant, boolean hasConnectingVariant, Supplier<Block> regularBlock, Supplier<Block> connectingBlock, Supplier<ItemBlock> regularItem, Supplier<ItemBlock> connectingItem){
        this.identifier = identifier;
        this.specification = specification;
        this.hasRegularVariant = hasRegularVariant;
        this.hasConnectingVariant = hasConnectingVariant;
        this.regularBlock = regularBlock;
        this.connectingBlock = connectingBlock;
        this.regularItem = regularItem;
        this.connectingItem = connectingItem;
    }

    public ResourceLocation getIdentifier(){
        return this.identifier;
    }

    public BlockSpecification getSpecification(){
        return this.specification;
    }

    @Override
    public boolean hasRegularVariant(){
        return this.hasRegularVariant;
    }

    public Block getRegularBlock(){
        return this.regularBlock == null ? null : this.regularBlock.get();
    }

    public Block getConnectingBlock(){
        return this.connectingBlock == null ? null : this.connectingBlock.get();
    }

    public ItemBlock getRegularItem(){
        return this.regularItem == null ? null : this.regularItem.get();
    }

    @Override
    public boolean hasConnectingVariant(){
        return this.hasConnectingVariant;
    }

    public ItemBlock getConnectingItem(){
        return this.connectingItem == null ? null : this.connectingItem.get();
    }
}
