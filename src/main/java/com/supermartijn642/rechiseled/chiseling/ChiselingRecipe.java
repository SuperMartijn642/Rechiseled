package com.supermartijn642.rechiseled.chiseling;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created 24/12/2021 by SuperMartijn642
 */
public class ChiselingRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    public static final Serializer SERIALIZER = new Serializer();

    private final List<ChiselingEntry> entries;

    public ChiselingRecipe(List<ChiselingEntry> entries){
        this.entries = Collections.unmodifiableList(entries);
    }

    public List<ChiselingEntry> getEntries(){
        return this.entries;
    }

    public boolean contains(ItemStack stack){
        for(ChiselingEntry entry : this.entries){
            if((entry.hasRegularItem() && entry.getRegularItem() == stack.getItem() && (!entry.getRegularItem().getHasSubtypes() || entry.getRegularItemData() == stack.getMetadata()))
                || (entry.hasConnectingItem() && entry.getConnectingItem() == stack.getItem() && (!entry.getConnectingItem().getHasSubtypes() || entry.getConnectingItemData() == stack.getMetadata())))
                return true;
        }
        return false;
    }

    @Override
    public boolean matches(InventoryCrafting inventory, World world){
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv){
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height){
        return false;
    }

    @Override
    public ItemStack getRecipeOutput(){
        return ItemStack.EMPTY;
    }

    public static class Serializer {

        public ChiselingRecipe fromJson(JsonContext context, JsonObject json){
            List<ChiselingEntry> chiselingEntries = new ArrayList<>();

            // read entries
            if(!JsonUtils.isJsonArray(json, "entries"))
                throw new JsonParseException("Recipe must have an 'entries' array!");
            JsonArray array = JsonUtils.getJsonArray(json, "entries");
            for(JsonElement element : array){
                if(!element.isJsonObject())
                    throw new JsonParseException("Recipe entries must be json objects with 'item' and 'connecting_item' keys!");
                JsonObject object = element.getAsJsonObject();
                if(!object.has("item") && !object.has("connecting_item"))
                    throw new JsonParseException("Recipe entry must be have at least one of 'item' or 'connecting_item' keys!");

                boolean optional = JsonUtils.getBoolean(object, "optional", false);

                String regularItemName = JsonUtils.getString(object, "item", "");
                Item regularItem;
                int regularItemData;
                if(regularItemName.isEmpty()){
                    regularItem = null;
                    regularItemData = 0;
                }else{
                    regularItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(regularItemName));
                    if(regularItem == null && !optional)
                        throw new JsonParseException("Unknown item '" + regularItemName + "'");
                    if(regularItem != null && regularItem.getHasSubtypes() && !object.has("item_data"))
                        throw new JsonParseException("Missing data for item '" + regularItemName + "'");
                    regularItemData = JsonUtils.getInt(object, "item_data", 0);
                }
                String connectingItemName = JsonUtils.getString(object, "connecting_item", "");
                Item connectingItem;
                int connectingItemData;
                if(connectingItemName.isEmpty()){
                    connectingItem = null;
                    connectingItemData = 0;
                }else{
                    connectingItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(connectingItemName));
                    if(connectingItem == null && !optional)
                        throw new JsonParseException("Unknown item '" + connectingItemName + "'");
                    if(connectingItem != null && connectingItem.getHasSubtypes() && !object.has("connecting_item_data"))
                        throw new JsonParseException("Missing data for item '" + connectingItemName + "'");
                    connectingItemData = JsonUtils.getInt(object, "connecting_item_data", 0);
                }

                if(regularItem == null && connectingItem == null)
                    throw new JsonParseException("Recipe entry must be have at least one of 'item' or 'connecting_item' keys!");

                chiselingEntries.add(new ChiselingEntry(regularItem, regularItemData, connectingItem, connectingItemData));
            }

            return new ChiselingRecipe(chiselingEntries);
        }
    }
}
