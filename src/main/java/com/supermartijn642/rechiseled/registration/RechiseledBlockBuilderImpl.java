package com.supermartijn642.rechiseled.registration;

import com.supermartijn642.core.block.BlockProperties;
import com.supermartijn642.core.registry.RegistrationHandler;
import com.supermartijn642.rechiseled.api.blocks.*;
import com.supermartijn642.rechiseled.blocks.RechiseledBlock;
import com.supermartijn642.rechiseled.blocks.RechiseledGlassBlock;
import com.supermartijn642.rechiseled.blocks.RechiseledGlassPillarBlock;
import com.supermartijn642.rechiseled.blocks.RechiseledPillarBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created 18/04/2023 by SuperMartijn642
 */
public class RechiseledBlockBuilderImpl extends RechiseledCommonBlockBuilderImpl<RechiseledBlockBuilder> implements RechiseledBlockBuilder {

    private RechiseledStairsBuilderImpl stairs;
    private RechiseledSlabBuilderImpl slabs;
    private BlockSpecification specification = BlockSpecification.BASIC;
    private ResourceLocation recipe;
    private BlockModelType modelType;
    private final Set<ResourceLocation> stairsSlabsBlockTags = new HashSet<>(), stairsSlabsItemTags = new HashSet<>();

    public RechiseledBlockBuilderImpl(RechiseledRegistrationImpl registration, String identifier){
        super(registration, identifier);
    }

    public boolean hasStairs(){
        return this.stairs != null;
    }

    public boolean hasSlabs(){
        return this.slabs != null;
    }

    public RechiseledStairsBuilderImpl getStairs(){
        return this.stairs;
    }

    public RechiseledSlabBuilderImpl getSlabs(){
        return this.slabs;
    }

    public BlockSpecification getSpecification(){
        return this.specification;
    }

    public ResourceLocation getRecipe(){
        return this.recipe;
    }

    public BlockModelType getModelType(){
        return this.modelType;
    }

    @Override
    protected Block createBlock(BlockSpecification specification, Block parent, boolean connecting, BlockProperties properties, ResourceLocation identifier){
        if(specification == BlockSpecification.BASIC)
            return new RechiseledBlock(connecting, properties);
        if(specification == BlockSpecification.PILLAR)
            return new RechiseledPillarBlock(connecting, properties);
        if(specification == BlockSpecification.GLASS)
            return new RechiseledGlassBlock(connecting, properties);
        if(specification == BlockSpecification.GLASS_PILLAR)
            return new RechiseledGlassPillarBlock(connecting, properties);
        throw new IllegalStateException("Unknown specification: " + specification);
    }

    @Override
    protected void setBlockReferences(Block regularBlock, Block regularStairs, Block regularSlab, Block connectingBlock, Block connectingStairs, Block connectingSlab){
        if(this.hasRegularVariant && this.regularBlock.get() instanceof RechiseledGlassBlock)
            ((RechiseledGlassBlock)this.regularBlock.get()).setStairsAndSlab(regularStairs, regularSlab);
        if(this.hasRegularVariant && this.regularBlock.get() instanceof RechiseledGlassPillarBlock)
            ((RechiseledGlassPillarBlock)this.regularBlock.get()).setStairsAndSlab(regularStairs, regularSlab);
        if(this.hasConnectingVariant && this.connectingBlock.get() instanceof RechiseledGlassBlock)
            ((RechiseledGlassBlock)this.connectingBlock.get()).setStairsAndSlab(connectingStairs, connectingSlab);
        if(this.hasConnectingVariant && this.connectingBlock.get() instanceof RechiseledGlassPillarBlock)
            ((RechiseledGlassPillarBlock)this.connectingBlock.get()).setStairsAndSlab(connectingStairs, connectingSlab);
    }

    @Override
    public RechiseledBlockType build(){
        this.checkMutable();
        // Add the stairs and slab tags that were added through the block's builder
        if(this.stairs != null){
            this.stairsSlabsBlockTags.forEach(this.stairs::blockTag);
            this.stairsSlabsItemTags.forEach(this.stairs::itemTag);
        }
        if(this.slabs != null){
            this.stairsSlabsBlockTags.forEach(this.slabs::blockTag);
            this.stairsSlabsItemTags.forEach(this.slabs::itemTag);
        }

        // Mark builders as complete
        this.complete();
        if(this.stairs != null)
            this.stairs.complete();
        if(this.slabs != null)
            this.slabs.complete();

        // Create blocks
        this.createBlocks(this.specification);
        if(this.stairs != null)
            this.stairs.createBlocks(this.specification);
        if(this.slabs != null)
            this.slabs.createBlocks(this.specification);

        // Update blocks with references to each other
        RegistrationHandler.get(this.registration.getModid()).registerBlockCallback(helper -> {
            Block regularBlock = this.hasRegularVariant ? this.regularBlock.get() : null;
            Block connectingBlock = this.hasConnectingVariant ? this.connectingBlock.get() : null;
            Block regularStairs = this.stairs != null && this.stairs.hasRegularVariant ? this.stairs.regularBlock.get() : null;
            Block connectingStairs = this.stairs != null && this.stairs.hasConnectingVariant ? this.stairs.connectingBlock.get() : null;
            Block regularSlab = this.slabs != null && this.slabs.hasRegularVariant ? this.slabs.regularBlock.get() : null;
            Block connectingSlab = this.slabs != null && this.slabs.hasConnectingVariant ? this.slabs.connectingBlock.get() : null;
            this.setBlockReferences(regularBlock, regularStairs, regularSlab, connectingBlock, connectingStairs, connectingSlab);
            if(this.stairs != null)
                this.stairs.setBlockReferences(regularBlock, regularStairs, regularSlab, connectingBlock, connectingStairs, connectingSlab);
            if(this.slabs != null)
                this.slabs.setBlockReferences(regularBlock, regularStairs, regularSlab, connectingBlock, connectingStairs, connectingSlab);
        });

        // Create the block type
        RechiseledBlockTypeImpl blockType = new RechiseledBlockTypeImpl(
            this.hasRegularVariant,
            this.hasConnectingVariant,
            this.hasRegularVariant ? this.regularBlock : null,
            this.hasConnectingVariant ? this.connectingBlock : null,
            this.hasRegularVariant ? this.regularItem : null,
            this.hasConnectingVariant ? this.connectingItem : null,
            this.stairs != null, this.slabs != null,
            this.stairs != null && this.stairs.hasRegularVariant ? this.stairs.regularBlock : null,
            this.stairs != null && this.stairs.hasConnectingVariant ? this.stairs.connectingBlock : null,
            this.slabs != null && this.slabs.hasRegularVariant ? this.slabs.regularBlock : null,
            this.slabs != null && this.slabs.hasConnectingVariant ? this.slabs.connectingBlock : null,
            this.stairs != null && this.stairs.hasRegularVariant ? this.stairs.regularItem : null,
            this.stairs != null && this.stairs.hasConnectingVariant ? this.stairs.connectingItem : null,
            this.slabs != null && this.slabs.hasRegularVariant ? this.slabs.regularItem : null,
            this.slabs != null && this.slabs.hasConnectingVariant ? this.slabs.connectingItem : null
        );
        this.registration.finalizeBuilder(this, blockType);
        return blockType;
    }

    @Override
    public RechiseledBlockBuilder copyProperties(Supplier<Block> block){
        this.checkMutable();
        return this.properties(() -> {
            Block resolvedBlock = block.get();
            try{
                return BlockProperties.copy(resolvedBlock);
            }catch(Exception e){
                throw new RuntimeException("Encountered an exception copying properties from block '" + resolvedBlock + "' for block builder '" + this.identifier + "' from mod '" + this.registration.getModid() + "'!", e);
            }
        });
    }

    @Override
    public RechiseledBlockBuilder specification(BlockSpecification specification){
        this.checkMutable();
        this.specification = specification;
        return this;
    }

    @Override
    public RechiseledBlockBuilder regularVariant(Supplier<Block> block, Supplier<Block> stairs, Supplier<Block> slab){
        this.checkMutable();
        this.regularVariant(block);
        if(stairs != null)
            this.withStairs(b -> b.regularVariant(stairs));
        if(slab != null)
            this.withSlabs(b -> b.regularVariant(slab));
        return this;
    }

    @Override
    public RechiseledBlockBuilder connectingVariant(Supplier<Block> block, Supplier<Block> stairs, Supplier<Block> slab){
        this.checkMutable();
        this.connectingVariant(block);
        if(stairs != null)
            this.withStairs(b -> b.connectingVariant(stairs));
        if(slab != null)
            this.withSlabs(b -> b.connectingVariant(slab));
        return this;
    }

    @Override
    public RechiseledBlockBuilder withStairs(){
        this.checkMutable();
        if(this.stairs == null)
            this.stairs = new RechiseledStairsBuilderImpl(this);
        return this;
    }

    @Override
    public RechiseledBlockBuilder withStairs(Consumer<RechiseledStairsBuilder> builder){
        this.withStairs();
        builder.accept(this.stairs);
        return this;
    }

    @Override
    public RechiseledBlockBuilder withSlabs(){
        this.checkMutable();
        if(this.slabs == null)
            this.slabs = new RechiseledSlabBuilderImpl(this);
        return this;
    }

    @Override
    public RechiseledBlockBuilder withSlabs(Consumer<RechiseledSlabBuilder> builder){
        this.withSlabs();
        builder.accept(this.slabs);
        return this;
    }

    @Override
    public RechiseledBlockBuilder recipe(ResourceLocation location){
        this.checkMutable();
        this.recipe = location;
        return this;
    }

    @Override
    public RechiseledBlockBuilder model(BlockModelType modelType){
        this.checkMutable();
        this.modelType = modelType;
        return this;
    }

    @Override
    public RechiseledBlockBuilder blockTag(ResourceLocation identifier, boolean includeStairsAndSlabs){
        this.checkMutable();
        this.stairsSlabsBlockTags.add(identifier);
        return this.blockTag(identifier);
    }

    @Override
    public RechiseledBlockBuilder itemTag(ResourceLocation identifier, boolean includeStairsAndSlabs){
        this.checkMutable();
        this.stairsSlabsItemTags.add(identifier);
        return this.itemTag(identifier);
    }

    @Override
    public RechiseledBlockBuilder configure(Consumer<RechiseledBlockBuilder> configurer){
        configurer.accept(this);
        return this;
    }
}
