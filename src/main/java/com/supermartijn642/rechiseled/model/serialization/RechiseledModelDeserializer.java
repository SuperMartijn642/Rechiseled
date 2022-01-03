package com.supermartijn642.rechiseled.model.serialization;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.supermartijn642.rechiseled.model.RechiseledModel;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
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
        Map<String,Either<RenderMaterial,String>> map = getTextureMap(json);
        boolean ambientOcclusion = JSONUtils.getAsBoolean(json, "ambientocclusion", true);
        ItemCameraTransforms cameraTransforms = ItemCameraTransforms.NO_TRANSFORMS;
        if(json.has("display")){
            JsonObject transform = JSONUtils.getAsJsonObject(json, "display");
            cameraTransforms = context.deserialize(transform, ItemCameraTransforms.class);
        }
        List<ItemOverride> overrides = getOverrides(context, json);
        BlockModel.GuiLight guiLighting = null;
        if(json.has("gui_light"))
            guiLighting = BlockModel.GuiLight.getByName(JSONUtils.getAsString(json, "gui_light"));

        return new RechiseledModel(shouldConnect, parent, elements, map, ambientOcclusion, guiLighting, cameraTransforms, overrides);
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

    private static Map<String,Either<RenderMaterial,String>> getTextureMap(JsonObject json){
        ResourceLocation blockAtlas = AtlasTexture.LOCATION_BLOCKS;
        Map<String,Either<RenderMaterial,String>> map = Maps.newHashMap();
        if(json.has("textures")){
            JsonObject textures = JSONUtils.getAsJsonObject(json, "textures");

            for(Map.Entry<String,JsonElement> entry : textures.entrySet())
                map.put(entry.getKey(), parseTextureLocationOrReference(blockAtlas, entry.getValue().getAsString()));
        }

        return map;
    }

    private static Either<RenderMaterial,String> parseTextureLocationOrReference(ResourceLocation atlas, String reference){
        if(reference.charAt(0) == '#')
            return Either.right(reference.substring(1));
        else{
            ResourceLocation resourcelocation = ResourceLocation.tryParse(reference);
            if(resourcelocation == null)
                throw new JsonParseException(reference + " is not valid resource location");
            else
                return Either.left(new RenderMaterial(atlas, resourcelocation));
        }
    }
}
