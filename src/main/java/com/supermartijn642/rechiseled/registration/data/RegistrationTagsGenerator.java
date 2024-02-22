package com.supermartijn642.rechiseled.registration.data;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockBuilderImpl;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockTypeImpl;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.fabricmc.fabric.impl.resource.loader.FabricModResourcePack;
import net.fabricmc.fabric.impl.resource.loader.GroupResourcePack;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackCreator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created 04/05/2023 by SuperMartijn642
 */
public class RegistrationTagsGenerator extends TagGenerator {

    private static final Gson GSON = new GsonBuilder().create();
    private static final List<PackResources> ALL_DATA_PACKS;

    static{
        Field packsField;
        try{
            packsField = GroupResourcePack.class.getDeclaredField("packs");
            packsField.setAccessible(true);
        }catch(NoSuchFieldException e){
            throw new RuntimeException(e);
        }

        List<PackResources> packs = new ArrayList<>();
        packs.add(ServerPacksSource.createVanillaPackSource());
        new ModResourcePackCreator(PackType.SERVER_DATA).loadPacks(pack -> {
            try{
                packs.add(pack.open());
            }catch(Exception e){
                Rechiseled.LOGGER.info("Encountered an exception whilst loading data packs for 'RegistrationTagsGenerator'!", e);
            }
        });
        ALL_DATA_PACKS = packs.stream()
            .flatMap(pack -> {
                if(pack instanceof FabricModResourcePack){
                    try{
                        //noinspection unchecked
                        return ((List<? extends PackResources>)packsField.get(pack)).stream();
                    }catch(IllegalAccessException e){
                        throw new RuntimeException(e);
                    }
                }
                return Stream.of(pack);
            })
            .toList();
    }

    private final RechiseledRegistrationImpl registration;
    private final ResourceManager resources;

    public RegistrationTagsGenerator(RechiseledRegistrationImpl registration, ResourceCache cache){
        super(registration.getModid(), cache);
        this.registration = registration;
        this.resources = new MultiPackResourceManager(
            PackType.SERVER_DATA,
            ALL_DATA_PACKS.stream()
                .filter(pack -> !(pack instanceof ModResourcePack) || !((ModResourcePack)pack).getFabricModMetadata().getId().equals(registration.getModid()))
                .toList()
        );
    }

    @Override
    public void generate(){
        if(!this.registration.providersRegistered)
            return;
        this.registration.getBlockBuilders().forEach(
            pair -> {
                RechiseledBlockBuilderImpl builder = pair.left();
                RechiseledBlockTypeImpl type = pair.right();
                if(type.hasRegularVariant())
                    this.addTags(builder, type.getRegularBlock());
                if(type.hasConnectingVariant())
                    this.addTags(builder, type.getConnectingBlock());
            }
        );
    }

    private void addTags(RechiseledBlockBuilderImpl builder, Block block){
        builder.tags.stream().map(this::blockTag).forEach(tag -> tag.add(block));
        if(builder.miningTagsFromBlock != null)
            this.getTagsForBlock(builder.miningTagsFromBlock).stream().map(this::blockTag).forEach(tag -> tag.add(block));
    }

    private Set<ResourceLocation> getTagsForBlock(Supplier<Block> block){
        return Stream.of(
                BlockTags.MINEABLE_WITH_AXE,
                BlockTags.MINEABLE_WITH_HOE,
                BlockTags.MINEABLE_WITH_PICKAXE,
                BlockTags.MINEABLE_WITH_SHOVEL,
                BlockTags.NEEDS_STONE_TOOL,
                BlockTags.NEEDS_IRON_TOOL,
                BlockTags.NEEDS_DIAMOND_TOOL
            )
            .map(TagKey::location)
            .filter(tag -> this.loadVanillaTag(tag).contains(block.get()))
            .collect(Collectors.toSet());
    }

    private final Map<ResourceLocation,List<Block>> loadedTags = Maps.newHashMap();

    private List<Block> loadVanillaTag(ResourceLocation location){
        if(this.loadedTags.containsKey(location))
            return this.loadedTags.get(location);

        List<Block> blocks = new ArrayList<>();

        ResourceLocation tagLocation = new ResourceLocation(location.getNamespace(), "tags/blocks/" + location.getPath() + ".json");
        for(Resource resource : this.resources.getResourceStack(tagLocation)){
            try(InputStream stream = resource.open()){
                JsonObject json = GSON.fromJson(new InputStreamReader(stream), JsonObject.class);
                JsonArray array = json.getAsJsonArray("values");
                for(JsonElement element : array){
                    String name = element.getAsString();
                    if(name.charAt(0) == '#'){
                        blocks.addAll(this.loadVanillaTag(new ResourceLocation(name.substring(1))));
                        continue;
                    }
                    ResourceLocation registryName = new ResourceLocation(name);
                    Block block = Registries.BLOCKS.getValue(registryName);
                    if(block == null)
                        throw new JsonParseException("Unknown block '" + registryName + "' in '" + location + "'");
                    blocks.add(block);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        this.loadedTags.put(location, blocks);
        return blocks;
    }

    @Override
    public String getName(){
        return "Registration Tag Generator: " + this.modName;
    }
}
