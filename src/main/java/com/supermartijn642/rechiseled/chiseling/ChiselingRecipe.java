package com.supermartijn642.rechiseled.chiseling;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created 24/12/2021 by SuperMartijn642
 */
public class ChiselingRecipe implements IRecipe<IInventory> {

    public static final Serializer SERIALIZER = new Serializer();

    private final ResourceLocation recipeId;
    private final List<ChiselingEntry> entries;

    public ChiselingRecipe(ResourceLocation recipeId, List<ChiselingEntry> entries){
        this.recipeId = recipeId;
        this.entries = Collections.unmodifiableList(entries);
    }

    public List<ChiselingEntry> getEntries(){
        return this.entries;
    }

    public boolean contains(ItemStack stack){
        for(ChiselingEntry entry : this.entries){
            if((entry.hasRegularItem() && entry.getRegularItem() == stack.getItem())
                || (entry.hasConnectingItem() && entry.getConnectingItem() == stack.getItem()))
                return true;
        }
        return false;
    }

    @Override
    public boolean matches(IInventory inventory, World world){
        return false;
    }

    @Override
    public ItemStack assemble(IInventory inventory){
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height){
        return false;
    }

    @Override
    public ItemStack getResultItem(){
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId(){
        return this.recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer(){
        return SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType(){
        return ChiselingRecipes.CHISELING;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ChiselingRecipe> {

        @Override
        public ChiselingRecipe fromJson(ResourceLocation resourceLocation, JsonObject json){
            List<ChiselingEntry> chiselingEntries = new ArrayList<>();

            // read entries
            if(!JSONUtils.isArrayNode(json, "entries"))
                throw new JsonParseException("Recipe must have an 'entries' array!");
            JsonArray array = JSONUtils.getAsJsonArray(json, "entries");
            for(JsonElement element : array){
                if(!element.isJsonObject())
                    throw new JsonParseException("Recipe entries must be json objects with 'item' and 'connecting_item' keys!");
                JsonObject object = element.getAsJsonObject();
                if(!object.has("item") && !object.has("connecting_item"))
                    throw new JsonParseException("Recipe entry must be have at least one of 'item' or 'connecting_item' keys!");

                boolean optional = JSONUtils.getAsBoolean(object, "optional", false);

                String regularItemName = JSONUtils.getAsString(object, "item", "");
                Item regularItem;
                if(regularItemName.isEmpty())
                    regularItem = null;
                else{
                    regularItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(regularItemName));
                    if(regularItem == null && !optional)
                        throw new JsonParseException("Unknown item '" + regularItemName + "'");
                }
                String connectingItemName = JSONUtils.getAsString(object, "connecting_item", "");
                Item connectingItem;
                if(connectingItemName.isEmpty())
                    connectingItem = null;
                else{
                    connectingItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(connectingItemName));
                    if(connectingItem == null && !optional)
                        throw new JsonParseException("Unknown item '" + connectingItemName + "'");
                }

                if(regularItem == null && connectingItem == null)
                    throw new JsonParseException("Recipe entry must be have at least one of 'item' or 'connecting_item' keys!");

                chiselingEntries.add(new ChiselingEntry(regularItem, connectingItem));
            }

            return new ChiselingRecipe(resourceLocation, chiselingEntries);
        }

        @Nullable
        @Override
        public ChiselingRecipe fromNetwork(ResourceLocation resourceLocation, PacketBuffer buffer){
            return null;
        }

        @Override
        public void toNetwork(PacketBuffer buffer, ChiselingRecipe recipe){

        }
    }
}
