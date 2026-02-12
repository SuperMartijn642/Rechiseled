package com.supermartijn642.rechiseled.compat.jei;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.gui.elements.OffsetDrawable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created 28/12/2021 by SuperMartijn642
 */
public class ChiselingRecipeCategory implements IRecipeCategory<ChiselingRecipe> {

    private final IDrawable slot, icon, arrow, focussedSlot;

    public ChiselingRecipeCategory(IGuiHelper guiHelper){
        this.slot = OffsetDrawable.create(guiHelper.getSlotDrawable(), -1, -1);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Rechiseled.chisel));
        this.arrow = guiHelper.drawableBuilder(Rechiseled.identifier("textures/screen/curved_arrow.png"), 0, 0, 20, 20).setTextureSize(20, 20).build();
        this.focussedSlot = guiHelper.drawableBuilder(Rechiseled.identifier("textures/screen/focussed_slot.png"), 0, 0, 18, 18).setTextureSize(18, 18).build();
    }

    @Override
    public RecipeType<ChiselingRecipe> getRecipeType(){
        return ChiselingJEIPlugin.CHISELING_RECIPE_TYPE;
    }

    @Override
    public Component getTitle(){
        return TextComponents.translation("rechiseled.recipe_category.title").get();
    }

    @Override
    public IDrawable getIcon(){
        return this.icon;
    }

    @Override
    public int getWidth(){
        return 178;
    }

    @Override
    public int getHeight(){
        return 94;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, ChiselingRecipe recipe, IFocusGroup focuses){
        List<Item> inputs = new ArrayList<>();
        List<List<Item>> outputs = new ArrayList<>();

        for(ChiselingBlockShape shape : ChiselingBlockShape.values()){
            for(ChiselingEntry entry : recipe.entries()){
                List<Item> output = new ArrayList<>();
                if(entry.hasRegularItem(shape)){
                    //noinspection DataFlowIssue
                    inputs.add(entry.getRegularItem(shape).item());
                    //noinspection DataFlowIssue
                    output.add(entry.getRegularItem(shape).item());
                }
                if(entry.hasConnectingItem(shape)){
                    //noinspection DataFlowIssue
                    inputs.add(entry.getConnectingItem(shape).item());
                    //noinspection DataFlowIssue
                    output.add(entry.getConnectingItem(shape).item());
                }
                if(output.isEmpty())
                    continue;
                outputs.add(output);
            }
        }

        // Add focused items first
        Set<Item> focusedInputs = focusedItems(focuses, RecipeIngredientRole.INPUT);
        if(!focusedInputs.isEmpty())
            inputs.sort(Comparator.comparing(item -> !focusedInputs.contains(item)));
        Set<Item> focusedOutputs = focusedItems(focuses, RecipeIngredientRole.OUTPUT);
        if(!focusedOutputs.isEmpty())
            outputs.sort(Comparator.comparing(stacks -> stacks.stream().noneMatch(focusedOutputs::contains)));

        // Item conversion factor tooltip
        IRecipeSlotRichTooltipCallback tooltip = (slot, builder) -> {
            float worth = slot.getDisplayedItemStack().map(stack -> recipe.getWorth(stack.getItem())).map(ItemWithWorth::worth).orElse(1f);
            if(worth != 1){
                worth = Math.round(worth * 1000) / 1000f;
                builder.add(TextComponents.translation("rechiseled.recipe_category.conversion_value", TextComponents.number(worth).color(ChatFormatting.GOLD).get()).color(ChatFormatting.GRAY).get());
            }
        };

        // Input slot
        IRecipeSlotBuilder input = recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 1, 28).addRichTooltipCallback(tooltip);
        inputs.forEach(input::addItemLike);
        if(inputs.stream().anyMatch(focusedInputs::contains))
            input.setBackground(this.focussedSlot, -1, -1);
        else
            input.setStandardSlotBackground();

        // Output slots
        for(List<Item> items : outputs){
            IRecipeSlotBuilder output = recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 0, 0).addRichTooltipCallback(tooltip);
            items.forEach(output::addItemLike);
            if(items.stream().anyMatch(focusedOutputs::contains))
                output.setBackground(this.focussedSlot, -1, -1);
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, ChiselingRecipe recipe, IFocusGroup focuses){
        builder.getRecipeSlots().getSlots(RecipeIngredientRole.INPUT).get(0).setPosition(73, 1);
        builder.addDrawable(this.arrow).setPosition(89, 0);
        List<IRecipeSlotDrawable> scrollableSlots = builder.getRecipeSlots().getSlots(RecipeIngredientRole.OUTPUT);
        JEIScrollableSlotsWidget scrollableSlotsWidget = new JEIScrollableSlotsWidget(0, 22, scrollableSlots, this.slot);
        builder.addSlottedWidget(scrollableSlotsWidget, scrollableSlots);
        builder.addInputHandler(scrollableSlotsWidget);
    }

    private static Set<Item> focusedItems(IFocusGroup focuses, RecipeIngredientRole role){
        return focuses.getItemStackFocuses(role)
            .map(IFocus::getTypedValue)
            .map(ITypedIngredient::getItemStack)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(ItemStack::getItem)
            .collect(Collectors.toSet());
    }
}
