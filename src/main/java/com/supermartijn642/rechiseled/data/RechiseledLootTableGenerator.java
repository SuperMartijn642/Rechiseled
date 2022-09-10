package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.LootTableGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.core.util.Pair;

/**
 * Created 7/8/2021 by SuperMartijn642
 */
public class RechiseledLootTableGenerator extends LootTableGenerator {

    public RechiseledLootTableGenerator(ResourceCache cache){
        super("rechiseled", cache);
    }

    @Override
    public void generate(){
        Registries.BLOCKS.getEntries().stream()
            .filter(pair -> pair.left().getNamespace().equals("rechiseled"))
            .map(Pair::right)
            .forEach(this::dropSelf);
    }
}
