package com.supermartijn642.rechiseled.chiseling;

import com.supermartijn642.core.CoreSide;
import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 18/01/2022 by SuperMartijn642
 */
public class PacketChiselingRecipes implements BasePacket {

    private List<ChiselingRecipe> recipes;

    public PacketChiselingRecipes(List<ChiselingRecipe> recipes){
        this.recipes = recipes;
    }

    public PacketChiselingRecipes(){
    }

    @Override
    public void write(FriendlyByteBuf buffer){
        buffer.writeInt(this.recipes.size());
        this.recipes.forEach(recipe -> ChiselingRecipe.Serializer.toNetwork(buffer, recipe));
    }

    @Override
    public void read(FriendlyByteBuf buffer){
        this.recipes = new ArrayList<>();

        int recipeCount = buffer.readInt();
        for(int i = 0; i < recipeCount; i++)
            this.recipes.add(ChiselingRecipe.Serializer.fromNetwork(buffer));
    }

    @Override
    public boolean verify(PacketContext context){
        return context.getHandlingSide() == CoreSide.CLIENT;
    }

    @Override
    public void handle(PacketContext context){
        ChiselingRecipes.setRecipes(this.recipes);
    }
}
