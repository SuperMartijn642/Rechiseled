package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.AtlasSourceGenerator;
import com.supermartijn642.core.generator.ResourceCache;

/**
 * Created 08/04/2023 by SuperMartijn642
 */
public class RechiseledAtlasSourceGenerator extends AtlasSourceGenerator {

    public RechiseledAtlasSourceGenerator(ResourceCache cache){
        super("rechiseled", cache);
    }

    @Override
    public void generate(){
        RechiseledConnectingBlockModelProvider.TEXTURES.forEach(this.blockAtlas()::texture);
    }
}
