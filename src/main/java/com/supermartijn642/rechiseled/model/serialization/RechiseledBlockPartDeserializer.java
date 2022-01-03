package com.supermartijn642.rechiseled.model.serialization;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.core.Direction;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledBlockPartDeserializer {

    public static BlockElement deserialize(JsonObject json, JsonDeserializationContext context){
        Vector3f from = getVector3f(json, "from", true);
        Vector3f to = getVector3f(json, "to", true);
        BlockElementRotation rotation = getRotation(json);
        Map<Direction,BlockElementFace> faces = getFaces(context, json);
        if(json.has("shade") && !GsonHelper.isBooleanValue(json, "shade"))
            throw new JsonParseException("Expected shade to be a Boolean");
        boolean shading = GsonHelper.getAsBoolean(json, "shade", true);
        return new BlockElement(from, to, faces, rotation, shading);
    }

    @Nullable
    private static BlockElementRotation getRotation(JsonObject json){
        BlockElementRotation partRotation = null;
        if(json.has("rotation")){
            JsonObject rotation = GsonHelper.getAsJsonObject(json, "rotation");

            // origin
            Vector3f origin = getVector3f(rotation, "origin", false);
            origin.mul(0.0625f);

            // axis
            String axisName = GsonHelper.getAsString(json, "axis");
            Direction.Axis axis = Direction.Axis.byName(axisName.toLowerCase(Locale.ROOT));
            if(axis == null)
                throw new JsonParseException("Invalid rotation axis: " + axisName);

            // angle
            float angle = GsonHelper.getAsFloat(json, "angle");
            if(angle != 0 && Mth.abs(angle) != 22.5F && Mth.abs(angle) != 45)
                throw new JsonParseException("Invalid rotation " + angle + " found, only -45/-22.5/0/22.5/45 allowed");

            // scale
            boolean scale = GsonHelper.getAsBoolean(rotation, "rescale", false);

            partRotation = new BlockElementRotation(origin, axis, angle, scale);
        }

        return partRotation;
    }

    private static Map<Direction,BlockElementFace> getFaces(JsonDeserializationContext context, JsonObject json){
        Map<Direction,BlockElementFace> faces = Maps.newEnumMap(Direction.class);
        JsonObject object = GsonHelper.getAsJsonObject(json, "faces");

        for(Map.Entry<String,JsonElement> entry : object.entrySet()){
            Direction direction = Direction.byName(entry.getKey());
            if(direction == null)
                throw new JsonParseException("Unknown facing: " + entry.getKey());

            BlockElementFace face = RechiseledBlockPartFaceDeserializer.deserialize(entry.getValue().getAsJsonObject(), context);
            faces.put(direction, face);
        }

        if(faces.isEmpty())
            throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");

        return faces;
    }

    private static Vector3f getVector3f(JsonObject json, String key, boolean restricted){
        JsonArray jsonarray = GsonHelper.getAsJsonArray(json, key);
        if(jsonarray.size() != 3)
            throw new JsonParseException("Expected 3 " + key + " values, found: " + jsonarray.size());

        float x = GsonHelper.convertToFloat(jsonarray.get(0), key + "[0]");
        float y = GsonHelper.convertToFloat(jsonarray.get(1), key + "[1]");
        float z = GsonHelper.convertToFloat(jsonarray.get(2), key + "[2]");
        if(restricted && (x < -16 || y < -16 || z < -16 || x > 32 || y > 32 || z > 32))
            throw new JsonParseException("'" + key + "' specifier exceeds the allowed boundaries: [" + x + ", " + y + ", " + z + "]");

        return new Vector3f(x, y, z);
    }
}
