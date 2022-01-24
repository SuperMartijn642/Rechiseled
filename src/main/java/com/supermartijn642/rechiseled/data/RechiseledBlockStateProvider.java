package com.supermartijn642.rechiseled.data;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created 7/8/2021 by SuperMartijn642
 */
public class RechiseledBlockStateProvider extends BlockStateProvider {

    public RechiseledBlockStateProvider(GatherDataEvent e, ExistingFileHelper existingFileHelper){
        super(e.getGenerator(), "rechiseled", existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels(){
        ForgeRegistries.BLOCKS.getValues().stream()
            .filter(block -> block.getRegistryName().getNamespace().equals("rechiseled"))
            .forEach(
                block -> this.getVariantBuilder(block).forAllStates(
                    state -> new ConfiguredModel[]{new ConfiguredModel(this.getExistingFile(new ResourceLocation("rechiseled", "block/" + block.getRegistryName().getPath())))}
                )
            );
    }
}
