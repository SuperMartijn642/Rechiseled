package com.supermartijn642.rechiseled.compat.jei;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeImpl;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientVisibility;
import mezz.jei.common.gui.elements.OffsetDrawable;
import mezz.jei.common.gui.recipes.layout.RecipeLayoutBuilder;
import mezz.jei.common.gui.recipes.layout.builder.IRecipeLayoutSlotSource;
import mezz.jei.common.ingredients.RegisteredIngredients;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created 28/12/2021 by SuperMartijn642
 */
public class ChiselingRecipeCategory implements IRecipeCategory<ChiselingRecipe> {

    private final IDrawable background, slot, icon, arrow, focussedSlot;

    public ChiselingRecipeCategory(IGuiHelper guiHelper){
        this.background = guiHelper.createBlankDrawable(this.getWidth(), this.getHeight());
        this.slot = OffsetDrawable.create(guiHelper.getSlotDrawable(), -1, -1);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Rechiseled.chisel));
        this.arrow = guiHelper.drawableBuilder(Rechiseled.identifier("textures/screen/curved_arrow.png"), 0, 0, 20, 20).setTextureSize(20, 20).build();
        this.focussedSlot = OffsetDrawable.create(guiHelper.drawableBuilder(Rechiseled.identifier("textures/screen/focussed_slot.png"), 0, 0, 18, 18).setTextureSize(18, 18).build(), -1, -1);
    }

    @Override
    public RecipeType<ChiselingRecipe> getRecipeType(){
        return ChiselingJEIPlugin.CHISELING_RECIPE_TYPE;
    }

    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getUid(){
        return ChiselingJEIPlugin.CHISELING_RECIPE_TYPE.getUid();
    }

    @Override
    @SuppressWarnings("removal")
    public Class<? extends ChiselingRecipe> getRecipeClass(){
        return ChiselingRecipe.class;
    }

    @Override
    public Component getTitle(){
        return TextComponents.translation("rechiseled.recipe_category.title").get();
    }

    @Override
    public IDrawable getBackground(){
        return this.background;
    }

    @Override
    public IDrawable getIcon(){
        return this.icon;
    }

    public int getWidth(){
        return 178;
    }

    public int getHeight(){
        return 94;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder b, ChiselingRecipe recipe, IFocusGroup focuses){
        if(!(b instanceof RecipeLayoutBuilder))
            return;
        RecipeLayoutBuilder recipeLayoutBuilder = (RecipeLayoutBuilder)b;
        RegisteredIngredients registeredIngredients = JEIFieldAccess.getRecipeLayoutBuilderRegisteredIngredients(recipeLayoutBuilder);
        int ingredientCycleOffset = JEIFieldAccess.getRecipeLayoutBuilderIngredientCycleOffset(recipeLayoutBuilder);
        List<IRecipeLayoutSlotSource> slots = JEIFieldAccess.getRecipeLayoutBuilderSlots(recipeLayoutBuilder);
        IIngredientVisibility ingredientVisibility = JEIFieldAccess.getRecipeLayoutBuilderIngredientVisibility(recipeLayoutBuilder);

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
        IRecipeSlotTooltipCallback tooltip = (slot, builder) -> {
            float worth = slot.getDisplayedItemStack().map(stack -> recipe.getWorth(stack.getItem())).map(ItemWithWorth::worth).orElse(1f);
            if(worth != 1){
                worth = Math.round(worth * 1000) / 1000f;
                builder.add(TextComponents.translation("rechiseled.recipe_category.conversion_value", TextComponents.number(worth).color(ChatFormatting.GOLD).get()).color(ChatFormatting.GRAY).get());
            }
        };

        // Input slot
        JEIScrollableSlotsSlot input = new JEIScrollableSlotsSlot(registeredIngredients, RecipeIngredientRole.INPUT, ingredientCycleOffset, JEIFieldAccess.getRecipeLayoutBuilderLegacyIngredientIndex(recipeLayoutBuilder), ingredientVisibility, 73, 1);
        JEIFieldAccess.setRecipeLayoutBuilderLegacyIngredientIndex(recipeLayoutBuilder, JEIFieldAccess.getRecipeLayoutBuilderLegacyIngredientIndex(recipeLayoutBuilder) + 1);
        input.addTooltipCallback(tooltip);
        inputs.forEach(input::addItem);
        if(inputs.stream().anyMatch(focusedInputs::contains))
            input.setBackground(this.focussedSlot);
        else
            input.setBackground(this.slot);
        slots.add(input);

        // Output slots
        List<JEIScrollableSlotsSlot> outputSlots = new ArrayList<>();
        for(List<Item> items : outputs){
            JEIScrollableSlotsSlot output = new JEIScrollableSlotsSlot(registeredIngredients, RecipeIngredientRole.OUTPUT, ingredientCycleOffset, JEIFieldAccess.getRecipeLayoutBuilderLegacyIngredientIndex(recipeLayoutBuilder), ingredientVisibility);
            JEIFieldAccess.setRecipeLayoutBuilderLegacyIngredientIndex(recipeLayoutBuilder, JEIFieldAccess.getRecipeLayoutBuilderLegacyIngredientIndex(recipeLayoutBuilder) + 1);
            output.addTooltipCallback(tooltip);
            items.forEach(output::addItem);
            if(items.stream().anyMatch(focusedOutputs::contains))
                output.setBackground(this.focussedSlot);
            else
                output.setBackground(this.slot);
            slots.add(output);
            outputSlots.add(output);
        }

        // Create scrollable slots widget
        ((ChiselingRecipeImpl)recipe).jeiScrollBox = new JEIScrollableSlotsWidget(0, 22, outputSlots, this.slot);
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

    @Override
    public void draw(ChiselingRecipe recipe, IRecipeSlotsView slots, PoseStack poseStack, double mouseX, double mouseY){
        // Arrow
        this.arrow.draw(poseStack, 89, 0);
        // Scrollable slots
        JEIScrollableSlotsWidget scrollableSlotsWidget = (JEIScrollableSlotsWidget)((ChiselingRecipeImpl)recipe).jeiScrollBox;
        if(scrollableSlotsWidget != null)
            scrollableSlotsWidget.draw(poseStack, mouseX, mouseY);
    }

    @Override
    public boolean handleInput(ChiselingRecipe recipe, double mouseX, double mouseY, InputConstants.Key input){
        if(input.getType() != InputConstants.Type.MOUSE)
            return false;
        JEIScrollableSlotsWidget scrollableSlotsWidget = (JEIScrollableSlotsWidget)((ChiselingRecipeImpl)recipe).jeiScrollBox;
        return scrollableSlotsWidget != null && scrollableSlotsWidget.mousePressed(mouseX, mouseY, input.getValue());
    }

    public boolean mouseReleased(ChiselingRecipe recipe, double mouseX, double mouseY, int button){
        JEIScrollableSlotsWidget scrollableSlotsWidget = (JEIScrollableSlotsWidget)((ChiselingRecipeImpl)recipe).jeiScrollBox;
        return scrollableSlotsWidget != null && scrollableSlotsWidget.mouseReleased(mouseX, mouseY, button);
    }

    public boolean mouseScrolled(ChiselingRecipe recipe, double mouseX, double mouseY, double amountY){
        JEIScrollableSlotsWidget scrollableSlotsWidget = (JEIScrollableSlotsWidget)((ChiselingRecipeImpl)recipe).jeiScrollBox;
        return scrollableSlotsWidget != null && scrollableSlotsWidget.mouseScrolled(mouseX, mouseY, amountY);
    }
}
