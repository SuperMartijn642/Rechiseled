package com.supermartijn642.rechiseled.model.serialization;

import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BlockPart;
import net.minecraft.client.renderer.model.BlockPartFace;
import net.minecraft.client.renderer.model.BlockPartRotation;
import net.minecraft.util.Direction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledBlockPartDeserializer {

    public static BlockPart deserialize(JsonObject json, JsonDeserializationContext context){
        Vector3f from = getVector3f(json, "from", true);
        Vector3f to = getVector3f(json, "to", true);
        BlockPartRotation rotation = getRotation(json);
        Map<Direction,BlockPartFace> faces = getFaces(context, json);
        if(json.has("shade") && !JSONUtils.isBooleanValue(json, "shade"))
            throw new JsonParseException("Expected shade to be a Boolean");
        boolean shading = JSONUtils.getAsBoolean(json, "shade", true);
        return new BlockPart(from, to, faces, rotation, shading);
    }

    @Nullable
    private static BlockPartRotation getRotation(JsonObject json){
        BlockPartRotation partRotation = null;
        if(json.has("rotation")){
            JsonObject rotation = JSONUtils.getAsJsonObject(json, "rotation");

            // origin
            Vector3f origin = getVector3f(rotation, "origin", false);
            origin.mul(0.0625f);

            // axis
            String axisName = JSONUtils.getAsString(json, "axis");
            Direction.Axis axis = Direction.Axis.byName(axisName.toLowerCase(Locale.ROOT));
            if(axis == null)
                throw new JsonParseException("Invalid rotation axis: " + axisName);

            // angle
            float angle = JSONUtils.getAsFloat(json, "angle");
            if(angle != 0 && MathHelper.abs(angle) != 22.5F && MathHelper.abs(angle) != 45)
                throw new JsonParseException("Invalid rotation " + angle + " found, only -45/-22.5/0/22.5/45 allowed");

            // scale
            boolean scale = JSONUtils.getAsBoolean(rotation, "rescale", false);

            partRotation = new BlockPartRotation(origin, axis, angle, scale);
        }

        return partRotation;
    }

    private static Map<Direction,BlockPartFace> getFaces(JsonDeserializationContext context, JsonObject json){
        Map<Direction,BlockPartFace> faces = Maps.newEnumMap(Direction.class);
        JsonObject object = JSONUtils.getAsJsonObject(json, "faces");

        for(Map.Entry<String,JsonElement> entry : object.entrySet()){
            Direction direction = Direction.byName(entry.getKey());
            if(direction == null)
                throw new JsonParseException("Unknown facing: " + entry.getKey());

            BlockPartFace face = RechiseledBlockPartFaceDeserializer.deserialize(entry.getValue().getAsJsonObject(), context);
            faces.put(direction, face);
        }

        if(faces.isEmpty())
            throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");

        return faces;
    }

    private static Vector3f getVector3f(JsonObject json, String key, boolean restricted){
        JsonArray jsonarray = JSONUtils.getAsJsonArray(json, key);
        if(jsonarray.size() != 3)
            throw new JsonParseException("Expected 3 " + key + " values, found: " + jsonarray.size());

        float x = JSONUtils.convertToFloat(jsonarray.get(0), key + "[0]");
        float y = JSONUtils.convertToFloat(jsonarray.get(1), key + "[1]");
        float z = JSONUtils.convertToFloat(jsonarray.get(2), key + "[2]");
        if(restricted && (x < -16 || y < -16 || z < -16 || x > 32 || y > 32 || z > 32))
            throw new JsonParseException("'" + key + "' specifier exceeds the allowed boundaries: [" + x + ", " + y + ", " + z + "]");

        return new Vector3f(x, y, z);
    }
}
