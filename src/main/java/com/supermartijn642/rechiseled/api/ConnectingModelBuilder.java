package com.supermartijn642.rechiseled.api;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created 24/01/2022 by SuperMartijn642
 */
public class ConnectingModelBuilder extends ModelBuilder<ConnectingModelBuilder> {

    // whether a texture is a texture sheet or a regular texture
    protected final Map<String,Boolean> connectingTextures = new LinkedHashMap<>();
    private boolean shouldConnect = false;

    public ConnectingModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper){
        super(outputLocation, existingFileHelper);
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
    public JsonObject toJson(){
        JsonObject json = super.toJson();

        // Set the 'loader' property
        json.addProperty("loader", "rechiseled:connecting_model");

        // Set the 'should_connect' property
        if(this.shouldConnect)
            json.addProperty("should_connect", true);

        // Set 'connecting' property for all textures
        if(!this.textures.isEmpty() && !this.connectingTextures.isEmpty() && json.has("textures")){
            JsonObject textures = json.remove("textures").getAsJsonObject();
            for(Map.Entry<String,Boolean> e : this.connectingTextures.entrySet()){
                textures.remove(e.getKey());
                JsonObject textureJson = new JsonObject();
                textureJson.addProperty("location", this.textures.get(e.getKey()));
                textureJson.addProperty("connecting", e.getValue());
                textures.add(e.getKey(), textureJson);
            }
            json.add("model_textures", textures);
        }

        return json;
    }
}
