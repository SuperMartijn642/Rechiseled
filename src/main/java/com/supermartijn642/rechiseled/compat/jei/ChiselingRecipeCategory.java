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
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.gui.ingredient.ITooltipCallback;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.gui.ingredients.GuiIngredient;
import mezz.jei.gui.ingredients.GuiIngredientGroup;
import mezz.jei.gui.ingredients.GuiItemStackGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

/**
 * Created 28/12/2021 by SuperMartijn642
 */
public class ChiselingRecipeCategory implements IRecipeCategory<ChiselingRecipe> {

    private final IDrawable background, slot, icon, arrow, focussedSlot;

    public ChiselingRecipeCategory(IGuiHelper guiHelper){
        this.background = guiHelper.createBlankDrawable(this.getWidth(), this.getHeight());
        this.slot = guiHelper.getSlotDrawable();
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(Rechiseled.chisel));
        this.arrow = guiHelper.drawableBuilder(Rechiseled.identifier("textures/screen/curved_arrow.png"), 0, 0, 20, 20).setTextureSize(20, 20).build();
        this.focussedSlot = guiHelper.drawableBuilder(Rechiseled.identifier("textures/screen/focussed_slot.png"), 0, 0, 18, 18).setTextureSize(18, 18).build();
    }

    @Override
    public ResourceLocation getUid(){
        return Rechiseled.identifier("chiseling");
    }

    @Override
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
    public void setIngredients(ChiselingRecipe recipe, IIngredients ingredients){
        List<ItemStack> inputs = new ArrayList<>();
        List<List<ItemStack>> outputs = new ArrayList<>();

        for(ChiselingBlockShape shape : ChiselingBlockShape.values()){
            for(ChiselingEntry entry : recipe.entries()){
                List<ItemStack> output = new ArrayList<>();
                if(entry.hasRegularItem(shape)){
                    //noinspection DataFlowIssue
                    inputs.add(new ItemStack(entry.getRegularItem(shape).item()));
                    //noinspection DataFlowIssue
                    output.add(new ItemStack(entry.getRegularItem(shape).item()));
                }
                if(entry.hasConnectingItem(shape)){
                    //noinspection DataFlowIssue
                    inputs.add(new ItemStack(entry.getConnectingItem(shape).item()));
                    //noinspection DataFlowIssue
                    output.add(new ItemStack(entry.getConnectingItem(shape).item()));
                }
                if(output.isEmpty())
                    continue;
                outputs.add(output);
            }
        }

        ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(inputs));
        ingredients.setOutputLists(VanillaTypes.ITEM, outputs);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ChiselingRecipe recipe, IIngredients ingredients){
        List<ItemStack> inputs = ingredients.getInputs(VanillaTypes.ITEM).get(0);
        List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);

        // Add focused items first
        IFocus<ItemStack> focus = recipeLayout.getFocus(VanillaTypes.ITEM);
        Set<Item> focusedItems = focus == null ? Collections.emptySet() : focusedItems(focus);
        if(focus != null && focus.getMode() == IFocus.Mode.INPUT && !focusedItems.isEmpty())
            inputs.sort(Comparator.comparing(item -> !focusedItems.contains(item.getItem())));
        if(focus != null && focus.getMode() == IFocus.Mode.OUTPUT && !outputs.isEmpty())
            outputs.sort(Comparator.comparing(stacks -> stacks.stream().map(ItemStack::getItem).noneMatch(focusedItems::contains)));

        // Item conversion factor tooltip
        ITooltipCallback<ItemStack> tooltip = (slot, input, stack, builder) -> {
            ItemWithWorth itemWithWorth = recipe.getWorth(stack.getItem());
            float worth = itemWithWorth == null ? 1 : itemWithWorth.worth(); // Should never be null, but just in case
            if(worth != 1){
                worth = Math.round(worth * 1000) / 1000f;
                builder.add(TextComponents.translation("rechiseled.recipe_category.conversion_value", TextComponents.number(worth).color(ChatFormatting.GOLD).get()).color(ChatFormatting.GRAY).get());
            }
        };

        IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();
        //noinspection unchecked
        Map<Integer,GuiIngredient<ItemStack>> slots = (Map<Integer,GuiIngredient<ItemStack>>)itemStackGroup.getGuiIngredients();
        IIngredientRenderer<ItemStack> ingredientRenderer = JEIFieldAccess.getGuiIngredientGroupIngredientRenderer((GuiItemStackGroup)itemStackGroup);
        IIngredientHelper<ItemStack> ingredientHelper = JEIFieldAccess.getGuiIngredientGroupIngredientHelper((GuiItemStackGroup)itemStackGroup);
        int cycleOffset = JEIFieldAccess.getGuiIngredientGroupCycleOffset((GuiIngredientGroup<?>)itemStackGroup);

        // Input slot
        itemStackGroup.init(0, true, 73, 1);
        itemStackGroup.addTooltipCallback(tooltip);
        itemStackGroup.set(0, inputs);
        if(focus != null && focus.getMode() == IFocus.Mode.INPUT && inputs.stream().map(ItemStack::getItem).anyMatch(focusedItems::contains))
            itemStackGroup.setBackground(0, this.focussedSlot);
        else
            itemStackGroup.setBackground(0, this.slot);

        // Output slots
        List<JEIScrollableSlotsSlot> outputSlots = new ArrayList<>();
        for(List<ItemStack> items : outputs){
            JEIScrollableSlotsSlot output = new JEIScrollableSlotsSlot(slots.size(), ingredientRenderer, ingredientHelper, cycleOffset);
            slots.put(slots.size(), output);
            itemStackGroup.set(slots.size() - 1, items);
            if(focus != null && focus.getMode() == IFocus.Mode.OUTPUT && items.stream().map(ItemStack::getItem).anyMatch(focusedItems::contains))
                itemStackGroup.setBackground(slots.size() - 1, this.focussedSlot);
            else
                itemStackGroup.setBackground(slots.size() - 1, this.slot);
            outputSlots.add(output);
        }

        // Create scrollable slots widget
        ((ChiselingRecipeImpl)recipe).jeiScrollBox = new JEIScrollableSlotsWidget(0, 22, outputSlots, this.slot);
    }

    private static Set<Item> focusedItems(IFocus<ItemStack> focus){
        return Collections.singleton(focus.getValue().getItem());
    }

    @Override
    public void draw(ChiselingRecipe recipe, PoseStack poseStack, double mouseX, double mouseY){
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
