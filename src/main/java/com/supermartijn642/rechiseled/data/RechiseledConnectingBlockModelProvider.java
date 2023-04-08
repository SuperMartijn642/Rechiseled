package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.RechiseledBlockType;
import com.supermartijn642.rechiseled.api.ConnectingBlockModelProvider;
import com.supermartijn642.rechiseled.api.ConnectingModelBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

import static com.supermartijn642.rechiseled.RechiseledBlockType.BlockOption.*;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledConnectingBlockModelProvider extends ConnectingBlockModelProvider {

    public static final Set<ResourceLocation> TEXTURES = new HashSet<>();

    public RechiseledConnectingBlockModelProvider(FabricDataOutput generator){
        super("rechiseled", generator);
    }

    @Override
    protected void createModels(){
        for(RechiseledBlockType type : RechiseledBlockType.values()){
            if(type.regularBlockMode == NORMAL)
                this.cubeAll("block/" + type.regularRegistryName, new ResourceLocation("rechiseled", "block/" + type.regularRegistryName));
            else if(type.regularBlockMode == NON_CONNECTING)
                this.cubeAll("block/" + type.regularRegistryName, new ResourceLocation("rechiseled", "block/" + type.regularRegistryName), true);
            else if(type.regularBlockMode == CONNECTING)
                this.cubeAll("block/" + type.regularRegistryName, new ResourceLocation("rechiseled", "block/" + type.regularRegistryName), true).connectToOtherBlocks();

            if(type.connectingBlockMode == NORMAL)
                this.cubeAll("block/" + type.connectingRegistryName, new ResourceLocation("rechiseled", "block/" + type.regularRegistryName));
            else if(type.connectingBlockMode == NON_CONNECTING)
                this.cubeAll("block/" + type.connectingRegistryName, new ResourceLocation("rechiseled", "block/" + type.regularRegistryName), true);
            else if(type.connectingBlockMode == CONNECTING)
                this.cubeAll("block/" + type.connectingRegistryName, new ResourceLocation("rechiseled", "block/" + type.regularRegistryName), true).connectToOtherBlocks();
        }

        // Item models for all the blocks
        Registries.BLOCKS.getIdentifiers().stream()
            .filter(identifier -> identifier.getNamespace().equals("rechiseled"))
            .forEach(identifier -> this.model("item/" + identifier.getPath()).parent("block/" + identifier.getPath()));
    }

    @Override
    protected ConnectingModelBuilder cubeAll(String location, ResourceLocation texture){
        TEXTURES.add(texture);
        return super.cubeAll(location, texture);
    }

    @Override
    public ConnectingModelBuilder cubeAll(String location, ResourceLocation texture, boolean connecting){
        TEXTURES.add(texture);
        return super.cubeAll(location, texture, connecting);
    }
}
