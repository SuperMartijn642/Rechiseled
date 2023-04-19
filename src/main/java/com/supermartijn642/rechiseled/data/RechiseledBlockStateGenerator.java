package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.BlockStateGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.rechiseled.block.BlockSpecification;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created 7/8/2021 by SuperMartijn642
 */
public class RechiseledBlockStateGenerator extends BlockStateGenerator {

    private static final Map<BlockSpecification,List<Supplier<Block>>> BLOCKS = new EnumMap<>(BlockSpecification.class);

    public static void addBlock(BlockSpecification specification, Supplier<Block> blockSupplier){
        BLOCKS.computeIfAbsent(specification, s -> new ArrayList<>()).add(blockSupplier);
    }

    public RechiseledBlockStateGenerator(ResourceCache cache){
        super("rechiseled", cache);
    }

    @Override
    public void generate(){
        BLOCKS.entrySet().stream().flatMap(entry -> entry.getValue().stream().map(block -> Pair.of(entry.getKey(), block))).forEach(
            entry -> {
                BlockSpecification specification = entry.left();
                Block block = entry.right().get();
                ResourceLocation identifier = Registries.BLOCKS.getIdentifier(block);
                if(specification == BlockSpecification.BASIC)
                    this.blockState(block).emptyVariant(variant -> variant.model(identifier.getResourceDomain(), "block/" + identifier.getResourcePath()));
            }
        );
    }
}
