package com.supermartijn642.rechiseled.data;

import com.supermartijn642.rechiseled.RechiseledBlockType;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.Locale;

/**
 * Created 7/8/2021 by SuperMartijn642
 */
public class RechiseledItemModelProvider extends ItemModelProvider {

    public RechiseledItemModelProvider(GatherDataEvent e){
        super(e.getGenerator(), "rechiseled", new ExistingFileHelper(Collections.emptyList(), true){
            @Override
            public boolean exists(ResourceLocation loc, ResourcePackType type, String pathSuffix, String pathPrefix){
                if(loc.getNamespace().equals("rechiseled") && loc.getPath().startsWith("block/")){
                    String blockType = loc.getPath().substring("block/".length());
                    if(blockType.endsWith("_connecting"))
                        blockType = blockType.substring(0, blockType.length() - "_connecting".length());
                    try{
                        RechiseledBlockType.valueOf(blockType.toUpperCase(Locale.ROOT));
                        return true;
                    }catch(Exception ignore){}
                }
                return e.getExistingFileHelper().exists(loc, type, pathSuffix, pathPrefix);
            }
        });
    }

    @Override
    protected void registerModels(){
        ForgeRegistries.BLOCKS.getValues().stream()
            .filter(block -> block.getRegistryName().getNamespace().equals("rechiseled"))
            .forEach(
                block -> this.withExistingParent(
                    "item/" + block.getRegistryName().getPath(),
                    new ResourceLocation("rechiseled", "block/" + block.getRegistryName().getPath())
                )
            );
    }
}
