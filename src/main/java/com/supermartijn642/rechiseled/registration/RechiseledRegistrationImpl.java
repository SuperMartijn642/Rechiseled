package com.supermartijn642.rechiseled.registration;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistryUtil;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.rechiseled.api.blocks.RechiseledBlockBuilder;
import com.supermartijn642.rechiseled.api.blocks.RechiseledBlockType;
import com.supermartijn642.rechiseled.api.chiseling.data.ChiselingEntryBuilder;
import com.supermartijn642.rechiseled.api.registration.RechiseledRegistration;
import com.supermartijn642.rechiseled.registration.data.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created 26/04/2023 by SuperMartijn642
 */
public class RechiseledRegistrationImpl implements RechiseledRegistration {

    private static final Map<String,RechiseledRegistrationImpl> REGISTRATION_MAP = new HashMap<>();
    private static boolean finalized = false;

    public static synchronized RechiseledRegistration get(String modid){
        if(!RegistryUtil.isValidNamespace(modid))
            throw new RuntimeException("Invalid modid '" + modid + "'!");
        if(!CommonUtils.isModLoaded(modid))
            throw new RuntimeException("Could not find any mod for modid '" + modid + "'!");
        return REGISTRATION_MAP.computeIfAbsent(modid, RechiseledRegistrationImpl::new);
    }

    public static void finalizeRegistration(){ // TODO
        finalized = true;
        // Report unfinished block builders
        REGISTRATION_MAP.values().forEach(registration -> {
            if(!registration.unfinishedBlockBuilders.isEmpty()){
                if(registration.unfinishedBlockBuilders.size() == 1)
                    throw new IllegalStateException("Found uncompleted rechiseled block builder '" + registration.modid + ":" + registration.unfinishedBlockBuilders.get(0).getIdentifier() + "'!");
                else{
                    String builders = registration.unfinishedBlockBuilders.stream().map(b -> "'" + registration.modid + ":" + b.getIdentifier() + "'").collect(Collectors.joining(","));
                    throw new IllegalStateException("Found uncompleted rechiseled block builders " + builders + "!");
                }
            }
        });
    }

    private static void checkNotFinalized(){
        if(finalized)
            throw new IllegalStateException("Registration must be configured during mod initialization!");
    }

    private final String modid;
    private final List<RechiseledBlockBuilderImpl> unfinishedBlockBuilders = new ArrayList<>();
    private final List<RechiseledBlockBuilderImpl> blockBuilders = new ArrayList<>();
    private final Set<String> usedBlockIdentifiers = new HashSet<>();
    private final List<RechiseledBlockTypeImpl> blockTypes = new ArrayList<>();
    private final List<Pair<ResourceLocation,Consumer<ChiselingEntryBuilder>>> chiselingEntries = new ArrayList<>();
    private CreativeItemGroup itemGroup;
    private String itemGroupTranslation;
    public boolean providersRegistered = false;

    private RechiseledRegistrationImpl(String modid){
        this.modid = modid;
    }

    @Override
    public RechiseledBlockBuilder block(String identifier){
        checkNotFinalized();
        if(!this.usedBlockIdentifiers.add(identifier))
            throw new RuntimeException("Duplicate block builder request from '" + this.modid + "' for identifier '" + identifier + "'!");

        RechiseledBlockBuilderImpl builder = new RechiseledBlockBuilderImpl(this, identifier);
        this.unfinishedBlockBuilders.add(builder);
        return builder;
    }

    @Override
    public void chiselingEntry(ResourceLocation recipe, Consumer<ChiselingEntryBuilder> builder){
        checkNotFinalized();
        Objects.requireNonNull(recipe);
        this.chiselingEntries.add(Pair.of(recipe, builder));
    }

    @Override
    public void chiselingEntry(ResourceLocation recipe, Supplier<ItemLike> regularBlock, Supplier<ItemLike> connectingBlock){
        if(regularBlock == null && connectingBlock == null)
            throw new IllegalArgumentException("Entry must have at least one item!");
        this.chiselingEntry(recipe, entry -> {
            if(regularBlock != null)
                entry.regularBlock(regularBlock.get());
            if(connectingBlock != null)
                entry.connectingBlock(connectingBlock.get());
        });
    }

    @Override
    public CreativeItemGroup itemGroup(Supplier<ItemLike> icon, String translation){
        checkNotFinalized();
        if(this.itemGroup != null)
            throw new IllegalStateException("An item group for '" + this.modid + "' registration has already been created!");
        this.itemGroup = CreativeItemGroup.create(this.modid, () -> icon.get().asItem());
        this.itemGroup.filler(stackConsumer -> {
            List<Item> items = new LinkedList<>();
            for(RechiseledBlockType type : this.getAllBlockTypes()){
                if(type.hasRegularVariant()) items.add(type.getRegularItem());
                if(type.hasConnectingVariant()) items.add(type.getConnectingItem());
                if(type.hasRegularStairs()) items.add(type.getRegularStairsItem());
                if(type.hasConnectingStairs()) items.add(type.getConnectingStairsItem());
                if(type.hasRegularSlab()) items.add(type.getRegularSlabItem());
                if(type.hasConnectingSlab()) items.add(type.getConnectingSlabItem());
            }
            items.stream().map(ItemStack::new).forEach(stackConsumer);
        });
        this.itemGroupTranslation = translation;
        return this.itemGroup;
    }

    @Override
    public Collection<RechiseledBlockType> getAllBlockTypes(){
        return Collections.unmodifiableCollection(this.blockTypes);
    }

    @Override
    public void registerDataProviders(){
        if(this.providersRegistered)
            throw new RuntimeException("Data providers have already been registered!");
        this.providersRegistered = true;

        // Register data providers
        GeneratorRegistrationHandler handler = GeneratorRegistrationHandler.get(this.modid);
        handler.addProvider(generator -> new RegistrationFusionModelProvider(this, generator.getPackOutput()));
        handler.addGenerator(cache -> new RegistrationModelGenerator(this, cache));
        handler.addGenerator(cache -> new RegistrationBlockStateGenerator(this, cache));
        handler.addProvider((generator, existingFileHelper) -> new RegistrationChiselingRecipeProvider(this, generator, existingFileHelper));
        handler.addGenerator(cache -> new RegistrationLanguageGenerator(this, cache));
        handler.addGenerator(cache -> new RegistrationLootTableGenerator(this, cache));
        handler.addGenerator(cache -> new RegistrationTagsGenerator(this, cache));
    }

    public String getModid(){
        return this.modid;
    }

    public void finalizeBuilder(RechiseledBlockBuilderImpl builder, RechiseledBlockTypeImpl blockType){
        this.unfinishedBlockBuilders.remove(builder);
        this.blockBuilders.add(builder);
        this.blockTypes.add(blockType);
    }

    public List<RechiseledBlockBuilderImpl> getBlockBuilders(){
        return this.blockBuilders;
    }

    public List<Pair<ResourceLocation,Consumer<ChiselingEntryBuilder>>> getChiselingEntries(){
        return this.chiselingEntries;
    }

    public CreativeItemGroup getItemGroup(){
        return this.itemGroup;
    }

    public String getItemGroupTranslation(){
        return this.itemGroupTranslation;
    }
}
