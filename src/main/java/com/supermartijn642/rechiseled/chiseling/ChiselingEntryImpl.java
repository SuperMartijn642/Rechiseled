package com.supermartijn642.rechiseled.chiseling;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public class ChiselingEntryImpl implements ChiselingEntry {

    private final ResourceLocation owner, recipe;
    private final ItemWithWorth regularBlock, regularStairs, regularSlab;
    private final ItemWithWorth connectingBlock, connectingStairs, connectingSlab;

    private final ItemWithWorth primaryItem, primaryBlock, primaryStair, primarySlab, primaryRegularItem, primaryConnectingItem;
    private final Map<Item,ItemWithWorth> items;

    public ChiselingEntryImpl(ResourceLocation owner, ResourceLocation recipe,
                              ItemWithWorth regularBlock, ItemWithWorth regularStairs, ItemWithWorth regularSlab,
                              ItemWithWorth connectingBlock, ItemWithWorth connectingStairs, ItemWithWorth connectingSlab){
        Item duplicate = checkDuplicateItems(regularBlock, regularStairs, regularSlab, connectingBlock, connectingStairs, connectingSlab);
        if(duplicate != null)
            throw new IllegalArgumentException("Duplicate item '" + BuiltInRegistries.ITEM.getKey(duplicate) + "'!");
        this.owner = owner;
        this.recipe = recipe;
        this.regularBlock = regularBlock;
        this.regularStairs = regularStairs;
        this.regularSlab = regularSlab;
        this.connectingBlock = connectingBlock;
        this.connectingStairs = connectingStairs;
        this.connectingSlab = connectingSlab;

        this.primaryBlock = regularBlock == null ? connectingBlock : regularBlock;
        this.primaryStair = regularStairs == null ? connectingStairs : regularStairs;
        this.primarySlab = regularSlab == null ? connectingSlab : regularSlab;
        this.primaryRegularItem = regularBlock == null ? regularStairs == null ? regularSlab : regularStairs : regularBlock;
        this.primaryConnectingItem = connectingBlock == null ? connectingStairs == null ? connectingSlab : connectingStairs : connectingBlock;
        this.primaryItem = this.primaryBlock == null ? this.primaryStair == null ? this.primarySlab : this.primaryStair : this.primaryBlock;
        if(this.primaryItem == null)
            throw new IllegalArgumentException("Entry must have at least one item!");
        this.items = Stream.of(
            regularBlock, regularStairs, regularSlab,
            connectingBlock, connectingStairs, connectingSlab
        ).filter(Objects::nonNull).collect(Collectors.toUnmodifiableMap(ItemWithWorth::item, Function.identity()));
    }

    @Override
    public ResourceLocation owner(){
        return this.owner;
    }

    @Override
    public @Nullable ResourceLocation recipe(){
        return this.recipe;
    }

    public Map<Item,ItemWithWorth> items(){
        return this.items;
    }

    @Override
    public boolean hasShape(ChiselingBlockShape shape){
        return switch(shape){
            case BLOCK -> this.primaryBlock != null;
            case STAIRS -> this.primaryStair != null;
            case SLAB -> this.primarySlab != null;
        };
    }

    @Override
    public boolean hasRegularItem(ChiselingBlockShape shape){
        return switch(shape){
            case BLOCK -> this.regularBlock != null;
            case STAIRS -> this.regularStairs != null;
            case SLAB -> this.regularSlab != null;
        };
    }

    @Override
    public boolean hasConnectingItem(ChiselingBlockShape shape){
        return switch(shape){
            case BLOCK -> this.connectingBlock != null;
            case STAIRS -> this.connectingStairs != null;
            case SLAB -> this.connectingSlab != null;
        };
    }

    @Override
    public @Nullable ItemWithWorth getRegularItem(ChiselingBlockShape shape){
        return switch(shape){
            case BLOCK -> this.regularBlock;
            case STAIRS -> this.regularStairs;
            case SLAB -> this.regularSlab;
        };
    }

    @Override
    public @Nullable ItemWithWorth getConnectingItem(ChiselingBlockShape shape){
        return switch(shape){
            case BLOCK -> this.connectingBlock;
            case STAIRS -> this.connectingStairs;
            case SLAB -> this.connectingSlab;
        };
    }

    @Override
    public @Nullable ItemWithWorth getAnyItem(ChiselingBlockShape shape){
        return switch(shape){
            case BLOCK -> this.primaryBlock;
            case STAIRS -> this.primaryStair;
            case SLAB -> this.primarySlab;
        };
    }

    @Override
    public ItemWithWorth getAnyItem(){
        return this.primaryItem;
    }

    @Override
    public @Nullable ItemWithWorth getAnyRegularItem(){
        return this.primaryRegularItem;
    }

    @Override
    public @Nullable ItemWithWorth getAnyConnectingItem(){
        return this.primaryConnectingItem;
    }

    @Override
    public boolean contains(ItemLike item){
        return this.items.containsKey(item.asItem());
    }

    public static ChiselingEntry fromJson(JsonElement element){
        if(element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()){
            String s = element.getAsString();
            ResourceLocation identifier = ResourceLocation.tryParse(s);
            if(identifier == null)
                throw new JsonParseException("Invalid identifier '" + s + "'!");
            Optional<Item> optional = BuiltInRegistries.ITEM.getOptional(identifier);
            if(optional.isEmpty())
                throw new JsonParseException("Unknown item '" + identifier + "'!");
            ItemWithWorth item = ItemWithWorthImpl.defaultWorth(optional.get());
            return new ChiselingEntryImpl(
                null, null,
                item, null, null,
                null, null, null
            );
        }
        if(!element.isJsonObject())
            throw new JsonParseException("Entry elements must be objects!");
        JsonObject json = element.getAsJsonObject();
        boolean optional = false;
        if(json.has("optional")){
            if(!json.get("optional").isJsonPrimitive() || !json.getAsJsonPrimitive("optional").isBoolean())
                throw new JsonParseException("Entry property 'optional' must be a boolean!");
            optional = json.get("optional").getAsBoolean();
        }

        // Legacy format
        if(json.has("item") || json.has("connecting_item")){
            ItemWithWorth item = readItem(json, "item", optional);
            ItemWithWorth connectingItem = readItem(json, "connecting_item", optional);
            if(item == null && connectingItem == null){
                if(!optional)
                    throw new JsonParseException("Empty chiseling entry!");
                return null;
            }
            Item duplicate = checkDuplicateItems(item, connectingItem);
            if(duplicate != null)
                throw new JsonParseException("Duplicate item '" + BuiltInRegistries.ITEM.getKey(duplicate) + "' within one entry!");
            return new ChiselingEntryImpl(
                null, null,
                item, null, null,
                connectingItem, null, null
            );
        }

        if(!json.has("block") && !json.has("stairs") && !json.has("slab") && !json.has("connecting_block") && !json.has("connecting_stairs") && !json.has("connecting_slab"))
            throw new JsonParseException("Entry must have at least one of 'block', 'stairs', 'slab', 'connecting_block', 'connecting_stairs' or 'connecting_slab'!");
        ItemWithWorth regularBlock = readItem(json, "block", optional);
        ItemWithWorth regularStairs = readItem(json, "stairs", optional);
        ItemWithWorth regularSlab = readItem(json, "slab", optional);
        ItemWithWorth connectingBlock = readItem(json, "connecting_block", optional);
        ItemWithWorth connectingStairs = readItem(json, "connecting_stairs", optional);
        ItemWithWorth connectingSlab = readItem(json, "connecting_slab", optional);
        if(regularBlock == null && regularStairs == null && regularSlab == null && connectingBlock == null && connectingStairs == null && connectingSlab == null){
            if(!optional)
                throw new JsonParseException("Empty chiseling entry!");
            return null;
        }
        Item duplicate = checkDuplicateItems(regularBlock, regularStairs, regularSlab, connectingBlock, connectingStairs, connectingSlab);
        if(duplicate != null)
            throw new JsonParseException("Duplicate item '" + BuiltInRegistries.ITEM.getKey(duplicate) + "' within one entry!");
        return new ChiselingEntryImpl(
            null, null,
            regularBlock, regularStairs, regularSlab,
            connectingBlock, connectingStairs, connectingSlab
        );
    }

    private static ItemWithWorth readItem(JsonObject json, String key, boolean optional){
        // Item
        if(!json.has(key))
            return null;
        if(!json.get(key).isJsonPrimitive() || !json.getAsJsonPrimitive(key).isString())
            throw new JsonParseException("Entry property '" + key + "' must be a string!");
        String s = json.get(key).getAsString();
        ResourceLocation identifier = ResourceLocation.tryParse(s);
        if(identifier == null)
            throw new JsonParseException("Invalid identifier '" + s + "' for entry property '" + key + "'!");
        Optional<Item> item = BuiltInRegistries.ITEM.getOptional(identifier);
        if(item.isEmpty()){
            if(!optional)
                throw new JsonParseException("Unknown item '" + identifier + "' for entry property '" + key + "'!");
            return null;
        }
        if(!json.has(key + "_worth"))
            return item.map(ItemWithWorthImpl::defaultWorth).orElse(null);

        // Worth
        JsonElement element = json.get(key + "_worth");
        if(!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isNumber())
            throw new JsonParseException("Entry property '" + key + "' must be a number!");
        float worth = element.getAsFloat();
        if(worth <= 0)
            throw new JsonParseException("Invalid worth '" + worth + "' for entry property '" + key + "'!");
        return ItemWithWorthImpl.of(item.get(), worth);
    }

    private static Item checkDuplicateItems(ItemWithWorth... items){
        for(int i = 0; i < items.length; i++){
            if(items[i] == null)
                continue;
            for(int j = i + 1; j < items.length; j++){
                if(items[j] != null && items[i].item() == items[j].item())
                    return items[i].item();
            }
        }
        return null;
    }
}
