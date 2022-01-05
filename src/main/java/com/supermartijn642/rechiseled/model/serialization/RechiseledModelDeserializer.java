package com.supermartijn642.rechiseled.model.serialization;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.supermartijn642.rechiseled.model.RechiseledModel;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledModelDeserializer {

    public static RechiseledModel deserialize(ResourceLocation modelLocation, JsonObject json, Gson context){
        // Get whether the model's textures should connect
        boolean shouldConnect = JsonUtils.getBoolean(json, "should_connect", false);

        // Deserialize the block parts
        List<BlockPart> elements = deserializeElements(json, context);

        // Deserialize vanilla stuff
        String parentString = JsonUtils.getString(json, "parent", "");
        ResourceLocation parent = parentString.isEmpty() ? null : new ResourceLocation(parentString);
        Map<String,String> map = getTextureMap(json);
        Boolean ambientOcclusion = JsonUtils.isBoolean(json, "ambientocclusion") ? JsonUtils.getBoolean(json, "ambientocclusion", true) : null;
        ItemCameraTransforms cameraTransforms = ItemCameraTransforms.DEFAULT;
        if(json.has("display")){
            JsonObject transform = JsonUtils.getJsonObject(json, "display");
            cameraTransforms = context.fromJson(transform, ItemCameraTransforms.class);
        }
        List<ItemOverride> overrides = getOverrides(json, context);
        Boolean gui3d = JsonUtils.isBoolean(json, "gui_3d") ? JsonUtils.getBoolean(json, "gui_3d", false) : null;

        return new RechiseledModel(modelLocation, shouldConnect, parent, elements, map, ambientOcclusion, gui3d, cameraTransforms, overrides);
    }

    private static List<BlockPart> deserializeElements(JsonObject json, Gson context){
        List<BlockPart> parts = Lists.newArrayList();
        if(json.has("elements")){
            for(JsonElement element : JsonUtils.getJsonArray(json, "elements")){
                parts.add(RechiseledBlockPartDeserializer.deserialize(element.getAsJsonObject(), context));
            }
        }

        return parts;
    }

    private static List<ItemOverride> getOverrides(JsonObject json, Gson context){
        if(json.has("overrides")){
            List<ItemOverride> overrides = Lists.newArrayList();
            for(JsonElement jsonelement : JsonUtils.getJsonArray(json, "overrides")){
                overrides.add(context.fromJson(jsonelement, ItemOverride.class));
            }
            return overrides;
        }
        return null;
    }

    private static Map<String,String> getTextureMap(JsonObject json){
        Map<String,String> map = Maps.newHashMap();
        if(json.has("textures")){
            JsonObject textures = JsonUtils.getJsonObject(json, "textures");

            for(Map.Entry<String,JsonElement> entry : textures.entrySet())
                map.put(entry.getKey(), entry.getValue().getAsString());
        }

        return map;
    }
}
