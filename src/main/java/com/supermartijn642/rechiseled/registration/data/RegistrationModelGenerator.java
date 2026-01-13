package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.ModelGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.blocks.BlockModelType;
import com.supermartijn642.rechiseled.api.blocks.BlockSpecification;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

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
            builder -> {
                // Blocks
                if(builder.hasRegularVariant()){
                    BlockModelType modelType = builder.getModelType() == null ? builder.getSpecification().getDefaultModelType() : builder.getModelType();
                    String texture = builder.getIdentifier();
                    this.addBlockModel(modelType, builder.getRegularBlock(), texture);
                    if(builder.hasStairs() && builder.getStairs().hasRegularVariant())
                        this.addStairsModels(modelType, builder.getStairs().getRegularBlock(), texture, builder.getSpecification());
                    if(builder.hasSlabs() && builder.getSlabs().hasRegularVariant())
                        this.addSlabModels(modelType, builder.getSlabs().getRegularBlock(), texture);
                }
                // Items
                if(builder.hasRegularVariant()){
                    this.addBlockItemModel(builder.getRegularBlock());
                    if(builder.hasStairs())
                        this.addStairsItemModel(builder.getStairs().getRegularBlock(), false);
                    if(builder.hasSlabs())
                        this.addSlabItemModel(builder.getSlabs().getRegularBlock());
                }
                if(builder.hasConnectingVariant()){
                    this.addBlockItemModel(builder.getConnectingBlock());
                    if(builder.hasStairs())
                        this.addStairsItemModel(builder.getStairs().getConnectingBlock(), true);
                    if(builder.hasSlabs())
                        this.addSlabItemModel(builder.getSlabs().getConnectingBlock());
                }
            }
        );
    }

    private void addBlockModel(BlockModelType modelType, Block block, String texturePath){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(block);
        ResourceLocation texture = identifier.withPath("block/" + texturePath);

        // Get textures
        ResourceLocation up = texture;
        ResourceLocation down = texture;
        ResourceLocation north = texture;
        ResourceLocation east = texture;
        ResourceLocation south = texture;
        ResourceLocation west = texture;
        ResourceLocation particle = texture;
        if(modelType == BlockModelType.CUBE){
            up = texture.withSuffix("_up");
            down = texture.withSuffix("_down");
            north = texture.withSuffix("_north");
            east = texture.withSuffix("_east");
            south = texture.withSuffix("_south");
            west = texture.withSuffix("_west");
            particle = up;
        }else if(modelType == BlockModelType.PILLAR){
            up = down = texture.withSuffix("_end");
            north = east = south = west = texture.withSuffix("_side");
            particle = north;
        }

        // Create models
        ResourceLocation modelIdentifier = identifier.withPrefix("block/");
        this.model(modelIdentifier)
            .parent("minecraft", "block/cube")
            .texture("up", up)
            .texture("down", down)
            .texture("north", north)
            .texture("east", east)
            .texture("south", south)
            .texture("west", west)
            .texture("particle", particle);
    }

    private void addStairsModels(BlockModelType modelType, Block stairs, String texturePath, BlockSpecification specification){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(stairs);
        ResourceLocation texture = identifier.withPath("block/" + texturePath);

        // Get textures
        ResourceLocation bottom = texture;
        ResourceLocation side = texture;
        ResourceLocation top = texture;
        if(modelType == BlockModelType.CUBE)
            throw new UnsupportedOperationException();
        if(modelType == BlockModelType.PILLAR){
            bottom = texture.withSuffix("_end");
            side = texture.withSuffix("_side");
            top = texture.withSuffix("_end");
        }

        // Create models
        ResourceLocation modelIdentifier = identifier.withPrefix("block/");
        ResourceLocation parent = specification == BlockSpecification.GLASS || specification == BlockSpecification.GLASS_PILLAR ?
            Rechiseled.identifier("block/glass_stairs") : new ResourceLocation("block/stairs");
        this.model(modelIdentifier)
            .parent(parent)
            .texture("bottom", bottom)
            .texture("side", side)
            .texture("top", top);
        parent = specification == BlockSpecification.GLASS || specification == BlockSpecification.GLASS_PILLAR ?
            Rechiseled.identifier("block/glass_inner_stairs") : new ResourceLocation("block/inner_stairs");
        this.model(modelIdentifier.withSuffix("_inner"))
            .parent(parent)
            .texture("bottom", bottom)
            .texture("side", side)
            .texture("top", top);
        parent = specification == BlockSpecification.GLASS || specification == BlockSpecification.GLASS_PILLAR ?
            Rechiseled.identifier("block/glass_outer_stairs") : new ResourceLocation("block/outer_stairs");
        this.model(modelIdentifier.withSuffix("_outer"))
            .parent(parent)
            .texture("bottom", bottom)
            .texture("side", side)
            .texture("top", top);
    }

    private void addSlabModels(BlockModelType modelType, Block slab, String texturePath){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(slab);
        ResourceLocation texture = identifier.withPath("block/" + texturePath);

        // Get textures
        ResourceLocation bottom = texture;
        ResourceLocation side = texture;
        ResourceLocation top = texture;
        if(modelType == BlockModelType.CUBE)
            throw new UnsupportedOperationException();
        if(modelType == BlockModelType.PILLAR){
            bottom = texture.withSuffix("_end");
            side = texture.withSuffix("_side");
            top = texture.withSuffix("_end");
        }

        // Create models
        ResourceLocation modelIdentifier = identifier.withPrefix("block/");
        this.model(modelIdentifier.withSuffix("_double"))
            .parent("minecraft", "block/cube")
            .texture("up", top)
            .texture("down", bottom)
            .texture("north", side)
            .texture("east", side)
            .texture("south", side)
            .texture("west", side)
            .texture("particle", side);
        this.model(modelIdentifier.withSuffix("_bottom"))
            .parent("minecraft", "block/slab")
            .texture("bottom", bottom)
            .texture("side", side)
            .texture("top", top);
        this.model(modelIdentifier.withSuffix("_top"))
            .parent("minecraft", "block/slab_top")
            .texture("bottom", bottom)
            .texture("side", side)
            .texture("top", top);
    }

    private void addBlockItemModel(Block block){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(block);
        this.model(identifier.withPrefix("item/")).parent(identifier.withPrefix("block/"));
    }

    private void addStairsItemModel(Block stairs, boolean connecting){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(stairs);
        if(connecting)
            this.model(identifier.withPrefix("item/")).parent(identifier.withPrefix("block/").withSuffix("_bottom"));
        else
            this.model(identifier.withPrefix("item/")).parent(identifier.withPrefix("block/"));
    }

    private void addSlabItemModel(Block slab){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(slab);
        this.model(identifier.withPrefix("item/")).parent(identifier.withPrefix("block/").withSuffix("_bottom"));
    }

    @Override
    public String getName(){
        return "Registration Model Generator: " + this.modName;
    }
}
