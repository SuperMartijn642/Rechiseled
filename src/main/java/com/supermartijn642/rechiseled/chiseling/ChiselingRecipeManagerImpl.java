package com.supermartijn642.rechiseled.chiseling;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.supermartijn642.core.registry.RegistryUtil;
import com.supermartijn642.core.util.Holder;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipeManager;
import com.supermartijn642.rechiseled.api.chiseling.plugin.ChiselingRecipePlugin;
import com.supermartijn642.rechiseled.api.chiseling.plugin.ChiselingRecipesLoadedContext;
import com.supermartijn642.rechiseled.api.chiseling.plugin.MutableChiselingRecipe;
import com.supermartijn642.rechiseled.api.chiseling.plugin.RechiseledChiselingRecipePlugin;
import com.supermartijn642.rechiseled.api.util.ItemWithMeta;
import com.supermartijn642.rechiseled.chiseling.plugin.ChiselingRecipeMutationContextImpl;
import com.supermartijn642.rechiseled.chiseling.plugin.ChiselingRecipesLoadedContextImpl;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public class ChiselingRecipeManagerImpl implements ChiselingRecipeManager {

    private static final ChiselingRecipeManagerImpl SERVER = new ChiselingRecipeManagerImpl();
    private static final ChiselingRecipeManagerImpl CLIENT = new ChiselingRecipeManagerImpl();

    public static ChiselingRecipeManagerImpl get(boolean client){
        return client ? CLIENT : SERVER;
    }

    private List<ChiselingRecipe> recipes;

    @Override
    public List<ChiselingRecipe> getAllRecipes(){
        if(this.recipes == null)
            throw new IllegalStateException("Recipes can only be queried when in a game!");
        return this.recipes;
    }

    @Override
    public @Nullable ChiselingRecipe getRecipeForItem(ItemWithMeta item){
        if(this.recipes == null)
            throw new IllegalStateException("Recipes can only be queried when in a game!");
        for(ChiselingRecipe recipe : this.recipes){
            if(recipe.contains(item))
                return recipe;
        }
        return null;
    }

    private static final List<PluginEntry> PLUGINS = new ArrayList<>();
    private static final Map<ResourceLocation,PluginEntry> PLUGINS_BY_IDENTIFIER = new HashMap<>();
    private static boolean finalized = false;

    public synchronized static void registerPlugin(ResourceLocation identifier, ChiselingRecipePlugin plugin, int priority){
        if(finalized)
            throw new IllegalStateException("Trying to register chiseling plugin '" + identifier + "' after initialization!");
        if(PLUGINS_BY_IDENTIFIER.containsKey(identifier))
            throw new IllegalStateException("Duplicate chiseling plugin registration for '" + identifier + "': '" + PLUGINS_BY_IDENTIFIER.get(identifier).plugin.getClass().getName() + "' and '" + plugin.getClass().getName() + "'!");
        PluginEntry entry = new PluginEntry(identifier, priority, plugin);
        PLUGINS_BY_IDENTIFIER.put(identifier, entry);
        for(int i = 0; i <= PLUGINS.size(); i++){
            if(i == PLUGINS.size())
                PLUGINS.add(entry);
            else if(PLUGINS.get(i).priority > priority)
                PLUGINS.add(i, entry);
            else continue;
            break;
        }
    }

    public synchronized static void finalizePlugins(){
        if(finalized)
            throw new IllegalStateException("Plugins are already finalized!");

        // Add Rechiseled's datapack plugin
        PluginEntry datapacksPlugin = new PluginEntry(Rechiseled.identifier("datapacks"), 0, ChiselingRecipeDatapackPlugin.INSTANCE);
        for(int i = 0; i <= PLUGINS.size(); i++){
            if(i == PLUGINS.size())
                PLUGINS.add(datapacksPlugin);
            else if(PLUGINS.get(i).priority >= 0)
                PLUGINS.add(i, datapacksPlugin);
            else continue;
            break;
        }

        finalized = true;

        Rechiseled.LOGGER.info("{} chiseling plugins were registered: {}", PLUGINS.size(), PLUGINS.stream().map(p -> p.identifier).toArray());
    }

    public static void loadAnnotationPlugins(ASMDataTable dataTable){
        for(ASMDataTable.ASMData annotation : dataTable.getAll(RechiseledChiselingRecipePlugin.class.getName())){
            // Try to figure out a modid
            if(annotation.getCandidate().getContainedMods().isEmpty())
                continue;
            String modid = annotation.getCandidate().getContainedMods().get(0).getModId();
            // Process annotation
            try{
                if(!annotation.getClassName().equals(annotation.getObjectName()))
                    throw new RuntimeException("Chiseling plugin annotation must be a applied to a class!");
                // Get annotation properties
                String identifier = (String)annotation.getAnnotationInfo().getOrDefault("identifier", "main");
                if(!RegistryUtil.isValidIdentifier(identifier))
                    throw new RuntimeException("Rechiseled chiseling plugin from mod '" + modid + "' has invalid identifier '" + identifier + "'!");
                int priority = (int)annotation.getAnnotationInfo().getOrDefault("priority", 0);
                // Create plugin instance
                Class<?> clazz;
                try{
                    clazz = Class.forName(annotation.getClassName());
                }catch(Exception e){
                    throw new RuntimeException("Failed to obtain class '" + annotation.getClassName() + "'!", e);
                }
                if(!ChiselingRecipePlugin.class.isAssignableFrom(clazz))
                    throw new RuntimeException("Plugin class '" + clazz.getName() + "' must extend '" + ChiselingRecipePlugin.class.getSimpleName() + "!");
                Constructor<?> constructor;
                try{
                    constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                }catch(Exception e){
                    throw new RuntimeException("Plugin class '" + clazz.getName() + "' must have a default constructor!", e);
                }
                ChiselingRecipePlugin plugin;
                try{
                    plugin = (ChiselingRecipePlugin)constructor.newInstance();
                }catch(Exception e){
                    throw new RuntimeException("Failed to create instance of '" + annotation.getClassName() + "'!", e);
                }
                // Add the plugin
                registerPlugin(new ResourceLocation(modid, identifier), plugin, priority);
            }catch(Exception e){
                Rechiseled.LOGGER.error("Failed to create chiseling recipe plugin from mod '{}'!", modid, e);
            }
        }
    }

    public static void loadRecipes(){
        if(SERVER.recipes != null) // In 1.12, recipes are loaded at startup and cannot be reloaded in correspondence with the absence datapacks
            throw new IllegalStateException("Recipes can only be loaded once!");

        // Create plugin context
        Holder<ResourceLocation> activePlugin = new Holder<ResourceLocation>() {
            @Override
            public ResourceLocation get(){
                ResourceLocation identifier = super.get();
                if(identifier == null)
                    throw new IllegalStateException("No active plugin found!");
                return identifier;
            }
        };
        ChiselingRecipeMutationContextImpl context = new ChiselingRecipeMutationContextImpl(activePlugin::get);

        // Call mutate for each plugin
        for(PluginEntry plugin : PLUGINS){
            activePlugin.set(plugin.identifier);
            try{
                plugin.plugin.mutateRecipes(context);
            }catch(Exception e){
                throw new RuntimeException("Chiseling recipe plugin '" + plugin.identifier + "' threw an exception whilst mutation chiseling recipes!", e);
            }
        }
        activePlugin.set(null);
        context.invalidate();

        // Get all recipes
        List<MutableChiselingRecipe> recipes = context.getRecipesUnsafe();
        // Merge recipes with overlapping items
        List<ChiselingRecipe> mergedRecipes = mergeRecipes(recipes);
        Rechiseled.LOGGER.info("Loaded {} chiseling recipes", mergedRecipes.size());
        // Update recipes
        SERVER.updateRecipes(mergedRecipes);
    }

    private static List<ChiselingRecipe> mergeRecipes(List<MutableChiselingRecipe> recipes){
        // Partition the recipes into groups with overlapping items
        List<Pair<Set<ItemWithMeta>,List<ChiselingRecipe>>> groupedRecipes = new ArrayList<>(recipes.size());
        for(MutableChiselingRecipe recipe : recipes){
            if(recipe.entries().isEmpty())
                continue;
            ChiselingRecipeImpl newRecipe = new ChiselingRecipeImpl(recipe.entries());
            groupedRecipes.add(Pair.of(new HashSet<>(newRecipe.getItems()), new ArrayList<>(Collections.singleton(newRecipe))));
        }
        loop:
        for(int i = 0; i < groupedRecipes.size(); i++){
            Pair<Set<ItemWithMeta>,List<ChiselingRecipe>> group = groupedRecipes.get(i);
            for(int j = i + 1; j < groupedRecipes.size(); j++){
                Pair<Set<ItemWithMeta>,List<ChiselingRecipe>> otherGroup = groupedRecipes.get(j);
                // Check if groups have overlapping items
                if(!Sets.intersection(group.left(), otherGroup.left()).isEmpty()){
                    group.left().addAll(otherGroup.left());
                    group.right().addAll(otherGroup.right());
                    groupedRecipes.remove(j);
                    i--;
                    continue loop;
                }
            }
        }

        // Make sure recipes maintain the original ordering
        for(Pair<Set<ItemWithMeta>,List<ChiselingRecipe>> group : groupedRecipes)
            //noinspection SuspiciousMethodCalls
            group.right().sort(Comparator.comparingInt(recipes::indexOf));

        // Merge recipes with overlapping items into a single recipe
        List<List<ChiselingEntry>> groupedEntries = new ArrayList<>(groupedRecipes.size());
        for(Pair<Set<ItemWithMeta>,List<ChiselingRecipe>> group : groupedRecipes){
            List<ChiselingEntry> entries = new ArrayList<>();
            for(ChiselingRecipe recipe : group.right()){
                entryLoop:
                for(ChiselingEntry entry : recipe.entries()){
                    // Check if there's already an entry that contains all items from this entry
                    for(ChiselingEntry existingEntry : entries){
                        if(((ChiselingEntryImpl)existingEntry).items().containsAll(((ChiselingEntryImpl)entry).items()))
                            continue entryLoop;
                    }
                    // Check if this entry contains all items of an existing entry
                    for(int i = 0; i < entries.size(); i++){
                        if(((ChiselingEntryImpl)entry).items().containsAll(((ChiselingEntryImpl)entries.get(i)).items())){
                            entries.remove(i);
                            i--;
                        }
                    }
                    entries.add(entry);
                }
            }
            groupedEntries.add(entries);
        }

        // Create the merged recipes
        List<ChiselingRecipe> mergedRecipes = new ArrayList<>(groupedEntries.size());
        for(List<ChiselingEntry> entries : groupedEntries)
            mergedRecipes.add(new ChiselingRecipeImpl(entries));
        return mergedRecipes;
    }

    public void updateRecipes(List<ChiselingRecipe> recipes){
        this.recipes = ImmutableList.copyOf(recipes);

        // Call recipe update for each plugin
        ChiselingRecipesLoadedContext context = new ChiselingRecipesLoadedContextImpl(this, this == CLIENT);
        for(PluginEntry plugin : PLUGINS){
            try{
                plugin.plugin.onRecipesLoaded(context);
            }catch(Exception e){
                Rechiseled.LOGGER.error("Chiseling recipe plugin '{}' threw an exception whilst handling chiseling recipe update!", plugin.identifier, e);
            }
        }
    }

    public void clearRecipes(){
        this.recipes = null;
    }

    private static class PluginEntry {

        private final ResourceLocation identifier;
        private final int priority;
        private final ChiselingRecipePlugin plugin;

        private PluginEntry(ResourceLocation identifier, int priority, ChiselingRecipePlugin plugin){
            this.identifier = identifier;
            this.priority = priority;
            this.plugin = plugin;
        }
    }
}
