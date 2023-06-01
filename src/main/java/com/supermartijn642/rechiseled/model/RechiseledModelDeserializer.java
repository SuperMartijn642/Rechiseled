package com.supermartijn642.rechiseled.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
@Deprecated
public class RechiseledModelDeserializer {

    public static RechiseledModel deserialize(JsonObject json, JsonDeserializationContext context){
        // Get whether the model's textures should connect
        boolean shouldConnect = JSONUtils.getAsBoolean(json, "should_connect", false);

        // Deserialize the texture map
        Map<String,Either<Pair<RenderMaterial,Boolean>,String>> map = getTextureMap(json);

        // Deserialize vanilla stuff
        List<BlockPart> elements = deserializeElements(json, context);
        String parentString = JSONUtils.getAsString(json, "parent", "");
        ResourceLocation parent = parentString.isEmpty() ? null : new ResourceLocation(parentString);
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
                parts.add(context.deserialize(element, BlockPart.class));
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

    private static Map<String,Either<Pair<RenderMaterial,Boolean>,String>> getTextureMap(JsonObject json){
        ResourceLocation blockAtlas = AtlasTexture.LOCATION_BLOCKS;
        Map<String,Either<Pair<RenderMaterial,Boolean>,String>> map = Maps.newHashMap();
        if(json.has("model_textures")){
            JsonObject textures = JSONUtils.getAsJsonObject(json, "model_textures");

            for(Map.Entry<String,JsonElement> entry : textures.entrySet()){
                if(entry.getValue().isJsonObject())
                    map.put(entry.getKey(), parseTextureLocationOrReference(blockAtlas, JSONUtils.getAsString(entry.getValue().getAsJsonObject(), "location"), JSONUtils.getAsBoolean(entry.getValue().getAsJsonObject(), "connecting", false)));
                else
                    map.put(entry.getKey(), parseTextureLocationOrReference(blockAtlas, entry.getValue().getAsString(), false));
            }
        }

        return map;
    }

    private static Either<Pair<RenderMaterial,Boolean>,String> parseTextureLocationOrReference(ResourceLocation atlas, String reference, boolean connecting){
        if(reference.charAt(0) == '#')
            return Either.right(reference.substring(1));
        else{
            ResourceLocation resourcelocation = ResourceLocation.tryParse(reference);
            if(resourcelocation == null)
                throw new JsonParseException(reference + " is not valid resource location");
            else
                return Either.left(Pair.of(new RenderMaterial(atlas, resourcelocation), connecting));
        }
    }
}
