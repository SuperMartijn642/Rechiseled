package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import com.supermartijn642.core.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created 05/01/2022 by SuperMartijn642
 */
public class RechiseledBlockTagsGenerator extends TagGenerator {

    private static final List<Pair<Supplier<Block>,Set<ResourceLocation>>> TAGS = new ArrayList<>();
    private static final List<Pair<Supplier<Block>,Supplier<Block>>> TAGS_FROM_BLOCKS = new ArrayList<>();

    public static void addBlockTags(Supplier<Block> blockSupplier, Set<ResourceLocation> tags){
        TAGS.add(Pair.of(blockSupplier, tags));
    }

    public static void addBlockTagsFromOtherBlock(Supplier<Block> blockSupplier, Supplier<Block> otherBlock){
        TAGS_FROM_BLOCKS.add(Pair.of(blockSupplier, otherBlock));
    }

    public RechiseledBlockTagsGenerator(ResourceCache cache){
        super("rechiseled", cache);
    }

    @Override
    public void generate(){
        TAGS_FROM_BLOCKS.stream().map(pair -> pair.mapRight(this::getTagsForBlock))
            .flatMap(pair -> pair.right().stream().map(c -> Pair.of(pair.left(), c)))
            .forEach(pair -> pair.right().accept(pair.left().get()));
        TAGS.stream()
            .flatMap(pair -> pair.right().stream().map(tag -> Pair.of(pair.left(), tag)))
            .map(pair -> pair.mapLeft(Supplier::get))
            .forEach(pair -> this.blockTag(pair.right()).add(pair.left()));
    }

    private List<Consumer<Block>> getTagsForBlock(Supplier<Block> block){
        return Stream.<Pair<Supplier<TagBuilder<Block>>,Predicate<Block>>>of(
                Pair.of(this::blockMineableWithAxe, b -> "axe".equals(b.getHarvestTool(b.getDefaultState()))),
                Pair.of(this::blockMineableWithHoe, b -> "hoe".equals(b.getHarvestTool(b.getDefaultState()))),
                Pair.of(this::blockMineableWithPickaxe, b -> "pickaxe".equals(b.getHarvestTool(b.getDefaultState()))),
                Pair.of(this::blockMineableWithShovel, b -> "shovel".equals(b.getHarvestTool(b.getDefaultState()))),
                Pair.of(this::blockNeedsStoneTool, b -> b.getHarvestLevel(b.getDefaultState()) == 1),
                Pair.of(this::blockNeedsIronTool, b -> b.getHarvestLevel(b.getDefaultState()) == 2),
                Pair.of(this::blockNeedsDiamondTool, b -> b.getHarvestLevel(b.getDefaultState()) == 3)
            )
            .filter(pair -> pair.right().test(block.get()))
            .map(Pair::left)
            .map(Supplier::get)
            .<Consumer<Block>>map(builder -> builder::add)
            .collect(Collectors.toList());
    }
}
