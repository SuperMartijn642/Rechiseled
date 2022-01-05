package com.supermartijn642.rechiseled.chiseling;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

/**
 * Created 04/01/2022 by SuperMartijn642
 */
public class ChiselingRecipeFactory implements IRecipeFactory {

    @Override
    public IRecipe parse(JsonContext context, JsonObject json){
        return ChiselingRecipe.SERIALIZER.fromJson(context, json);
    }
}
