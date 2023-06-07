package com.supermartijn642.rechiseled.api;

import com.google.gson.JsonObject;
import com.supermartijn642.core.generator.ModelGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.ResourceType;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created 24/01/2022 by SuperMartijn642
 * @deprecated use Fusion instead
 */
@Deprecated
public abstract class ConnectingBlockModelProvider extends ModelGenerator {

    private final Map<ResourceLocation,ConnectingModelBuilder> models = new HashMap();

    public ConnectingBlockModelProvider(String modid, ResourceCache cache){
        super(modid, cache);
    }

    @Override
    public String getName(){
        return "Connecting Block Models: " + this.modName;
    }

    @Override
    public void generate(){
        this.createModels();
    }

    @Override
    public void save(){
        // Loop over all models
        for(ConnectingModelBuilder modelBuilder : this.models.values()){
            JsonObject json = this.convertToJson(modelBuilder);

            // Save the object to the cache
            ResourceLocation identifier = modelBuilder.identifier;
            this.cache.saveJsonResource(ResourceType.ASSET, json, identifier.getResourceDomain(), "models", identifier.getResourcePath());
        }
    }

    @Override
    protected JsonObject convertToJson(ModelBuilder modelBuilder){
        JsonObject json = super.convertToJson(modelBuilder);

        // Set the 'loader' property
        json.addProperty("loader", "rechiseled:connecting_model");

        // Set the 'should_connect' property
        if(((ConnectingModelBuilder)modelBuilder).shouldConnect)
            json.addProperty("should_connect", true);

        // Set 'connecting' property for all textures
        if(!((ConnectingModelBuilder)modelBuilder).connectingTextures.isEmpty() && json.has("textures")){
            JsonObject textures = json.remove("textures").getAsJsonObject();
            for(Map.Entry<String,Boolean> e : ((ConnectingModelBuilder)modelBuilder).connectingTextures.entrySet()){
                String texture = textures.remove(e.getKey()).getAsString();
                JsonObject textureJson = new JsonObject();
                textureJson.addProperty("location", texture);
                textureJson.addProperty("connecting", e.getValue());
                textures.add(e.getKey(), textureJson);
            }
            json.add("model_textures", textures);
        }

        return json;
    }

    /**
     * All functions in {@link ModelGenerator.ModelBuilder}
     * have been adapted to also have an 'isConnectingTexture' argument. The
     * 'isConnectingTexture' argument can be used for connecting textures.
     *
     * <p>{@link ConnectingModelBuilder#connectToOtherBlocks(boolean)} can be used
     * to set whether connecting textures is the model should connect. If false,
     * connecting textures will simply use their top left texture (the fully
     * bordered one).
     */
    protected abstract void createModels();

    @Override
    protected ConnectingModelBuilder model(ResourceLocation location){
        this.cache.trackToBeGeneratedResource(ResourceType.ASSET, location.getResourceDomain(), "models", location.getResourcePath(), ".json");
        return this.models.computeIfAbsent(location, i -> new ConnectingModelBuilder(this.modid, i));
    }

    @Override
    protected ConnectingModelBuilder model(String namespace, String path){
        return (ConnectingModelBuilder)super.model(namespace, path);
    }

    @Override
    protected ConnectingModelBuilder model(String location){
        return (ConnectingModelBuilder)super.model(location);
    }

    @Override
    protected ConnectingModelBuilder cube(ResourceLocation location, ResourceLocation up, ResourceLocation down, ResourceLocation north, ResourceLocation east, ResourceLocation south, ResourceLocation west){
        return (ConnectingModelBuilder)super.cube(location, up, down, north, east, south, west);
    }

    @Override
    protected ConnectingModelBuilder cube(String namespace, String path, ResourceLocation up, ResourceLocation down, ResourceLocation north, ResourceLocation east, ResourceLocation south, ResourceLocation west){
        return (ConnectingModelBuilder)super.cube(namespace, path, up, down, north, east, south, west);
    }

    @Override
    protected ConnectingModelBuilder cube(String location, ResourceLocation up, ResourceLocation down, ResourceLocation north, ResourceLocation east, ResourceLocation south, ResourceLocation west){
        return (ConnectingModelBuilder)super.cube(location, up, down, north, east, south, west);
    }

    @Override
    protected ConnectingModelBuilder cubeAll(ResourceLocation location, ResourceLocation texture){
        return (ConnectingModelBuilder)super.cubeAll(location, texture);
    }

    @Override
    protected ConnectingModelBuilder cubeAll(String namespace, String path, ResourceLocation texture){
        return (ConnectingModelBuilder)super.cubeAll(namespace, path, texture);
    }

    @Override
    protected ConnectingModelBuilder cubeAll(String location, ResourceLocation texture){
        return (ConnectingModelBuilder)super.cubeAll(location, texture);
    }

    protected ConnectingModelBuilder cubeAll(String location, ResourceLocation texture, boolean isConnectingTexture){
        return ((ConnectingModelBuilder)super.model(location)).parent("minecraft", "block/cube_all").texture("all", texture, isConnectingTexture);
    }

    public ConnectingModelBuilder getBuilder(String path){
        return this.model(path);
    }

    public ConnectingModelBuilder cube(String name, ResourceLocation down, boolean downConnecting, ResourceLocation up, boolean upConnecting, ResourceLocation north, boolean northConnecting, ResourceLocation south, boolean southConnecting, ResourceLocation east, boolean eastConnecting, ResourceLocation west, boolean westConnecting){
        return this.model(name)
            .parent("minecraft", "block/cube")
            .texture("down", down, downConnecting)
            .texture("up", up, upConnecting)
            .texture("north", north, northConnecting)
            .texture("south", south, southConnecting)
            .texture("east", east, eastConnecting)
            .texture("west", west, westConnecting);
    }

    public static class ConnectingModelBuilder extends ModelBuilder {

        private final ResourceLocation identifier;
        // whether a texture is a texture sheet or a regular texture
        protected final Map<String,Boolean> connectingTextures = new LinkedHashMap<>();
        private boolean shouldConnect = false;

        private ConnectingModelBuilder(String modid, ResourceLocation identifier){
            super(modid, identifier);
            this.identifier = identifier;
        }

        /**
         * Indicate that any connecting textures should connect to touching blocks.
         */
        public ConnectingModelBuilder connectToOtherBlocks(){
            this.shouldConnect = true;
            return this;
        }

        /**
         * Indicate whether any connecting textures should connect to touching blocks.
         * If false, any connecting textures will show their top left texture (the fully
         * bordered one).
         */
        public ConnectingModelBuilder connectToOtherBlocks(boolean connect){
            this.shouldConnect = connect;
            return this;
        }

        /**
         * Set the texture for a given dictionary key.
         * @param key                 the texture key
         * @param texture             the texture, can be another reference e.g. '#all'
         * @param isConnectingTexture whether the texture is a connecting texture
         * @throws NullPointerException     when {@code key} is {@code null}
         * @throws NullPointerException     when {@code texture} is {@code null}
         * @throws IllegalStateException    when {@code texture} is not a reference (does not start
         *                                  with '#') and does not exist
         * @throws IllegalArgumentException when {@code texture} is a reference (starts
         *                                  with '#') and {@code isConnectingTexture} is {@code true}
         */
        public ConnectingModelBuilder texture(String key, String texture, boolean isConnectingTexture){
            if(isConnectingTexture && texture.charAt(0) == '#')
                throw new IllegalArgumentException("Only textures can be 'connecting', references (starts with '#') cannot: " + texture);

            super.texture(key, texture);
            this.connectingTextures.put(key, isConnectingTexture);
            return this;
        }


        /**
         * Set the texture for a given dictionary key.
         * @param key                 the texture key
         * @param texture             the texture
         * @param isConnectingTexture whether the texture is a connecting texture
         * @throws NullPointerException  when {@code key} is {@code null}
         * @throws NullPointerException  when {@code texture} is {@code null}
         * @throws IllegalStateException when {@code texture} does not exist
         */
        public ConnectingModelBuilder texture(String key, ResourceLocation texture, boolean isConnectingTexture){
            super.texture(key, texture);
            this.connectingTextures.put(key, isConnectingTexture);
            return this;
        }

        @Override
        public ConnectingModelBuilder texture(String key, String texture){
            super.texture(key, texture);
            this.connectingTextures.put(key, false);
            return this;
        }

        @Override
        public ConnectingModelBuilder texture(String key, ResourceLocation texture){
            super.texture(key, texture);
            this.connectingTextures.put(key, false);
            return this;
        }

        @Override
        public ModelBuilder texture(String key, String namespace, String identifier){
            super.texture(key, namespace, identifier);
            this.connectingTextures.put(key, false);
            return this;
        }

        @Override
        public ConnectingModelBuilder parent(ResourceLocation model){
            return (ConnectingModelBuilder)super.parent(model);
        }

        @Override
        public ConnectingModelBuilder parent(String namespace, String path){
            return (ConnectingModelBuilder)super.parent(namespace, path);
        }

        @Override
        public ConnectingModelBuilder parent(String model){
            return (ConnectingModelBuilder)super.parent(model);
        }
    }
}
