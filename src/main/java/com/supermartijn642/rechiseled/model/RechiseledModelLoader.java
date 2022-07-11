package com.supermartijn642.rechiseled.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledModelLoader implements IGeometryLoader<RechiseledModel> {

    @Override
    public RechiseledModel read(JsonObject json, JsonDeserializationContext context){
        return RechiseledModelDeserializer.deserialize(json, context);
    }
}
