package com.supermartijn642.rechiseled.chiseling;

import com.google.common.collect.ImmutableList;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.api.util.ItemWithMeta;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public class ChiselingRecipeImpl implements ChiselingRecipe {

    private final List<ChiselingEntry> entries;
    private final Set<ItemWithMeta> items;

    public ChiselingRecipeImpl(List<ChiselingEntry> entries){
        this.entries = ImmutableList.copyOf(entries);
        this.items = entries.stream()
            .map(ChiselingEntryImpl.class::cast)
            .map(ChiselingEntryImpl::items)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    @Override
    public List<ChiselingEntry> entries(){
        return this.entries;
    }

    @Override
    public boolean contains(ItemWithMeta item){
        return this.items.contains(item);
    }

    public Set<ItemWithMeta> getItems(){
        return this.items;
    }

    public static void writeToStream(ChiselingRecipe recipe, PacketBuffer buffer){
        List<ChiselingEntry> entries = ((ChiselingRecipeImpl)recipe).entries;
        buffer.writeInt(entries.size());
        for(ChiselingEntry entry : entries){
            buffer.writeResourceLocation(entry.owner());
            buffer.writeBoolean(entry.recipe() != null);
            if(entry.recipe() != null)
                //noinspection DataFlowIssue
                buffer.writeResourceLocation(entry.recipe());
            for(ChiselingBlockShape shape : ChiselingBlockShape.values()){
                writeItemToStream(entry.getRegularItem(shape), buffer);
                writeItemToStream(entry.getConnectingItem(shape), buffer);
            }
        }
    }

    public static ChiselingRecipe readFromStream(PacketBuffer buffer){
        int entryCount = buffer.readInt();
        List<ChiselingEntry> entries = new ArrayList<>(Math.min(entryCount, 255));
        for(int i = 0; i < entryCount; i++){
            ResourceLocation owner = buffer.readResourceLocation();
            ResourceLocation recipe = buffer.readBoolean() ? buffer.readResourceLocation() : null;
            ItemWithMeta regularBlock = readItemFromStream(buffer);
            ItemWithMeta connectingBlock = readItemFromStream(buffer);
            ItemWithMeta regularStair = readItemFromStream(buffer);
            ItemWithMeta connectingStair = readItemFromStream(buffer);
            ItemWithMeta regularSlab = readItemFromStream(buffer);
            ItemWithMeta connectingSlab = readItemFromStream(buffer);
            entries.add(new ChiselingEntryImpl(
                owner,
                recipe,
                regularBlock, regularStair, regularSlab,
                connectingBlock, connectingStair, connectingSlab
            ));
        }
        return new ChiselingRecipeImpl(entries);
    }

    private static void writeItemToStream(ItemWithMeta item, PacketBuffer buffer){
        if(item == null){
            buffer.writeBoolean(false);
            return;
        }
        buffer.writeBoolean(true);
        buffer.writeInt(Item.getIdFromItem(item.item()));
        if(item.hasSubtypes())
            buffer.writeInt(item.meta());
    }

    private static ItemWithMeta readItemFromStream(PacketBuffer buffer){
        if(!buffer.readBoolean())
            return null;
        Item item = Item.getItemById(buffer.readInt());
        if(!item.getHasSubtypes())
            return ItemWithMeta.of(item);
        return ItemWithMeta.of(item, buffer.readInt());
    }
}
