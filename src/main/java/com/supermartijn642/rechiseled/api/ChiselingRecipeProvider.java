package com.supermartijn642.rechiseled.api;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.ResourceGenerator;
import com.supermartijn642.core.generator.ResourceType;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.core.registry.RegistryUtil;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;
import com.supermartijn642.rechiseled.api.chiseling.data.ChiselingEntryBuilder;
import com.supermartijn642.rechiseled.api.util.ItemWithMeta;
import com.supermartijn642.rechiseled.chiseling.data.ChiselingEntryBuilderImpl;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

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
            ChiselingRecipeBuilder recipe = entry.getValue();

            // Write the recipe
            JsonObject json = serializeRecipe(recipeName, recipe);
            this.cache.saveJsonResource(ResourceType.ASSET, json, recipeName.getResourceDomain(), "chiseling_recipes", recipeName.getResourcePath());
        }
    }

    private static JsonObject serializeRecipe(ResourceLocation recipeName, ChiselingRecipeBuilder recipe){
        JsonObject json = new JsonObject();

        json.addProperty("type", Rechiseled.identifier("chiseling").toString());
        json.addProperty("overwrite", recipe.overwrite);

        Set<ItemWithMeta> items = Sets.newHashSet();
        JsonArray entries = new JsonArray();
        for(ChiselingEntryBuilderImpl entry : recipe.entries){
            if(entry.items.isEmpty() && entry.connectingItems.isEmpty())
                throw new IllegalStateException("Entry for recipe '" + recipeName + "' has no items!");
            JsonObject object = new JsonObject();
            if(entry.items.containsKey(ChiselingBlockShape.BLOCK))
                serializeItem(object, "block", entry.items.get(ChiselingBlockShape.BLOCK), items, recipeName);
            if(entry.items.containsKey(ChiselingBlockShape.STAIRS))
                serializeItem(object, "stairs", entry.items.get(ChiselingBlockShape.STAIRS), items, recipeName);
            if(entry.items.containsKey(ChiselingBlockShape.SLAB))
                serializeItem(object, "slab", entry.items.get(ChiselingBlockShape.SLAB), items, recipeName);
            if(entry.connectingItems.containsKey(ChiselingBlockShape.BLOCK))
                serializeItem(object, "connecting_block", entry.connectingItems.get(ChiselingBlockShape.BLOCK), items, recipeName);
            if(entry.connectingItems.containsKey(ChiselingBlockShape.STAIRS))
                serializeItem(object, "connecting_stairs", entry.connectingItems.get(ChiselingBlockShape.STAIRS), items, recipeName);
            if(entry.connectingItems.containsKey(ChiselingBlockShape.SLAB))
                serializeItem(object, "connecting_slab", entry.connectingItems.get(ChiselingBlockShape.SLAB), items, recipeName);
            if(entry.optional)
                object.addProperty("optional", true);
            entries.add(object);
        }

        json.add("entries", entries);
        return json;
    }

    private static void serializeItem(JsonObject json, String key, ItemWithWorth item, Set<ItemWithMeta> items, ResourceLocation recipeName){
        if(!items.add(item.item()))
            throw new IllegalStateException("Duplicate item '" + item + "' in chiseling recipe '" + recipeName + "'");
        json.addProperty(key, Registries.ITEMS.getIdentifier(item.item().item()).toString());
        if(item.item().hasSubtypes())
            json.addProperty(key + "_meta", item.item().meta());
        if(item.worth() != 1)
            json.addProperty(key + "_worth", item.worth());
    }

    /**
     * Recipes can be created using a recipe builder obtained from {@link #beginRecipe(String)}.
     * All recipe builders will be saved and written to file automatically.
     */
    protected abstract void buildRecipes();

    /**
     * Creates a new chiseling recipe builder.
     * Entries can be added to the recipe through {@link ChiselingRecipeBuilder#entry()}.
     * @param recipeName the name of the recipe
     * @return a chiseling recipe builder for the given recipe name
     */
    protected ChiselingRecipeBuilder beginRecipe(String recipeName){
        if(!RegistryUtil.isValidPath(recipeName))
            throw new IllegalArgumentException("Recipe name must be a valid resource location path, not '" + recipeName + "'!");

        this.cache.trackToBeGeneratedResource(ResourceType.ASSET, this.modid, "chiseling_recipes", recipeName, ".json");
        return this.recipes.computeIfAbsent(new ResourceLocation(this.modid, recipeName), s -> new ChiselingRecipeBuilder());
    }

    /**
     * Creates a new chiseling recipe builder.
     * Entries can be added to the recipe through {@link ChiselingRecipeBuilder#entry()}.
     * @param recipe the identifier of the recipe
     * @return a chiseling recipe builder for the given recipe identifier
     */
    protected ChiselingRecipeBuilder beginRecipe(ResourceLocation recipe){
        this.cache.trackToBeGeneratedResource(ResourceType.ASSET, recipe.getResourceDomain(), "chiseling_recipes", recipe.getResourcePath(), ".json");
        return this.recipes.computeIfAbsent(recipe, s -> new ChiselingRecipeBuilder());
    }

    public static class ChiselingRecipeBuilder {

        private final List<ChiselingEntryBuilderImpl> entries = new LinkedList<>();
        private boolean overwrite = false;

        private ChiselingRecipeBuilder(){
        }

        /**
         * Sets the overwrite flag for this recipe builder.
         * If overwrite is true, any entries that came before this one in the resource stack will be discarded.
         * <p>
         * The overwrite flag works similarly to the 'replace' key for tags.
         * @param overwrite whether the lower level resources' entries should be overwritten
         */
        public void overwrite(boolean overwrite){
            this.overwrite = overwrite;
        }

        /**
         * Creates a new entry builder for this recipe.
         * <p>
         * An entry consists of a regular and a connecting item for each {@link ChiselingBlockShape}.
         * The connecting item is typically the variant of the regular item with connecting textures.<br>
         * An entry must have at least one item.
         * @see ChiselingBlockShape
         */
        public ChiselingEntryBuilder entry(){
            ChiselingEntryBuilderImpl entry = new ChiselingEntryBuilderImpl();
            this.entries.add(entry);
            return entry;
        }

        /**
         * Creates a new entry builder for this recipe that is configured through the given builder.
         */
        public ChiselingRecipeBuilder entry(Consumer<ChiselingEntryBuilder> builder){
            builder.accept(this.entry());
            return this;
        }

        /**
         * @deprecated Use {@link #entry()}.
         */
        @Deprecated
        public ChiselingRecipeBuilder add(@Nullable ItemWithMeta regularBlock, @Nullable ItemWithMeta connectingBlock, boolean optional){
            if(regularBlock == null && connectingBlock == null)
                throw new IllegalArgumentException("At least one of regular item or connecting item must not be null!");

            ChiselingEntryBuilder entry = this.entry().optional(optional);
            if(regularBlock != null)
                entry.regularItem(ChiselingBlockShape.BLOCK, regularBlock);
            if(connectingBlock != null)
                entry.connectingItem(ChiselingBlockShape.BLOCK, connectingBlock);
            return this;
        }

        /**
         * @deprecated Use {@link #entry()}.
         */
        @Deprecated
        public ChiselingRecipeBuilder add(ItemWithMeta regularBlock, ItemWithMeta connectingBlock){
            return this.add(regularBlock, connectingBlock, false);
        }

        /**
         * @deprecated Use {@link #entry()}.
         */
        @Deprecated
        public ChiselingRecipeBuilder addRegularItem(ItemWithMeta item, boolean optional){
            if(item == null)
                throw new IllegalArgumentException("Item must not be null!");

            return this.add(item, null, optional);
        }

        /**
         * @deprecated Use {@link #entry()}.
         */
        @Deprecated
        public ChiselingRecipeBuilder addRegularItem(ItemWithMeta item){
            return this.addRegularItem(item, false);
        }

        /**
         * @deprecated Use {@link #entry()}.
         */
        @Deprecated
        public ChiselingRecipeBuilder addConnectingItem(ItemWithMeta item, boolean optional){
            if(item == null)
                throw new IllegalArgumentException("Item must not be null!");

            return this.add(null, item, optional);
        }

        /**
         * @deprecated Use {@link #entry()}.
         */
        @Deprecated
        public ChiselingRecipeBuilder addConnectingItem(ItemWithMeta item){
            return this.addConnectingItem(item, false);
        }
    }

}
