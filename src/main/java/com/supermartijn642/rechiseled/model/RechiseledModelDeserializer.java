package com.supermartijn642.rechiseled.model;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.supermartijn642.core.render.TextureAtlases;
import com.supermartijn642.core.util.Either;
import com.supermartijn642.core.util.Pair;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.Map;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
@Deprecated
public class RechiseledModelDeserializer {

    public static RechiseledModel deserialize(JsonObject json, JsonDeserializationContext context){
        // Get whether the model's textures should connect
        boolean shouldConnect = GsonHelper.getAsBoolean(json, "should_connect", false);

        // Deserialize the texture map
        Map<String,Either<Pair<Material,Boolean>,String>> textures = getTextureMap(json);

        return new RechiseledModel(shouldConnect, textures);
    }

    private static Map<String,Either<Pair<Material,Boolean>,String>> getTextureMap(JsonObject json){
        ResourceLocation blockAtlas = TextureAtlases.getBlocks();
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
