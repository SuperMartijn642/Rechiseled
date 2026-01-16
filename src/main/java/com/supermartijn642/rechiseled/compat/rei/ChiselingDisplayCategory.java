package com.supermartijn642.rechiseled.compat.rei;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.rechiseled.Rechiseled;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 29/07/2025 by SuperMartijn642
 */
public class ChiselingDisplayCategory implements DisplayCategory<ChiselingRecipeDisplay> {

    private static final Component TITLE = TextComponents.translation("rechiseled.recipe_category.title").get();

    private final EntryStack<ItemStack> icon;

    public ChiselingDisplayCategory(){
        this.icon = EntryStacks.of(Rechiseled.chisel);
    }

    @Override
    public CategoryIdentifier<? extends ChiselingRecipeDisplay> getCategoryIdentifier(){
        return ChiselingREIPlugin.CHISELING_CATEGORY;
    }

    @Override
    public Component getTitle(){
        return TITLE;
    }

    @Override
    public Renderer getIcon(){
        return this.icon;
    }

    @Override
    public int getDisplayWidth(ChiselingRecipeDisplay display){
        return 178 + 10;
    }

    @Override
    public int getDisplayHeight(){
        return 94 + 10;
    }

    @Override
    public List<Widget> setupDisplay(ChiselingRecipeDisplay display, Rectangle bounds){
        int left = bounds.x + 5, top = bounds.y + 5;
        List<Widget> widgets = new ArrayList<>();
        // Background
        widgets.add(Widgets.createRecipeBase(bounds));
        // Arrow
        widgets.add(Widgets.createTexturedWidget(Rechiseled.identifier("textures/screen/curved_arrow.png"), left + 89, top, 20, 20));
        // Input slot
        widgets.add(Widgets.createSlot(new Point(left + 73, top + 1)).entries(display.getInputEntries().get(0)).markInput());
        // Output slots
        List<Slot> outputs = display.getOutputEntries().stream()
            .map(entry -> Widgets.createSlot(new Point()).entries(entry).markOutput())
            .toList();
        widgets.add(new REIScrollableSlotsWidget(left, top + 22, outputs));
        return widgets;
    }
}
