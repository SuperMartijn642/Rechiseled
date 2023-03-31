package com.supermartijn642.rechiseled.data;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.RechiseledBlockType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * Created 05/01/2022 by SuperMartijn642
 */
public class RechiseledBlockTagsGenerator extends TagGenerator {

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
            .map(TagKey::location)
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

        ResourceLocation tagLocation = new ResourceLocation(location.getNamespace(), "tags/blocks/" + location.getPath() + ".json");
        try(VanillaPackResources resources = new VanillaPackResources(ServerPacksSource.BUILT_IN_METADATA, ServerPacksSource.VANILLA_ID)){
            try(InputStream stream = resources.getResource(PackType.SERVER_DATA, tagLocation)){
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
}
