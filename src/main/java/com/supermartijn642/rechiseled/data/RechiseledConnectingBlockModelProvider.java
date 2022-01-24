package com.supermartijn642.rechiseled.data;

import com.supermartijn642.rechiseled.RechiseledBlockType;
import com.supermartijn642.rechiseled.api.ConnectingBlockModelProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import static com.supermartijn642.rechiseled.RechiseledBlockType.BlockOption.*;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledConnectingBlockModelProvider extends ConnectingBlockModelProvider {

    public RechiseledConnectingBlockModelProvider(GatherDataEvent e){
        super("rechiseled", e.getGenerator(), e.getExistingFileHelper());
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
    }
}
