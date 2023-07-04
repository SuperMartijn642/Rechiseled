package com.supermartijn642.rechiseled.chiseling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.supermartijn642.core.registry.RegistryUtil;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created 18/01/2022 by SuperMartijn642
 */
@Mod.EventBusSubscriber
public class ChiselingRecipeLoader {

    @SubscribeEvent
    public static void onLoadRecipes(RegistryEvent.Register<IRecipe> e){
        loadChiselingRecipes();
    }

    private static final Gson GSON = new GsonBuilder().setLenient().create();

    private static void loadChiselingRecipes(){
        Map<ResourceLocation,List<Pair<ModContainer,JsonObject>>> resources = new HashMap<>();

        // Collect all recipe jsons
        List<ModContainer> mods = Loader.instance().getActiveModList();
        for(int i = mods.size() - 1; i >= 0; i--)
            collectResourcesForMod(mods.get(i), resources);

        // Load the recipes
        Executor executor = ForkJoinPool.commonPool();
        List<CompletableFuture<ChiselingRecipe>> recipeFutures = resources.keySet().stream()
            .map(location -> CompletableFuture.supplyAsync(() -> loadRecipe(location, Collections.unmodifiableList(resources.get(location))), executor))
            .collect(Collectors.toList());
        CompletableFuture.allOf(recipeFutures.toArray(new CompletableFuture[0])).join();
        List<ChiselingRecipe> recipes = mergeRecipes(recipeFutures.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toList()));

        // Apply the recipes
        Rechiseled.LOGGER.info("Loaded " + recipes.size() + " chiseling recipes");
        ChiselingRecipes.setRecipes(recipes);
    }

    private static void collectResourcesForMod(ModContainer modContainer, Map<ResourceLocation,List<Pair<ModContainer,JsonObject>>> resources){
        FileSystem fs = null;
        try{
            File source = modContainer.getSource();
            Path root;
            if(source.isFile()){
                try{
                    fs = FileSystems.newFileSystem(source.toPath(), null);
                    root = fs.getPath("/assets");
                }catch(IOException e){
                    FMLLog.log.error("Error loading FileSystem from jar: ", e);
                    return;
                }
            }else if(source.isDirectory())
                root = source.toPath().resolve("assets");
            else
                return;

            // If there's no 'assets' folder, simply return
            if(!Files.exists(root))
                return;

            // Find all namespaces
            List<String> namespaces;
            try(Stream<Path> files = Files.list(root)){
                namespaces = files
                    .filter(Files::isDirectory)
                    .map(p -> p.getFileName().toString())
                    .filter(RegistryUtil::isValidNamespace)
                    .collect(Collectors.toList());
            }

            // Iterate over each namespace
            for(String namespace : namespaces){
                Path folder = root.resolve(namespace).resolve("chiseling_recipes");
                if(!Files.exists(folder))
                    continue;

                // Load each file
                try(Stream<Path> paths = Files.walk(folder).filter(Files::isRegularFile).filter(p -> p.getFileName().toString().endsWith(".json"))){
                    paths.forEach((file) -> {
                        // Get the identifier for the recipe
                        String name = FilenameUtils.removeExtension(folder.relativize(file).toString()).replaceAll("\\\\", "/");
                        ResourceLocation recipesLocation = new ResourceLocation(modContainer.getModId(), name);

                        // Read the json from file
                        JsonObject json;
                        try(Reader reader = Files.newBufferedReader(file)){
                            json = GSON.fromJson(reader, JsonObject.class);
                        }catch(Exception e){
                            Rechiseled.LOGGER.error("Encountered an exception whilst trying to load chiseling recipe json '" + recipesLocation + "' from '" + modContainer.getName() + "'!", e);
                            return;
                        }

                        // Add the json to the map
                        resources.computeIfAbsent(recipesLocation, i -> new ArrayList<>()).add(Pair.of(modContainer, json));
                    });
                }
            }
        }catch(Exception e){
            Rechiseled.LOGGER.error("Encountered an error whilst loading resources from mod '" + modContainer.getName() + "'!", e);
        }finally{
            IOUtils.closeQuietly(fs);
        }
    }

    private static ChiselingRecipe loadRecipe(ResourceLocation recipeLocation, List<Pair<ModContainer,JsonObject>> resourceStack){
        ResourceLocation parentRecipe = null;
        List<ChiselingEntry> entries = new ArrayList<>();
        try{
            // Loop over the resource stack for the recipe location
            for(Pair<ModContainer,JsonObject> resource : resourceStack){
                JsonObject json = resource.right();
                ChiselingRecipe recipe = ChiselingRecipe.Serializer.fromJson(recipeLocation, json);
                // Check overwrite
                if(recipe.overwrite)
                    entries.clear();
                // Add entries
                entries.addAll(recipe.getEntries());
                // Check parent
                if(recipe.parentRecipeId != null){
                    Rechiseled.LOGGER.warn("Chiseling recipe '" + recipe.getRecipeId() + "' from '" + resource.left().getName() + "' uses the 'parent' field! This will be removed in Rechiseled 1.2.0. Recipes automatically get merged based on entries!");
                    parentRecipe = recipe.parentRecipeId;
                    break;
                }
            }
        }catch(Exception e){
            Rechiseled.LOGGER.error("Encountered an exception whilst trying to load chiseling recipe '" + recipeLocation + "'!", e);
            return null;
        }
        return new ChiselingRecipe(recipeLocation, parentRecipe, entries);
    }

    private static List<ChiselingRecipe> mergeRecipes(List<ChiselingRecipe> recipes){
        // Keep track of the entries of all recipes
        Map<ResourceLocation,Set<ChiselingEntry>> recipeEntries = new LinkedHashMap<>();
        recipes.stream()
            .sorted(Comparator.comparing(r -> r.getRecipeId().toString()))
            .forEach(recipe -> recipeEntries.put(recipe.getRecipeId(), new LinkedHashSet<>(recipe.getEntries())));

        // Merge parent recipes TODO remove in 1.2.0
        Map<ResourceLocation,ChiselingRecipe> recipesWithParent = new LinkedHashMap<>();
        for(ChiselingRecipe recipe : recipes){
            if(recipe.parentRecipeId != null)
                recipesWithParent.put(recipe.getRecipeId(), recipe);
        }
        loop:
        for(ChiselingRecipe recipe : recipesWithParent.values()){
            // Keep track of which recipe ids have been covered
            Set<ResourceLocation> coveredRecipes = new HashSet<>();

            ResourceLocation parentRecipe = recipe.parentRecipeId;
            while(recipesWithParent.containsKey(parentRecipe)){
                // Check for loop
                coveredRecipes.add(parentRecipe);
                if(coveredRecipes.contains(parentRecipe)){
                    Rechiseled.LOGGER.error("Found circular parent references when trying to load chiseling recipe '" + recipe.getRecipeId() + "': " + coveredRecipes);
                    continue loop;
                }

                parentRecipe = recipesWithParent.get(parentRecipe).parentRecipeId;
            }

            // Check if parent exists
            if(parentRecipe == null){
                Rechiseled.LOGGER.error("Could not find parent '" + parentRecipe + "' when trying to load chiseling recipe '" + recipe.getRecipeId() + "'!");
                continue;
            }

            // Finally, add the entries to the greatest parent
            recipeEntries.get(parentRecipe).addAll(recipe.getEntries());
        }

        // Merge recipes based on entries
        Map<ResourceLocation,Set<Pair<Item,Integer>>> itemsPerRecipe = new HashMap<>();
        for(Map.Entry<ResourceLocation,Set<ChiselingEntry>> recipe : recipeEntries.entrySet()){
            HashSet<Pair<Item,Integer>> items = new HashSet<>();
            recipe.getValue().forEach(e -> {
                if(e.hasRegularItem())
                    items.add(Pair.of(e.getRegularItem(), e.getRegularItemData()));
                if(e.hasConnectingItem())
                    items.add(Pair.of(e.getConnectingItem(), e.getConnectingItemData()));
            });
            itemsPerRecipe.put(recipe.getKey(), items);
        }
        ResourceLocation[] locations = recipeEntries.keySet().toArray(new ResourceLocation[0]);
        loop:
        for(int i = 0; i < locations.length; i++){
            for(int j = i + 1; j < locations.length; j++){
                // Check if the recipes contain overlapping items
                if(!Collections.disjoint(itemsPerRecipe.get(locations[i]), itemsPerRecipe.get(locations[j]))){
                    // Merge the recipes
                    recipeEntries.get(locations[j]).addAll(recipeEntries.get(locations[i]));
                    recipeEntries.remove(locations[i]);
                    itemsPerRecipe.get(locations[j]).addAll(itemsPerRecipe.get(locations[i]));
                    itemsPerRecipe.remove(locations[i]);
                    continue loop;
                }
            }
        }

        // Remove unnecessary entries
        for(ResourceLocation location : recipeEntries.keySet()){
            Set<ChiselingEntry> entries = recipeEntries.get(location);
            HashMap<Pair<Item,Integer>,Integer> itemCounts = new HashMap<>();
            itemsPerRecipe.get(location).forEach(item -> itemCounts.compute(item, (i, c) -> c == null ? 1 : c + 1));

            List<ChiselingEntry> toRemove = new LinkedList<>();
            for(ChiselingEntry entry : entries){
                if(!entry.hasRegularItem() && itemCounts.get(Pair.of(entry.getConnectingItem(), entry.getConnectingItemData())) > 1){
                    toRemove.add(entry);
                    itemCounts.compute(Pair.of(entry.getConnectingItem(), entry.getConnectingItemData()), (i, c) -> c - 1);
                }else if(!entry.hasConnectingItem() && itemCounts.get(Pair.of(entry.getRegularItem(), entry.getRegularItemData())) > 1){
                    toRemove.add(entry);
                    itemCounts.compute(Pair.of(entry.getRegularItem(), entry.getRegularItemData()), (i, c) -> c - 1);
                }
            }
            toRemove.forEach(entries::remove);
        }

        // Finally, create new recipes
        recipes = recipeEntries.entrySet().stream()
            .map(entry -> new ChiselingRecipe(entry.getKey(), null, entry.getValue()))
            .collect(Collectors.toList());
        return recipes;
    }
}
