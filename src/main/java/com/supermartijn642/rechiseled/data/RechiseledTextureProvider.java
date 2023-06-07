package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.rechiseled.api.ChiseledTextureProvider;
import net.minecraft.util.ResourceLocation;

/**
 * Created 24/01/2022 by SuperMartijn642
 */
public class RechiseledTextureProvider extends ChiseledTextureProvider {

    public RechiseledTextureProvider(ResourceCache cache){
        super("rechiseled", cache);
    }

    @Override
    protected void createTextures(){
        // Use the newer textures from 1.13+
        this.createPlankTextures(new ResourceLocation("rechiseled", "vanilla/acacia_planks"), "block/acacia_planks");
        this.createPlankTextures(new ResourceLocation("rechiseled", "vanilla/birch_planks"), "block/birch_planks");
        this.createPlankTextures(new ResourceLocation("rechiseled", "vanilla/dark_oak_planks"), "block/dark_oak_planks");
        this.createPlankTextures(new ResourceLocation("rechiseled", "vanilla/jungle_planks"), "block/jungle_planks");
        this.createPlankTextures(new ResourceLocation("rechiseled", "vanilla/spruce_planks"), "block/spruce_planks");
    }
}
