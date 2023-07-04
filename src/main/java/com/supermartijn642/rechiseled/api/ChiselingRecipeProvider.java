package com.supermartijn642.rechiseled.api;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.ResourceGenerator;
import com.supermartijn642.core.generator.ResourceType;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.core.registry.RegistryUtil;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.core.util.Triple;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created 24/12/2021 by SuperMartijn642
 */
public abstract class ChiselingRecipeProvider extends ResourceGenerator {

    private final Map<ResourceLocation,ChiselingRecipeBuilder> recipes = new HashMap<>();

    public ChiselingRecipeProvider(String modid, ResourceCache cache){
        super(modid, cache);
    }

    @Override
    public String getName(){
        return "Chiseling Recipes: " + this.modName;
    }

    @Override
    public void generate(){
        this.buildRecipes();
    }

    @Override
    public void save(){
        for(Map.Entry<ResourceLocation,ChiselingRecipeBuilder> entry : this.recipes.entrySet()){
            ResourceLocation recipeName = entry.getKey();
            ChiselingRecipeBuilder builder = entry.getValue();

            // Check if parent exists
            if(builder.parent != null){
                ResourceLocation parent = builder.parent;
                // Find greater parents in the current recipe provider
                while(parent != null && parent.getResourceDomain().equals(this.modid) && this.recipes.containsKey(parent))
                    parent = this.recipes.get(parent).parent;
                // If not found in this recipe provider, check existing files
                if(parent != null && !this.cache.doesResourceExist(ResourceType.ASSET, parent.getResourceDomain(), "chiseling_recipes", parent.getResourcePath(), ".json"))
                    throw new IllegalStateException("Could not find upward parent '" + parent + "' at '/assets/" + parent.getResourceDomain() + "/" + parent.getResourcePath() + "' for chiseling recipe: " + recipeName);
            }

            // Write the recipe
            JsonObject json = serializeRecipe(recipeName, builder);
            this.cache.saveJsonResource(ResourceType.ASSET, json, recipeName.getResourceDomain(), "chiseling_recipes", recipeName.getResourcePath());
        }
    }

    private static JsonObject serializeRecipe(ResourceLocation recipeName, ChiselingRecipeBuilder recipe){
        JsonObject json = new JsonObject();

        json.addProperty("type", "rechiseled:chiseling");

        if(recipe.parent != null)
            json.addProperty("parent", recipe.parent.toString());

        json.addProperty("overwrite", recipe.overwrite);

        Set<Pair<Item,Integer>> items = Sets.newHashSet();
        JsonArray entries = new JsonArray();
        for(Triple<ItemStack,ItemStack,Boolean> entry : recipe.entries){
            JsonObject object = new JsonObject();
            if(entry.left() != null){
                ItemStack stack = entry.left();
                if(!items.add(Pair.of(stack.getItem(), stack.getHasSubtypes() ? stack.getMetadata() : 0)))
                    throw new IllegalStateException("Duplicate item '" + TextComponents.item(stack.getItem()).format() + "' in chiseling recipe '" + recipeName + "'");
                object.addProperty("item", Registries.ITEMS.getIdentifier(stack.getItem()).toString());
                if(stack.getHasSubtypes())
                    object.addProperty("item_data", stack.getMetadata());
            }
            if(entry.middle() != null){
                ItemStack stack = entry.middle();
                if(!items.add(Pair.of(stack.getItem(), stack.getHasSubtypes() ? stack.getMetadata() : 0)))
                    throw new IllegalStateException("Duplicate item '" + TextComponents.item(stack.getItem()).format() + "' in chiseling recipe '" + recipeName + "'");
                object.addProperty("connecting_item", Registries.ITEMS.getIdentifier(stack.getItem()).toString());
                if(stack.getHasSubtypes())
                    object.addProperty("connecting_item_data", stack.getMetadata());
            }
            if(entry.right())
                object.addProperty("optional", true);
            entries.add(object);
        }

        json.add("entries", entries);
        return json;
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
        if(!RegistryUtil.isValidPath(recipeName))
            throw new IllegalArgumentException("Recipe name must be a valid resource location path, not '" + recipeName + "'!");

        this.cache.trackToBeGeneratedResource(ResourceType.ASSET, this.modid, "chiseling_recipes", recipeName, ".json");
        return this.recipes.computeIfAbsent(new ResourceLocation(this.modid, recipeName), s -> new ChiselingRecipeBuilder());
    }

    protected ChiselingRecipeBuilder beginRecipe(ResourceLocation recipe){
        this.cache.trackToBeGeneratedResource(ResourceType.ASSET, recipe.getResourceDomain(), "chiseling_recipes", recipe.getResourcePath(), ".json");
        return this.recipes.computeIfAbsent(recipe, s -> new ChiselingRecipeBuilder());
    }

    protected class ChiselingRecipeBuilder {

        private final List<Triple<ItemStack,ItemStack,Boolean>> entries = new LinkedList<>();
        private ResourceLocation parent;
        private boolean overwrite = false;

        private ChiselingRecipeBuilder(){
        }

        /**
         * Sets a parent recipe for this recipe builder.
         * All entries from this recipe builder will be combined with the parent recipe.
         * {@link BaseChiselingRecipes} contains recipe locations for the default rechiseled recipes.
         * @param parent the parent recipe location
         * @throws IllegalArgumentException when {@code parent} recipe does not exist
         * @deprecated
         */
        @Deprecated
        public ChiselingRecipeBuilder parent(ResourceLocation parent){
            this.parent = parent;
            return this;
        }

        /**
         * Sets the overwrite flag for this recipe builder.
         * If overwrite is true, any entries that came before this one in the resource stack will be discarded.
         * The overwrite flag works similarly to the 'replace' key for tags.
         * @param overwrite whether the lower level resources' entries should be overwritten
         */
        public void overwrite(boolean overwrite){
            this.overwrite = overwrite;
        }

        /**
         * Add a new entry to this recipe builder.
         * Each entry can have a regular variant and a variant with connecting textures.
         * @param regularItem    the regular variant, i.e. without connecting textures
         * @param connectingItem the variant with connecting textures
         * @param optional       whether the recipe may ignore the entry when the entry's items are not present
         * @throws IllegalArgumentException when both {@code regularItem} and {@code connectingItem} are {@code null}
         */
        public ChiselingRecipeBuilder add(@Nullable Item regularItem, int regularItemData, @Nullable Item connectingItem, int connectingItemData, boolean optional){
            if(regularItem == null && connectingItem == null)
                throw new IllegalArgumentException("At least one of regular item or connecting item must not be null!");

            this.entries.add(new Triple<>(regularItem == null ? null : new ItemStack(regularItem, 1, regularItemData), connectingItem == null ? null : new ItemStack(connectingItem, 1, connectingItemData), optional));
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
            return this.add(regularItem, 0, connectingItem, 0, optional);
        }

        /**
         * Adds a new entry to this recipe builder.
         * Each entry can have a regular variant and a variant with connecting textures.
         * @param regularItem    the regular variant, i.e. without connecting textures
         * @param connectingItem the variant with connecting textures
         * @throws IllegalArgumentException when both {@code regularItem} and {@code connectingItem} are {@code null}
         */
        public ChiselingRecipeBuilder add(Item regularItem, int regularItemData, Item connectingItem, int connectingItemData){
            return this.add(regularItem, regularItemData, connectingItem, connectingItemData, false);
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
        public ChiselingRecipeBuilder addRegularItem(Item item, int data, boolean optional){
            if(item == null)
                throw new IllegalArgumentException("Item must not be null!");

            return this.add(item, data, null, 0, optional);
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
        public ChiselingRecipeBuilder addRegularItem(Item item, int data){
            return this.addRegularItem(item, data, false);
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
        public ChiselingRecipeBuilder addConnectingItem(Item item, int data, boolean optional){
            if(item == null)
                throw new IllegalArgumentException("Item must not be null!");

            return this.add(null, 0, item, data, optional);
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
        public ChiselingRecipeBuilder addConnectingItem(Item item, int data){
            return this.addConnectingItem(item, data, false);
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
