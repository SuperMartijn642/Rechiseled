package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.BlockStateGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.api.blocks.BlockSpecification;
import com.supermartijn642.rechiseled.blocks.RechiseledPillarBlock;
import com.supermartijn642.rechiseled.blocks.RechiseledSlabBlock;
import com.supermartijn642.rechiseled.blocks.RechiseledStairBlock;
import com.supermartijn642.rechiseled.blocks.impl.SlabType;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import static net.minecraft.block.BlockStairs.EnumHalf;
import static net.minecraft.block.BlockStairs.EnumShape;

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
        ResourceLocation model = new ResourceLocation(identifier.getResourceDomain(), "block/" + identifier.getResourcePath());
        if(specification == BlockSpecification.BASIC || specification == BlockSpecification.GLASS)
            this.blockState(block).emptyVariant(variant -> variant.model(model));
        else if(specification == BlockSpecification.PILLAR || specification == BlockSpecification.GLASS_PILLAR){
            this.blockState(block).variantsForProperty(RechiseledPillarBlock.AXIS_PROPERTY, (state, variant) -> {
                EnumFacing.Axis axis = state.get(RechiseledPillarBlock.AXIS_PROPERTY);
                if(axis == EnumFacing.Axis.X)
                    variant.model(model, 90, 90);
                else if(axis == EnumFacing.Axis.Z)
                    variant.model(model, 90, 0);
                else
                    variant.model(model);
            });
        }
    }

    private void createConnectingBlockState(BlockSpecification specification, Block block){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(block);
        ResourceLocation model = new ResourceLocation(identifier.getResourceDomain(), "block/" + identifier.getResourcePath());
        if(specification == BlockSpecification.BASIC || specification == BlockSpecification.GLASS)
            this.blockState(block).emptyVariant(variant -> variant.model(model));
        else if(specification == BlockSpecification.PILLAR || specification == BlockSpecification.GLASS_PILLAR){
            this.blockState(block).variantsForProperty(RechiseledPillarBlock.AXIS_PROPERTY, (state, variant) -> {
                EnumFacing.Axis axis = state.get(RechiseledPillarBlock.AXIS_PROPERTY);
                if(axis == EnumFacing.Axis.X)
                    variant.model(new ResourceLocation(model.getResourceDomain(), model.getResourcePath() + "_horizontal"), 90, 90);
                else if(axis == EnumFacing.Axis.Z)
                    variant.model(new ResourceLocation(model.getResourceDomain(), model.getResourcePath() + "_horizontal"), 90, 0);
                else
                    variant.model(model);
            });
        }
    }

    private void createStairsState(Block block, boolean connecting){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(block);
        ResourceLocation model = new ResourceLocation(identifier.getResourceDomain(), "block/" + identifier.getResourcePath());
        this.blockState(block).variantsForAllExcept((state, variant) -> {
            EnumFacing facing = state.get(RechiseledStairBlock.FACING);
            EnumHalf half = state.get(RechiseledStairBlock.HALF);
            EnumShape shape = state.get(RechiseledStairBlock.SHAPE);
            // Get rotation
            int yRotation = getYRotation(facing) + 90;
            if(half == EnumHalf.BOTTOM && (shape == EnumShape.INNER_LEFT || shape == EnumShape.OUTER_LEFT))
                yRotation -= 90;
            else if(half == EnumHalf.TOP && (shape == EnumShape.INNER_RIGHT || shape == EnumShape.OUTER_RIGHT))
                yRotation += 90;
            int xRotation = !connecting && half == EnumHalf.TOP ? 180 : 0;
            // Get model suffix
            String suffix = connecting ? half == EnumHalf.BOTTOM ? "_bottom" : "_top" : "";
            if(shape == EnumShape.INNER_LEFT || shape == EnumShape.INNER_RIGHT)
                suffix = "_inner" + suffix;
            else if(shape == EnumShape.OUTER_LEFT || shape == EnumShape.OUTER_RIGHT)
                suffix = "_outer" + suffix;
            // Set variant model
            variant.model(new ResourceLocation(model.getResourceDomain(), model.getResourcePath() + suffix), xRotation, (yRotation + 360) % 360, true);
        });
    }

    private void createSlabState(Block block){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(block);
        ResourceLocation model = new ResourceLocation(identifier.getResourceDomain(), "block/" + identifier.getResourcePath());
        this.blockState(block)
            .variantsForProperty(RechiseledSlabBlock.TYPE, (state, variant) -> {
                if(state.get(RechiseledSlabBlock.TYPE) == SlabType.DOUBLE)
                    variant.model(new ResourceLocation(model.getResourceDomain(), model.getResourcePath() + "_double"));
                else if(state.get(RechiseledSlabBlock.TYPE) == SlabType.BOTTOM)
                    variant.model(new ResourceLocation(model.getResourceDomain(), model.getResourcePath() + "_bottom"));
                else
                    variant.model(new ResourceLocation(model.getResourceDomain(), model.getResourcePath() + "_top"));
            });
    }

    @Override
    public String getName(){
        return "Registration Block State Generator: " + this.modName;
    }

    private static int getYRotation(EnumFacing direction){
        return (direction.getHorizontalIndex() & 3) * 90;
    }
}
