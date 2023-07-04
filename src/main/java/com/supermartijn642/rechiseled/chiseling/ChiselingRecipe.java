package com.supermartijn642.rechiseled.chiseling;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.supermartijn642.core.registry.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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
        this.entries = Collections.unmodifiableList(Arrays.asList(entries.toArray(ChiselingEntry[]::new)));
    }

    ChiselingRecipe(ResourceLocation recipeId, ResourceLocation parentRecipeId, Collection<ChiselingEntry> entries){
        this(recipeId, parentRecipeId, false, entries);
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

    public ResourceLocation getRecipeId(){
        return this.recipeId;
    }

    public static class Serializer {

        public static ChiselingRecipe fromJson(ResourceLocation resourceLocation, JsonObject json){
            List<ChiselingEntry> chiselingEntries = new ArrayList<>();

            // read parent id
            String parentRecipeString = GsonHelper.getAsString(json, "parent", null); // TODO remove in 1.2.0
            ResourceLocation parentRecipe = parentRecipeString == null ? null : new ResourceLocation(parentRecipeString);

            // Read overwrite value
            boolean overwrite = GsonHelper.getAsBoolean(json, "overwrite", false);

            // read entries
            if(!GsonHelper.isArrayNode(json, "entries"))
                throw new JsonParseException("Recipe must have an 'entries' array!");
            JsonArray array = GsonHelper.getAsJsonArray(json, "entries");
            for(JsonElement element : array){
                if(!element.isJsonObject())
                    throw new JsonParseException("Recipe entries must be json objects with 'item' and 'connecting_item' keys!");
                JsonObject object = element.getAsJsonObject();
                if(!object.has("item") && !object.has("connecting_item"))
                    throw new JsonParseException("Recipe entry must be have at least one of 'item' or 'connecting_item' keys!");

                boolean optional = GsonHelper.getAsBoolean(object, "optional", false);

                String regularItemName = GsonHelper.getAsString(object, "item", "");
                Item regularItem;
                if(regularItemName.isEmpty())
                    regularItem = null;
                else{
                    regularItem = Registries.ITEMS.getValue(new ResourceLocation(regularItemName));
                    if(regularItem == null && !optional)
                        throw new JsonParseException("Unknown item '" + regularItemName + "'");
                }
                String connectingItemName = GsonHelper.getAsString(object, "connecting_item", "");
                Item connectingItem;
                if(connectingItemName.isEmpty())
                    connectingItem = null;
                else{
                    connectingItem = Registries.ITEMS.getValue(new ResourceLocation(connectingItemName));
                    if(connectingItem == null && !optional)
                        throw new JsonParseException("Unknown item '" + connectingItemName + "'");
                }

                if(regularItem == null && connectingItem == null)
                    throw new JsonParseException("Recipe entry must be have at least one of 'item' or 'connecting_item' keys!");

                chiselingEntries.add(new ChiselingEntry(regularItem, connectingItem));
            }

            return new ChiselingRecipe(resourceLocation, parentRecipe, overwrite, chiselingEntries);
        }

        public static ChiselingRecipe fromNetwork(FriendlyByteBuf buffer){
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

            return new ChiselingRecipe(recipeId, null, false, chiselingEntries);
        }

        public static void toNetwork(FriendlyByteBuf buffer, ChiselingRecipe recipe){
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
