package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockBuilderImpl;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockTypeImpl;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.block.Block;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created 04/05/2023 by SuperMartijn642
 */
public class RegistrationTagsGenerator extends TagGenerator {

    private final RechiseledRegistrationImpl registration;

    public RegistrationTagsGenerator(RechiseledRegistrationImpl registration, ResourceCache cache){
        super(registration.getModid(), cache);
        this.registration = registration;
    }

    @Override
    public void generate(){
        if(!this.registration.providersRegistered)
            return;
        this.registration.getBlockBuilders().forEach(
            pair -> {
                RechiseledBlockBuilderImpl builder = pair.left();
                RechiseledBlockTypeImpl type = pair.right();
                if(type.hasRegularVariant())
                    this.addTags(builder, type.getRegularBlock());
                if(type.hasConnectingVariant())
                    this.addTags(builder, type.getConnectingBlock());
            }
        );
    }

    private void addTags(RechiseledBlockBuilderImpl builder, Block block){
        builder.tags.stream().map(this::blockTag).forEach(tag -> tag.add(block));
        if(builder.miningTagsFromBlock != null)
            this.getTagsForBlock(builder.miningTagsFromBlock).forEach(tag -> tag.accept(block));
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
