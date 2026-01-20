package com.supermartijn642.rechiseled.chiseling;

import com.google.gson.*;
import com.supermartijn642.core.registry.RegistryUtil;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.plugin.ChiselingRecipeMutationContext;
import com.supermartijn642.rechiseled.api.chiseling.plugin.ChiselingRecipePlugin;
import com.supermartijn642.rechiseled.api.chiseling.plugin.MutableChiselingRecipe;
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
public class ChiselingRecipeDatapackPlugin implements ChiselingRecipePlugin {

    @SubscribeEvent
    public static void onLoadRecipes(RegistryEvent.Register<IRecipe> e){
        INSTANCE.reload().join();
    }

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
                    if(entry.hasRegularItem(shape))
                        builder.regularItem(shape, entry.getRegularItem(shape));
                    if(entry.hasConnectingItem(shape))
                        builder.connectingItem(shape, entry.getConnectingItem(shape));
                }
                builder.submit();
            }
        }

        this.chiselingRecipes = null;
    }

    private CompletableFuture<Void> reload(){
        Map<ResourceLocation,List<Pair<ModContainer,JsonObject>>> resources = new HashMap<>();

        // Collect all recipe jsons
        List<ModContainer> mods = Loader.instance().getActiveModList();
        for(int i = mods.size() - 1; i >= 0; i--)
            collectResourcesForMod(mods.get(i), resources);

        // Load the recipes
        Executor executor = ForkJoinPool.commonPool();
        List<CompletableFuture<ChiselingRecipeProperties>> recipes = resources.keySet().stream()
            .map(location -> CompletableFuture.supplyAsync(() -> loadRecipe(location, Collections.unmodifiableList(resources.get(location))), executor))
            .collect(Collectors.toList());
        return CompletableFuture.allOf(recipes.toArray(new CompletableFuture[0]))
            .thenApplyAsync(o -> recipes.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toList()), executor)
            .thenAccept(resolvedRecipes -> this.chiselingRecipes = resolvedRecipes);
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
                    .map(name -> name.endsWith("/") ? name.substring(0, name.length() - 1) : name) // For some reason the name includes '/' for jar file systems
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
                            Rechiseled.LOGGER.error("Encountered an exception whilst trying to load chiseling recipe json '{}' from '{}'!", recipesLocation, modContainer.getName(), e);
                            return;
                        }

                        // Add the json to the map
                        resources.computeIfAbsent(recipesLocation, i -> new ArrayList<>()).add(Pair.of(modContainer, json));
                    });
                }
            }
        }catch(Exception e){
            Rechiseled.LOGGER.error("Encountered an error whilst loading resources from mod '{}'!", modContainer.getName(), e);
        }finally{
            IOUtils.closeQuietly(fs);
        }
    }

    private static ChiselingRecipeProperties loadRecipe(ResourceLocation recipeLocation, List<Pair<ModContainer,JsonObject>> resourceStack){
        List<ChiselingEntry> combinedEntries = new ArrayList<>();
        try{
            // Loop over the resource stack for the recipe location
            for(Pair<ModContainer,JsonObject> resource : resourceStack){
                ModContainer mod = resource.left();
                JsonObject json = resource.right();

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
                        for(JsonElement element : json.getAsJsonArray("entries"))
                            entries.add(ChiselingEntryImpl.fromJson(element));
                    }
                }catch(JsonParseException e){
                    Rechiseled.LOGGER.error("Invalid chiseling recipe '{}' from '{}':\n   {}", recipeLocation, mod.getModId(), e.getMessage(), e);
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
        return new ChiselingRecipeProperties(recipeLocation, combinedEntries);
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
