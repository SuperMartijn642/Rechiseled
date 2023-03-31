package com.supermartijn642.rechiseled.chiseling;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.supermartijn642.rechiseled.Rechiseled;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;

import java.util.*;

/**
 * Created 18/01/2022 by SuperMartijn642
 */
public class ChiselingRecipeLoader extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {

    public static void addListeners(){
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> onDataPackSync(player));
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new ChiselingRecipeLoader());
    }

    public static void onDataPackSync(Player player){
        if(player == null)
            Rechiseled.CHANNEL.sendToAllPlayers(new PacketChiselingRecipes(ChiselingRecipes.getAllRecipes()));
        else
            Rechiseled.CHANNEL.sendToPlayer(player, new PacketChiselingRecipes(ChiselingRecipes.getAllRecipes()));
    }

    private static final Gson GSON = new GsonBuilder().setLenient().create();

    public ChiselingRecipeLoader(){
        super(GSON, "chiseling_recipes");
    }

    @Override
    protected void apply(Map<ResourceLocation,JsonElement> entries, ResourceManager resourceManager, ProfilerFiller profilerFiller){
        Map<ResourceLocation,ChiselingRecipe> recipes = Maps.newHashMap();
        List<ChiselingRecipe> recipesWithoutParent = new ArrayList<>();
        List<ChiselingRecipe> recipesWithParent = new ArrayList<>();

        // Load all recipes from json
        for(Map.Entry<ResourceLocation,JsonElement> entry : entries.entrySet()){

            ResourceLocation recipeId = entry.getKey();
            JsonObject json = entry.getValue().getAsJsonObject();
            ChiselingRecipe recipe;
            try{
                recipe = ChiselingRecipe.Serializer.fromJson(entry.getKey(), json);
            }catch(Exception e){
                System.err.println("Encountered an exception when trying to load chiseling recipe: " + recipeId);
                e.printStackTrace();
                continue;
            }

            recipes.put(recipeId, recipe);
            if(recipe.parentRecipeId == null)
                recipesWithoutParent.add(recipe);
            else
                recipesWithParent.add(recipe);
        }

        // Combine recipes
        int successfullyLoadedRecipes = recipesWithoutParent.size();
        loop:
        for(ChiselingRecipe recipe : recipesWithParent){
            // Keep track of which recipe ids have been covered
            Set<ResourceLocation> coveredRecipes = new HashSet<>();
            List<ChiselingEntry> chiselingEntries = recipe.getEntries();

            ChiselingRecipe parentRecipe = recipe;

            while(parentRecipe.parentRecipeId != null){
                coveredRecipes.add(parentRecipe.getRecipeId());

                ResourceLocation parentId = parentRecipe.parentRecipeId;
                // Check for loop
                if(coveredRecipes.contains(parentId)){
                    System.err.println("Found circular parent references when trying to load chiseling recipe: " + recipe.getRecipeId());
                    continue loop;
                }

                parentRecipe = recipes.get(parentId);
                // Check if parent exists
                if(parentRecipe == null){
                    System.err.println("Could not find parent '" + parentId + "' when trying to load chiseling recipe: " + recipe.getRecipeId());
                    continue loop;
                }
            }

            // Finally, add the entries to the greatest parent
            parentRecipe.addEntries(chiselingEntries);
            successfullyLoadedRecipes++;
        }

        // Apply the loaded recipes
        System.out.println("Loaded " + successfullyLoadedRecipes + " chiseling recipes");
        ChiselingRecipes.setRecipes(Collections.unmodifiableList(recipesWithoutParent));
    }

    @Override
    public ResourceLocation getFabricId(){
        return new ResourceLocation("rechiseled", "chiseling_recipe_loader");
    }
}
