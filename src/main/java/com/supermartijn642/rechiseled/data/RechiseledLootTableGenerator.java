package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.LootTableGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created 7/8/2021 by SuperMartijn642
 */
public class RechiseledLootTableGenerator extends LootTableGenerator {

    private static final List<Supplier<Block>> BLOCKS = new ArrayList<>();

    public static void addDropSelfBlock(Supplier<Block> blockSupplier){
        BLOCKS.add(blockSupplier);
    }

    public RechiseledLootTableGenerator(ResourceCache cache){
        super("rechiseled", cache);
    }

    @Override
    public void generate(){
        BLOCKS.stream().map(Supplier::get).forEach(this::dropSelf);
    }
}
