package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.ModelGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.api.blocks.BlockModelType;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockBuilderImpl;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockTypeImpl;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.block.Block;

/**
 * Created 05/05/2023 by SuperMartijn642
 */
public class RegistrationModelGenerator extends ModelGenerator {

    private final RechiseledRegistrationImpl registration;

    public RegistrationModelGenerator(RechiseledRegistrationImpl registration, ResourceCache cache){
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
                BlockModelType modelType = builder.modelType == null ? type.getSpecification().getDefaultModelType() : builder.modelType;
                if(type.hasRegularVariant()){
                    this.addModel(modelType, type.getRegularBlock(), type.getIdentifier().getResourcePath());
                    this.addItemModel(modelType, type.getRegularBlock());
                }
                if(type.hasConnectingVariant())
                    this.addItemModel(modelType, type.getConnectingBlock());
            }
        );
    }

    private void addModel(BlockModelType modelType, Block block, String texture){
        String namespace = Registries.BLOCKS.getIdentifier(block).getResourceDomain();
        String identifier = Registries.BLOCKS.getIdentifier(block).getResourcePath();
        if(modelType == BlockModelType.CUBE)
            this.model(namespace, "block/" + identifier)
                .parent("minecraft", "block/cube")
                .texture("up", namespace, "block/" + texture + "_up")
                .texture("down", namespace, "block/" + texture + "_down")
                .texture("north", namespace, "block/" + texture + "_north")
                .texture("east", namespace, "block/" + texture + "_east")
                .texture("south", namespace, "block/" + texture + "_south")
                .texture("west", namespace, "block/" + texture + "_west")
                .texture("particle", namespace, "block/" + texture + "_up");
        else if(modelType == BlockModelType.CUBE_ALL)
            this.model(namespace, "block/" + identifier).parent("minecraft", "block/cube_all").texture("all", namespace, "block/" + texture);
        else if(modelType == BlockModelType.PILLAR)
            this.model(namespace, "block/" + identifier).parent("minecraft", "block/cube")
                .texture("up", namespace, "block/" + texture + "_end")
                .texture("down", namespace, "block/" + texture + "_end")
                .texture("north", namespace, "block/" + texture + "_side")
                .texture("east", namespace, "block/" + texture + "_side")
                .texture("south", namespace, "block/" + texture + "_side")
                .texture("west", namespace, "block/" + texture + "_side")
                .texture("particle", namespace, "block/" + texture + "_side");
    }

    private void addItemModel(BlockModelType modelType, Block block){
        String namespace = Registries.BLOCKS.getIdentifier(block).getResourceDomain();
        String identifier = Registries.BLOCKS.getIdentifier(block).getResourcePath();
        this.model(namespace, "item/" + identifier).parent(namespace, "block/" + identifier);
    }
}
