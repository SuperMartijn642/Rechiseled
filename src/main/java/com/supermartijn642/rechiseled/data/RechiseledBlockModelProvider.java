package com.supermartijn642.rechiseled.data;

import com.supermartijn642.rechiseled.RechiseledBlockType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import static com.supermartijn642.rechiseled.RechiseledBlockType.BlockOption.*;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledBlockModelProvider extends BlockModelProvider {

    public RechiseledBlockModelProvider(GatherDataEvent e){
        super(e.getGenerator(), "rechiseled", e.getExistingFileHelper());
    }

    @Override
    protected void registerModels(){
        for(RechiseledBlockType type : RechiseledBlockType.values()){
            if(type.regularBlockMode == NORMAL)
                this.cubeAll("block/" + type.regularRegistryName, new ResourceLocation("rechiseled", "block/" + type.regularRegistryName));
            else if(type.regularBlockMode == NON_CONNECTING)
                this.regularCubeAll("block/" + type.regularRegistryName, new ResourceLocation("rechiseled", "block/" + type.regularRegistryName));
            else if(type.regularBlockMode == CONNECTING)
                this.connectingCubeAll("block/" + type.regularRegistryName, new ResourceLocation("rechiseled", "block/" + type.regularRegistryName));

            if(type.connectingBlockMode == NORMAL)
                this.cubeAll("block/" + type.connectingRegistryName, new ResourceLocation("rechiseled", "block/" + type.regularRegistryName));
            else if(type.connectingBlockMode == NON_CONNECTING)
                this.regularCubeAll("block/" + type.connectingRegistryName, new ResourceLocation("rechiseled", "block/" + type.regularRegistryName));
            else if(type.connectingBlockMode == CONNECTING)
                this.connectingCubeAll("block/" + type.connectingRegistryName, new ResourceLocation("rechiseled", "block/" + type.regularRegistryName));
        }
    }

    public RechiseledBlockModelBuilder<BlockModelBuilder> regularCubeAll(String name, ResourceLocation texture){
        return this.singleTexture(name, new ResourceLocation("rechiseled", "block/connecting_cube_all"), "all", texture)
            .customLoader(RechiseledBlockModelBuilder::new);
    }

    public RechiseledBlockModelBuilder<BlockModelBuilder> connectingCubeAll(String name, ResourceLocation texture){
        return this.singleTexture(name, new ResourceLocation("rechiseled", "block/connecting_cube_all"), "all", texture)
            .customLoader(RechiseledBlockModelBuilder::new)
            .connectToOtherBlocks();
    }
}
