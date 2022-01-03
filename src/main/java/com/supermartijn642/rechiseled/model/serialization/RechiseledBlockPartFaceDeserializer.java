package com.supermartijn642.rechiseled.model.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.supermartijn642.rechiseled.model.RechiseledBlockPartFace;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.core.Direction;
import net.minecraft.util.GsonHelper;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledBlockPartFaceDeserializer {

    public static BlockElementFace deserialize(JsonObject json, JsonDeserializationContext context){
        // Connecting boolean
        boolean connecting = GsonHelper.getAsBoolean(json, "connecting", false);

        // Vanilla stuff
        Direction direction = Direction.byName(GsonHelper.getAsString(json, "cullface", ""));
        int tintIndex = GsonHelper.getAsInt(json, "tintindex", -1);
        String texture = GsonHelper.getAsString(json, "texture");
        BlockFaceUV blockfaceuv = context.deserialize(json, BlockFaceUV.class);

        return new RechiseledBlockPartFace(direction, tintIndex, texture, blockfaceuv, connecting);
    }
}
