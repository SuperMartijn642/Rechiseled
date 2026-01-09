package com.supermartijn642.rechiseled.chiseling;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.*;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public class ChiselingRecipeImpl implements ChiselingRecipe {

    private final List<ChiselingEntry> entries;
    private final Map<Item,ItemWithWorth> items;

    public ChiselingRecipeImpl(List<ChiselingEntry> entries){
        this.entries = ImmutableList.copyOf(entries);
        Map<Item,ItemWithWorth> items = new HashMap<>();
        for(ChiselingEntry entry : entries){
            ((ChiselingEntryImpl)entry).items().forEach((item, worth) -> {
                items.merge(item, worth, (worth1, worth2) -> worth1.worth() > worth2.worth() ? worth1 : worth2);
            });
        }
        this.items = ImmutableMap.copyOf(items);
    }

    @Override
    public List<ChiselingEntry> entries(){
        return this.entries;
    }

    @Override
    public boolean contains(IItemProvider item){
        return this.items.containsKey(item.asItem());
    }

    @Override
    public float getWorth(IItemProvider item){
        ItemWithWorth worth = this.items.get(item.asItem());
        return worth == null ? -1 : worth.worth();
    }

    public Set<Item> getItems(){
        return this.items.keySet();
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
            ItemWithWorth regularBlock = readItemFromStream(buffer);
            ItemWithWorth connectingBlock = readItemFromStream(buffer);
            ItemWithWorth regularStair = readItemFromStream(buffer);
            ItemWithWorth connectingStair = readItemFromStream(buffer);
            ItemWithWorth regularSlab = readItemFromStream(buffer);
            ItemWithWorth connectingSlab = readItemFromStream(buffer);
            entries.add(new ChiselingEntryImpl(
                owner,
                recipe,
                regularBlock, regularStair, regularSlab,
                connectingBlock, connectingStair, connectingSlab
            ));
        }
        return new ChiselingRecipeImpl(entries);
    }

    private static void writeItemToStream(ItemWithWorth item, PacketBuffer buffer){
        if(item == null)
            buffer.writeBoolean(false);
        else{
            buffer.writeBoolean(true);
            buffer.writeInt(Item.getId(item.item()));
            buffer.writeFloat(item.worth());
        }
    }

    private static ItemWithWorth readItemFromStream(PacketBuffer buffer){
        if(buffer.readBoolean()){
            Item item = Item.byId(buffer.readInt());
            float worth = buffer.readFloat();
            return ItemWithWorthImpl.of(item, worth);
        }
        return null;
    }
}
