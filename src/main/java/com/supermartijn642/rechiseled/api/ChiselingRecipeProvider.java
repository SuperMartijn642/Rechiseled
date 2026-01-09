package com.supermartijn642.rechiseled.api;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;
import com.supermartijn642.rechiseled.api.chiseling.data.ChiselingEntryBuilder;
import com.supermartijn642.rechiseled.chiseling.data.ChiselingEntryBuilderImpl;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Created 24/12/2021 by SuperMartijn642
 */
public abstract class ChiselingRecipeProvider implements DataProvider {

    private final String modid;
    private final DataGenerator generator;
    private final ExistingFileHelper existingFileHelper;
    private final Map<ResourceLocation,ChiselingRecipeBuilder> recipes = new HashMap<>();

    public ChiselingRecipeProvider(String modid, DataGenerator generator, ExistingFileHelper existingFileHelper){
        this.modid = modid;
        this.generator = generator;
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    public String getName(){
        return "Chiseling Recipes: " + this.modid;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache){
        this.buildRecipes();

        Path path = this.generator.getPackOutput().getOutputFolder();
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        for(Map.Entry<ResourceLocation,ChiselingRecipeBuilder> entry : this.recipes.entrySet()){
            ResourceLocation recipeName = entry.getKey();
            ChiselingRecipeBuilder recipe = entry.getValue();

            // Write the recipe
            JsonObject json = serializeRecipe(recipeName, recipe);
            Path recipePath = path.resolve("data/" + recipeName.getNamespace() + "/chiseling_recipes/" + recipeName.getPath() + ".json");
            tasks.add(DataProvider.saveStable(cache, json, recipePath));
        }
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    private static JsonObject serializeRecipe(ResourceLocation recipeName, ChiselingRecipeBuilder recipe){
        JsonObject json = new JsonObject();

        json.addProperty("type", Rechiseled.identifier("chiseling").toString());
        json.addProperty("overwrite", recipe.overwrite);

        Set<Item> items = Sets.newHashSet();
        JsonArray entries = new JsonArray();
        for(ChiselingEntryBuilderImpl entry : recipe.entries){
            if(entry.items.isEmpty() && entry.connectingItems.isEmpty())
                throw new IllegalStateException("Entry for recipe '" + recipeName + "' has no items!");
            if(entry.items.containsKey(ChiselingBlockShape.BLOCK) && entry.items.size() == 1 && entry.items.get(ChiselingBlockShape.BLOCK).worth() == 1 && entry.connectingItems.isEmpty() && !entry.optional){
                ItemWithWorth item = entry.items.get(ChiselingBlockShape.BLOCK);
                if(!items.add(item.item()))
                    throw new IllegalStateException("Duplicate item '" + Registries.ITEMS.getIdentifier(item.item()) + "' in chiseling recipe '" + recipeName + "'");
                entries.add(Registries.ITEMS.getIdentifier(item.item()).toString());
                continue;
            }
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

    private static void serializeItem(JsonObject json, String key, ItemWithWorth item, Set<Item> items, ResourceLocation recipeName){
        if(!items.add(item.item()))
            throw new IllegalStateException("Duplicate item '" + Registries.ITEMS.getIdentifier(item.item()) + "' in chiseling recipe '" + recipeName + "'");
        json.addProperty(key, Registries.ITEMS.getIdentifier(item.item()).toString());
        if(item.worth() != 1)
            json.addProperty(key + "_worth", item.worth());
    }

    private void trackRecipe(ResourceLocation recipe){
        this.existingFileHelper.trackGenerated(recipe, PackType.SERVER_DATA, ".json", "chiseling_recipes");
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
        this.trackRecipe(new ResourceLocation(this.modid, recipeName));
        return this.recipes.computeIfAbsent(new ResourceLocation(this.modid, recipeName), s -> new ChiselingRecipeBuilder());
    }

    /**
     * Creates a new chiseling recipe builder.
     * Entries can be added to the recipe through {@link ChiselingRecipeBuilder#entry()}.
     * @param recipe the identifier of the recipe
     * @return a chiseling recipe builder for the given recipe identifier
     */
    protected ChiselingRecipeBuilder beginRecipe(ResourceLocation recipe){
        this.trackRecipe(recipe);
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
        public ChiselingRecipeBuilder add(@Nullable Item regularBlock, @Nullable Item connectingBlock, boolean optional){
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
        public ChiselingRecipeBuilder add(Item regularBlock, Item connectingBlock){
            return this.add(regularBlock, connectingBlock, false);
        }

        /**
         * @deprecated Use {@link #entry()}.
         */
        @Deprecated
        public ChiselingRecipeBuilder addRegularItem(Item item, boolean optional){
            if(item == null)
                throw new IllegalArgumentException("Item must not be null!");

            return this.add(item, null, optional);
        }

        /**
         * @deprecated Use {@link #entry()}.
         */
        @Deprecated
        public ChiselingRecipeBuilder addRegularItem(Item item){
            return this.addRegularItem(item, false);
        }

        /**
         * @deprecated Use {@link #entry()}.
         */
        @Deprecated
        public ChiselingRecipeBuilder addConnectingItem(Item item, boolean optional){
            if(item == null)
                throw new IllegalArgumentException("Item must not be null!");

            return this.add(null, item, optional);
        }

        /**
         * @deprecated Use {@link #entry()}.
         */
        @Deprecated
        public ChiselingRecipeBuilder addConnectingItem(Item item){
            return this.addConnectingItem(item, false);
        }
    }

}
