package com.supermartijn642.rechiseled.chiseling;

import com.google.common.collect.Sets;
import com.supermartijn642.core.registry.RegistryUtil;
import com.supermartijn642.core.util.Holder;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.*;
import com.supermartijn642.rechiseled.api.chiseling.plugin.ChiselingRecipePlugin;
import com.supermartijn642.rechiseled.api.chiseling.plugin.ChiselingRecipesLoadedContext;
import com.supermartijn642.rechiseled.api.chiseling.plugin.MutableChiselingRecipe;
import com.supermartijn642.rechiseled.api.chiseling.plugin.RechiseledChiselingRecipePlugin;
import com.supermartijn642.rechiseled.chiseling.plugin.ChiselingRecipeMutationContextImpl;
import com.supermartijn642.rechiseled.chiseling.plugin.ChiselingRecipesLoadedContextImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;

import java.lang.annotation.ElementType;
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
    public @Nullable ChiselingRecipe getRecipeForItem(ItemLike item){
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

    public static synchronized void registerPlugin(ResourceLocation identifier, ChiselingRecipePlugin plugin, int priority){
        if(finalized)
            throw new IllegalStateException("Trying to register chiseling plugin '" + identifier + "' after initialization!");
        if(PLUGINS_BY_IDENTIFIER.containsKey(identifier))
            throw new IllegalStateException("Duplicate chiseling plugin registration for '" + identifier + "': '" + PLUGINS_BY_IDENTIFIER.get(identifier).plugin.getClass().getName() + "' and '" + plugin.getClass().getName() + "'!");
        PluginEntry entry = new PluginEntry(identifier, priority, plugin);
        PLUGINS_BY_IDENTIFIER.put(identifier, entry);
        PLUGINS.add(entry);
    }

    public static synchronized void finalizePlugins(){
        if(finalized)
            throw new IllegalStateException("Plugins are already finalized!");

        // Add Rechiseled's datapack plugin
        registerPlugin(ChiselingRecipeDatapackPlugin.IDENTIFIER, ChiselingRecipeDatapackPlugin.INSTANCE, 0);
        // Add annotation plugins
        loadAnnotationPlugins();

        // Sort plugins
        PLUGINS.sort(Comparator.comparingInt(PluginEntry::priority).thenComparing(PluginEntry::identifier));

        finalized = true;
        Rechiseled.LOGGER.info("{} chiseling plugins were registered: {}", PLUGINS.size(), PLUGINS.stream().map(PluginEntry::identifier).toArray());
    }

    private static void loadAnnotationPlugins(){
        Type pluginAnnotation = Type.getType(RechiseledChiselingRecipePlugin.class);
        for(ModFileScanData scanData : ModList.get().getAllScanData()){
            // Try to figure out a modid
            if(scanData.getIModInfoData().isEmpty() || scanData.getIModInfoData().get(0).getMods().isEmpty())
                continue;
            String modid = scanData.getIModInfoData().get(0).getMods().get(0).getModId();
            // Find annotations
            for(ModFileScanData.AnnotationData annotation : scanData.getAnnotations()){
                if(!annotation.annotationType().equals(pluginAnnotation))
                    continue;
                try{
                    if(!annotation.targetType().equals(ElementType.TYPE))
                        throw new RuntimeException("Chiseling plugin annotation must be a applied to a class!");
                    // Get annotation properties
                    String identifier = (String)annotation.annotationData().getOrDefault("identifier", "plugin");
                    if(!RegistryUtil.isValidIdentifier(identifier))
                        throw new RuntimeException("Rechiseled chiseling plugin from mod '" + modid + "' has invalid identifier '" + identifier + "'!");
                    int priority = (int)annotation.annotationData().getOrDefault("priority", ChiselingRecipePlugin.DEFAULT_PLUGIN_PRIORITY);
                    // Create plugin instance
                    Class<?> clazz;
                    try{
                        clazz = Class.forName(annotation.clazz().getClassName());
                    }catch(Exception e){
                        throw new RuntimeException("Failed to obtain class '" + annotation.clazz().getClassName() + "'!", e);
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
                        throw new RuntimeException("Failed to create instance of '" + annotation.clazz().getClassName() + "'!", e);
                    }
                    // Add the plugin
                    registerPlugin(new ResourceLocation(modid, identifier), plugin, priority);
                }catch(Exception e){
                    Rechiseled.LOGGER.error("Failed to create chiseling recipe plugin from mod '{}'!", modid, e);
                }
            }
        }
    }

    public static void loadRecipes(){
        // Create plugin context
        Holder<ResourceLocation> activePlugin = new Holder<>() {
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
        List<Pair<Set<Item>,List<ChiselingRecipe>>> groupedRecipes = new ArrayList<>(recipes.size());
        for(MutableChiselingRecipe recipe : recipes){
            if(recipe.entries().isEmpty())
                continue;
            ChiselingRecipeImpl newRecipe = new ChiselingRecipeImpl(recipe.entries());
            groupedRecipes.add(Pair.of(new HashSet<>(newRecipe.getItems()), new ArrayList<>(List.of(newRecipe))));
        }
        loop:
        for(int i = 0; i < groupedRecipes.size(); i++){
            Pair<Set<Item>,List<ChiselingRecipe>> group = groupedRecipes.get(i);
            for(int j = i + 1; j < groupedRecipes.size(); j++){
                Pair<Set<Item>,List<ChiselingRecipe>> otherGroup = groupedRecipes.get(j);
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
        for(Pair<Set<Item>,List<ChiselingRecipe>> group : groupedRecipes)
            //noinspection SuspiciousMethodCalls
            group.right().sort(Comparator.comparingInt(recipes::indexOf));

        // Find the highest worth per item
        Map<Item,ItemWithWorth> worths = new HashMap<>();
        for(Pair<Set<Item>,List<ChiselingRecipe>> group : groupedRecipes){
            for(ChiselingRecipe recipe : group.right()){
                for(ChiselingEntry entry : recipe.entries()){
                    ((ChiselingEntryImpl)entry).items().forEach((item, worth) -> {
                        worths.merge(item, worth, (worth1, worth2) -> worth1.worth() > worth2.worth() ? worth1 : worth2);
                    });
                }
            }
        }

        // Merge recipes with overlapping items into a single recipe
        List<List<ChiselingEntry>> groupedEntries = new ArrayList<>(groupedRecipes.size());
        for(Pair<Set<Item>,List<ChiselingRecipe>> group : groupedRecipes){
            List<ChiselingEntry> entries = new ArrayList<>();
            for(ChiselingRecipe recipe : group.right()){
                entryLoop:
                for(ChiselingEntry entry : recipe.entries()){
                    // Check if there's already an entry that contains all items from this entry
                    for(ChiselingEntry existingEntry : entries){
                        if(((ChiselingEntryImpl)existingEntry).items().keySet().containsAll(((ChiselingEntryImpl)entry).items().keySet()))
                            continue entryLoop;
                    }
                    // Check if this entry contains all items of an existing entry
                    for(int i = 0; i < entries.size(); i++){
                        if(((ChiselingEntryImpl)entry).items().keySet().containsAll(((ChiselingEntryImpl)entries.get(i)).items().keySet())){
                            entries.remove(i);
                            i--;
                        }
                    }
                    entries.add(entry);
                }
            }
            groupedEntries.add(entries);
        }

        // Replace items worths with the highest worths for that item
        for(List<ChiselingEntry> entries : groupedEntries){
            entries.replaceAll(entry -> {
                ItemWithWorth regularBlock = entry.hasRegularItem(ChiselingBlockShape.BLOCK) ? worths.get(entry.getRegularItem(ChiselingBlockShape.BLOCK).item()) : null;
                ItemWithWorth regularStairs = entry.hasRegularItem(ChiselingBlockShape.STAIRS) ? worths.get(entry.getRegularItem(ChiselingBlockShape.STAIRS).item()) : null;
                ItemWithWorth regularSlab = entry.hasRegularItem(ChiselingBlockShape.SLAB) ? worths.get(entry.getRegularItem(ChiselingBlockShape.SLAB).item()) : null;
                ItemWithWorth connectingBlock = entry.hasConnectingItem(ChiselingBlockShape.BLOCK) ? worths.get(entry.getConnectingItem(ChiselingBlockShape.BLOCK).item()) : null;
                ItemWithWorth connectingStairs = entry.hasConnectingItem(ChiselingBlockShape.STAIRS) ? worths.get(entry.getConnectingItem(ChiselingBlockShape.STAIRS).item()) : null;
                ItemWithWorth connectingSlab = entry.hasConnectingItem(ChiselingBlockShape.SLAB) ? worths.get(entry.getConnectingItem(ChiselingBlockShape.SLAB).item()) : null;
                return new ChiselingEntryImpl(
                    entry.owner(), entry.recipe(),
                    regularBlock, regularStairs, regularSlab,
                    connectingBlock, connectingStairs, connectingSlab
                );
            });
        }

        // Create the merged recipes
        List<ChiselingRecipe> mergedRecipes = new ArrayList<>(groupedEntries.size());
        for(List<ChiselingEntry> entries : groupedEntries)
            mergedRecipes.add(new ChiselingRecipeImpl(entries));
        return mergedRecipes;
    }

    public void updateRecipes(List<ChiselingRecipe> recipes){
        this.recipes = List.copyOf(recipes);

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

    private record PluginEntry(ResourceLocation identifier, int priority, ChiselingRecipePlugin plugin) {
    }
}
