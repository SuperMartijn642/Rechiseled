package com.supermartijn642.rechiseled.api;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Created 24/12/2021 by SuperMartijn642
 */
public abstract class ChiselingRecipeProvider implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final String modid;
    private final DataGenerator generator;
    private final ExistingFileHelper existingFileHelper;
    private final Map<String,ChiselingRecipeBuilder> recipes = new HashMap<>();

    public ChiselingRecipeProvider(String modid, DataGenerator generator, ExistingFileHelper existingFileHelper){
        this.modid = modid;
        this.generator = generator;
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    public String getName(){
        return "Chiseling Recipes";
    }

    @Override
    public void run(HashCache cache){
        this.buildRecipes();

        Path path = this.generator.getOutputFolder();
        for(Map.Entry<String,ChiselingRecipeBuilder> entry : this.recipes.entrySet()){
            String recipeName = entry.getKey();
            ChiselingRecipeBuilder builder = entry.getValue();

            // Check if parent exists
            if(builder.parent != null){
                ResourceLocation parent = builder.parent;
                // Find greater parents in the current recipe provider
                while(parent != null && parent.getNamespace().equals(this.modid) && this.recipes.containsKey(parent.getPath())){
                    parent = this.recipes.get(parent.getPath()).parent;
                }
                // If not found in this recipe provider, check existing files
                if(parent != null){
                    ResourceLocation parentLocation = new ResourceLocation(parent.getNamespace(), "chiseling_recipes/" + (parent.getPath().endsWith(".json") ? parent.getPath() : parent.getPath() + ".json"));
                    if(!this.existingFileHelper.exists(parentLocation, PackType.SERVER_DATA))
                        throw new IllegalStateException("Could not find upward parent '" + parent + "' at '/data/" + parentLocation.getNamespace() + "/" + parentLocation.getPath() + "' for chiseling recipe: " + recipeName);
                }
            }

            // Write the recipe
            JsonObject json = serializeRecipe(recipeName, builder);
            Path recipePath = path.resolve("data/" + this.modid + "/chiseling_recipes/" + recipeName + ".json");
            saveRecipe(cache, json, recipePath);
        }
    }

    private static JsonObject serializeRecipe(String recipeName, ChiselingRecipeBuilder recipe){
        JsonObject json = new JsonObject();

        json.addProperty("type", "rechiseled:chiseling");

        if(recipe.parent != null)
            json.addProperty("parent", recipe.parent.toString());

        Set<Item> items = Sets.newHashSet();
        JsonArray entries = new JsonArray();
        for(Triple<Item,Item,Boolean> entry : recipe.entries){
            JsonObject object = new JsonObject();
            if(entry.getLeft() != null){
                if(!items.add(entry.getLeft()))
                    throw new IllegalStateException("Duplicate item '" + entry.getLeft().getRegistryName() + "' in chiseling recipe '" + recipeName + "'");
                object.addProperty("item", entry.getLeft().getRegistryName().toString());
            }
            if(entry.getMiddle() != null){
                if(!items.add(entry.getMiddle()))
                    throw new IllegalStateException("Duplicate item '" + entry.getMiddle().getRegistryName() + "' in chiseling recipe '" + recipeName + "'");
                object.addProperty("connecting_item", entry.getMiddle().getRegistryName().toString());
            }
            if(entry.getRight())
                object.addProperty("optional", true);
            entries.add(object);
        }

        json.add("entries", entries);
        return json;
    }

    private static void saveRecipe(HashCache cache, JsonObject json, Path path){
        try{
            String jsonString = GSON.toJson(json);
            String hash = SHA1.hashUnencodedChars(jsonString).toString();
            if(!Objects.equals(cache.getHash(path), hash) || !Files.exists(path)){
                Files.createDirectories(path.getParent());

                try(BufferedWriter bufferedwriter = Files.newBufferedWriter(path)){
                    bufferedwriter.write(jsonString);
                }
            }

            cache.putNew(path, hash);
        }catch(IOException exception){
            System.err.println("Couldn't save recipe '" + path + "'");
            exception.printStackTrace();
        }
    }

    /**
     * Recipes can be created using a recipe builder obtained from {@link #beginRecipe(String)}.
     * All recipe builders will be saved and written to file automatically.
     */
    protected abstract void buildRecipes();

    /**
     * Creates a new chiseling recipe builder.
     * @param recipeName the name of the recipe
     * @return a chiseling recipe builder for the given recipe name
     */
    protected ChiselingRecipeBuilder beginRecipe(String recipeName){
        return this.recipes.computeIfAbsent(recipeName, s -> new ChiselingRecipeBuilder());
    }

    protected static class ChiselingRecipeBuilder {

        private final List<Triple<Item,Item,Boolean>> entries = new LinkedList<>();
        private ResourceLocation parent;

        private ChiselingRecipeBuilder(){
        }

        /**
         * Sets a parent recipe for this recipe builder.
         * All entries from this recipe builder will be combined with the parent recipe.
         * {@link BaseChiselingRecipes} contains recipe locations for the default rechiseled recipes.
         * @param parent the parent recipe location
         */
        public ChiselingRecipeBuilder parent(ResourceLocation parent){
            this.parent = parent;
            return this;
        }

        /**
         * Add a new entry to this recipe builder.
         * Each entry can have a regular variant and a variant with connecting textures.
         * @param regularItem    the regular variant, i.e. without connecting textures
         * @param connectingItem the variant with connecting textures
         * @param optional       whether the recipe may ignore the entry when the entry's items are not present
         * @throws IllegalArgumentException when both {@code regularItem} and {@code connectingItem} are {@code null}
         */
        public ChiselingRecipeBuilder add(@Nullable Item regularItem, @Nullable Item connectingItem, boolean optional){
            if(regularItem == null && connectingItem == null)
                throw new IllegalArgumentException("At least one of regular item or connecting item must not be null!");

            this.entries.add(new ImmutableTriple<>(regularItem, connectingItem, optional));
            return this;
        }

        /**
         * Adds a new entry to this recipe builder.
         * Each entry can have a regular variant and a variant with connecting textures.
         * @param regularItem    the regular variant, i.e. without connecting textures
         * @param connectingItem the variant with connecting textures
         * @throws IllegalArgumentException when both {@code regularItem} and {@code connectingItem} are {@code null}
         */
        public ChiselingRecipeBuilder add(Item regularItem, Item connectingItem){
            return this.add(regularItem, connectingItem, false);
        }

        /**
         * Adds a new entry to this recipe builder for an item without connecting textures.
         * @param item     an item without connecting textures
         * @param optional whether the recipe may ignore the entry when the entry's items are not present
         * @throws IllegalArgumentException when {@code item} is {@code null}
         */
        public ChiselingRecipeBuilder addRegularItem(Item item, boolean optional){
            if(item == null)
                throw new IllegalArgumentException("Item must not be null!");

            return this.add(item, null, optional);
        }

        /**
         * Adds a new entry to this recipe builder for an item without connecting textures.
         * @param item an item without connecting textures
         * @throws IllegalArgumentException when {@code item} is {@code null}
         */
        public ChiselingRecipeBuilder addRegularItem(Item item){
            return this.addRegularItem(item, false);
        }

        /**
         * Adds a new entry to this recipe builder for an item with connecting textures.
         * @param item     an item with connecting textures
         * @param optional whether the recipe may ignore the entry when the entry's items are not present
         * @throws IllegalArgumentException when {@code item} is {@code null}
         */
        public ChiselingRecipeBuilder addConnectingItem(Item item, boolean optional){
            if(item == null)
                throw new IllegalArgumentException("Item must not be null!");

            return this.add(null, item, optional);
        }

        /**
         * Adds a new entry to this recipe builder for an item with connecting textures.
         * @param item an item with connecting textures
         * @throws IllegalArgumentException when {@code item} is {@code null}
         */
        public ChiselingRecipeBuilder addConnectingItem(Item item){
            return this.addConnectingItem(item, false);
        }
    }
}
