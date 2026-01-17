package com.supermartijn642.rechiseled.chiseling.plugin;

import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.plugin.MutableChiselingRecipe;
import com.supermartijn642.rechiseled.chiseling.ChiselingEntryImpl;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
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
    public boolean contains(IItemProvider item){
        for(ChiselingEntry entry : this.entries){
            if(entry.contains(item))
                return true;
        }
        return false;
    }

    private class EntryBuilderImpl implements EntryBuilder {

        private Item regularBlock, regularStair, regularSlab;
        private Item connectingBlock, connectingStair, connectingSlab;

        @Override
        public EntryBuilder regularItem(ChiselingBlockShape shape, IItemProvider item){
            if(shape == ChiselingBlockShape.BLOCK)
                this.regularBlock = item.asItem();
            else if(shape == ChiselingBlockShape.STAIRS)
                this.regularStair = item.asItem();
            else if(shape == ChiselingBlockShape.SLAB)
                this.regularSlab = item.asItem();
            return this;
        }

        @Override
        public EntryBuilder connectingItem(ChiselingBlockShape shape, IItemProvider item){
            if(shape == ChiselingBlockShape.BLOCK)
                this.connectingBlock = item.asItem();
            else if(shape == ChiselingBlockShape.STAIRS)
                this.connectingStair = item.asItem();
            else if(shape == ChiselingBlockShape.SLAB)
                this.connectingSlab = item.asItem();
            return this;
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
