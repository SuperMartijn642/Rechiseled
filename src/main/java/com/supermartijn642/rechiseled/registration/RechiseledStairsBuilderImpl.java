package com.supermartijn642.rechiseled.registration;

import com.supermartijn642.core.block.BlockProperties;
import com.supermartijn642.rechiseled.api.blocks.BlockSpecification;
import com.supermartijn642.rechiseled.api.blocks.RechiseledStairsBuilder;
import com.supermartijn642.rechiseled.blocks.RechiseledGlassStairBlock;
import com.supermartijn642.rechiseled.blocks.RechiseledStairBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Created 11/01/2026 by SuperMartijn642
 */
public class RechiseledStairsBuilderImpl extends RechiseledCommonBlockBuilderImpl<RechiseledStairsBuilder> implements RechiseledStairsBuilder {
    RechiseledStairsBuilderImpl(RechiseledBlockBuilderImpl parent){
        super(parent, "_stairs", " Stairs");
    }

    @Override
    protected Block createBlock(BlockSpecification specification, Block parent, boolean connecting, BlockProperties properties, ResourceLocation identifier){
        //noinspection deprecation
        BlockBehaviour.Properties vanillaProperties = properties.toUnderlying();
        if(specification == BlockSpecification.BASIC || specification == BlockSpecification.PILLAR)
            return new RechiseledStairBlock(connecting, parent.defaultBlockState(), vanillaProperties);
        if(specification == BlockSpecification.GLASS || specification == BlockSpecification.GLASS_PILLAR)
            return new RechiseledGlassStairBlock(connecting, parent.defaultBlockState(), vanillaProperties);
        throw new IllegalStateException("Unknown specification: " + specification);
    }

    @Override
    protected void setBlockReferences(Block regularBlock, Block regularStairs, Block regularSlab, Block connectingBlock, Block connectingStairs, Block connectingSlab){
        if(this.hasRegularVariant && this.regularBlock.get() instanceof RechiseledGlassStairBlock)
            ((RechiseledGlassStairBlock)this.regularBlock.get()).setBlockAndSlab(regularBlock, regularSlab);
        if(this.hasConnectingVariant && this.connectingBlock.get() instanceof RechiseledGlassStairBlock)
            ((RechiseledGlassStairBlock)this.connectingBlock.get()).setBlockAndSlab(connectingBlock, connectingSlab);
    }
}
