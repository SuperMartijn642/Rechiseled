package com.supermartijn642.rechiseled.data;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelBuilder;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledBlockModelBuilder extends ModelBuilder<RechiseledBlockModelBuilder> {

    private boolean shouldConnect = false;

    protected RechiseledBlockModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper){
        super(outputLocation, existingFileHelper);
    }

    public RechiseledBlockModelBuilder connectToOtherBlocks(){
        this.shouldConnect = true;
        return this;
    }

    public RechiseledBlockModelBuilder connectToOtherBlocks(boolean connect){
        this.shouldConnect = connect;
        return this;
    }

    @Override
    public JsonObject toJson(){
        JsonObject json = super.toJson();

        json.addProperty("loader", "rechiseled:connecting_model");

        // Set the 'should connect' property
        if(this.shouldConnect)
            json.addProperty("should_connect", true);

        return json;
    }
}
