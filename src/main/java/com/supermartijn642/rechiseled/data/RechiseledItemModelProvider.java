package com.supermartijn642.rechiseled.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created 7/8/2021 by SuperMartijn642
 */
public class RechiseledItemModelProvider extends ItemModelProvider {

    public RechiseledItemModelProvider(GatherDataEvent e){
        super(e.getGenerator(), "rechiseled", e.getExistingFileHelper());
    }

    @Override
    protected void registerModels(){
        ForgeRegistries.BLOCKS.getEntries().stream()
            .filter(entry -> entry.getKey().location().getNamespace().equals("rechiseled"))
            .forEach(
                entry -> this.withExistingParent(
                    "item/" + entry.getKey().location().getPath(),
                    new ResourceLocation("rechiseled", "block/" + entry.getKey().location().getPath())
                )
            );
    }
}
