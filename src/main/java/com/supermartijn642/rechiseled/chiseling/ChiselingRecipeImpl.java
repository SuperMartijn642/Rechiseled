package com.supermartijn642.rechiseled.chiseling;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.*;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public class ChiselingRecipeImpl implements ChiselingRecipe {

    private final List<ChiselingEntry> entries;
    private final Map<Item,ItemWithWorth> items;

    /**
     * Used to hold a jei display widget on the client.
     */
    public Object jeiScrollBox;

    public ChiselingRecipeImpl(List<ChiselingEntry> entries){
        this.entries = List.copyOf(entries);
        Map<Item,ItemWithWorth> items = new HashMap<>();
        for(ChiselingEntry entry : entries){
            ((ChiselingEntryImpl)entry).items().forEach((item, worth) -> {
                items.merge(item, worth, (worth1, worth2) -> worth1.worth() > worth2.worth() ? worth1 : worth2);
            });
        }
        this.items = Map.copyOf(items);
    }

    @Override
    public List<ChiselingEntry> entries(){
        return this.entries;
    }

    @Override
    public boolean contains(ItemLike item){
        return this.items.containsKey(item.asItem());
    }

    @Override
    public ItemWithWorth getWorth(ItemLike item){
        return this.items.get(item.asItem());
    }

    public Set<Item> getItems(){
        return this.items.keySet();
    }

    public static void writeToStream(ChiselingRecipe recipe, FriendlyByteBuf buffer){
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

    public static ChiselingRecipe readFromStream(FriendlyByteBuf buffer){
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

    private static void writeItemToStream(ItemWithWorth item, FriendlyByteBuf buffer){
        if(item == null)
            buffer.writeBoolean(false);
        else{
            buffer.writeBoolean(true);
            buffer.writeInt(Item.getId(item.item()));
            buffer.writeFloat(item.worth());
        }
    }

    private static ItemWithWorth readItemFromStream(FriendlyByteBuf buffer){
        if(buffer.readBoolean()){
            Item item = Item.byId(buffer.readInt());
            float worth = buffer.readFloat();
            return ItemWithWorthImpl.of(item, worth);
        }
        return null;
    }
}
