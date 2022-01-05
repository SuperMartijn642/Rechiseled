package com.supermartijn642.rechiseled.chiseling;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created 24/12/2021 by SuperMartijn642
 */
public class ChiselingRecipe implements Recipe<Container> {

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
    public boolean matches(Container inventory, Level world){
        return false;
    }

    @Override
    public ItemStack assemble(Container inventory){
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
    public RecipeSerializer<?> getSerializer(){
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType(){
        return ChiselingRecipes.CHISELING;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ChiselingRecipe> {

        @Override
        public ChiselingRecipe fromJson(ResourceLocation resourceLocation, JsonObject json){
            List<ChiselingEntry> chiselingEntries = new ArrayList<>();

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
                    regularItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(regularItemName));
                    if(regularItem == null && !optional)
                        throw new JsonParseException("Unknown item '" + regularItemName + "'");
                }
                String connectingItemName = GsonHelper.getAsString(object, "connecting_item", "");
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
        public ChiselingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buffer){
            List<ChiselingEntry> chiselingEntries = new ArrayList<>();

            int entries = buffer.readInt();
            for(int i = 0; i < entries; i++)
                chiselingEntries.add(
                    new ChiselingEntry(
                        buffer.readBoolean() ? Item.byId(buffer.readVarInt()) : null,
                        buffer.readBoolean() ? Item.byId(buffer.readVarInt()) : null
                    )
                );

            return new ChiselingRecipe(resourceLocation, chiselingEntries);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ChiselingRecipe recipe){
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
