package com.supermartijn642.rechiseled.model.serialization;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.supermartijn642.rechiseled.model.RechiseledModel;
import net.minecraft.client.renderer.model.BlockPart;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverride;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledModelDeserializer {

    public static RechiseledModel deserialize(JsonObject json, JsonDeserializationContext context){
        // Get whether the model's textures should connect
        boolean shouldConnect = JSONUtils.getAsBoolean(json, "should_connect", false);

        // Deserialize the block parts
        List<BlockPart> elements = deserializeElements(json, context);

        // Deserialize vanilla stuff
        String parentString = JSONUtils.getAsString(json, "parent", "");
        ResourceLocation parent = parentString.isEmpty() ? null : new ResourceLocation(parentString);
        Map<String,String> map = getTextureMap(json);
        boolean ambientOcclusion = JSONUtils.getAsBoolean(json, "ambientocclusion", true);
        ItemCameraTransforms cameraTransforms = ItemCameraTransforms.NO_TRANSFORMS;
        if(json.has("display")){
            JsonObject transform = JSONUtils.getAsJsonObject(json, "display");
            cameraTransforms = context.deserialize(transform, ItemCameraTransforms.class);
        }
        List<ItemOverride> overrides = getOverrides(context, json);
        boolean gui3d = JSONUtils.getAsBoolean(json, "gui_3d", true);

        return new RechiseledModel(shouldConnect, parent, elements, map, ambientOcclusion, gui3d, cameraTransforms, overrides);
    }

    private static List<BlockPart> deserializeElements(JsonObject json, JsonDeserializationContext context){
        List<BlockPart> parts = Lists.newArrayList();
        if(json.has("elements")){
            for(JsonElement element : JSONUtils.getAsJsonArray(json, "elements")){
                parts.add(RechiseledBlockPartDeserializer.deserialize(element.getAsJsonObject(), context));
            }
        }

        return parts;
    }

    private static List<ItemOverride> getOverrides(JsonDeserializationContext context, JsonObject json){
        List<ItemOverride> overrides = Lists.newArrayList();
        if(json.has("overrides")){
            for(JsonElement jsonelement : JSONUtils.getAsJsonArray(json, "overrides")){
                overrides.add(context.deserialize(jsonelement, ItemOverride.class));
            }
        }

        return overrides;
    }

    private static Map<String,String> getTextureMap(JsonObject json){
        Map<String,String> map = Maps.newHashMap();
        if(json.has("textures")){
            JsonObject textures = JSONUtils.getAsJsonObject(json, "textures");

            for(Map.Entry<String,JsonElement> entry : textures.entrySet())
                map.put(entry.getKey(), entry.getValue().getAsString());
        }

        return map;
    }
}
