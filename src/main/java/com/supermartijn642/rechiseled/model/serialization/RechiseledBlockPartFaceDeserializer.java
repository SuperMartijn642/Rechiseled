package com.supermartijn642.rechiseled.model.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.supermartijn642.rechiseled.model.RechiseledBlockPartFace;
import net.minecraft.client.renderer.model.BlockFaceUV;
import net.minecraft.client.renderer.model.BlockPartFace;
import net.minecraft.util.Direction;
import net.minecraft.util.JSONUtils;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledBlockPartFaceDeserializer {

    public static BlockPartFace deserialize(JsonObject json, JsonDeserializationContext context){
        // Connecting boolean
        boolean connecting = JSONUtils.getAsBoolean(json, "connecting", false);

        // Vanilla stuff
        Direction direction = Direction.byName(JSONUtils.getAsString(json, "cullface", ""));
        int tintIndex = JSONUtils.getAsInt(json, "tintindex", -1);
        String texture = JSONUtils.getAsString(json, "texture");
        BlockFaceUV blockfaceuv = context.deserialize(json, BlockFaceUV.class);

        return new RechiseledBlockPartFace(direction, tintIndex, texture, blockfaceuv, connecting);
    }
}
