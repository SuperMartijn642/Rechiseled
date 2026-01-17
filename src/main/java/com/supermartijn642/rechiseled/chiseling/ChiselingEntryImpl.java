package com.supermartijn642.rechiseled.chiseling;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public class ChiselingEntryImpl implements ChiselingEntry {

    private final ResourceLocation owner, recipe;
    private final Item regularBlock, regularStairs, regularSlab;
    private final Item connectingBlock, connectingStairs, connectingSlab;

    private final Item primaryItem, primaryBlock, primaryStair, primarySlab;
    private final Set<Item> items;

    public ChiselingEntryImpl(ResourceLocation owner, ResourceLocation recipe, Item regularBlock, Item regularStairs, Item regularSlab, Item connectingBlock, Item connectingStairs, Item connectingSlab){
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
        this.primaryItem = this.primaryBlock == null ? this.primaryStair == null ? this.primarySlab : this.primaryStair : this.primaryBlock;
        if(this.primaryItem == null)
            throw new IllegalArgumentException("Entry must have at least one item!");
        this.items = Stream.of(
            regularBlock, regularStairs, regularStairs,
            connectingBlock, connectingStairs, connectingSlab
        ).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @Override
    public ResourceLocation owner(){
        return this.owner;
    }

    @Override
    public @Nullable ResourceLocation recipe(){
        return this.recipe;
    }

    Set<Item> items(){
        return this.items;
    }

    @Override
    public boolean hasShape(ChiselingBlockShape shape){
        switch(shape){
            case BLOCK:
                return this.primaryBlock != null;
            case STAIRS:
                return this.primaryStair != null;
            case SLAB:
                return this.primarySlab != null;
        }
        throw new AssertionError();
    }

    @Override
    public boolean hasRegularItem(ChiselingBlockShape shape){
        switch(shape){
            case BLOCK:
                return this.regularBlock != null;
            case STAIRS:
                return this.regularStairs != null;
            case SLAB:
                return this.regularSlab != null;
        }
        throw new AssertionError();
    }

    @Override
    public boolean hasConnectingItem(ChiselingBlockShape shape){
        switch(shape){
            case BLOCK:
                return this.connectingBlock != null;
            case STAIRS:
                return this.connectingStairs != null;
            case SLAB:
                return this.connectingSlab != null;
        }
        throw new AssertionError();
    }

    @Override
    public @Nullable Item getRegularItem(ChiselingBlockShape shape){
        switch(shape){
            case BLOCK:
                return this.primaryBlock;
            case STAIRS:
                return this.primaryStair;
            case SLAB:
                return this.primarySlab;
        }
        throw new AssertionError();
    }

    @Override
    public @Nullable Item getConnectingItem(ChiselingBlockShape shape){
        switch(shape){
            case BLOCK:
                return this.connectingBlock;
            case STAIRS:
                return this.connectingStairs;
            case SLAB:
                return this.connectingSlab;
        }
        throw new AssertionError();
    }

    @Override
    public @Nullable Item getAnyItem(ChiselingBlockShape shape){
        switch(shape){
            case BLOCK:
                return this.primaryBlock;
            case STAIRS:
                return this.primaryStair;
            case SLAB:
                return this.primarySlab;
        }
        throw new AssertionError();
    }

    @Override
    public Item getAnyItem(){
        return this.primaryItem;
    }

    @Override
    public boolean contains(IItemProvider item){
        return this.items.contains(item.asItem());
    }

    public static JsonObject toJson(ChiselingEntry entry){
        JsonObject json = new JsonObject();
        if(((ChiselingEntryImpl)entry).regularBlock != null)
            json.addProperty("block", Registries.ITEMS.getIdentifier(((ChiselingEntryImpl)entry).regularBlock).toString());
        if(((ChiselingEntryImpl)entry).regularStairs != null)
            json.addProperty("stairs", Registries.ITEMS.getIdentifier(((ChiselingEntryImpl)entry).regularStairs).toString());
        if(((ChiselingEntryImpl)entry).regularSlab != null)
            json.addProperty("slab", Registries.ITEMS.getIdentifier(((ChiselingEntryImpl)entry).regularSlab).toString());
        if(((ChiselingEntryImpl)entry).connectingBlock != null)
            json.addProperty("block", Registries.ITEMS.getIdentifier(((ChiselingEntryImpl)entry).connectingBlock).toString());
        if(((ChiselingEntryImpl)entry).connectingStairs != null)
            json.addProperty("stairs", Registries.ITEMS.getIdentifier(((ChiselingEntryImpl)entry).connectingStairs).toString());
        if(((ChiselingEntryImpl)entry).connectingSlab != null)
            json.addProperty("slab", Registries.ITEMS.getIdentifier(((ChiselingEntryImpl)entry).connectingSlab).toString());
        return json;
    }

    public static ChiselingEntry fromJson(JsonElement element){
        if(element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()){
            String s = element.getAsString();
            ResourceLocation identifier = ResourceLocation.tryParse(s);
            if(identifier == null)
                throw new JsonParseException("Invalid identifier '" + s + "'!");
            if(!Registries.ITEMS.hasIdentifier(identifier))
                throw new JsonParseException("Unknown item '" + identifier + "'!");
        }
        if(!element.isJsonObject())
            throw new JsonParseException("Entry elements must be objects!");
        JsonObject json = element.getAsJsonObject();

        // Legacy format
        if(json.has("item") || json.has("connecting_item")){
            Item item = readItem(json, "item");
            Item connectingItem = readItem(json, "connecting_item");
            if(item == null && connectingItem == null)
                throw new JsonParseException("Empty chiseling entry!");
            return new ChiselingEntryImpl(
                null, null,
                item, null, null,
                connectingItem, null, null
            );
        }

        Item regularBlock = readItem(json, "block");
        Item regularStairs = readItem(json, "stairs");
        Item regularSlab = readItem(json, "slab");
        Item connectingBlock = readItem(json, "connecting_block");
        Item connectingStairs = readItem(json, "connecting_stairs");
        Item connectingSlab = readItem(json, "connecting_slab");
        if(regularBlock == null && regularStairs == null && regularSlab == null && connectingBlock == null && connectingStairs == null && connectingSlab == null)
            throw new JsonParseException("Empty chiseling entry!");
        return new ChiselingEntryImpl(
            null, null,
            regularBlock, regularStairs, regularSlab,
            connectingBlock, connectingStairs, connectingSlab
        );
    }

    private static Item readItem(JsonObject json, String key){
        if(!json.has(key))
            return null;
        if(!json.get(key).isJsonPrimitive() || !json.getAsJsonPrimitive(key).isString())
            throw new JsonParseException("Entry property '" + key + "' must be a string!");
        String s = json.get(key).getAsString();
        ResourceLocation identifier = ResourceLocation.tryParse(s);
        if(identifier == null)
            throw new JsonParseException("Invalid identifier '" + s + "' for entry property '" + key + "'!");
        if(!Registries.ITEMS.hasIdentifier(identifier))
            throw new JsonParseException("Unknown item '" + identifier + "' for entry property '" + key + "'!");
        return Registries.ITEMS.getValue(identifier);
    }
}
