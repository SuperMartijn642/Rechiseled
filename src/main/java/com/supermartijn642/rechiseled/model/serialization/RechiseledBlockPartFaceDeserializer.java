package com.supermartijn642.rechiseled.model.serialization;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.supermartijn642.rechiseled.model.RechiseledBlockPartFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledBlockPartFaceDeserializer {

    public static BlockPartFace deserialize(JsonObject json, Gson context){
        // Connecting boolean
        boolean connecting = JsonUtils.getBoolean(json, "connecting", false);

        // Vanilla stuff
        EnumFacing direction = EnumFacing.byName(JsonUtils.getString(json, "cullface", ""));
        int tintIndex = JsonUtils.getInt(json, "tintindex", -1);
        String texture = JsonUtils.getString(json, "texture");
        BlockFaceUV blockfaceuv = context.fromJson(json, BlockFaceUV.class);

        return new RechiseledBlockPartFace(direction, tintIndex, texture, blockfaceuv, connecting);
    }
}
