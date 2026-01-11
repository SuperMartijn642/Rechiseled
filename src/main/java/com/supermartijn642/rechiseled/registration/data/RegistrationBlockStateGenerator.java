package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.BlockStateGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.api.blocks.BlockSpecification;
import com.supermartijn642.rechiseled.blocks.RechiseledPillarBlock;
import com.supermartijn642.rechiseled.blocks.RechiseledSlabBlock;
import com.supermartijn642.rechiseled.blocks.RechiseledStairBlock;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;

/**
 * Created 03/05/2023 by SuperMartijn642
 */
public class RegistrationBlockStateGenerator extends BlockStateGenerator {

    private final RechiseledRegistrationImpl registration;

    public RegistrationBlockStateGenerator(RechiseledRegistrationImpl registration, ResourceCache cache){
        super(registration.getModid(), cache);
        this.registration = registration;
    }

    @Override
    public void generate(){
        if(!this.registration.providersRegistered)
            return;
        this.registration.getBlockBuilders().forEach(
            builder -> {
                if(builder.hasRegularVariant()){
                    this.createBlockState(builder.getSpecification(), builder.getRegularBlock());
                    if(builder.hasStairs() && builder.getStairs().hasRegularVariant())
                        this.createStairsState(builder.getStairs().getRegularBlock(), false);
                    if(builder.hasSlabs() && builder.getSlabs().hasRegularVariant())
                        this.createSlabState(builder.getSlabs().getRegularBlock());
                }
                if(builder.hasConnectingVariant()){
                    this.createConnectingBlockState(builder.getSpecification(), builder.getConnectingBlock());
                    if(builder.hasStairs() && builder.getStairs().hasConnectingVariant())
                        this.createStairsState(builder.getStairs().getConnectingBlock(), true);
                    if(builder.hasSlabs() && builder.getSlabs().hasConnectingVariant())
                        this.createSlabState(builder.getSlabs().getConnectingBlock());
                }
            }
        );
    }

    private void createBlockState(BlockSpecification specification, Block block){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(block);
        ResourceLocation model = identifier.withPrefix("block/");
        if(specification == BlockSpecification.BASIC || specification == BlockSpecification.GLASS)
            this.blockState(block).emptyVariant(variant -> variant.model(model));
        else if(specification == BlockSpecification.PILLAR || specification == BlockSpecification.GLASS_PILLAR){
            this.blockState(block).variantsForProperty(RechiseledPillarBlock.AXIS_PROPERTY, (state, variant) -> {
                Direction.Axis axis = state.get(RechiseledPillarBlock.AXIS_PROPERTY);
                if(axis == Direction.Axis.X)
                    variant.model(model, 90, 90);
                else if(axis == Direction.Axis.Z)
                    variant.model(model, 90, 0);
                else
                    variant.model(model);
            });
        }
    }

    private void createConnectingBlockState(BlockSpecification specification, Block block){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(block);
        ResourceLocation model = identifier.withPrefix("block/");
        if(specification == BlockSpecification.BASIC || specification == BlockSpecification.GLASS)
            this.blockState(block).emptyVariant(variant -> variant.model(model));
        else if(specification == BlockSpecification.PILLAR || specification == BlockSpecification.GLASS_PILLAR){
            this.blockState(block).variantsForProperty(RechiseledPillarBlock.AXIS_PROPERTY, (state, variant) -> {
                Direction.Axis axis = state.get(RechiseledPillarBlock.AXIS_PROPERTY);
                if(axis == Direction.Axis.X)
                    variant.model(model.withSuffix("_horizontal"), 90, 90);
                else if(axis == Direction.Axis.Z)
                    variant.model(model.withSuffix("_horizontal"), 90, 0);
                else
                    variant.model(model);
            });
        }
    }

    private void createStairsState(Block block, boolean connecting){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(block);
        ResourceLocation model = identifier.withPrefix("block/");
        this.blockState(block).variantsForAllExcept((state, variant) -> {
            Direction facing = state.get(RechiseledStairBlock.FACING);
            Half half = state.get(RechiseledStairBlock.HALF);
            StairsShape shape = state.get(RechiseledStairBlock.SHAPE);
            // Get rotation
            int yRotation = (int)facing.toYRot() + 90;
            if(half == Half.BOTTOM && (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT))
                yRotation -= 90;
            else if(half == Half.TOP && (shape == StairsShape.INNER_RIGHT || shape == StairsShape.OUTER_RIGHT))
                yRotation += 90;
            int xRotation = !connecting && half == Half.TOP ? 180 : 0;
            // Get model suffix
            String suffix = connecting ? half == Half.BOTTOM ? "_bottom" : "_top" : "";
            if(shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT)
                suffix = "_inner" + suffix;
            else if(shape == StairsShape.OUTER_LEFT || shape == StairsShape.OUTER_RIGHT)
                suffix = "_outer" + suffix;
            // Set variant model
            variant.model(model.withSuffix(suffix), xRotation, (yRotation + 360) % 360, true);
        }, BlockStateProperties.WATERLOGGED);
    }

    private void createSlabState(Block block){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(block);
        ResourceLocation model = identifier.withPrefix("block/");
        this.blockState(block)
            .variantsForProperty(RechiseledSlabBlock.TYPE, (state, variant) -> {
                if(state.get(RechiseledSlabBlock.TYPE) == SlabType.DOUBLE)
                    variant.model(model.withSuffix("_double"));
                else if(state.get(RechiseledSlabBlock.TYPE) == SlabType.BOTTOM)
                    variant.model(model.withSuffix("_bottom"));
                else
                    variant.model(model.withSuffix("_top"));
            });
    }

    @Override
    public String getName(){
        return "Registration Block State Generator: " + this.modName;
    }
}
