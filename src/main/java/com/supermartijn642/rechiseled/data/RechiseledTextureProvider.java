package com.supermartijn642.rechiseled.data;

import com.supermartijn642.rechiseled.api.ChiseledTextureProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

/**
 * Created 24/01/2022 by SuperMartijn642
 */
public class RechiseledTextureProvider extends ChiseledTextureProvider {

    public RechiseledTextureProvider(GatherDataEvent e, ExistingFileHelper existingFileHelper){
        super("rechiseled", e.getGenerator(), existingFileHelper);
    }

    @Override
    protected void createTextures(){
        this.createPlankTextures(new ResourceLocation("minecraft", "block/acacia_planks"), "block/acacia_planks");
        this.createPlankTextures(new ResourceLocation("minecraft", "block/birch_planks"), "block/birch_planks");
        this.createPlankTextures(new ResourceLocation("minecraft", "block/dark_oak_planks"), "block/dark_oak_planks");
        this.createPlankTextures(new ResourceLocation("minecraft", "block/jungle_planks"), "block/jungle_planks");
        this.createPlankTextures(new ResourceLocation("minecraft", "block/spruce_planks"), "block/spruce_planks");
    }
}
