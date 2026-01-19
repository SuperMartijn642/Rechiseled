package com.supermartijn642.rechiseled.compat.jei;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.gui.ingredients.GuiIngredientGroup;

import java.lang.reflect.Field;

/**
 * Created 17/01/2026 by SuperMartijn642
 */
public class JEIFieldAccess {

    private static Field guiIngredientGroupIngredientHelper;

    public static <T> IIngredientHelper<T> getGuiIngredientGroupIngredientHelper(GuiIngredientGroup<T> guiIngredientGroup){
        if(guiIngredientGroupIngredientHelper == null)
            guiIngredientGroupIngredientHelper = getField(GuiIngredientGroup.class, "ingredientHelper");
        return getValue(guiIngredientGroupIngredientHelper, guiIngredientGroup);
    }

    private static Field guiIngredientGroupIngredientRenderer;

    public static <T> IIngredientRenderer<T> getGuiIngredientGroupIngredientRenderer(GuiIngredientGroup<T> guiIngredientGroup){
        if(guiIngredientGroupIngredientRenderer == null)
            guiIngredientGroupIngredientRenderer = getField(GuiIngredientGroup.class, "ingredientRenderer");
        return getValue(guiIngredientGroupIngredientRenderer, guiIngredientGroup);
    }

    private static Field guiIngredientGroupCycleOffset;

    public static int getGuiIngredientGroupCycleOffset(GuiIngredientGroup<?> guiIngredientGroup){
        if(guiIngredientGroupCycleOffset == null)
            guiIngredientGroupCycleOffset = getField(GuiIngredientGroup.class, "cycleOffset");
        return getValue(guiIngredientGroupCycleOffset, guiIngredientGroup);
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
