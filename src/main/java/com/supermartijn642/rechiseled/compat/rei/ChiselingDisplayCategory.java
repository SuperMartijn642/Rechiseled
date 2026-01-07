package com.supermartijn642.rechiseled.compat.rei;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.rechiseled.Rechiseled;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 29/07/2025 by SuperMartijn642
 */
public class ChiselingDisplayCategory implements DisplayCategory<ChiselingRecipeDisplay> {

    private static final Identifier BACKGROUND = Rechiseled.identifier("textures/screen/jei_category_background.png");
    private static final Component TITLE = TextComponents.translation("rechiseled.jei_category.title").get();

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
        return 174 + 10;
    }

    @Override
    public int getDisplayHeight(){
        return 72 + 10;
    }

    @Override
    public List<Widget> setupDisplay(ChiselingRecipeDisplay display, Rectangle bounds){
        int left = bounds.x + 5, top = bounds.y + 5;
        List<Widget> widgets = new ArrayList<>();

        // Background
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(BACKGROUND, new Rectangle(left, top, 174, 72)));

        // Input slot
        widgets.add(Widgets.createSlot(new Point(left + 1, top + 28)).entries(display.getInputEntries().getFirst()).markInput().disableBackground());

        // Output slots
        List<EntryIngredient> outputs = display.getOutputEntries();
        for(int i = 0; i < outputs.size(); i++){
            int x = left + 49 + 18 * (i % 7);
            int y = top + 1 + 18 * (i / 7);
            widgets.add(Widgets.createSlot(new Point(x, y)).entries(outputs.get(i)).markOutput().disableBackground());
        }
        return widgets;
    }
}
