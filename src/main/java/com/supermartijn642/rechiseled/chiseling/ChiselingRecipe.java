package com.supermartijn642.rechiseled.chiseling;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created 24/12/2021 by SuperMartijn642
 */
public class ChiselingRecipe {

    private final ResourceLocation recipeId;
    final ResourceLocation parentRecipeId;
    private final List<ChiselingEntry> entries;

    private ChiselingRecipe(ResourceLocation recipeId, ResourceLocation parentRecipeId, List<ChiselingEntry> entries){
        this.recipeId = recipeId;
        this.parentRecipeId = parentRecipeId;
        this.entries = new ArrayList<>(entries);
    }

    public List<ChiselingEntry> getEntries(){
        return Collections.unmodifiableList(this.entries);
    }

    void addEntries(List<ChiselingEntry> entries){
        this.entries.addAll(entries);
    }

    public boolean contains(ItemStack stack){
        for(ChiselingEntry entry : this.entries){
            if((entry.hasRegularItem() && entry.getRegularItem() == stack.getItem())
                || (entry.hasConnectingItem() && entry.getConnectingItem() == stack.getItem()))
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
            String parentRecipeString = JSONUtils.getAsString(json, "parent", null);
            ResourceLocation parentRecipe = parentRecipeString == null ? null : new ResourceLocation(parentRecipeString);

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

            return new ChiselingRecipe(resourceLocation, parentRecipe, chiselingEntries);
        }

        public static ChiselingRecipe fromNetwork(PacketBuffer buffer){
            List<ChiselingEntry> chiselingEntries = new ArrayList<>();

            ResourceLocation recipeId = buffer.readResourceLocation();

            int entries = buffer.readInt();
            for(int i = 0; i < entries; i++)
                chiselingEntries.add(
                    new ChiselingEntry(
                        buffer.readBoolean() ? Item.byId(buffer.readVarInt()) : null,
                        buffer.readBoolean() ? Item.byId(buffer.readVarInt()) : null
                    )
                );

            return new ChiselingRecipe(recipeId, null, chiselingEntries);
        }

        public static void toNetwork(PacketBuffer buffer, ChiselingRecipe recipe){
            buffer.writeResourceLocation(recipe.recipeId);
            buffer.writeInt(recipe.entries.size());
            for(ChiselingEntry entry : recipe.entries){
                buffer.writeBoolean(entry.getRegularItem() != null);
                if(entry.getRegularItem() != null)
                    buffer.writeVarInt(Item.getId(entry.getRegularItem()));
                buffer.writeBoolean(entry.getConnectingItem() != null);
                if(entry.getConnectingItem() != null)
                    buffer.writeVarInt(Item.getId(entry.getConnectingItem()));
            }
        }
    }
}
