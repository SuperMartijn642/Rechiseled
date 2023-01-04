package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.rechiseled.RechiseledBlockType;
import net.minecraft.block.Block;
import net.minecraftforge.common.ToolType;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created 05/01/2022 by SuperMartijn642
 */
public class RechiseledBlockTagsGenerator extends TagGenerator {

    public RechiseledBlockTagsGenerator(ResourceCache cache){
        super("rechiseled", cache);
    }

    @Override
    public void generate(){
        List<BiConsumer<Block,Block>> tags = Stream.<Pair<Supplier<TagBuilder<Block>>,Predicate<Block>>>of(
                Pair.of(this::blockMineableWithAxe, block -> block.getHarvestTool(block.defaultBlockState()) == ToolType.AXE),
                Pair.of(this::blockMineableWithHoe, block -> block.getHarvestTool(block.defaultBlockState()) == ToolType.HOE),
                Pair.of(this::blockMineableWithPickaxe, block -> block.getHarvestTool(block.defaultBlockState()) == ToolType.PICKAXE),
                Pair.of(this::blockMineableWithShovel, block -> block.getHarvestTool(block.defaultBlockState()) == ToolType.SHOVEL),
                Pair.of(this::blockNeedsStoneTool, block -> block.getHarvestLevel(block.defaultBlockState()) == 1),
                Pair.of(this::blockNeedsIronTool, block -> block.getHarvestLevel(block.defaultBlockState()) == 2),
                Pair.of(this::blockNeedsDiamondTool, block -> block.getHarvestLevel(block.defaultBlockState()) == 3)
            )
            .map(pair -> (BiConsumer<Block,Block>)(parent, block) -> {
                if(pair.right().test(parent))
                    pair.left().get().add(block);
            }).collect(Collectors.toList());

        for(RechiseledBlockType type : RechiseledBlockType.values()){
            if(type.hasCreatedRegularBlock())
                tags.forEach(consumer -> consumer.accept(type.parentBlock.get(), type.getRegularBlock()));
            if(type.hasCreatedConnectingBlock())
                tags.forEach(consumer -> consumer.accept(type.parentBlock.get(), type.getConnectingBlock()));
        }
    }
}
