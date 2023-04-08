package com.supermartijn642.rechiseled.api;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created 24/01/2022 by SuperMartijn642
 */
public abstract class ConnectingBlockModelProvider implements DataProvider {

    private final String modid;
    private final FabricDataOutput generator;
    private final Map<ResourceLocation,ConnectingModelBuilder> models = new HashMap<>();

    public ConnectingBlockModelProvider(String modid, FabricDataOutput generator){
        this.modid = modid;
        this.generator = generator;
    }

    @Override
    public String getName(){
        return "Connecting Block Models: " + this.modid;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache){
        this.createModels();

        List<CompletableFuture<?>> tasks = new ArrayList<>(this.models.size());
        Path path = this.generator.getOutputFolder();
        for(Map.Entry<ResourceLocation,ConnectingModelBuilder> entry : this.models.entrySet()){
            ResourceLocation identifier = entry.getKey();
            ConnectingModelBuilder builder = entry.getValue();

            // Write the recipe
            JsonObject json = builder.toJson();
            Path modelPath = path.resolve("assets/" + identifier.getNamespace() + "/models/" + identifier.getPath() + ".json");
            tasks.add(DataProvider.saveStable(cache, json, modelPath));
        }
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    /**
     * All functions in {@link com.supermartijn642.core.generator.ModelGenerator.ModelBuilder}
     * have been adapted to also have an 'isConnectingTexture' argument. The
     * 'isConnectingTexture' argument can be used for connecting textures.
     *
     * <p>{@link ConnectingModelBuilder#connectToOtherBlocks(boolean)} can be used
     * to set whether connecting textures is the model should connect. If false,
     * connecting textures will simply use their top left texture (the fully
     * bordered one).
     */
    protected abstract void createModels();

    /**
     * Gets a model builder for the given location. The returned model builder may be a new model builder or an existing one if requested before.
     * @param location resource location of the model
     */
    protected ConnectingModelBuilder model(ResourceLocation location){
        return this.models.computeIfAbsent(location, i -> new ConnectingModelBuilder(this.modid, i));
    }

    /**
     * Gets a model builder for the given location. The returned model builder may be a new model builder or an existing one if requested before.
     * @param namespace namespace of the model location
     * @param path      path of the model location
     */
    protected ConnectingModelBuilder model(String namespace, String path){
        return this.model(new ResourceLocation(namespace, path));
    }

    /**
     * Gets a model builder for the given location. The returned model builder may be a new model builder or an existing one if requested before.
     * @param location path of the model location
     */
    protected ConnectingModelBuilder model(String location){
        return this.model(this.modid, location);
    }


    /**
     * Creates a new model with parent 'minecraft:block/cube' and the given textures for the faces.
     * @param location resource location of the model
     * @param up       resource location of the texture for the top face
     * @param down     resource location of the texture for the bottom face
     * @param north    resource location of the texture for the north face
     * @param east     resource location of the texture for the east face
     * @param south    resource location of the texture for the south face
     * @param west     resource location of the texture for the west face
     */
    protected ConnectingModelBuilder cube(ResourceLocation location, ResourceLocation up, ResourceLocation down, ResourceLocation north, ResourceLocation east, ResourceLocation south, ResourceLocation west){
        return this.model(location).parent("minecraft", "block/cube").texture("up", up).texture("down", down).texture("north", north).texture("east", east).texture("south", south).texture("west", west);
    }

    /**
     * Creates a new model with parent 'minecraft:block/cube' and the given textures for the faces.
     * @param namespace namespace of the model location
     * @param path      path of the model location
     * @param up        resource location of the texture for the top face
     * @param down      resource location of the texture for the bottom face
     * @param north     resource location of the texture for the north face
     * @param east      resource location of the texture for the east face
     * @param south     resource location of the texture for the south face
     * @param west      resource location of the texture for the west face
     */
    protected ConnectingModelBuilder cube(String namespace, String path, ResourceLocation up, ResourceLocation down, ResourceLocation north, ResourceLocation east, ResourceLocation south, ResourceLocation west){
        return this.model(namespace, path).parent("minecraft", "block/cube").texture("up", up).texture("down", down).texture("north", north).texture("east", east).texture("south", south).texture("west", west);
    }

    /**
     * Creates a new model with parent 'minecraft:block/cube' and the given textures for the faces.
     * @param location resource location of the model
     * @param up       resource location of the texture for the top face
     * @param down     resource location of the texture for the bottom face
     * @param north    resource location of the texture for the north face
     * @param east     resource location of the texture for the east face
     * @param south    resource location of the texture for the south face
     * @param west     resource location of the texture for the west face
     */
    protected ConnectingModelBuilder cube(String location, ResourceLocation up, ResourceLocation down, ResourceLocation north, ResourceLocation east, ResourceLocation south, ResourceLocation west){
        return this.model(location).parent("minecraft", "block/cube").texture("up", up).texture("down", down).texture("north", north).texture("east", east).texture("south", south).texture("west", west);
    }

    public ConnectingModelBuilder cube(String location, ResourceLocation down, boolean downConnecting, ResourceLocation up, boolean upConnecting, ResourceLocation north, boolean northConnecting, ResourceLocation south, boolean southConnecting, ResourceLocation east, boolean eastConnecting, ResourceLocation west, boolean westConnecting){
        return this.model(location)
            .parent("minecraft", "block/cube")
            .texture("down", down, downConnecting)
            .texture("up", up, upConnecting)
            .texture("north", north, northConnecting)
            .texture("south", south, southConnecting)
            .texture("east", east, eastConnecting)
            .texture("west", west, westConnecting);
    }

    /**
     * Creates a new model with parent 'minecraft:block/cube_all' and the given texture for '#all'.
     * @param location resource location of the model
     * @param texture  resource location of the texture for the cube's sides
     */
    protected ConnectingModelBuilder cubeAll(ResourceLocation location, ResourceLocation texture){
        return this.model(location).parent("minecraft", "block/cube_all").texture("all", texture);
    }

    /**
     * Creates a new model with parent 'minecraft:block/cube_all' and the given texture for '#all'.
     * @param namespace namespace of the model location
     * @param path      path of the model location
     * @param texture   resource location of the texture for the cube's sides
     */
    protected ConnectingModelBuilder cubeAll(String namespace, String path, ResourceLocation texture){
        return this.model(namespace, path).parent("minecraft", "block/cube_all").texture("all", texture);
    }

    /**
     * Creates a new model with parent 'minecraft:block/cube_all' and the given texture for '#all'.
     * @param location resource location of the model
     * @param texture  resource location of the texture for the cube's sides
     */
    protected ConnectingModelBuilder cubeAll(String location, ResourceLocation texture){
        return this.model(location).parent("minecraft", "block/cube_all").texture("all", texture);
    }

    public ConnectingModelBuilder cubeAll(String location, ResourceLocation texture, boolean connecting){
        return this.model(location)
            .parent("minecraft", "block/cube_all")
            .texture("all", texture, connecting);
    }
}
