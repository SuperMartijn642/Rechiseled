package com.supermartijn642.rechiseled.blocks;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.block.BlockProperties;
import com.supermartijn642.core.item.BaseBlockItem;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import com.supermartijn642.core.util.Holder;
import com.supermartijn642.rechiseled.api.blocks.BlockModelType;
import com.supermartijn642.rechiseled.api.blocks.BlockSpecification;
import com.supermartijn642.rechiseled.api.blocks.RechiseledBlockBuilder;
import com.supermartijn642.rechiseled.api.blocks.RechiseledBlockType;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created 18/04/2023 by SuperMartijn642
 */
public class RechiseledBlockBuilderImpl implements RechiseledBlockBuilder {

    private final RechiseledRegistrationImpl registration;
    private final String identifier;
    private Supplier<BlockProperties> properties;
    private BlockSpecification specification = BlockSpecification.BASIC;
    private boolean hasRegularVariant = true;
    private boolean hasConnectingVariant = true;
    public Supplier<Block> customRegularVariant;
    public Supplier<Block> customConnectingVariant;
    public ResourceLocation recipe;
    private final Set<ItemGroup> itemGroups = new HashSet<>();
    public final Set<ResourceLocation> tags = new HashSet<>();
    public Supplier<Block> miningTagsFromBlock;
    public String translation;
    public BlockModelType modelType;
    private boolean completed = false;

    public RechiseledBlockBuilderImpl(RechiseledRegistrationImpl registration, String identifier){
        this.registration = registration;
        this.identifier = identifier;
    }

    public String getIdentifier(){
        return this.identifier;
    }

    @Override
    public RechiseledBlockBuilderImpl properties(BlockProperties properties){
        this.checkMutable();
        this.properties = () -> properties;
        return this;
    }

    @Override
    public RechiseledBlockBuilderImpl copyProperties(Supplier<Block> block){
        this.checkMutable();
        this.properties = () -> {
            Block resolvedBlock = block.get();
            try{
                return BlockProperties.copy(resolvedBlock);
            }catch(Exception e){
                throw new RuntimeException("Encountered an exception copying properties from block '" + resolvedBlock + "' for block builder '" + this.identifier + "' from mod '" + this.registration.getModid() + "'!", e);
            }
        };
        return this;
    }

    @Override
    public RechiseledBlockBuilderImpl properties(Supplier<BlockProperties> supplier){
        this.checkMutable();
        this.properties = supplier;
        return this;
    }

    @Override
    public RechiseledBlockBuilderImpl itemGroups(ItemGroup group, ItemGroup... groups){
        this.checkMutable();
        this.itemGroups.add(group);
        this.itemGroups.addAll(Arrays.asList(groups));
        return this;
    }

    @Override
    public RechiseledBlockBuilderImpl specification(BlockSpecification specification){
        this.checkMutable();
        this.specification = specification;
        return this;
    }

    @Override
    public RechiseledBlockBuilderImpl noRegularVariant(){
        this.checkMutable();
        this.hasRegularVariant = false;
        return this;
    }

    @Override
    public RechiseledBlockBuilderImpl noConnectingVariant(){
        this.checkMutable();
        this.hasConnectingVariant = false;
        return this;
    }

    @Override
    public RechiseledBlockBuilderImpl regularVariant(Supplier<Block> blockSupplier){
        this.checkMutable();
        this.customRegularVariant = blockSupplier;
        this.hasRegularVariant = false;
        return this;
    }

    @Override
    public RechiseledBlockBuilderImpl connectingVariant(Supplier<Block> blockSupplier){
        this.checkMutable();
        this.customConnectingVariant = blockSupplier;
        this.hasConnectingVariant = false;
        return this;
    }

    @Override
    public RechiseledBlockBuilderImpl recipe(ResourceLocation location){
        this.checkMutable();
        this.recipe = location;
        return this;
    }

    @Override
    public RechiseledBlockBuilderImpl blockTag(String namespace, String identifier){
        this.checkMutable();
        this.tags.add(new ResourceLocation(namespace, identifier));
        return this;
    }

    @Override
    public RechiseledBlockBuilder miningTagsFrom(Supplier<Block> blockSupplier){
        this.checkMutable();
        this.miningTagsFromBlock = blockSupplier;
        return this;
    }

    @Override
    public RechiseledBlockBuilderImpl translation(String translation){
        this.checkMutable();
        this.translation = translation;
        return this;
    }

    @Override
    public RechiseledBlockBuilder model(BlockModelType modelType){
        this.checkMutable();
        this.modelType = modelType;
        return this;
    }

    @Override
    public RechiseledBlockBuilder configure(Consumer<RechiseledBlockBuilder> configurer){
        configurer.accept(this);
        return this;
    }

    private void checkMutable(){
        if(this.completed)
            throw new RuntimeException("Builder has already been build!");
    }

    @Override
    public RechiseledBlockType build(){
        this.checkMutable();
        this.completed = true;

        // Get a registration handler
        RegistrationHandler handler = RegistrationHandler.get(this.registration.getModid());

        if(this.properties == null)
            throw new RuntimeException("Builder for '" + this.registration.getModid() + ":" + this.identifier + "' is missing block properties!");
        Holder<BlockProperties> properties = new Holder<BlockProperties>() {
            @Override
            public BlockProperties get(){
                BlockProperties properties = super.get();
                if(properties == null){
                    properties = RechiseledBlockBuilderImpl.this.properties.get();
                    if(properties == null)
                        throw new RuntimeException("Block properties supplier for '" + RechiseledBlockBuilderImpl.this.registration.getModid() + ":" + RechiseledBlockBuilderImpl.this.identifier + "' returned null!");
                    this.set(properties);
                }
                return properties;
            }
        };
        // Create holders to put the blocks in once they have been registered.
        Holder<Block> regularBlockHolder = new Holder<>();
        Holder<Block> connectingBlockHolder = new Holder<>();
        if(this.hasRegularVariant)
            handler.registerBlock(this.identifier, () -> {
                regularBlockHolder.set(this.createBlock(false, properties.get()));
                return regularBlockHolder.get();
            });
        if(this.hasConnectingVariant)
            handler.registerBlock(this.identifier + "_connecting", () -> {
                connectingBlockHolder.set(this.createBlock(true, properties.get()));
                return connectingBlockHolder.get();
            });
        // Create holders to put the items in once they have been registered.
        Holder<BlockItem> regularItemHolder = new Holder<>();
        Holder<BlockItem> connectingItemHolder = new Holder<>();
        ItemProperties itemProperties = ItemProperties.create();
        this.itemGroups.forEach(itemProperties::group);
        if(this.hasRegularVariant)
            handler.registerItem(this.identifier, () -> {
                regularItemHolder.set(new BaseBlockItem(regularBlockHolder.get(), itemProperties));
                return regularItemHolder.get();
            });
        if(this.hasConnectingVariant)
            handler.registerItem(this.identifier + "_connecting", () -> {
                connectingItemHolder.set(new BaseBlockItem(connectingBlockHolder.get(), itemProperties));
                return connectingItemHolder.get();
            });

        // Set the render type for transparent blocks
        if(CommonUtils.getEnvironmentSide().isClient()){
            if(this.specification == BlockSpecification.GLASS || this.specification == BlockSpecification.GLASS_PILLAR){
                if(this.hasRegularVariant)
                    ClientRegistrationHandler.get(this.registration.getModid()).registerBlockModelTranslucentRenderType(regularBlockHolder::get);
                if(this.hasConnectingVariant)
                    ClientRegistrationHandler.get(this.registration.getModid()).registerBlockModelTranslucentRenderType(connectingBlockHolder::get);
            }
        }

        // Create the block type
        RechiseledBlockTypeImpl blockType = new RechiseledBlockTypeImpl(
            new ResourceLocation(this.registration.getModid(), this.identifier),
            this.specification,
            this.hasRegularVariant,
            this.hasConnectingVariant,
            this.hasRegularVariant ? regularBlockHolder::get : null,
            this.hasConnectingVariant ? connectingBlockHolder::get : null,
            this.hasRegularVariant ? regularItemHolder::get : null,
            this.hasConnectingVariant ? connectingItemHolder::get : null
        );
        this.registration.finalizeBuilder(this, blockType);
        return blockType;
    }

    private RechiseledBlock createBlock(boolean connecting, BlockProperties properties){
        if(this.specification == BlockSpecification.BASIC)
            return new RechiseledBlock(connecting, properties);
        if(this.specification == BlockSpecification.PILLAR)
            return new RechiseledPillarBlock(connecting, properties);
        if(this.specification == BlockSpecification.GLASS)
            return new RechiseledGlassBlock(connecting, properties);
        if(this.specification == BlockSpecification.GLASS_PILLAR)
            return new RechiseledGlassPillarBlock(connecting, properties);
        throw new IllegalStateException("Unknown specification: " + this.specification);
    }
}
