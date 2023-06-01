package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockBuilderImpl;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockTypeImpl;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.block.Block;
import net.minecraftforge.common.ToolType;

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
                Pair.of(this::blockMineableWithAxe, b -> b.getHarvestTool(b.defaultBlockState()) == ToolType.AXE),
                Pair.of(this::blockMineableWithHoe, b -> b.getHarvestTool(b.defaultBlockState()) == ToolType.HOE),
                Pair.of(this::blockMineableWithPickaxe, b -> b.getHarvestTool(b.defaultBlockState()) == ToolType.PICKAXE),
                Pair.of(this::blockMineableWithShovel, b -> b.getHarvestTool(b.defaultBlockState()) == ToolType.SHOVEL),
                Pair.of(this::blockNeedsStoneTool, b -> b.getHarvestLevel(b.defaultBlockState()) == 1),
                Pair.of(this::blockNeedsIronTool, b -> b.getHarvestLevel(b.defaultBlockState()) == 2),
                Pair.of(this::blockNeedsDiamondTool, b -> b.getHarvestLevel(b.defaultBlockState()) == 3)
            )
            .filter(pair -> pair.right().test(block.get()))
            .map(Pair::left)
            .map(Supplier::get)
            .<Consumer<Block>>map(builder -> builder::add)
            .collect(Collectors.toList());
    }
}
