package com.supermartijn642.rechiseled.data;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledBlockModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

    private boolean shouldConnect = false;

    protected RechiseledBlockModelBuilder(T parent, ExistingFileHelper existingFileHelper){
        super(new ResourceLocation("rechiseled", "connecting_model"), parent, existingFileHelper);
    }

    public RechiseledBlockModelBuilder<T> connectToOtherBlocks(){
        this.shouldConnect = true;
        return this;
    }

    public RechiseledBlockModelBuilder<T> connectToOtherBlocks(boolean connect){
        this.shouldConnect = connect;
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json){
        super.toJson(json);

        // Set the 'should connect' property
        if(this.shouldConnect)
            json.addProperty("should_connect", true);

        return json;
    }
}
