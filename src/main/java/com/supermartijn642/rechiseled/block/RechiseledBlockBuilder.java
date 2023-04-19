package com.supermartijn642.rechiseled.block;

import com.supermartijn642.core.block.BlockProperties;
import com.supermartijn642.core.item.BaseBlockItem;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.core.registry.RegistrationHandler;
import com.supermartijn642.core.util.Holder;
import com.supermartijn642.rechiseled.data.*;
import com.supermartijn642.rechiseled.texture.TextureType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created 18/04/2023 by SuperMartijn642
 */
public class RechiseledBlockBuilder {

    private final String namespace, identifier;
    private Supplier<BlockProperties> properties;
    private List<Consumer<BlockProperties>> propertiesConfigurers = new ArrayList<>();
    private BlockSpecification specification = BlockSpecification.BASIC;
    private boolean hasRegularVariant = true;
    private boolean hasConnectingVariant = true;
    private Supplier<Block> customRegularVariant;
    private int customRegularVariantData;
    private Supplier<Block> customConnectingVariant;
    private int customConnectingVariantData;
    private Set<ResourceLocation> recipes = new HashSet<>();
    private CreativeItemGroup itemGroup;
    private Set<ResourceLocation> tags = new HashSet<>();
    private Supplier<Block> tagsFromBlock;
    private TextureType regularTextureType = TextureType.NON_CONNECTING;
    private TextureType connectingTextureType = TextureType.CONNECTING;
    private String translation;

    RechiseledBlockBuilder(String namespace, String identifier){
        this.namespace = namespace;
        this.identifier = identifier;
    }

    public RechiseledBlockBuilder properties(BlockProperties properties){
        this.properties = () -> properties;
        this.propertiesConfigurers.clear();
        return this;
    }

    public RechiseledBlockBuilder copyProperties(Supplier<Block> block){
        this.properties = () -> BlockProperties.copy(block.get());
        this.propertiesConfigurers.clear();
        return this;
    }

    public RechiseledBlockBuilder properties(Consumer<BlockProperties> configurer){
        if(this.properties == null)
            throw new RuntimeException("Properties cannot be null!");
        this.propertiesConfigurers.add(configurer);
        return this;
    }

    public RechiseledBlockBuilder itemGroup(CreativeItemGroup group){
        this.itemGroup = group;
        return this;
    }

    public RechiseledBlockBuilder specification(BlockSpecification specification){
        this.specification = specification;
        return this;
    }

    public RechiseledBlockBuilder noRegularVariant(){
        this.hasRegularVariant = false;
        return this;
    }

    public RechiseledBlockBuilder noConnectingVariant(){
        this.hasConnectingVariant = false;
        return this;
    }

    public RechiseledBlockBuilder regularVariant(Supplier<Block> blockSupplier, int data){
        this.customRegularVariant = blockSupplier;
        this.customRegularVariantData = data;
        this.hasRegularVariant = false;
        return this;
    }

    public RechiseledBlockBuilder connectingVariant(Supplier<Block> blockSupplier, int data){
        this.customConnectingVariant = blockSupplier;
        this.customConnectingVariantData = data;
        this.hasConnectingVariant = false;
        return this;
    }

    public RechiseledBlockBuilder recipes(ResourceLocation location, ResourceLocation... locations){
        this.recipes.add(location);
        this.recipes.addAll(Arrays.asList(locations));
        return this;
    }

    public RechiseledBlockBuilder blockTag(String namespace, String identifier){
        this.tags.add(new ResourceLocation(namespace, identifier));
        return this;
    }

    public RechiseledBlockBuilder blockTagsFrom(Supplier<Block> blockSupplier){
        this.tagsFromBlock = blockSupplier;
        return this;
    }

    public RechiseledBlockBuilder regularVariantTextureType(TextureType type){
        this.regularTextureType = type;
        return this;
    }

    public RechiseledBlockBuilder connectingVariantTextureType(TextureType type){
        this.connectingTextureType = type;
        return this;
    }

    public RechiseledBlockBuilder translation(String translation){
        this.translation = translation;
        return this;
    }

    public RechiseledBlockType build(){
        BlockProperties properties = this.properties.get();
        this.propertiesConfigurers.forEach(consumer -> consumer.accept(properties));

        RegistrationHandler handler = RegistrationHandler.get(this.namespace);
        // Create holders to put the blocks in once they have been registered.
        Holder<Block> regularBlockHolder = new Holder<>();
        Holder<Block> connectingBlockHolder = new Holder<>();
        if(this.hasRegularVariant)
            handler.registerBlock(this.identifier, () -> {
                regularBlockHolder.set(new RechiseledBlock(false, properties));
                return regularBlockHolder.get();
            });
        if(this.hasConnectingVariant)
            handler.registerBlock(this.identifier + "_connecting", () -> {
                connectingBlockHolder.set(new RechiseledBlock(true, properties));
                return connectingBlockHolder.get();
            });
        // Create holders to put the items in once they have been registered.
        Holder<Item> regularItemHolder = new Holder<>();
        Holder<Item> connectingItemHolder = new Holder<>();
        if(this.hasRegularVariant)
            handler.registerItem(this.identifier, () -> {
                regularItemHolder.set(new BaseBlockItem(regularBlockHolder.get(), ItemProperties.create().group(this.itemGroup)));
                return regularItemHolder.get();
            });
        if(this.hasConnectingVariant)
            handler.registerItem(this.identifier + "_connecting", () -> {
                connectingItemHolder.set(new BaseBlockItem(connectingBlockHolder.get(), ItemProperties.create().group(this.itemGroup)));
                return connectingItemHolder.get();
            });

        // Generate all the resources
        if(this.hasRegularVariant){
            RechiseledBlockStateGenerator.addBlock(this.specification, regularBlockHolder::get);
            RechiseledBlockTagsGenerator.addBlockTags(regularBlockHolder::get, this.tags);
            if(this.tagsFromBlock != null)
                RechiseledBlockTagsGenerator.addBlockTagsFromOtherBlock(regularBlockHolder::get, this.tagsFromBlock);
            RechiseledConnectingBlockModelProvider.addBlock(regularBlockHolder::get, this.specification, this.regularTextureType, new ResourceLocation(this.namespace, "block/" + this.identifier));
            RechiseledLanguageGenerator.addBlockTranslation(regularBlockHolder::get, this.translation);
            RechiseledLootTableGenerator.addDropSelfBlock(regularBlockHolder::get);
        }
        if(this.hasConnectingVariant){
            RechiseledBlockStateGenerator.addBlock(this.specification, connectingBlockHolder::get);
            RechiseledBlockTagsGenerator.addBlockTags(connectingBlockHolder::get, this.tags);
            if(this.tagsFromBlock != null)
                RechiseledBlockTagsGenerator.addBlockTagsFromOtherBlock(connectingBlockHolder::get, this.tagsFromBlock);
            RechiseledConnectingBlockModelProvider.addBlock(connectingBlockHolder::get, this.specification, this.connectingTextureType, new ResourceLocation(this.namespace, "block/" + this.identifier));
            RechiseledLanguageGenerator.addBlockTranslation(connectingBlockHolder::get, this.translation);
            RechiseledLootTableGenerator.addDropSelfBlock(connectingBlockHolder::get);
        }
        for(ResourceLocation recipe : this.recipes)
            RechiseledChiselingRecipeProvider.addRecipeEntry(
                recipe,
                this.hasRegularVariant ? regularItemHolder::get : () -> this.customRegularVariant == null ? null : Item.getItemFromBlock(this.customRegularVariant.get()),
                this.customRegularVariantData,
                this.hasConnectingVariant ? connectingItemHolder::get : () -> this.customConnectingVariant == null ? null : Item.getItemFromBlock(this.customConnectingVariant.get()),
                this.customConnectingVariantData
            );

        // Create the block type
        return new RechiseledBlockType(
            new ResourceLocation(this.namespace, this.identifier),
            this.hasRegularVariant ? regularBlockHolder::get : this.customRegularVariant,
            this.hasConnectingVariant ? connectingBlockHolder::get : this.customConnectingVariant,
            this.hasRegularVariant ? regularItemHolder::get : this.customRegularVariant == null ? null : () -> Item.getItemFromBlock(this.customRegularVariant.get()),
            this.hasConnectingVariant ? connectingItemHolder::get : this.customConnectingVariant == null ? null : () -> Item.getItemFromBlock(this.customConnectingVariant.get())
        );
    }
}
