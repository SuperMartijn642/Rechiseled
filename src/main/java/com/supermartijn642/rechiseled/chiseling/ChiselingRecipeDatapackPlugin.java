package com.supermartijn642.rechiseled.chiseling;

import com.google.gson.*;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;
import com.supermartijn642.rechiseled.api.chiseling.plugin.ChiselingRecipeMutationContext;
import com.supermartijn642.rechiseled.api.chiseling.plugin.ChiselingRecipePlugin;
import com.supermartijn642.rechiseled.api.chiseling.plugin.MutableChiselingRecipe;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * Created 18/01/2022 by SuperMartijn642
 */
public class ChiselingRecipeDatapackPlugin implements IFutureReloadListener, ChiselingRecipePlugin {

    public static final ResourceLocation IDENTIFIER = Rechiseled.identifier("datapacks");
    public static final ChiselingRecipeDatapackPlugin INSTANCE = new ChiselingRecipeDatapackPlugin();
    private static final Gson GSON = new GsonBuilder().setLenient().create();

    private List<ChiselingRecipeProperties> chiselingRecipes;

    private ChiselingRecipeDatapackPlugin(){
    }

    @Override
    public void mutateRecipes(ChiselingRecipeMutationContext context){
        if(this.chiselingRecipes == null)
            throw new IllegalStateException("Chiseling recipes have not been loaded!");

        // Create a new recipe for every recipe properties instance
        for(ChiselingRecipeProperties properties : this.chiselingRecipes){
            MutableChiselingRecipe recipe = context.getOrCreateRecipe(properties.identifier);
            for(ChiselingEntry entry : properties.entries){
                MutableChiselingRecipe.EntryBuilder builder = recipe.newEntry();
                for(ChiselingBlockShape shape : ChiselingBlockShape.values()){
                    if(entry.hasRegularItem(shape)){
                        ItemWithWorth regularItem = entry.getRegularItem(shape);
                        //noinspection DataFlowIssue
                        builder.regularItem(shape, regularItem.item(), regularItem.worth());
                    }
                    if(entry.hasConnectingItem(shape)){
                        ItemWithWorth connectingItem = entry.getConnectingItem(shape);
                        //noinspection DataFlowIssue
                        builder.connectingItem(shape, connectingItem.item(), connectingItem.worth());
                    }
                }
                builder.submit();
            }
        }

        this.chiselingRecipes = null;
    }

    @Override
    public CompletableFuture<Void> reload(IStage preparationBarrier, IResourceManager resourceManager, IProfiler profilerFiller, IProfiler profilerFiller2, Executor executor, Executor executor2){
        List<CompletableFuture<ChiselingRecipeProperties>> recipes = resourceManager.listResources("chiseling_recipes", s -> s.endsWith(".json")).stream()
            .map(location -> CompletableFuture.supplyAsync(() -> loadRecipe(resourceManager, location), executor))
            .collect(Collectors.toList());
        return CompletableFuture.allOf(recipes.toArray(new CompletableFuture[0]))
            .thenApplyAsync(o -> recipes.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toList()), executor)
            .thenCompose(preparationBarrier::wait)
            .thenAcceptAsync(resolvedRecipes -> this.chiselingRecipes = resolvedRecipes, executor2);
    }

    private static ChiselingRecipeProperties loadRecipe(IResourceManager resourceManager, ResourceLocation recipeLocation){
        List<ChiselingEntry> combinedEntries = new ArrayList<>();
        try{
            // Loop over the resource stack for the recipe location
            for(IResource resource : resourceManager.getResources(recipeLocation)){
                JsonObject json;
                try(Reader reader = new InputStreamReader(resource.getInputStream())){
                    json = GSON.fromJson(reader, JsonObject.class);
                }catch(Exception e){
                    Rechiseled.LOGGER.error("Invalid json for chiseling recipe '{}' from '{}'!", recipeLocation, resource.getSourceName(), e);
                    return null;
                }

                // Read recipe from json
                boolean overwrite = false;
                List<ChiselingEntry> entries = new ArrayList<>();
                try{
                    if(!json.has("overwrite") && !json.has("entries"))
                        throw new JsonParseException("Missing 'overwrite' or 'entries' property!");
                    if(json.has("overwrite")){
                        if(!json.getAsJsonPrimitive("overwrite").isBoolean())
                            throw new JsonParseException("Property 'overwrite' must be a boolean!");
                        overwrite = json.get("overwrite").getAsBoolean();
                    }
                    if(json.has("entries")){
                        if(!json.get("entries").isJsonArray())
                            throw new JsonParseException("Property 'entries' must be an array!");
                        for(JsonElement element : json.getAsJsonArray("entries")){
                            ChiselingEntry entry = ChiselingEntryImpl.fromJson(element);
                            if(entry != null)
                                entries.add(entry);
                        }
                    }
                }catch(JsonParseException e){
                    Rechiseled.LOGGER.error("Invalid chiseling recipe '{}' from '{}':\n   {}", recipeLocation, resource.getSourceName(), e.getMessage(), e);
                    return null;
                }

                if(overwrite)
                    combinedEntries.clear();
                combinedEntries.addAll(entries);
            }
        }catch(Exception e){
            Rechiseled.LOGGER.error("Encountered an exception whilst trying to load chiseling recipe '{}'!", recipeLocation, e);
            return null;
        }
        String recipeIdentifier = recipeLocation.getPath().substring("chiseling_recipes/".length(), recipeLocation.getPath().length() - ".json".length());
        return new ChiselingRecipeProperties(
            new ResourceLocation(recipeLocation.getNamespace(), recipeIdentifier),
            combinedEntries
        );
    }

    private static class ChiselingRecipeProperties {

        private final ResourceLocation identifier;
        private final List<ChiselingEntry> entries;

        private ChiselingRecipeProperties(ResourceLocation identifier, List<ChiselingEntry> entries){
            this.identifier = identifier;
            this.entries = entries;
        }
    }
}
