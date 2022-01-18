package com.supermartijn642.rechiseled.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.supermartijn642.rechiseled.model.serialization.RechiseledModelDeserializer;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.io.IOUtils;

import java.io.InputStreamReader;
import java.lang.reflect.Field;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledModelLoader implements ICustomModelLoader {

    private static final Gson GSON;
    private static final ICustomModelLoader vanillaModelLoader;

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

        ICustomModelLoader modelLoader = null;
        System.out.println("CLASSESSS:");
        loop:
        for(Class<?> declaredClass : ModelLoader.class.getDeclaredClasses()){
            System.out.println("declared class: " + declaredClass.getSimpleName());
            if(declaredClass.getSimpleName().equals("VanillaLoader")){
                for(Object object : declaredClass.getEnumConstants()){
                    System.out.println("object: " + object);
                    if(((Enum<?>)object).name().equals("INSTANCE")){
                        modelLoader = (ICustomModelLoader)object;
                        break loop;
                    }
                }
            }
        }
        vanillaModelLoader = modelLoader;
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
            if(JsonUtils.getString(json, "loader", "").equals("rechiseled:connecting_model"))
                model = RechiseledModelDeserializer.deserialize(file, json, GSON);
            else // Load vanilla model
                model = vanillaModelLoader.loadModel(new ResourceLocation(file.getResourceDomain(), file.getResourcePath().substring(0,file.getResourcePath().length() - ".json".length())));
        }finally{
            IOUtils.closeQuietly(resource);
        }
        return model;
    }
}
