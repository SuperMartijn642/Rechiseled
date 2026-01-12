package com.supermartijn642.rechiseled.compat.jei;

import mezz.jei.api.runtime.IIngredientVisibility;
import mezz.jei.common.gui.ingredients.RecipeSlot;
import mezz.jei.common.gui.recipes.layout.RecipeLayoutBuilder;
import mezz.jei.common.gui.recipes.layout.builder.IRecipeLayoutSlotSource;
import mezz.jei.common.ingredients.RegisteredIngredients;
import mezz.jei.common.util.ImmutableRect2i;

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

    public static List<IRecipeLayoutSlotSource> getRecipeLayoutBuilderSlots(RecipeLayoutBuilder recipeLayoutBuilder){
        if(recipeLayoutBuilderSlots == null)
            recipeLayoutBuilderSlots = getField(RecipeLayoutBuilder.class, "slots");
        return getValue(recipeLayoutBuilderSlots, recipeLayoutBuilder);
    }

    private static Field recipeLayoutBuilderRegisteredIngredients;

    public static RegisteredIngredients getRecipeLayoutBuilderRegisteredIngredients(RecipeLayoutBuilder recipeLayoutBuilder){
        if(recipeLayoutBuilderRegisteredIngredients == null)
            recipeLayoutBuilderRegisteredIngredients = getField(RecipeLayoutBuilder.class, "registeredIngredients");
        return getValue(recipeLayoutBuilderRegisteredIngredients, recipeLayoutBuilder);
    }

    private static Field recipeLayoutBuilderIngredientCycleOffset;

    public static int getRecipeLayoutBuilderIngredientCycleOffset(RecipeLayoutBuilder recipeLayoutBuilder){
        if(recipeLayoutBuilderIngredientCycleOffset == null)
            recipeLayoutBuilderIngredientCycleOffset = getField(RecipeLayoutBuilder.class, "ingredientCycleOffset");
        return getValue(recipeLayoutBuilderIngredientCycleOffset, recipeLayoutBuilder);
    }

    private static Field recipeLayoutBuilderIngredientVisibility;

    public static IIngredientVisibility getRecipeLayoutBuilderIngredientVisibility(RecipeLayoutBuilder recipeLayoutBuilder){
        if(recipeLayoutBuilderIngredientVisibility == null)
            recipeLayoutBuilderIngredientVisibility = getField(RecipeLayoutBuilder.class, "ingredientVisibility");
        return getValue(recipeLayoutBuilderIngredientVisibility, recipeLayoutBuilder);
    }

    private static Field recipeLayoutBuilderLegacyIngredientIndex;

    public static int getRecipeLayoutBuilderLegacyIngredientIndex(RecipeLayoutBuilder recipeLayoutBuilder){
        if(recipeLayoutBuilderLegacyIngredientIndex == null)
            recipeLayoutBuilderLegacyIngredientIndex = getField(RecipeLayoutBuilder.class, "legacyIngredientIndex");
        return getValue(recipeLayoutBuilderLegacyIngredientIndex, recipeLayoutBuilder);
    }

    public static void setRecipeLayoutBuilderLegacyIngredientIndex(RecipeLayoutBuilder recipeLayoutBuilder, int value){
        if(recipeLayoutBuilderLegacyIngredientIndex == null)
            recipeLayoutBuilderLegacyIngredientIndex = getField(RecipeLayoutBuilder.class, "legacyIngredientIndex");
        setValue(recipeLayoutBuilderLegacyIngredientIndex, recipeLayoutBuilder, value);
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
