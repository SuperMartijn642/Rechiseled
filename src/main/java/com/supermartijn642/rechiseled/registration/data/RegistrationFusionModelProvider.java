package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.fusion.api.model.DefaultModelTypes;
import com.supermartijn642.fusion.api.model.ModelInstance;
import com.supermartijn642.fusion.api.model.data.ConnectingModelDataBuilder;
import com.supermartijn642.fusion.api.predicate.DefaultConnectionPredicates;
import com.supermartijn642.fusion.api.provider.FusionModelProvider;
import com.supermartijn642.rechiseled.api.blocks.BlockModelType;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockBuilderImpl;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockTypeImpl;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * Created 05/05/2023 by SuperMartijn642
 */
public class RegistrationFusionModelProvider extends FusionModelProvider {

    private final RechiseledRegistrationImpl registration;

    public RegistrationFusionModelProvider(RechiseledRegistrationImpl registration, FabricDataOutput output){
        super(registration.getModid(), output);
        this.registration = registration;
    }

    @Override
    protected void generate(){
        if(!this.registration.providersRegistered)
            return;
        this.registration.getBlockBuilders().forEach(
            pair -> {
                RechiseledBlockBuilderImpl builder = pair.left();
                RechiseledBlockTypeImpl type = pair.right();
                BlockModelType modelType = builder.modelType == null ? type.getSpecification().getDefaultModelType() : builder.modelType;
                if(type.hasConnectingVariant())
                    this.addModel(modelType, type.getConnectingBlock(), type.getIdentifier().getPath());
            }
        );
    }

    private void addModel(BlockModelType modelType, Block block, String texture){
        String namespace = Registries.BLOCKS.getIdentifier(block).getNamespace();
        String identifier = Registries.BLOCKS.getIdentifier(block).getPath();
        if(modelType == BlockModelType.CUBE){
            ConnectingModelDataBuilder builder = ConnectingModelDataBuilder.builder()
                .parent(new ResourceLocation("minecraft", "block/cube"))
                .texture("up", new ResourceLocation(namespace, "block/" + texture + "_up"))
                .texture("down", new ResourceLocation(namespace, "block/" + texture + "_down"))
                .texture("north", new ResourceLocation(namespace, "block/" + texture + "_north"))
                .texture("east", new ResourceLocation(namespace, "block/" + texture + "_east"))
                .texture("south", new ResourceLocation(namespace, "block/" + texture + "_south"))
                .texture("west", new ResourceLocation(namespace, "block/" + texture + "_west"))
                .texture("particle", new ResourceLocation(namespace, "block/" + texture + "_up"));
            ModelInstance<?> model = ModelInstance.of(DefaultModelTypes.CONNECTING, builder.build());
            this.addModel(new ResourceLocation(namespace, "block/" + identifier), model);
        }else if(modelType == BlockModelType.CUBE_ALL){
            ConnectingModelDataBuilder builder = ConnectingModelDataBuilder.builder()
                .parent(new ResourceLocation("minecraft", "block/cube_all"))
                .texture("all", new ResourceLocation(namespace, "block/" + texture))
                .connection(DefaultConnectionPredicates.isSameBlock());
            ModelInstance<?> model = ModelInstance.of(DefaultModelTypes.CONNECTING, builder.build());
            this.addModel(new ResourceLocation(namespace, "block/" + identifier), model);
        }else if(modelType == BlockModelType.PILLAR){
            ConnectingModelDataBuilder builder = ConnectingModelDataBuilder.builder()
                .parent(new ResourceLocation("minecraft", "block/cube"))
                .texture("up", new ResourceLocation(namespace, "block/" + texture + "_end"))
                .texture("down", new ResourceLocation(namespace, "block/" + texture + "_end"))
                .texture("north", new ResourceLocation(namespace, "block/" + texture + "_side"))
                .texture("east", new ResourceLocation(namespace, "block/" + texture + "_side"))
                .texture("south", new ResourceLocation(namespace, "block/" + texture + "_side"))
                .texture("west", new ResourceLocation(namespace, "block/" + texture + "_side"))
                .texture("particle", new ResourceLocation(namespace, "block/" + texture + "_side"))
                .connection(DefaultConnectionPredicates.isSameState());
            ModelInstance<?> model = ModelInstance.of(DefaultModelTypes.CONNECTING, builder.build());
            this.addModel(new ResourceLocation(namespace, "block/" + identifier), model);
        }
    }

    @Override
    public String getName(){
        return "Registration Fusion Model Generator: " + this.registration.getModid();
    }
}
