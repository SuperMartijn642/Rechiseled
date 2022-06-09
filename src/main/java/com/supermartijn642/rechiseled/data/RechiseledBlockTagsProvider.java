package com.supermartijn642.rechiseled.data;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.supermartijn642.rechiseled.RechiseledBlockType;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created 05/01/2022 by SuperMartijn642
 */
public class RechiseledBlockTagsProvider extends BlockTagsProvider {

    private static final Gson GSON = new GsonBuilder().create();

    public RechiseledBlockTagsProvider(GatherDataEvent e){
        super(e.getGenerator(), "rechiseled", e.getExistingFileHelper());
    }

    @Override
    protected void addTags(){
        List<ResourceLocation> tags = Stream.of(
            BlockTags.MINEABLE_WITH_AXE,
            BlockTags.MINEABLE_WITH_HOE,
            BlockTags.MINEABLE_WITH_PICKAXE,
            BlockTags.MINEABLE_WITH_SHOVEL,
            BlockTags.NEEDS_STONE_TOOL,
            BlockTags.NEEDS_IRON_TOOL,
            BlockTags.NEEDS_DIAMOND_TOOL
        ).map(TagKey::location).collect(Collectors.toList());
        Map<Predicate<Block>,TagAppender<Block>> tagAppenderMap = Maps.newHashMap();
        tags.forEach(tag -> tagAppenderMap.put(this.loadVanillaTag(tag)::contains, this.tag(BlockTags.create(tag))));

        for(RechiseledBlockType type : RechiseledBlockType.values()){
            for(Map.Entry<Predicate<Block>,TagAppender<Block>> entry : tagAppenderMap.entrySet()){
                if(entry.getKey().test(type.parentBlock.get())){
                    if(type.hasCreatedRegularBlock())
                        entry.getValue().add(type.getRegularBlock());
                    if(type.hasCreatedConnectingBlock())
                        entry.getValue().add(type.getConnectingBlock());
                }
            }
        }
    }

    private final Map<ResourceLocation,List<Block>> loadedTags = Maps.newHashMap();

    private List<Block> loadVanillaTag(ResourceLocation location){
        if(this.loadedTags.containsKey(location))
            return this.loadedTags.get(location);

        try(InputStream resource = this.existingFileHelper.getResource(location, PackType.SERVER_DATA, ".json", "tags/blocks").open()){
            JsonObject json = GSON.fromJson(new InputStreamReader(resource), JsonObject.class);
            JsonArray array = json.getAsJsonArray("values");
            List<Block> blocks = new ArrayList<>();
            for(JsonElement element : array){
                String name = element.getAsString();
                if(name.charAt(0) == '#'){
                    blocks.addAll(this.loadVanillaTag(new ResourceLocation(name.substring(1))));
                    continue;
                }
                ResourceLocation registryName = new ResourceLocation(name);
                Block block = ForgeRegistries.BLOCKS.getValue(registryName);
                if(block == null)
                    throw new JsonParseException("Unknown block '" + registryName + "' in '" + location + "'");
                blocks.add(block);
            }
            this.loadedTags.put(location, blocks);
            return blocks;
        }catch(Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
