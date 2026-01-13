package com.supermartijn642.rechiseled.registration;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.block.BlockProperties;
import com.supermartijn642.core.item.BaseBlockItem;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import com.supermartijn642.core.util.Holder;
import com.supermartijn642.rechiseled.api.blocks.BlockSpecification;
import com.supermartijn642.rechiseled.api.blocks.RechiseledCommonBlockBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created 11/01/2026 by SuperMartijn642
 */
public abstract class RechiseledCommonBlockBuilderImpl<T extends RechiseledCommonBlockBuilder<?>> implements RechiseledCommonBlockBuilder<T> {

    private final RechiseledCommonBlockBuilderImpl<?> parent;
    protected final RechiseledRegistrationImpl registration;
    protected final String identifier;
    private final String translationSuffix;
    private Supplier<BlockProperties> properties;
    private Consumer<BlockProperties> propertiesConsumer;
    protected boolean hasRegularVariant = true, hasConnectingVariant = true;
    private Supplier<Block> customRegularVariant, customConnectingVariant;
    private Set<CreativeModeTab> itemGroups;
    private final Set<ResourceLocation> blockTags = new HashSet<>(), itemTags = new HashSet<>();
    private Supplier<Block> miningTagsFromBlock;
    private String translation;
    private boolean completed;

    protected Supplier<Block> regularBlock, connectingBlock;
    protected Supplier<BlockItem> regularItem, connectingItem;

    protected RechiseledCommonBlockBuilderImpl(RechiseledRegistrationImpl registration, String identifier){
        this.parent = this;
        this.registration = registration;
        this.identifier = identifier;
        this.translationSuffix = null;
    }

    protected RechiseledCommonBlockBuilderImpl(RechiseledBlockBuilderImpl parent, String identifierSuffix, String translationSuffix){
        this.parent = parent;
        this.registration = this.parent.registration;
        this.identifier = this.parent.identifier + identifierSuffix;
        this.translationSuffix = translationSuffix;
    }

    public String getIdentifier(){
        return this.identifier;
    }

    public boolean hasRegularVariant(){
        return this.hasRegularVariant;
    }

    public boolean hasConnectingVariant(){
        return this.hasConnectingVariant;
    }

    public Block getRegularBlock(){
        return this.customRegularVariant != null ? this.customRegularVariant.get() : this.hasRegularVariant ? this.regularBlock.get() : null;
    }

    public Block getConnectingBlock(){
        return this.customConnectingVariant != null ? this.customConnectingVariant.get() : this.hasConnectingVariant ? this.connectingBlock.get() : null;
    }

    public Set<ResourceLocation> getBlockTags(){
        return Collections.unmodifiableSet(this.blockTags);
    }

    public Set<ResourceLocation> getItemTags(){
        return Collections.unmodifiableSet(this.itemTags);
    }

    public Block getMiningTagsBlock(){
        return this.miningTagsFromBlock == null ? null : this.miningTagsFromBlock.get();
    }

    public String getTranslation(){
        return this.translation;
    }

    protected void complete(){
        if(this.completed)
            return;
        this.completed = true;
        if(this.properties == null){
            this.properties = this.parent.properties;
            this.propertiesConsumer = this.propertiesConsumer == null ? this.parent.propertiesConsumer : this.parent.propertiesConsumer.andThen(this.propertiesConsumer);
        }
        this.hasRegularVariant = this.parent.hasRegularVariant;
        this.hasConnectingVariant = this.parent.hasConnectingVariant;
        if(!this.parent.hasRegularVariant && this.parent.customRegularVariant == null)
            this.customRegularVariant = null;
        if(!this.parent.hasConnectingVariant && this.parent.customConnectingVariant == null)
            this.customConnectingVariant = null;
        if(this.itemGroups == null)
            this.itemGroups = this.parent.itemGroups;
        this.blockTags.addAll(this.parent.blockTags);
        this.itemTags.addAll(this.parent.itemTags);
        if(this.miningTagsFromBlock == null)
            this.miningTagsFromBlock = this.parent.miningTagsFromBlock;
        if(this.translation == null && this.parent.translation != null)
            this.translation = this.parent.translation + this.translationSuffix;
    }

    protected void checkMutable(){
        if(this.completed)
            throw new RuntimeException("Builder has already been built!");
    }

    void createBlocks(BlockSpecification specification){
        // Get a registration handler
        RegistrationHandler handler = RegistrationHandler.get(this.registration.getModid());

        // Resolve block properties
        if(this.properties == null && this.propertiesConsumer == null)
            throw new RuntimeException("Builder for '" + this.registration.getModid() + ":" + this.identifier + "' is missing block properties!");
        Holder<BlockProperties> propertiesHolder = new Holder<>() {
            @Override
            public BlockProperties get(){
                BlockProperties value = super.get();
                if(value == null){
                    value = RechiseledCommonBlockBuilderImpl.this.properties == null ?
                        BlockProperties.create() : RechiseledCommonBlockBuilderImpl.this.properties.get();
                    if(RechiseledCommonBlockBuilderImpl.this.propertiesConsumer != null)
                        RechiseledCommonBlockBuilderImpl.this.propertiesConsumer.accept(value);
                    this.set(value);
                }
                return value;
            }
        };

        // Create holders to put the blocks in once they have been registered.
        Holder<Block> regularBlockHolder = new Holder<>();
        Holder<Block> connectingBlockHolder = new Holder<>();
        ResourceLocation blockIdentifier = new ResourceLocation(this.registration.getModid(), this.identifier);
        if(this.hasRegularVariant && this.parent.hasRegularVariant)
            handler.registerBlock(this.identifier, () -> {
                Block parent = RechiseledCommonBlockBuilderImpl.this.parent == RechiseledCommonBlockBuilderImpl.this ? null : RechiseledCommonBlockBuilderImpl.this.parent.getRegularBlock();
                regularBlockHolder.set(this.createBlock(specification, parent, false, propertiesHolder.get(), blockIdentifier));
                return regularBlockHolder.get();
            });
        if(this.hasConnectingVariant && this.parent.hasConnectingVariant)
            handler.registerBlock(this.identifier + "_connecting", () -> {
                Block parent = RechiseledCommonBlockBuilderImpl.this.parent == RechiseledCommonBlockBuilderImpl.this ? null : RechiseledCommonBlockBuilderImpl.this.parent.getConnectingBlock();
                connectingBlockHolder.set(this.createBlock(specification, parent, true, propertiesHolder.get(), blockIdentifier.withSuffix("_connecting")));
                return connectingBlockHolder.get();
            });

        // Create holders to put the items in once they have been registered.
        Holder<BlockItem> regularItemHolder = new Holder<>();
        Holder<BlockItem> connectingItemHolder = new Holder<>();
        ItemProperties itemProperties = ItemProperties.create();
        if(this.itemGroups != null)
            this.itemGroups.forEach(itemProperties::group);
        if(this.hasRegularVariant)
            handler.registerItem(this.identifier, () -> {
                regularItemHolder.set(new BaseBlockItem(regularBlockHolder.get(), itemProperties));
                return regularItemHolder.get();
            });
        if(this.hasConnectingVariant)
            handler.registerItem(this.identifier + "_connecting", () -> {
                connectingItemHolder.set(new BaseBlockItem(connectingBlockHolder.get(), itemProperties) {
                    @Override
                    protected void appendItemInformation(ItemStack stack, @Nullable BlockGetter level, Consumer<Component> info, boolean advanced){
                        super.appendItemInformation(stack, level, info, advanced);
                        info.accept(TextComponents.translation("rechiseled.tooltip.connecting").color(ChatFormatting.GRAY).get());
                    }
                });
                return connectingItemHolder.get();
            });

        this.regularBlock = regularBlockHolder::get;
        this.connectingBlock = connectingBlockHolder::get;
        this.regularItem = regularItemHolder::get;
        this.connectingItem = connectingItemHolder::get;

        // Set the render type for transparent blocks
        if(specification == BlockSpecification.GLASS || specification == BlockSpecification.GLASS_PILLAR){
            if(CommonUtils.getEnvironmentSide().isClient()){
                if(this.hasRegularVariant)
                    ClientRegistrationHandler.get(this.registration.getModid()).registerBlockModelTranslucentRenderType(this.regularBlock);
                if(this.hasConnectingVariant)
                    ClientRegistrationHandler.get(this.registration.getModid()).registerBlockModelTranslucentRenderType(this.connectingBlock);
            }
        }
    }

    protected abstract Block createBlock(BlockSpecification specification, Block parent, boolean connecting, BlockProperties properties, ResourceLocation identifier);

    protected abstract void setBlockReferences(Block regularBlock, Block regularStairs, Block regularSlab, Block connectingBlock, Block connectingStairs, Block connectingSlab);

    @Override
    public T properties(BlockProperties properties){
        this.checkMutable();
        this.properties = () -> properties;
        return this.self();
    }

    @Override
    public T properties(Supplier<BlockProperties> properties){
        this.checkMutable();
        this.properties = properties;
        return this.self();
    }

    @Override
    public T properties(Consumer<BlockProperties> configurer){
        this.checkMutable();
        this.propertiesConsumer = configurer;
        return this.self();
    }

    @Override
    public T itemGroups(CreativeModeTab group, CreativeModeTab... groups){
        this.checkMutable();
        List<CreativeModeTab> tabs = new ArrayList<>(Arrays.asList(groups));
        tabs.add(group);
        this.itemGroups = Set.copyOf(tabs);
        return this.self();
    }

    @Override
    public T noItemGroups(){
        this.itemGroups = null;
        return this.self();
    }

    @Override
    public T noRegularVariant(){
        this.checkMutable();
        this.hasRegularVariant = false;
        return this.self();
    }

    @Override
    public T noConnectingVariant(){
        this.checkMutable();
        this.hasConnectingVariant = false;
        return this.self();
    }

    @Override
    public T regularVariant(Supplier<Block> block){
        this.noRegularVariant();
        this.customRegularVariant = block;
        return this.self();
    }

    @Override
    public T connectingVariant(Supplier<Block> block){
        this.noConnectingVariant();
        this.customConnectingVariant = block;
        return this.self();
    }

    @Override
    public T blockTag(ResourceLocation identifier){
        this.checkMutable();
        this.blockTags.add(identifier);
        return this.self();
    }

    @Override
    public T itemTag(ResourceLocation identifier){
        this.checkMutable();
        this.itemTags.add(identifier);
        return this.self();
    }

    @Override
    public T miningTagsFrom(Supplier<Block> block){
        this.checkMutable();
        this.miningTagsFromBlock = block;
        return this.self();
    }

    @Override
    public T translation(String translation){
        this.checkMutable();
        this.translation = translation;
        return this.self();
    }

    private T self(){
        //noinspection unchecked
        return (T)this;
    }
}
