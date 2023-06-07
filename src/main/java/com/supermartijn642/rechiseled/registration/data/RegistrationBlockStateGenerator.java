package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.BlockStateGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.rechiseled.api.blocks.BlockSpecification;
import com.supermartijn642.rechiseled.blocks.RechiseledPillarBlock;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;

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
        this.registration.getBlockBuilders().stream().map(Pair::right).forEach(
            type -> {
                if(type.hasRegularVariant())
                    this.createBlockState(type.getSpecification(), type.getRegularBlock());
                if(type.hasConnectingVariant())
                    this.createBlockState(type.getSpecification(), type.getConnectingBlock());
            }
        );
    }

    private void createBlockState(BlockSpecification specification, Block block){
        String namespace = Registries.BLOCKS.getIdentifier(block).getResourceDomain();
        String identifier = Registries.BLOCKS.getIdentifier(block).getResourcePath();
        if(specification == BlockSpecification.BASIC || specification == BlockSpecification.GLASS)
            this.blockState(block).emptyVariant(variant -> variant.model(namespace, "block/" + identifier));
        else if(specification == BlockSpecification.PILLAR || specification == BlockSpecification.GLASS_PILLAR){
            this.blockState(block).variantsForProperty(RechiseledPillarBlock.AXIS_PROPERTY, (state, variant) -> {
                EnumFacing.Axis axis = state.get(RechiseledPillarBlock.AXIS_PROPERTY);
                if(axis == EnumFacing.Axis.X)
                    variant.model(namespace, "block/" + identifier, 90, 90);
                else if(axis == EnumFacing.Axis.Z)
                    variant.model(namespace, "block/" + identifier, 90, 0);
                else
                    variant.model(namespace, "block/" + identifier);
            });
        }
    }
}
