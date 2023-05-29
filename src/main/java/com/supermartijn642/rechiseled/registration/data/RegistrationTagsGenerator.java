package com.supermartijn642.rechiseled.registration.data;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockBuilderImpl;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockTypeImpl;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.loading.DatagenModLoader;

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

    private static final Supplier<MultiPackResourceManager> SERVER_DATA_FIELD;

    static{
        try{
            Field existingFileHelper = DatagenModLoader.class.getDeclaredField("existingFileHelper");
            existingFileHelper.setAccessible(true);
            Field serverData = ExistingFileHelper.class.getDeclaredField("serverData");
            serverData.setAccessible(true);
            SERVER_DATA_FIELD = () -> {
                try{
                    return (MultiPackResourceManager)serverData.get(existingFileHelper.get(null));
                }catch(IllegalAccessException e){
                    throw new RuntimeException(e);
                }
            };
        }catch(NoSuchFieldException e){
            throw new RuntimeException(e);
        }
    }

    private static final Gson GSON = new GsonBuilder().create();

    private final RechiseledRegistrationImpl registration;

    public RegistrationTagsGenerator(RechiseledRegistrationImpl registration, ResourceCache cache){
        super(registration.getModid(), cache);
        this.registration = registration;
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

        MultiPackResourceManager resourceManager = SERVER_DATA_FIELD.get();
        for(Resource resource : resourceManager.getResourceStack(new ResourceLocation(location.getNamespace(), "tags/blocks/" + location.getPath() + ".json"))){
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
