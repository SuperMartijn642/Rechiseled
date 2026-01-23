package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.ModelGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.blocks.BlockModelType;
import com.supermartijn642.rechiseled.api.blocks.BlockSpecification;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

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
        ResourceLocation texture = new ResourceLocation(identifier.getResourceDomain(), "block/" + texturePath);

        // Get textures
        ResourceLocation up = texture;
        ResourceLocation down = texture;
        ResourceLocation north = texture;
        ResourceLocation east = texture;
        ResourceLocation south = texture;
        ResourceLocation west = texture;
        ResourceLocation particle = texture;
        if(modelType == BlockModelType.CUBE){
            up = new ResourceLocation(texture.getResourceDomain(), texture.getResourcePath() + "_up");
            down = new ResourceLocation(texture.getResourceDomain(), texture.getResourcePath() + "_down");
            north = new ResourceLocation(texture.getResourceDomain(), texture.getResourcePath() + "_north");
            east = new ResourceLocation(texture.getResourceDomain(), texture.getResourcePath() + "_east");
            south = new ResourceLocation(texture.getResourceDomain(), texture.getResourcePath() + "_south");
            west = new ResourceLocation(texture.getResourceDomain(), texture.getResourcePath() + "_west");
            particle = up;
        }else if(modelType == BlockModelType.PILLAR){
            up = down = new ResourceLocation(texture.getResourceDomain(), texture.getResourcePath() + "_end");
            north = east = south = west = new ResourceLocation(texture.getResourceDomain(), texture.getResourcePath() + "_side");
            particle = north;
        }

        // Create models
        ResourceLocation modelIdentifier = new ResourceLocation(identifier.getResourceDomain(), "block/" + identifier.getResourcePath());
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
        ResourceLocation texture = new ResourceLocation(identifier.getResourceDomain(), "block/" + texturePath);

        // Get textures
        ResourceLocation bottom = texture;
        ResourceLocation side = texture;
        ResourceLocation top = texture;
        if(modelType == BlockModelType.CUBE)
            throw new UnsupportedOperationException();
        if(modelType == BlockModelType.PILLAR){
            bottom = new ResourceLocation(texture.getResourceDomain(), texture.getResourcePath() + "_end");
            side = new ResourceLocation(texture.getResourceDomain(), texture.getResourcePath() + "_side");
            top = new ResourceLocation(texture.getResourceDomain(), texture.getResourcePath() + "_end");
        }

        // Create models
        ResourceLocation modelIdentifier = new ResourceLocation(identifier.getResourceDomain(), "block/" + identifier.getResourcePath());
        ResourceLocation parent = specification == BlockSpecification.GLASS || specification == BlockSpecification.GLASS_PILLAR ?
            Rechiseled.identifier("block/glass_stairs") : new ResourceLocation("block/stairs");
        this.model(modelIdentifier)
            .parent(parent)
            .texture("bottom", bottom)
            .texture("side", side)
            .texture("top", top);
        parent = specification == BlockSpecification.GLASS || specification == BlockSpecification.GLASS_PILLAR ?
            Rechiseled.identifier("block/glass_inner_stairs") : new ResourceLocation("block/inner_stairs");
        this.model(new ResourceLocation(modelIdentifier.getResourceDomain(), modelIdentifier.getResourcePath() + "_inner"))
            .parent(parent)
            .texture("bottom", bottom)
            .texture("side", side)
            .texture("top", top);
        parent = specification == BlockSpecification.GLASS || specification == BlockSpecification.GLASS_PILLAR ?
            Rechiseled.identifier("block/glass_outer_stairs") : new ResourceLocation("block/outer_stairs");
        this.model(new ResourceLocation(modelIdentifier.getResourceDomain(), modelIdentifier.getResourcePath() + "_outer"))
            .parent(parent)
            .texture("bottom", bottom)
            .texture("side", side)
            .texture("top", top);
    }

    private void addSlabModels(BlockModelType modelType, Block slab, String texturePath){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(slab);
        ResourceLocation texture = new ResourceLocation(identifier.getResourceDomain(), "block/" + texturePath);

        // Get textures
        ResourceLocation bottom = texture;
        ResourceLocation side = texture;
        ResourceLocation top = texture;
        if(modelType == BlockModelType.CUBE)
            throw new UnsupportedOperationException();
        if(modelType == BlockModelType.PILLAR){
            bottom = new ResourceLocation(texture.getResourceDomain(), texture.getResourcePath() + "_end");
            side = new ResourceLocation(texture.getResourceDomain(), texture.getResourcePath() + "_side");
            top = new ResourceLocation(texture.getResourceDomain(), texture.getResourcePath() + "_end");
        }

        // Create models
        ResourceLocation modelIdentifier = new ResourceLocation(identifier.getResourceDomain(), "block/" + identifier.getResourcePath());
        this.model(new ResourceLocation(modelIdentifier.getResourceDomain(), modelIdentifier.getResourcePath() + "_double"))
            .parent("minecraft", "block/cube")
            .texture("up", top)
            .texture("down", bottom)
            .texture("north", side)
            .texture("east", side)
            .texture("south", side)
            .texture("west", side)
            .texture("particle", side);
        this.model(new ResourceLocation(modelIdentifier.getResourceDomain(), modelIdentifier.getResourcePath() + "_bottom"))
            .parent("minecraft", "block/half_slab")
            .texture("bottom", bottom)
            .texture("side", side)
            .texture("top", top);
        this.model(new ResourceLocation(modelIdentifier.getResourceDomain(), modelIdentifier.getResourcePath() + "_top"))
            .parent("minecraft", "block/upper_slab")
            .texture("bottom", bottom)
            .texture("side", side)
            .texture("top", top);
    }

    private void addBlockItemModel(Block block){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(block);
        this.model(new ResourceLocation(identifier.getResourceDomain(), "item/" + identifier.getResourcePath())).parent(new ResourceLocation(identifier.getResourceDomain(), "block/" + identifier.getResourcePath()));
    }

    private void addStairsItemModel(Block stairs, boolean connecting){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(stairs);
        if(connecting)
            this.model(new ResourceLocation(identifier.getResourceDomain(), "item/" + identifier.getResourcePath())).parent(new ResourceLocation(identifier.getResourceDomain(), "block/" + identifier.getResourcePath() + "_bottom"));
        else
            this.model(new ResourceLocation(identifier.getResourceDomain(), "item/" + identifier.getResourcePath())).parent(new ResourceLocation(identifier.getResourceDomain(), "block/" + identifier.getResourcePath()));
    }

    private void addSlabItemModel(Block slab){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(slab);
        this.model(new ResourceLocation(identifier.getResourceDomain(), "item/" + identifier.getResourcePath())).parent(new ResourceLocation(identifier.getResourceDomain(), "block/" + identifier.getResourcePath() + "_bottom"));
    }
}
