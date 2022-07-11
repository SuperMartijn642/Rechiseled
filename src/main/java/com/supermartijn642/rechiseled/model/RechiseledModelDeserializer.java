package com.supermartijn642.rechiseled.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.List;
import java.util.Map;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledModelDeserializer {

    public static RechiseledModel deserialize(JsonObject json, JsonDeserializationContext context){
        // Get whether the model's textures should connect
        boolean shouldConnect = GsonHelper.getAsBoolean(json, "should_connect", false);

        // Deserialize the texture map
        Map<String,Either<Pair<Material,Boolean>,String>> map = getTextureMap(json);

        // Deserialize vanilla stuff
        List<BlockElement> elements = deserializeElements(json, context);
        String parentString = GsonHelper.getAsString(json, "parent", "");
        ResourceLocation parent = parentString.isEmpty() ? null : new ResourceLocation(parentString);
        boolean ambientOcclusion = GsonHelper.getAsBoolean(json, "ambientocclusion", true);
        ItemTransforms cameraTransforms = ItemTransforms.NO_TRANSFORMS;
        if(json.has("display")){
            JsonObject transform = GsonHelper.getAsJsonObject(json, "display");
            cameraTransforms = context.deserialize(transform, ItemTransforms.class);
        }
        List<ItemOverride> overrides = getOverrides(context, json);
        BlockModel.GuiLight guiLighting = null;
        if(json.has("gui_light"))
            guiLighting = BlockModel.GuiLight.getByName(GsonHelper.getAsString(json, "gui_light"));
        ResourceLocation renderTypeHint = json.has("render_type") ? new ResourceLocation(GsonHelper.getAsString(json, "render_type")) : null;

        return new RechiseledModel(shouldConnect, parent, elements, map, ambientOcclusion, guiLighting, cameraTransforms, overrides, renderTypeHint);
    }

    private static List<BlockElement> deserializeElements(JsonObject json, JsonDeserializationContext context){
        List<BlockElement> parts = Lists.newArrayList();
        if(json.has("elements")){
            for(JsonElement element : GsonHelper.getAsJsonArray(json, "elements")){
                parts.add(context.deserialize(element, BlockElement.class));
            }
        }

        return parts;
    }

    private static List<ItemOverride> getOverrides(JsonDeserializationContext context, JsonObject json){
        List<ItemOverride> overrides = Lists.newArrayList();
        if(json.has("overrides")){
            for(JsonElement jsonelement : GsonHelper.getAsJsonArray(json, "overrides")){
                overrides.add(context.deserialize(jsonelement, ItemOverride.class));
            }
        }

        return overrides;
    }

    private static Map<String,Either<Pair<Material,Boolean>,String>> getTextureMap(JsonObject json){
        ResourceLocation blockAtlas = TextureAtlas.LOCATION_BLOCKS;
        Map<String,Either<Pair<Material,Boolean>,String>> map = Maps.newHashMap();
        if(json.has("model_textures")){
            JsonObject textures = GsonHelper.getAsJsonObject(json, "model_textures");

            for(Map.Entry<String,JsonElement> entry : textures.entrySet()){
                if(entry.getValue().isJsonObject())
                    map.put(entry.getKey(), parseTextureLocationOrReference(blockAtlas, GsonHelper.getAsString(entry.getValue().getAsJsonObject(), "location"), GsonHelper.getAsBoolean(entry.getValue().getAsJsonObject(), "connecting", false)));
                else
                    map.put(entry.getKey(), parseTextureLocationOrReference(blockAtlas, entry.getValue().getAsString(), false));
            }
        }

        return map;
    }

    private static Either<Pair<Material,Boolean>,String> parseTextureLocationOrReference(ResourceLocation atlas, String reference, boolean connecting){
        if(reference.charAt(0) == '#')
            return Either.right(reference.substring(1));
        else{
            ResourceLocation resourcelocation = ResourceLocation.tryParse(reference);
            if(resourcelocation == null)
                throw new JsonParseException(reference + " is not valid resource location");
            else
                return Either.left(Pair.of(new Material(atlas, resourcelocation), connecting));
        }
    }
}
