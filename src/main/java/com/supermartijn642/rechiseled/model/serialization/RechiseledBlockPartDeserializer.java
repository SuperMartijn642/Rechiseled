package com.supermartijn642.rechiseled.model.serialization;

import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledBlockPartDeserializer {

    public static BlockPart deserialize(JsonObject json, Gson context){
        Vector3f from = getVector3f(json, "from", true);
        Vector3f to = getVector3f(json, "to", true);
        BlockPartRotation rotation = getRotation(json);
        Map<EnumFacing,BlockPartFace> faces = getFaces(json, context);
        if(json.has("shade") && !JsonUtils.isBoolean(json, "shade"))
            throw new JsonParseException("Expected shade to be a Boolean");
        boolean shading = JsonUtils.getBoolean(json, "shade", true);
        return new BlockPart(from, to, faces, rotation, shading);
    }

    @Nullable
    private static BlockPartRotation getRotation(JsonObject json){
        BlockPartRotation partRotation = null;
        if(json.has("rotation")){
            JsonObject rotation = JsonUtils.getJsonObject(json, "rotation");

            // origin
            Vector3f origin = getVector3f(rotation, "origin", false);
            origin.scale(0.0625f);

            // axis
            String axisName = JsonUtils.getString(json, "axis");
            EnumFacing.Axis axis = EnumFacing.Axis.byName(axisName.toLowerCase(Locale.ROOT));
            if(axis == null)
                throw new JsonParseException("Invalid rotation axis: " + axisName);

            // angle
            float angle = JsonUtils.getFloat(json, "angle");
            if(angle != 0 && MathHelper.abs(angle) != 22.5F && MathHelper.abs(angle) != 45)
                throw new JsonParseException("Invalid rotation " + angle + " found, only -45/-22.5/0/22.5/45 allowed");

            // scale
            boolean scale = JsonUtils.getBoolean(rotation, "rescale", false);

            partRotation = new BlockPartRotation(origin, axis, angle, scale);
        }

        return partRotation;
    }

    private static Map<EnumFacing,BlockPartFace> getFaces(JsonObject json, Gson context){
        Map<EnumFacing,BlockPartFace> faces = Maps.newEnumMap(EnumFacing.class);
        JsonObject object = JsonUtils.getJsonObject(json, "faces");

        for(Map.Entry<String,JsonElement> entry : object.entrySet()){
            EnumFacing direction = EnumFacing.byName(entry.getKey());
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
        JsonArray jsonarray = JsonUtils.getJsonArray(json, key);
        if(jsonarray.size() != 3)
            throw new JsonParseException("Expected 3 " + key + " values, found: " + jsonarray.size());

        float x = JsonUtils.getFloat(jsonarray.get(0), key + "[0]");
        float y = JsonUtils.getFloat(jsonarray.get(1), key + "[1]");
        float z = JsonUtils.getFloat(jsonarray.get(2), key + "[2]");
        if(restricted && (x < -16 || y < -16 || z < -16 || x > 32 || y > 32 || z > 32))
            throw new JsonParseException("'" + key + "' specifier exceeds the allowed boundaries: [" + x + ", " + y + ", " + z + "]");

        return new Vector3f(x, y, z);
    }
}
