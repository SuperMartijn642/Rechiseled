package com.supermartijn642.rechiseled.compat.rei;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created 29/07/2025 by SuperMartijn642
 */
public class ChiselingRecipeDisplay implements Display {

    private final ChiselingRecipe recipe;
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;

    public ChiselingRecipeDisplay(ChiselingRecipe recipe){
        this.recipe = recipe;

        // Gather inputs and outputs of the recipe
        List<ItemStack> inputs = new ArrayList<>();
        List<EntryIngredient> outputs = new ArrayList<>();
        for(ChiselingEntry entry : recipe.entries()){
            List<ItemStack> output = new ArrayList<>();
            if(entry.hasRegularItem(ChiselingBlockShape.BLOCK)){
                //noinspection DataFlowIssue
                inputs.add(new ItemStack(entry.getRegularItem(ChiselingBlockShape.BLOCK).item()));
                //noinspection DataFlowIssue
                output.add(new ItemStack(entry.getRegularItem(ChiselingBlockShape.BLOCK).item()));
            }
            if(entry.hasConnectingItem(ChiselingBlockShape.BLOCK)){
                //noinspection DataFlowIssue
                inputs.add(new ItemStack(entry.getConnectingItem(ChiselingBlockShape.BLOCK).item()));
                //noinspection DataFlowIssue
                output.add(new ItemStack(entry.getConnectingItem(ChiselingBlockShape.BLOCK).item()));
            }
            outputs.add(EntryIngredients.ofItemStacks(output));
        }
        this.inputs = List.of(EntryIngredients.ofItemStacks(inputs));
        this.outputs = List.copyOf(outputs);
    }

    @Override
    public List<EntryIngredient> getInputEntries(){
        return this.inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries(){
        return this.outputs;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier(){
        return ChiselingREIPlugin.CHISELING_CATEGORY;
    }

    @Override
    public Optional<Identifier> getDisplayLocation(){
        return Optional.empty();
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer(){
        return null;
    }
}
