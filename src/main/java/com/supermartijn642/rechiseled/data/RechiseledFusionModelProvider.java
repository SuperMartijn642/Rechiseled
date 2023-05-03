package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.core.util.Triple;
import com.supermartijn642.fusion.api.model.DefaultModelTypes;
import com.supermartijn642.fusion.api.model.ModelInstance;
import com.supermartijn642.fusion.api.model.data.ConnectingModelDataBuilder;
import com.supermartijn642.fusion.api.model.data.VanillaModelDataBuilder;
import com.supermartijn642.fusion.api.predicate.DefaultConnectionPredicates;
import com.supermartijn642.fusion.api.provider.FusionModelProvider;
import com.supermartijn642.rechiseled.block.BlockSpecification;
import com.supermartijn642.rechiseled.texture.TextureType;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created 03/05/2023 by SuperMartijn642
 */
public class RechiseledFusionModelProvider extends FusionModelProvider {

    private static final List<Triple<Supplier<Block>,BlockSpecification,Pair<TextureType,ResourceLocation>>> BLOCKS = new ArrayList<>();

    public static void addBlock(Supplier<Block> blockSupplier, BlockSpecification specification, TextureType textureType, ResourceLocation texture){
        BLOCKS.add(Triple.of(blockSupplier, specification, Pair.of(textureType, texture)));
    }

    public RechiseledFusionModelProvider(DataGenerator generator){
        super("rechiseled", generator);
    }

    @Override
    protected void generate(){
        for(Triple<Supplier<Block>,BlockSpecification,Pair<TextureType,ResourceLocation>> entry : BLOCKS){
            Block block = entry.left().get();
            String namespace = Registries.BLOCKS.getIdentifier(block).getNamespace();
            String identifier = Registries.BLOCKS.getIdentifier(block).getPath();
            BlockSpecification specification = entry.middle();
            TextureType textureType = entry.right().left();
            ResourceLocation texture = entry.right().right();
            String textureNamespace = texture.getNamespace();
            String texturePath = texture.getPath();

            if(specification == BlockSpecification.BASIC){
                ConnectingModelDataBuilder builder = ConnectingModelDataBuilder.builder();
                builder.parent(new ResourceLocation("minecraft", "block/cube_all"));
                builder.texture("all", texture);
                if(textureType == TextureType.CONNECTING)
                    builder.connection(DefaultConnectionPredicates.isSameBlock());
                ModelInstance<?> model = textureType == TextureType.CONNECTING ?
                    ModelInstance.of(DefaultModelTypes.CONNECTING, builder.build()) :
                    ModelInstance.of(DefaultModelTypes.VANILLA, builder.build().left());
                this.addModel(new ResourceLocation(namespace, "block/" + identifier), model);
            }
            if(specification == BlockSpecification.PILLAR){
                ConnectingModelDataBuilder builder = ConnectingModelDataBuilder.builder();
                builder.parent(new ResourceLocation("minecraft", "block/cube"));
                builder.texture("up", new ResourceLocation(textureNamespace, texturePath + "_top"));
                builder.texture("down", new ResourceLocation(textureNamespace, texturePath + "_top"));
                builder.texture("north", texture);
                builder.texture("east", texture);
                builder.texture("south", texture);
                builder.texture("west", texture);
                if(textureType == TextureType.CONNECTING)
                    builder.connection(DefaultConnectionPredicates.isSameBlock());
                this.addModel(new ResourceLocation(namespace, "block/" + identifier), ModelInstance.of(DefaultModelTypes.CONNECTING, builder.build()));
            }
            this.addModel(new ResourceLocation(namespace, "item/" + identifier), ModelInstance.of(DefaultModelTypes.VANILLA, VanillaModelDataBuilder.builder().parent(new ResourceLocation(namespace, "block/" + identifier)).build()));
        }
    }
}
