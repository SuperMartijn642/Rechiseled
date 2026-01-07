package com.supermartijn642.rechiseled.chiseling;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

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
    private final Set<Item> items;

    public ChiselingRecipeImpl(List<ChiselingEntry> entries){
        this.entries = List.copyOf(entries);
        this.items = entries.stream()
            .map(ChiselingEntryImpl.class::cast)
            .map(ChiselingEntryImpl::items)
            .flatMap(Collection::stream)
            .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public List<ChiselingEntry> entries(){
        return this.entries;
    }

    @Override
    public boolean contains(ItemLike item){
        return this.items.contains(item.asItem());
    }

    public Set<Item> getItems(){
        return this.items;
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
                buffer.writeBoolean(entry.hasRegularItem(shape));
                if(entry.hasRegularItem(shape))
                    buffer.writeInt(Item.getId(entry.getRegularItem(shape)));
                buffer.writeBoolean(entry.hasConnectingItem(shape));
                if(entry.hasConnectingItem(shape))
                    buffer.writeInt(Item.getId(entry.getConnectingItem(shape)));
            }
        }
    }

    public static ChiselingRecipe readFromStream(FriendlyByteBuf buffer){
        int entryCount = buffer.readInt();
        List<ChiselingEntry> entries = new ArrayList<>(Math.min(entryCount, 255));
        for(int i = 0; i < entryCount; i++){
            ResourceLocation owner = buffer.readResourceLocation();
            ResourceLocation recipe = buffer.readBoolean() ? buffer.readResourceLocation() : null;
            Item regularBlock = buffer.readBoolean() ? Item.byId(buffer.readInt()) : null;
            Item connectingBlock = buffer.readBoolean() ? Item.byId(buffer.readInt()) : null;
            Item regularStair = buffer.readBoolean() ? Item.byId(buffer.readInt()) : null;
            Item connectingStair = buffer.readBoolean() ? Item.byId(buffer.readInt()) : null;
            Item regularSlab = buffer.readBoolean() ? Item.byId(buffer.readInt()) : null;
            Item connectingSlab = buffer.readBoolean() ? Item.byId(buffer.readInt()) : null;
            entries.add(new ChiselingEntryImpl(
                owner,
                recipe,
                regularBlock, regularStair, regularSlab,
                connectingBlock, connectingStair, connectingSlab
            ));
        }
        return new ChiselingRecipeImpl(entries);
    }
}
