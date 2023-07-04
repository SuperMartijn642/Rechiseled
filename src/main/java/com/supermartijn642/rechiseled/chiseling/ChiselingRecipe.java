package com.supermartijn642.rechiseled.chiseling;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.*;

/**
 * Created 24/12/2021 by SuperMartijn642
 */
public class ChiselingRecipe {

    private final ResourceLocation recipeId;
    final ResourceLocation parentRecipeId;
    final boolean overwrite;
    private final List<ChiselingEntry> entries;

    private ChiselingRecipe(ResourceLocation recipeId, ResourceLocation parentRecipeId, boolean overwrite, Collection<ChiselingEntry> entries){
        this.recipeId = recipeId;
        this.parentRecipeId = parentRecipeId;
        this.overwrite = overwrite;
        this.entries = Collections.unmodifiableList(Arrays.asList(entries.toArray(new ChiselingEntry[0])));
    }

    ChiselingRecipe(ResourceLocation recipeId, ResourceLocation parentRecipeId, Collection<ChiselingEntry> entries){
        this(recipeId, parentRecipeId, false, entries);
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

    public ResourceLocation getRecipeId(){
        return this.recipeId;
    }

    public static class Serializer {

        public static ChiselingRecipe fromJson(ResourceLocation resourceLocation, JsonObject json){
            List<ChiselingEntry> chiselingEntries = new ArrayList<>();

            // read parent id
            String parentRecipeString = JsonUtils.getString(json, "parent", null); // TODO remove in 1.2.0
            ResourceLocation parentRecipe = parentRecipeString == null ? null : new ResourceLocation(parentRecipeString);

            // Read overwrite value
            boolean overwrite = JsonUtils.getBoolean(json, "overwrite", false);

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

            return new ChiselingRecipe(resourceLocation, parentRecipe, overwrite, chiselingEntries);
        }
    }
}
