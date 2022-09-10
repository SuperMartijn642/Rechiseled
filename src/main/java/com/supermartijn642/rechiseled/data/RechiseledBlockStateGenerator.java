package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.BlockStateGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.registry.Registries;

/**
 * Created 7/8/2021 by SuperMartijn642
 */
public class RechiseledBlockStateGenerator extends BlockStateGenerator {

    public RechiseledBlockStateGenerator(ResourceCache cache){
        super("rechiseled", cache);
    }

    @Override
    public void generate(){
        Registries.BLOCKS.getEntries().stream()
            .filter(pair -> pair.left().getNamespace().equals("rechiseled"))
            .forEach(pair -> this.blockState(pair.right()).emptyVariant(builder -> builder.model("block/" + pair.left().getPath())));
    }
}
