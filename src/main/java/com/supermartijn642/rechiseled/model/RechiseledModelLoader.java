package com.supermartijn642.rechiseled.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.supermartijn642.rechiseled.model.serialization.RechiseledModelDeserializer;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.io.IOUtils;

import java.io.InputStreamReader;
import java.lang.reflect.Field;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledModelLoader implements ICustomModelLoader {

    private static final Gson GSON;

    static{
        Gson serializer = null;
        try{
            /**
             * {@link ModelBlock#SERIALIZER}
             */
            Field SERIALIZER = ObfuscationReflectionHelper.findField(ModelBlock.class, "field_178319_a");
            SERIALIZER.setAccessible(true);
            serializer = (Gson)SERIALIZER.get(null);
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
        GSON = serializer;
    }

    private IResourceManager manager;

    @Override
    public void onResourceManagerReload(IResourceManager manager){
        this.manager = manager;
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation){
        return modelLocation.getResourceDomain().equals("rechiseled") && !modelLocation.getResourcePath().endsWith("/chisel");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception{
        ResourceLocation file = new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath() + ".json");
        if(!file.getResourcePath().startsWith("models/"))
            file = new ResourceLocation(file.getResourceDomain(), "models/block/" + file.getResourcePath());

        IModel model;
        IResource resource = null;
        try{
            resource = this.manager.getResource(file);
            JsonReader reader = GSON.newJsonReader(new InputStreamReader(resource.getInputStream()));
            JsonObject json = GSON.fromJson(reader, JsonObject.class);
            model = RechiseledModelDeserializer.deserialize(file, json, GSON);
        }finally{
            IOUtils.closeQuietly(resource);
        }
        return model;
    }
}
