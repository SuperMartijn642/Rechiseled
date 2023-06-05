package com.supermartijn642.rechiseled.registration;

import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistryUtil;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.core.util.Triple;
import com.supermartijn642.rechiseled.api.blocks.RechiseledBlockBuilder;
import com.supermartijn642.rechiseled.api.blocks.RechiseledBlockType;
import com.supermartijn642.rechiseled.api.registration.RechiseledRegistration;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockBuilderImpl;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockTypeImpl;
import com.supermartijn642.rechiseled.registration.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.*;
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

    private final String modid;
    private final List<RechiseledBlockBuilderImpl> unfinishedBlockBuilders = new ArrayList<>();
    private final List<Pair<RechiseledBlockBuilderImpl,RechiseledBlockTypeImpl>> blockBuilders = new ArrayList<>();
    private final Set<String> usedBlockIdentifiers = new HashSet<>();
    private final List<RechiseledBlockTypeImpl> blockTypes = new ArrayList<>();
    private final List<Triple<ResourceLocation,Supplier<IItemProvider>,Supplier<IItemProvider>>> chiselingEntries = new ArrayList<>();
    private CreativeItemGroup itemGroup;
    private String itemGroupTranslation;
    public boolean providersRegistered = false;

    private RechiseledRegistrationImpl(String modid){
        this.modid = modid;
    }

    @Override
    public synchronized RechiseledBlockBuilder block(String identifier){
        if(finalized)
            throw new RuntimeException("Blocks must be built during mod initialization!");
        if(!this.usedBlockIdentifiers.add(identifier))
            throw new RuntimeException("Duplicate block builder request from '" + this.modid + "' for identifier '" + identifier + "'!");

        RechiseledBlockBuilderImpl builder = new RechiseledBlockBuilderImpl(this, identifier);
        this.unfinishedBlockBuilders.add(builder);
        return builder;
    }

    @Override
    public synchronized void chiselingEntry(ResourceLocation recipe, Supplier<IItemProvider> regularItem, Supplier<IItemProvider> connectingItem){
        if(finalized)
            throw new RuntimeException("Chiseling recipe entries must be added during mod initialization!");
        if(regularItem == null && connectingItem == null)
            throw new IllegalArgumentException("Either regular item or connecting item must not be null!");
        this.chiselingEntries.add(Triple.of(recipe, regularItem, connectingItem));
    }

    @Override
    public CreativeItemGroup itemGroup(Supplier<IItemProvider> icon, String translation){
        if(finalized)
            throw new RuntimeException("Chiseling recipe entries must be added during mod initialization!");
        if(this.itemGroup != null)
            throw new IllegalStateException("An item group for '" + this.modid + "' registration has already been created!");
        this.itemGroup = CreativeItemGroup.create(this.modid, () -> icon.get().asItem());
        this.itemGroup.filler(stackConsumer -> {
            List<Item> items = new LinkedList<>();
            for(RechiseledBlockType type : this.getAllBlockTypes()){
                items.add(type.getRegularItem());
                items.add(type.getConnectingItem());
            }
            items.stream().filter(Objects::nonNull).map(ItemStack::new).forEach(stackConsumer);
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
        handler.addProvider(generator -> new RegistrationFusionModelProvider(this, generator));
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
        this.blockBuilders.add(Pair.of(builder, blockType));
        this.blockTypes.add(blockType);
    }

    public List<Pair<RechiseledBlockBuilderImpl,RechiseledBlockTypeImpl>> getBlockBuilders(){
        return this.blockBuilders;
    }

    public List<Triple<ResourceLocation,Supplier<IItemProvider>,Supplier<IItemProvider>>> getChiselingEntries(){
        return this.chiselingEntries;
    }

    public CreativeItemGroup getItemGroup(){
        return this.itemGroup;
    }

    public String getItemGroupTranslation(){
        return this.itemGroupTranslation;
    }
}
