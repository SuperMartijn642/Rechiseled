package com.supermartijn642.rechiseled.chiseling;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.core.registry.RegistryUtil;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.util.ItemWithMeta;
import net.minecraft.item.Item;
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
    private final ItemWithMeta regularBlock, regularStairs, regularSlab;
    private final ItemWithMeta connectingBlock, connectingStairs, connectingSlab;

    private final ItemWithMeta primaryItem, primaryBlock, primaryStair, primarySlab;
    private final Set<ItemWithMeta> items;

    public ChiselingEntryImpl(ResourceLocation owner, ResourceLocation recipe, ItemWithMeta regularBlock, ItemWithMeta regularStairs, ItemWithMeta regularSlab, ItemWithMeta connectingBlock, ItemWithMeta connectingStairs, ItemWithMeta connectingSlab){
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
            regularBlock, regularStairs, regularSlab,
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

    Set<ItemWithMeta> items(){
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
    public @Nullable ItemWithMeta getRegularItem(ChiselingBlockShape shape){
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
    public @Nullable ItemWithMeta getConnectingItem(ChiselingBlockShape shape){
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
    public @Nullable ItemWithMeta getAnyItem(ChiselingBlockShape shape){
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
    public ItemWithMeta getAnyItem(){
        return this.primaryItem;
    }

    @Override
    public boolean contains(ItemWithMeta item){
        return this.items.contains(item);
    }

    public static JsonObject toJson(ChiselingEntry entry){
        JsonObject json = new JsonObject();
        writeItem(json, "block", ((ChiselingEntryImpl)entry).regularBlock);
        writeItem(json, "stairs", ((ChiselingEntryImpl)entry).regularStairs);
        writeItem(json, "slab", ((ChiselingEntryImpl)entry).regularSlab);
        writeItem(json, "block", ((ChiselingEntryImpl)entry).connectingBlock);
        writeItem(json, "stairs", ((ChiselingEntryImpl)entry).connectingStairs);
        writeItem(json, "slab", ((ChiselingEntryImpl)entry).connectingSlab);
        return json;
    }

    public static ChiselingEntry fromJson(JsonElement element){
        if(element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()){
            String s = element.getAsString();
            if(!RegistryUtil.isValidIdentifier(s))
                throw new JsonParseException("Invalid identifier '" + s + "'!");
            ResourceLocation identifier = new ResourceLocation(s);
            if(!Registries.ITEMS.hasIdentifier(identifier))
                throw new JsonParseException("Unknown item '" + identifier + "'!");
            Item item = Registries.ITEMS.getValue(identifier);
            return new ChiselingEntryImpl(
                null, null,
                ItemWithMeta.of(item), null, null,
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
            ItemWithMeta item = readItem(json, "item", optional);
            ItemWithMeta connectingItem = readItem(json, "connecting_item", optional);
            if(item == null && connectingItem == null){
                if(!optional)
                    throw new JsonParseException("Empty chiseling entry!");
                return null;
            }
            return new ChiselingEntryImpl(
                null, null,
                item, null, null,
                connectingItem, null, null
            );
        }

        if(!json.has("block") && !json.has("stairs") && !json.has("slab") && !json.has("connecting_block") && !json.has("connecting_stairs") && !json.has("connecting_slab"))
            throw new JsonParseException("Entry must have at least one of 'block', 'stairs', 'slab', 'connecting_block', 'connecting_stairs' or 'connecting_slab'!");
        ItemWithMeta regularBlock = readItem(json, "block", optional);
        ItemWithMeta regularStairs = readItem(json, "stairs", optional);
        ItemWithMeta regularSlab = readItem(json, "slab", optional);
        ItemWithMeta connectingBlock = readItem(json, "connecting_block", optional);
        ItemWithMeta connectingStairs = readItem(json, "connecting_stairs", optional);
        ItemWithMeta connectingSlab = readItem(json, "connecting_slab", optional);
        if(regularBlock == null && regularStairs == null && regularSlab == null && connectingBlock == null && connectingStairs == null && connectingSlab == null){
            if(!optional)
                throw new JsonParseException("Empty chiseling entry!");
            return null;
        }
        return new ChiselingEntryImpl(
            null, null,
            regularBlock, regularStairs, regularSlab,
            connectingBlock, connectingStairs, connectingSlab
        );
    }

    private static void writeItem(JsonObject json, String key, ItemWithMeta item){
        if(item == null)
            return;
        json.addProperty(key, Registries.ITEMS.getIdentifier(item.item()).toString());
        if(item.hasSubtypes())
            json.addProperty(key + "_meta", item.meta());
    }

    private static ItemWithMeta readItem(JsonObject json, String key, boolean optional){
        if(!json.has(key))
            return null;
        if(!json.get(key).isJsonPrimitive() || !json.getAsJsonPrimitive(key).isString())
            throw new JsonParseException("Entry property '" + key + "' must be a string!");
        String s = json.get(key).getAsString();
        if(!RegistryUtil.isValidIdentifier(s))
            throw new JsonParseException("Invalid identifier '" + s + "' for entry property '" + key + "'!");
        ResourceLocation identifier = new ResourceLocation(s);
        if(!Registries.ITEMS.hasIdentifier(identifier))
            throw new JsonParseException("Unknown item '" + identifier + "' for entry property '" + key + "'!");
        Item item = Registries.ITEMS.getValue(identifier);
        int meta = 0;
        if(json.has(key + "_meta")){
            if(!item.getHasSubtypes())
                throw new JsonParseException("Specified metadata '" + key + "_meta' for item without subtypes '" + identifier + "'!");
            if(!json.get(key + "_meta").isJsonPrimitive() || !json.getAsJsonPrimitive(key + "_meta").isNumber())
                throw new JsonParseException("Entry property '" + key + "_meta' must be an integer!");
            meta = json.getAsJsonPrimitive(key + "_meta").getAsInt();
            if(meta < 0)
                throw new JsonParseException("Invalid metadata '" + meta + "' for entry property '" + key + "_meta'!");
        }
        return ItemWithMeta.of(item, meta);
    }
}
