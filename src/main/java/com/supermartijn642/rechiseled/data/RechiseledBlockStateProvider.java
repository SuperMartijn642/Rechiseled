package com.supermartijn642.rechiseled.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created 7/8/2021 by SuperMartijn642
 */
public class RechiseledBlockStateProvider extends BlockStateProvider {

    public RechiseledBlockStateProvider(GatherDataEvent e){
        super(e.getGenerator(), "rechiseled", e.getExistingFileHelper());
    }

    @Override
    protected void registerStatesAndModels(){
        ForgeRegistries.BLOCKS.getEntries().stream()
            .filter(entry -> entry.getKey().location().getNamespace().equals("rechiseled"))
            .forEach(
                entry -> this.getVariantBuilder(entry.getValue()).forAllStates(
                    state -> new ConfiguredModel[]{new ConfiguredModel(this.models().getExistingFile(new ResourceLocation("rechiseled", "block/" + entry.getKey().location().getPath())))}
                )
            );
    }
}
