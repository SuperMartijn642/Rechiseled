package com.supermartijn642.rechiseled.registration;

import com.supermartijn642.core.block.BlockProperties;
import com.supermartijn642.rechiseled.api.blocks.BlockSpecification;
import com.supermartijn642.rechiseled.api.blocks.RechiseledSlabBuilder;
import com.supermartijn642.rechiseled.blocks.RechiseledGlassSlabBlock;
import com.supermartijn642.rechiseled.blocks.RechiseledSlabBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Created 11/01/2026 by SuperMartijn642
 */
public class RechiseledSlabBuilderImpl extends RechiseledCommonBlockBuilderImpl<RechiseledSlabBuilder> implements RechiseledSlabBuilder {
    RechiseledSlabBuilderImpl(RechiseledBlockBuilderImpl parent){
        super(parent, "_slab", " Slab");
    }

    @Override
    protected Block createBlock(BlockSpecification specification, Block parent, boolean connecting, BlockProperties properties, ResourceLocation identifier){
        //noinspection deprecation
        BlockBehaviour.Properties vanillaProperties = properties.toUnderlying();
        if(specification == BlockSpecification.BASIC || specification == BlockSpecification.PILLAR)
            return new RechiseledSlabBlock(connecting, vanillaProperties);
        if(specification == BlockSpecification.GLASS || specification == BlockSpecification.GLASS_PILLAR)
            return new RechiseledGlassSlabBlock(connecting, vanillaProperties);
        throw new IllegalStateException("Unknown specification: " + specification);
    }

    @Override
    protected void setBlockReferences(Block regularBlock, Block regularStairs, Block regularSlab, Block connectingBlock, Block connectingStairs, Block connectingSlab){
        if(this.hasRegularVariant && this.regularBlock.get() instanceof RechiseledGlassSlabBlock)
            ((RechiseledGlassSlabBlock)this.regularBlock.get()).setStairsAndSlab(regularBlock, regularStairs);
        if(this.hasConnectingVariant && this.connectingBlock.get() instanceof RechiseledGlassSlabBlock)
            ((RechiseledGlassSlabBlock)this.connectingBlock.get()).setStairsAndSlab(connectingBlock, connectingStairs);
    }
}
