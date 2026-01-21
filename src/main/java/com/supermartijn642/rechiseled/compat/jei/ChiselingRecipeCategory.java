package com.supermartijn642.rechiseled.compat.jei;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;
import com.supermartijn642.rechiseled.api.util.ItemWithMeta;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.gui.ingredients.GuiIngredient;
import mezz.jei.gui.ingredients.GuiIngredientGroup;
import mezz.jei.gui.ingredients.GuiItemStackGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.*;

/**
 * Created 28/12/2021 by SuperMartijn642
 */
public class ChiselingRecipeCategory implements IRecipeCategory<ChiselingRecipeCategory.ChiselingRecipeWrapper> {

    public static final String IDENTIFIER = Rechiseled.identifier("chiseling").toString();

    private final IDrawable background, slot, icon, arrow, focussedSlot;

    public ChiselingRecipeCategory(IGuiHelper guiHelper){
        this.background = guiHelper.createBlankDrawable(this.getWidth(), this.getHeight());
        this.slot = guiHelper.getSlotDrawable();
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(Rechiseled.chisel));
        this.arrow = guiHelper.drawableBuilder(Rechiseled.identifier("textures/screen/curved_arrow.png"), 0, 0, 20, 20).setTextureSize(20, 20).build();
        this.focussedSlot = guiHelper.drawableBuilder(Rechiseled.identifier("textures/screen/focussed_slot.png"), 0, 0, 18, 18).setTextureSize(18, 18).build();
    }

    @Override
    public String getModName(){
        return "Rechiseled";
    }

    @Override
    public String getUid(){
        return IDENTIFIER;
    }

    @Override
    public String getTitle(){
        return TextComponents.translation("rechiseled.recipe_category.title").format();
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
    public void setRecipe(IRecipeLayout recipeLayout, ChiselingRecipeWrapper wrapper, IIngredients ingredients){
        List<ItemStack> inputs = ingredients.getInputs(VanillaTypes.ITEM).get(0);
        List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);

        // Add focused items first
        IFocus<?> focus = recipeLayout.getFocus();
        Set<Item> focusedItems = focus == null ? Collections.emptySet() : focusedItems(focus);
        if(focus != null && focus.getMode() == IFocus.Mode.INPUT && !focusedItems.isEmpty())
            inputs.sort(Comparator.comparing(item -> !focusedItems.contains(item.getItem())));
        if(focus != null && focus.getMode() == IFocus.Mode.OUTPUT && !outputs.isEmpty())
            outputs.sort(Comparator.comparing(stacks -> stacks.stream().map(ItemStack::getItem).noneMatch(focusedItems::contains)));

        // Item conversion factor tooltip
        ITooltipCallback<ItemStack> tooltip = (slot, input, stack, builder) -> {
            ItemWithWorth itemWithWorth = wrapper.recipe.getWorth(ItemWithMeta.fromStack(stack));
            float worth = itemWithWorth == null ? 1 : itemWithWorth.worth(); // Should never be null, but just in case
            if(worth != 1){
                worth = Math.round(worth * 1000) / 1000f;
                builder.add(TextComponents.translation("rechiseled.recipe_category.conversion_value", TextComponents.number(worth).color(TextFormatting.GOLD).get()).color(TextFormatting.GRAY).format());
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
        wrapper.scrollBox = new JEIScrollableSlotsWidget(0, 22, outputSlots);
    }

    private static Set<Item> focusedItems(IFocus<?> focus){
        Object value = focus.getValue();
        if(value instanceof Item)
            return Collections.singleton((Item)value);
        else if(value instanceof ItemStack)
            return Collections.singleton(((ItemStack)value).getItem());
        return Collections.emptySet();
    }

    public void draw(ChiselingRecipeWrapper wrapper, double mouseX, double mouseY){
        // Arrow
        this.arrow.draw(ClientUtils.getMinecraft(), 89, 0);
        // Scrollable slots
        JEIScrollableSlotsWidget scrollableSlotsWidget = wrapper.scrollBox;
        if(scrollableSlotsWidget != null)
            scrollableSlotsWidget.draw(mouseX, mouseY);
    }

    public boolean mouseClicked(ChiselingRecipeWrapper wrapper, double mouseX, double mouseY, int button){
        JEIScrollableSlotsWidget scrollableSlotsWidget = wrapper.scrollBox;
        return scrollableSlotsWidget != null && scrollableSlotsWidget.mousePressed(mouseX, mouseY, button);
    }

    public boolean mouseReleased(ChiselingRecipeWrapper wrapper, double mouseX, double mouseY, int button){
        JEIScrollableSlotsWidget scrollableSlotsWidget = wrapper.scrollBox;
        return scrollableSlotsWidget != null && scrollableSlotsWidget.mouseReleased(mouseX, mouseY, button);
    }

    public boolean mouseScrolled(ChiselingRecipeWrapper wrapper, double mouseX, double mouseY, double amountY){
        JEIScrollableSlotsWidget scrollableSlotsWidget = wrapper.scrollBox;
        return scrollableSlotsWidget != null && scrollableSlotsWidget.mouseScrolled(mouseX, mouseY, amountY);
    }

    public static class ChiselingRecipeWrapper implements IRecipeWrapper {

        private final ChiselingRecipe recipe;
        public JEIScrollableSlotsWidget scrollBox;

        public ChiselingRecipeWrapper(ChiselingRecipe recipe){
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients){
            List<ItemStack> inputs = new ArrayList<>();
            List<List<ItemStack>> outputs = new ArrayList<>();

            for(ChiselingBlockShape shape : ChiselingBlockShape.values()){
                for(ChiselingEntry entry : this.recipe.entries()){
                    List<ItemStack> output = new ArrayList<>();
                    if(entry.hasRegularItem(shape)){
                        //noinspection DataFlowIssue
                        inputs.add(entry.getRegularItem(shape).item().toStack());
                        //noinspection DataFlowIssue
                        output.add(entry.getRegularItem(shape).item().toStack());
                    }
                    if(entry.hasConnectingItem(shape)){
                        //noinspection DataFlowIssue
                        inputs.add(entry.getConnectingItem(shape).item().toStack());
                        //noinspection DataFlowIssue
                        output.add(entry.getConnectingItem(shape).item().toStack());
                    }
                    if(output.isEmpty())
                        continue;
                    outputs.add(output);
                }
            }

            ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(inputs));
            ingredients.setOutputLists(VanillaTypes.ITEM, outputs);
        }
    }
}
