package com.supermartijn642.rechiseled.compat.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.recipes.RecipeGuiLayouts;
import mezz.jei.gui.recipes.RecipeLayoutWithButtons;
import mezz.jei.library.gui.ingredients.RecipeSlot;
import mezz.jei.library.gui.ingredients.RendererOverrides;
import mezz.jei.library.gui.recipes.layout.builder.RecipeLayoutBuilder;
import mezz.jei.library.gui.recipes.layout.builder.RecipeSlotBuilder;
import mezz.jei.library.ingredients.DisplayIngredientAcceptor;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created 17/01/2026 by SuperMartijn642
 */
public class JEIFieldAccess {

    private static Field recipeSlotRect;

    public static void setRecipeSlotRect(RecipeSlot slot, ImmutableRect2i rect){
        if(recipeSlotRect == null)
            recipeSlotRect = getField(RecipeSlot.class, "rect");
        setValue(recipeSlotRect, slot, rect);
    }

    private static Field recipeLayoutBuilderSlots;

    public static List<RecipeSlotBuilder> getRecipeLayoutBuilderSlots(RecipeLayoutBuilder<?> recipeLayoutBuilder){
        if(recipeLayoutBuilderSlots == null)
            recipeLayoutBuilderSlots = getField(RecipeLayoutBuilder.class, "visibleSlots");
        return getValue(recipeLayoutBuilderSlots, recipeLayoutBuilder);
    }

    private static Field recipeLayoutBuilderIngredientManager;

    public static IIngredientManager getRecipeLayoutBuilderIngredientManager(RecipeLayoutBuilder<?> recipeLayoutBuilder){
        if(recipeLayoutBuilderIngredientManager == null)
            recipeLayoutBuilderIngredientManager = getField(RecipeLayoutBuilder.class, "ingredientManager");
        return getValue(recipeLayoutBuilderIngredientManager, recipeLayoutBuilder);
    }

    private static Field recipeLayoutBuilderNextSlotIndex;

    public static int getRecipeLayoutBuilderNextSlotIndex(RecipeLayoutBuilder<?> recipeLayoutBuilder){
        if(recipeLayoutBuilderNextSlotIndex == null)
            recipeLayoutBuilderNextSlotIndex = getField(RecipeLayoutBuilder.class, "nextSlotIndex");
        return getValue(recipeLayoutBuilderNextSlotIndex, recipeLayoutBuilder);
    }

    public static void setRecipeLayoutBuilderNextSlotIndex(RecipeLayoutBuilder<?> recipeLayoutBuilder, int value){
        if(recipeLayoutBuilderNextSlotIndex == null)
            recipeLayoutBuilderNextSlotIndex = getField(RecipeLayoutBuilder.class, "nextSlotIndex");
        setValue(recipeLayoutBuilderNextSlotIndex, recipeLayoutBuilder, value);
    }

    private static Field recipeSlotBuilderIngredients;

    public static DisplayIngredientAcceptor getRecipeSlotBuilderIngredients(RecipeSlotBuilder recipeSlotBuilder){
        if(recipeSlotBuilderIngredients == null)
            recipeSlotBuilderIngredients = getField(RecipeSlotBuilder.class, "ingredients");
        return getValue(recipeSlotBuilderIngredients, recipeSlotBuilder);
    }

    private static Field recipeSlotBuilderRole;

    public static RecipeIngredientRole getRecipeSlotBuilderRole(RecipeSlotBuilder recipeSlotBuilder){
        if(recipeSlotBuilderRole == null)
            recipeSlotBuilderRole = getField(RecipeSlotBuilder.class, "role");
        return getValue(recipeSlotBuilderRole, recipeSlotBuilder);
    }

    private static Field recipeSlotBuilderTooltipCallbacks;

    public static List<IRecipeSlotTooltipCallback> getRecipeSlotBuilderTooltipCallbacks(RecipeSlotBuilder recipeSlotBuilder){
        if(recipeSlotBuilderTooltipCallbacks == null)
            recipeSlotBuilderTooltipCallbacks = getField(RecipeSlotBuilder.class, "tooltipCallbacks");
        return getValue(recipeSlotBuilderTooltipCallbacks, recipeSlotBuilder);
    }

    private static Field recipeSlotBuilderRect;

    public static ImmutableRect2i getRecipeSlotBuilderRect(RecipeSlotBuilder recipeSlotBuilder){
        if(recipeSlotBuilderRect == null)
            recipeSlotBuilderRect = getField(RecipeSlotBuilder.class, "rect");
        return getValue(recipeSlotBuilderRect, recipeSlotBuilder);
    }

    private static Field recipeSlotBuilderRendererOverrides;

    public static RendererOverrides getRecipeSlotBuilderRendererOverrides(RecipeSlotBuilder recipeSlotBuilder){
        if(recipeSlotBuilderRendererOverrides == null)
            recipeSlotBuilderRendererOverrides = getField(RecipeSlotBuilder.class, "rendererOverrides");
        return getValue(recipeSlotBuilderRendererOverrides, recipeSlotBuilder);
    }

    private static Field recipeSlotBuilderBackground;

    public static IDrawable getRecipeSlotBuilderBackground(RecipeSlotBuilder recipeSlotBuilder){
        if(recipeSlotBuilderBackground == null)
            recipeSlotBuilderBackground = getField(RecipeSlotBuilder.class, "background");
        return getValue(recipeSlotBuilderBackground, recipeSlotBuilder);
    }

    private static Field recipeSlotBuilderOverlay;

    public static IDrawable getRecipeSlotBuilderOverlay(RecipeSlotBuilder recipeSlotBuilder){
        if(recipeSlotBuilderOverlay == null)
            recipeSlotBuilderOverlay = getField(RecipeSlotBuilder.class, "overlay");
        return getValue(recipeSlotBuilderOverlay, recipeSlotBuilder);
    }

    private static Field recipeSlotBuilderSlotName;

    public static String getRecipeSlotBuilderSlotName(RecipeSlotBuilder recipeSlotBuilder){
        if(recipeSlotBuilderSlotName == null)
            recipeSlotBuilderSlotName = getField(RecipeSlotBuilder.class, "slotName");
        return getValue(recipeSlotBuilderSlotName, recipeSlotBuilder);
    }

    private static Field recipeGuiGuiLayoutsRecipeLayoutsWithButtons;

    public static List<RecipeLayoutWithButtons<?>> getRecipeGuiGuiLayoutsRecipeLayoutsWithButtons(RecipeGuiLayouts recipeGuiLayouts){
        if(recipeGuiGuiLayoutsRecipeLayoutsWithButtons == null)
            recipeGuiGuiLayoutsRecipeLayoutsWithButtons = getField(RecipeGuiLayouts.class, "recipeLayoutsWithButtons");
        return getValue(recipeGuiGuiLayoutsRecipeLayoutsWithButtons, recipeGuiLayouts);
    }

    private static Field getField(Class<?> clazz, String name){
        try{
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        }catch(Exception e){
            throw new RuntimeException("Rechiseled failed to obtain field '" + name + "' access for '" + clazz.getName() + "' from JEI!", e);
        }
    }

    private static void setValue(Field field, Object instance, Object value){
        try{
            field.set(instance, value);
        }catch(IllegalAccessException e){
            throw new RuntimeException("Rechiseled failed to set field '" + field + "' for '" + instance.getClass() + "'!", e);
        }
    }

    private static <T> T getValue(Field field, Object instance){
        try{
            //noinspection unchecked
            return (T)field.get(instance);
        }catch(IllegalAccessException e){
            throw new RuntimeException("Rechiseled failed to get field '" + field + "' for '" + instance.getClass() + "'!", e);
        }
    }
}
