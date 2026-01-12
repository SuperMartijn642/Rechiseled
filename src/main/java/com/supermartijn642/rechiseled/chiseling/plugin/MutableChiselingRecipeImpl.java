package com.supermartijn642.rechiseled.chiseling.plugin;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;
import com.supermartijn642.rechiseled.api.chiseling.plugin.MutableChiselingRecipe;
import com.supermartijn642.rechiseled.chiseling.ChiselingEntryImpl;
import com.supermartijn642.rechiseled.chiseling.ItemWithWorthImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public class MutableChiselingRecipeImpl implements MutableChiselingRecipe {

    private final Supplier<ResourceLocation> activePlugin;
    private final ResourceLocation identifier;

    private final List<ChiselingEntry> entries = new ArrayList<>();

    public MutableChiselingRecipeImpl(Supplier<ResourceLocation> activePlugin, ResourceLocation identifier){
        this.activePlugin = activePlugin;
        this.identifier = identifier;
    }

    @Override
    public @Nullable ResourceLocation identifier(){
        return this.identifier;
    }

    @Override
    public void clear(){
        this.entries.clear();
    }

    @Override
    public EntryBuilder newEntry(){
        return new EntryBuilderImpl();
    }

    @Override
    public Iterator<ChiselingEntry> iterator(){
        return this.entries.iterator();
    }

    @Override
    public List<ChiselingEntry> entries(){
        return Collections.unmodifiableList(this.entries);
    }

    @Override
    public boolean contains(ItemLike item){
        for(ChiselingEntry entry : this.entries){
            if(entry.contains(item))
                return true;
        }
        return false;
    }

    @Override
    public ItemWithWorth getWorth(ItemLike item){
        ItemWithWorth largestWorth = null;
        for(ChiselingEntry entry : this.entries){
            ItemWithWorth worth = ((ChiselingEntryImpl)entry).items().get(item.asItem());
            if(entry.contains(item) && (largestWorth == null || largestWorth.worth() < worth.worth()))
                largestWorth = worth;
        }
        return largestWorth;
    }

    private class EntryBuilderImpl implements EntryBuilder {

        private ItemWithWorth regularBlock, regularStair, regularSlab;
        private ItemWithWorth connectingBlock, connectingStair, connectingSlab;

        @Override
        public EntryBuilder regularItem(ChiselingBlockShape shape, ItemLike item, float worth){
            if(worth <= 0)
                throw new IllegalArgumentException("Worth must be greater than zero!");
            ItemWithWorth withWorth = ItemWithWorthImpl.of(item.asItem(), worth);
            switch(shape){
                case BLOCK -> this.regularBlock = withWorth;
                case STAIRS -> this.regularStair = withWorth;
                case SLAB -> this.regularSlab = withWorth;
            }
            return this;
        }

        @Override
        public EntryBuilder connectingItem(ChiselingBlockShape shape, ItemLike item, float worth){
            if(worth <= 0)
                throw new IllegalArgumentException("Worth must be greater than zero!");
            ItemWithWorth withWorth = ItemWithWorthImpl.of(item.asItem(), worth);
            switch(shape){
                case BLOCK -> this.connectingBlock = withWorth;
                case STAIRS -> this.connectingStair = withWorth;
                case SLAB -> this.connectingSlab = withWorth;
            }
            return null;
        }

        @Override
        public EntryBuilder regularItem(ChiselingBlockShape shape, ItemLike item){
            return this.regularItem(shape, item, 1);
        }

        @Override
        public EntryBuilder connectingItem(ChiselingBlockShape shape, ItemLike item){
            return this.connectingItem(shape, item, 1);
        }

        @Override
        public void submit(){
            MutableChiselingRecipeImpl.this.entries.add(new ChiselingEntryImpl(
                MutableChiselingRecipeImpl.this.activePlugin.get(),
                MutableChiselingRecipeImpl.this.identifier,
                this.regularBlock, this.regularStair, this.regularSlab,
                this.connectingBlock, this.connectingStair, this.connectingSlab
            ));
        }
    }
}
