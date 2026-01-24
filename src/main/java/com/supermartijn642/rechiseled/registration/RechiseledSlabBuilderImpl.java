package com.supermartijn642.rechiseled.registration;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.block.BlockProperties;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.rechiseled.api.blocks.BlockSpecification;
import com.supermartijn642.rechiseled.api.blocks.RechiseledSlabBuilder;
import com.supermartijn642.rechiseled.blocks.RechiseledGlassSlabBlock;
import com.supermartijn642.rechiseled.blocks.RechiseledSlabBlock;
import com.supermartijn642.rechiseled.blocks.impl.SlabItem;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Created 11/01/2026 by SuperMartijn642
 */
public class RechiseledSlabBuilderImpl extends RechiseledCommonBlockBuilderImpl<RechiseledSlabBuilder> implements RechiseledSlabBuilder {
    RechiseledSlabBuilderImpl(RechiseledBlockBuilderImpl parent){
        super(parent, "_slab", " Slab");
    }

    @Override
    protected Block createBlock(BlockSpecification specification, Block parent, boolean connecting, BlockProperties properties, ResourceLocation identifier){
        if(specification == BlockSpecification.BASIC || specification == BlockSpecification.PILLAR)
            return new RechiseledSlabBlock(connecting, properties);
        if(specification == BlockSpecification.GLASS || specification == BlockSpecification.GLASS_PILLAR)
            return new RechiseledGlassSlabBlock(connecting, properties);
        throw new IllegalStateException("Unknown specification: " + specification);
    }

    @Override
    protected ItemBlock createItem(Block parent, boolean connecting, ItemProperties properties){
        if(connecting){
            return new SlabItem(parent, properties) {
                @Override
                protected void appendItemInformation(ItemStack stack, @Nullable IBlockAccess level, Consumer<ITextComponent> info, boolean advanced){
                    super.appendItemInformation(stack, level, info, advanced);
                    info.accept(TextComponents.translation("rechiseled.tooltip.connecting").color(TextFormatting.GRAY).get());
                }
            };
        }
        return new SlabItem(parent, properties);
    }

    @Override
    protected void setBlockReferences(Block regularBlock, Block regularStairs, Block regularSlab, Block connectingBlock, Block connectingStairs, Block connectingSlab){
        if(this.hasRegularVariant && this.regularBlock.get() instanceof RechiseledGlassSlabBlock)
            ((RechiseledGlassSlabBlock)this.regularBlock.get()).setStairsAndSlab(regularBlock, regularStairs);
        if(this.hasConnectingVariant && this.connectingBlock.get() instanceof RechiseledGlassSlabBlock)
            ((RechiseledGlassSlabBlock)this.connectingBlock.get()).setStairsAndSlab(connectingBlock, connectingStairs);
    }
}
