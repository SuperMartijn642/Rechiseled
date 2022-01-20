package com.supermartijn642.rechiseled.chiseling;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * Created 18/01/2022 by SuperMartijn642
 */
@Mod.EventBusSubscriber
public class ChiselingRecipeLoader {

    @SubscribeEvent
    public static void onAddReloadListener(RegistryEvent.Register<IRecipe> e){
        loadChiselingRecipes();
    }

    private static final Gson GSON = new GsonBuilder().setLenient().create();

    private static void loadChiselingRecipes(){
        Map<ResourceLocation,JsonObject> files = Maps.newHashMap();

        // Collect all recipe jsons
        for(ModContainer modContainer : Loader.instance().getActiveModList())
            collectChiselingRecipesForMod(modContainer, files);

        // Load the recipes
        apply(files);
    }

    private static void collectChiselingRecipesForMod(ModContainer modContainer, Map<ResourceLocation,JsonObject> files){
        // Find all recipe jsons
        CraftingHelper.findFiles(
            modContainer,
            "assets/" + modContainer.getModId() + "/chiseling_recipes",
            null,
            (root, file) -> {
                String relative = root.relativize(file).toString();
                // Skip non json files
                if(!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
                    return true;

                String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
                ResourceLocation key = new ResourceLocation(modContainer.getModId(), name);

                try(BufferedReader reader = Files.newBufferedReader(file)){
                    // Read the json from file
                    JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
                    files.put(key, json);
                }catch(JsonParseException e){
                    FMLLog.log.error("Parsing error loading chiseling recipe {}", key, e);
                    return false;
                }catch(IOException e){
                    FMLLog.log.error("Couldn't read chiseling recipe {} from {}", key, file, e);
                    return false;
                }
                return true;
            },
            true,
            true
        );
    }

    private static void apply(Map<ResourceLocation,JsonObject> entries){
        Map<ResourceLocation,ChiselingRecipe> recipes = Maps.newHashMap();
        List<ChiselingRecipe> recipesWithoutParent = new ArrayList<>();
        List<ChiselingRecipe> recipesWithParent = new ArrayList<>();

        // Load all recipes from json
        for(Map.Entry<ResourceLocation,JsonObject> entry : entries.entrySet()){

            ResourceLocation recipeId = entry.getKey();
            JsonObject json = entry.getValue();
            ChiselingRecipe recipe;
            try{
                recipe = ChiselingRecipe.Serializer.fromJson(entry.getKey(), json);
            }catch(Exception e){
                System.out.println("Encountered an exception when trying to load chiseling recipe: " + recipeId);
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
}
