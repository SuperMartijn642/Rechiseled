package com.supermartijn642.rechiseled.data;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.RechiseledBlockType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fmllegacy.DatagenModLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created 05/01/2022 by SuperMartijn642
 */
public class RechiseledBlockTagsGenerator extends TagGenerator {

    private static final Supplier<ResourceManager> SERVER_DATA_FIELD;

    static{
        try{
            Field existingFileHelper = DatagenModLoader.class.getDeclaredField("existingFileHelper");
            existingFileHelper.setAccessible(true);
            Field serverData = ExistingFileHelper.class.getDeclaredField("serverData");
            serverData.setAccessible(true);
            SERVER_DATA_FIELD = () -> {
                try{
                    return (ResourceManager)serverData.get(existingFileHelper.get(null));
                }catch(IllegalAccessException e){
                    throw new RuntimeException(e);
                }
            };
        }catch(NoSuchFieldException e){
            throw new RuntimeException(e);
        }
    }

    private static final Gson GSON = new GsonBuilder().create();

    public RechiseledBlockTagsGenerator(ResourceCache cache){
        super("rechiseled", cache);
    }

    @Override
    public void generate(){
        List<BiConsumer<Block,Block>> tags = Stream.of(
                BlockTags.MINEABLE_WITH_AXE,
                BlockTags.MINEABLE_WITH_HOE,
                BlockTags.MINEABLE_WITH_PICKAXE,
                BlockTags.MINEABLE_WITH_SHOVEL,
                BlockTags.NEEDS_STONE_TOOL,
                BlockTags.NEEDS_IRON_TOOL,
                BlockTags.NEEDS_DIAMOND_TOOL
            )
            .map(Tag.Named::getName)
            .map(tag -> (BiConsumer<Block,Block>)(parent, block) -> {
                if(this.loadVanillaTag(tag).contains(parent))
                    this.blockTag(tag).add(block);
            }).toList();

        for(RechiseledBlockType type : RechiseledBlockType.values()){
            if(type.hasCreatedRegularBlock())
                tags.forEach(consumer -> consumer.accept(type.parentBlock.get(), type.getRegularBlock()));
            if(type.hasCreatedConnectingBlock())
                tags.forEach(consumer -> consumer.accept(type.parentBlock.get(), type.getConnectingBlock()));
        }
    }

    private final Map<ResourceLocation,List<Block>> loadedTags = Maps.newHashMap();

    private List<Block> loadVanillaTag(ResourceLocation location){
        if(this.loadedTags.containsKey(location))
            return this.loadedTags.get(location);

        List<Block> blocks = new ArrayList<>();

        ResourceManager resourceManager = SERVER_DATA_FIELD.get();
        try{
            for(Resource resource : resourceManager.getResources(new ResourceLocation(location.getNamespace(), "tags/blocks/" + location.getPath() + ".json"))){
                try(InputStream stream = resource.getInputStream()){
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
        }catch(IOException e){
            throw new RuntimeException(e);
        }

        this.loadedTags.put(location, blocks);
        return blocks;
    }
}
