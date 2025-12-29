package com.supermartijn642.rechiseled.chiseling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.Strictness;
import com.supermartijn642.rechiseled.Rechiseled;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.io.Reader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * Created 18/01/2022 by SuperMartijn642
 */
public class ChiselingRecipeLoader implements PreparableReloadListener {

    public static void addListeners(){
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> onDataPackSync(player));
        ResourceLoader.get(PackType.SERVER_DATA).registerReloader(Identifier.fromNamespaceAndPath("rechiseled", "chiseling_recipe_loader"), new ChiselingRecipeLoader());
    }

    public static void onDataPackSync(Player player){
        if(player == null)
            Rechiseled.CHANNEL.sendToAllPlayers(new PacketChiselingRecipes(ChiselingRecipes.getAllRecipes()));
        else
            Rechiseled.CHANNEL.sendToPlayer(player, new PacketChiselingRecipes(ChiselingRecipes.getAllRecipes()));
    }

    private static final Gson GSON = new GsonBuilder().setStrictness(Strictness.LENIENT).create();

    @Override
    public CompletableFuture<Void> reload(SharedState sharedState, Executor executor, PreparationBarrier preparationBarrier, Executor executor2){
        ResourceManager resourceManager = sharedState.resourceManager();
        List<CompletableFuture<ChiselingRecipe>> recipes = resourceManager.listResources("chiseling_recipes", r -> r.getPath().endsWith(".json")).keySet().stream()
            .map(location -> CompletableFuture.supplyAsync(() -> loadRecipe(resourceManager, location), executor))
            .toList();
        return CompletableFuture.allOf(recipes.toArray(CompletableFuture[]::new))
            .thenApplyAsync(o -> mergeRecipes(recipes.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toList())), executor)
            .thenCompose(preparationBarrier::wait)
            .thenAcceptAsync(resolvedRecipes -> {
                Rechiseled.LOGGER.info("Loaded " + resolvedRecipes.size() + " chiseling recipes");
                ChiselingRecipes.setRecipes(resolvedRecipes);
            }, executor2);
    }

    private static ChiselingRecipe loadRecipe(ResourceManager resourceManager, Identifier recipeLocation){
        Identifier parentRecipe = null;
        List<ChiselingEntry> entries = new ArrayList<>();
        try{
            // Loop over the resource stack for the recipe location
            for(Resource resource : resourceManager.getResourceStack(recipeLocation)){
                JsonObject json;
                try(Reader reader = resource.openAsReader()){
                    json = GSON.fromJson(reader, JsonObject.class);
                }catch(Exception e){
                    Rechiseled.LOGGER.error("Encountered an exception whilst trying to load chiseling recipe json '" + recipeLocation + "' from '" + resource.sourcePackId() + "'!", e);
                    break;
                }

                ChiselingRecipe recipe = ChiselingRecipe.Serializer.fromJson(recipeLocation, json);
                // Check overwrite
                if(recipe.overwrite)
                    entries.clear();
                // Add entries
                entries.addAll(recipe.getEntries());
                // Check parent
                if(recipe.parentRecipeId != null){
                    Rechiseled.LOGGER.warn("Chiseling recipe '" + recipe.getRecipeId() + "' from '" + resource.sourcePackId() + "' uses the 'parent' field! This will be removed in Rechiseled 1.2.0. Recipes automatically get merged based on entries!");
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
        Map<Identifier,Set<ChiselingEntry>> recipeEntries = new LinkedHashMap<>();
        recipes.stream()
            .sorted(Comparator.comparing(r -> r.getRecipeId().toString()))
            .forEach(recipe -> recipeEntries.put(recipe.getRecipeId(), new LinkedHashSet<>(recipe.getEntries())));

        // Merge parent recipes TODO remove in 1.2.0
        Map<Identifier,ChiselingRecipe> recipesWithParent = new LinkedHashMap<>();
        for(ChiselingRecipe recipe : recipes){
            if(recipe.parentRecipeId != null)
                recipesWithParent.put(recipe.getRecipeId(), recipe);
        }
        loop:
        for(ChiselingRecipe recipe : recipesWithParent.values()){
            // Keep track of which recipe ids have been covered
            Set<Identifier> coveredRecipes = new HashSet<>();

            Identifier parentRecipe = recipe.parentRecipeId;
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
        Map<Identifier,Set<Item>> itemsPerRecipe = new HashMap<>();
        for(Map.Entry<Identifier,Set<ChiselingEntry>> recipe : recipeEntries.entrySet()){
            HashSet<Item> items = new HashSet<>();
            recipe.getValue().forEach(e -> {
                if(e.hasRegularItem())
                    items.add(e.getRegularItem());
                if(e.hasConnectingItem())
                    items.add(e.getConnectingItem());
            });
            itemsPerRecipe.put(recipe.getKey(), items);
        }
        Identifier[] locations = recipeEntries.keySet().toArray(Identifier[]::new);
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
        for(Identifier location : recipeEntries.keySet()){
            Set<ChiselingEntry> entries = recipeEntries.get(location);
            HashMap<Item,Integer> itemCounts = new HashMap<>();
            itemsPerRecipe.get(location).forEach(item -> itemCounts.compute(item, (i, c) -> c == null ? 1 : c + 1));

            List<ChiselingEntry> toRemove = new LinkedList<>();
            for(ChiselingEntry entry : entries){
                if(!entry.hasRegularItem() && itemCounts.get(entry.getConnectingItem()) > 1){
                    toRemove.add(entry);
                    itemCounts.compute(entry.getConnectingItem(), (i, c) -> c - 1);
                }else if(!entry.hasConnectingItem() && itemCounts.get(entry.getRegularItem()) > 1){
                    toRemove.add(entry);
                    itemCounts.compute(entry.getRegularItem(), (i, c) -> c - 1);
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
