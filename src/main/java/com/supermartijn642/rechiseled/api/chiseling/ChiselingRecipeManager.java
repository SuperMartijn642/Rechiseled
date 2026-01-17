package com.supermartijn642.rechiseled.api.chiseling;

import com.supermartijn642.rechiseled.api.chiseling.plugin.ChiselingRecipePlugin;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeManagerImpl;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public interface ChiselingRecipeManager {

    /**
     * Registers the given plugin with the given priority.
     * Plugins with a lower priority value are applied before plugins with a high priority value.
     * The default priority for plugins is {@code 0}.
     */
    static void registerPlugin(ResourceLocation identifier, ChiselingRecipePlugin plugin, int priority){
        ChiselingRecipeManagerImpl.registerPlugin(identifier, plugin, priority);
    }

    /**
     * Register the given plugin with default priority.
     */
    static void registerPlugin(ResourceLocation identifier, ChiselingRecipePlugin plugin){
        registerPlugin(identifier, plugin, 0);
    }

    static ChiselingRecipeManager get(boolean client){
        return ChiselingRecipeManagerImpl.get(client);
    }

    static ChiselingRecipeManager get(IWorldReader level){
        return get(level.isClientSide());
    }

    List<ChiselingRecipe> getAllRecipes();

    @Nullable
    ChiselingRecipe getRecipeForItem(IItemProvider item);
}
