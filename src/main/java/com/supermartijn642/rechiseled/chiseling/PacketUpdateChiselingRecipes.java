package com.supermartijn642.rechiseled.chiseling;

import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 18/01/2022 by SuperMartijn642
 */
public class PacketUpdateChiselingRecipes implements BasePacket {

    private List<ChiselingRecipe> recipes;

    public PacketUpdateChiselingRecipes(List<ChiselingRecipe> recipes){
        this.recipes = recipes;
    }

    public PacketUpdateChiselingRecipes(){
    }

    @Override
    public void write(FriendlyByteBuf buffer){
        buffer.writeInt(this.recipes.size());
        this.recipes.forEach(recipe -> ChiselingRecipeImpl.writeToStream(recipe, buffer));
    }

    @Override
    public void read(FriendlyByteBuf buffer){
        this.recipes = new ArrayList<>();

        int recipeCount = buffer.readInt();
        for(int i = 0; i < recipeCount; i++)
            this.recipes.add(ChiselingRecipeImpl.readFromStream(buffer));
    }

    @Override
    public void handle(PacketContext context){
        ChiselingRecipeManagerImpl.get(true).updateRecipes(this.recipes);
    }
}
