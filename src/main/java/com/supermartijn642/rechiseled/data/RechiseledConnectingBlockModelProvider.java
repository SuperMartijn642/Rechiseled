package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.core.util.Triple;
import com.supermartijn642.rechiseled.api.ConnectingBlockModelProvider;
import com.supermartijn642.rechiseled.api.ConnectingModelBuilder;
import com.supermartijn642.rechiseled.block.BlockSpecification;
import com.supermartijn642.rechiseled.texture.TextureType;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledConnectingBlockModelProvider extends ConnectingBlockModelProvider {

    public static final Set<ResourceLocation> TEXTURES = new HashSet<>();
    private static final List<Triple<Supplier<Block>,BlockSpecification,Pair<TextureType,ResourceLocation>>> BLOCKS = new ArrayList<>();

    public static void addBlock(Supplier<Block> blockSupplier, BlockSpecification specification, TextureType textureType, ResourceLocation texture){
        BLOCKS.add(Triple.of(blockSupplier, specification, Pair.of(textureType, texture)));
    }

    public RechiseledConnectingBlockModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper){
        super("rechiseled", generator, existingFileHelper);
    }

    @Override
    protected void createModels(){
        for(Triple<Supplier<Block>,BlockSpecification,Pair<TextureType,ResourceLocation>> entry : BLOCKS){
            Block block = entry.left().get();
            String namespace = Registries.BLOCKS.getIdentifier(block).getNamespace();
            String identifier = Registries.BLOCKS.getIdentifier(block).getPath();
            BlockSpecification specification = entry.middle();
            TextureType textureType = entry.right().left();
            ResourceLocation texture = entry.right().right();
            String textureNamespace = texture.getNamespace();
            String texturePath = texture.getPath();

            if(specification == BlockSpecification.BASIC)
                this.cubeAll(namespace, "block/" + identifier, texture, textureType).connectToOtherBlocks(textureType == TextureType.CONNECTING);
            if(specification == BlockSpecification.PILLAR){ // TODO Figure out a way to match orientation
                ConnectingModelBuilder builder = this.getBuilder(namespace + ":" + identifier);
                builder.parent(this.getExistingFile(new ResourceLocation("minecraft", "block/cube")));
                builder.connectToOtherBlocks(textureType == TextureType.CONNECTING);
                texture(builder, "up", textureNamespace, texturePath + "_top", textureType);
                texture(builder, "down", textureNamespace, texturePath + "_top", textureType);
                texture(builder, "north", texture, textureType);
                texture(builder, "east", texture, textureType);
                texture(builder, "south", texture, textureType);
                texture(builder, "west", texture, textureType);
            }
            this.withExistingParent(namespace + ":item/" + identifier, namespace + ":block/" + identifier);
        }
    }

    public ConnectingModelBuilder cubeAll(String namespace, String location, ResourceLocation texture, TextureType textureType){
        ConnectingModelBuilder builder = this.getBuilder(namespace + ":" + location);
        builder.parent(this.getExistingFile(new ResourceLocation("minecraft", "block/cube_all")));
        texture(builder, "all", texture, textureType);
        return builder;
    }

    private static void texture(ConnectingModelBuilder builder, String key, ResourceLocation texture, TextureType textureType){
        TEXTURES.add(texture);
        if(textureType == TextureType.REGULAR)
            builder.texture(key, texture);
        else
            builder.texture(key, texture, true);
    }

    private static void texture(ConnectingModelBuilder builder, String key, String namespace, String location, TextureType textureType){
        texture(builder, key, new ResourceLocation(namespace, location), textureType);
    }
}
