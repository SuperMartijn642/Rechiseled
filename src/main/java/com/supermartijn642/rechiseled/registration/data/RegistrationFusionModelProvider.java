package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.fusion.api.model.DefaultModelTypes;
import com.supermartijn642.fusion.api.model.ModelInstance;
import com.supermartijn642.fusion.api.model.data.ConnectingModelDataBuilder;
import com.supermartijn642.fusion.api.predicate.ConnectionDirection;
import com.supermartijn642.fusion.api.predicate.ConnectionPredicate;
import com.supermartijn642.fusion.api.predicate.DefaultConnectionPredicates;
import com.supermartijn642.fusion.api.provider.FusionModelProvider;
import com.supermartijn642.fusion.api.util.Pair;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.blocks.BlockModelType;
import com.supermartijn642.rechiseled.api.blocks.BlockSpecification;
import com.supermartijn642.rechiseled.blocks.RechiseledPillarBlock;
import com.supermartijn642.rechiseled.blocks.RechiseledSlabBlock;
import com.supermartijn642.rechiseled.blocks.RechiseledStairBlock;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;

/**
 * Created 05/05/2023 by SuperMartijn642
 */
public class RegistrationFusionModelProvider extends FusionModelProvider {

    private final RechiseledRegistrationImpl registration;

    public RegistrationFusionModelProvider(RechiseledRegistrationImpl registration, PackOutput output){
        super(registration.getModid(), output);
        this.registration = registration;
    }

    @Override
    protected void generate(){
        if(!this.registration.providersRegistered)
            return;
        this.registration.getBlockBuilders().forEach(
            builder -> {
                if(builder.hasConnectingVariant()){
                    // Get block, stairs, and slab
                    Block block = builder.getConnectingBlock();
                    boolean hasStairs = builder.hasStairs() && builder.getStairs().hasConnectingVariant();
                    Block stairs = hasStairs ? builder.getStairs().getConnectingBlock() : null;
                    boolean hasSlab = builder.hasSlabs() && builder.getSlabs().hasConnectingVariant();
                    Block slab = builder.hasSlabs() && builder.getSlabs().hasConnectingVariant() ? builder.getSlabs().getConnectingBlock() : null;
                    // Add models
                    BlockModelType modelType = builder.getModelType() == null ? builder.getSpecification().getDefaultModelType() : builder.getModelType();
                    String texture = builder.getIdentifier();
                    this.addBlockModel(modelType, block, stairs, slab, texture);
                    if(hasStairs)
                        this.addStairsModels(modelType, block, stairs, slab, texture, builder.getSpecification());
                    if(hasSlab)
                        this.addSlabModels(modelType, block, stairs, slab, texture);
                }
            }
        );
    }

    private void addBlockModel(BlockModelType modelType, Block block, Block stairs, Block slab, String texturePath){
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
        this.addModel(modelIdentifier, ModelInstance.of(
            DefaultModelTypes.CONNECTING,
            ConnectingModelDataBuilder.builder()
                .parent(new ResourceLocation("block/cube"))
                .texture("up", up)
                .texture("down", down)
                .texture("north", north)
                .texture("east", east)
                .texture("south", south)
                .texture("west", west)
                .texture("particle", particle)
                .connections("north", "side")
                .connections("east", "side")
                .connections("south", "side")
                .connections("west", "side")
                .connections("side", blockConnectionsSide(block, stairs, slab))
                .connections("up", blockConnectionsTop(block, stairs, slab))
                .connections("down", blockConnectionsBottom(block, stairs, slab))
                .build()
        ));

        // Horizontal pillar models
        if(modelType == BlockModelType.PILLAR){
            this.addModel(modelIdentifier.withSuffix("_horizontal"), ModelInstance.of(
                DefaultModelTypes.CONNECTING,
                ConnectingModelDataBuilder.builder()
                    .parent(modelIdentifier)
                    .connections("up", "all")
                    .connections("down", "all")
                    .connections("north", "all")
                    .connections("east", "all")
                    .connections("south", "all")
                    .connections("west", "all")
                    .connections("all", DefaultConnectionPredicates.isSameState())
                    .build()
            ));
        }
    }

    private void addStairsModels(BlockModelType modelType, Block block, Block stairs, Block slab, String texturePath, BlockSpecification specification){
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
        this.addModel(modelIdentifier.withSuffix("_bottom"), ModelInstance.of(
            DefaultModelTypes.CONNECTING,
            ConnectingModelDataBuilder.builder()
                .parent(parent)
                .texture("bottom", bottom)
                .texture("side", side)
                .texture("top", top)
                .connections("bottom", bottomStairConnectionsBottom(block, stairs, slab))
                .connections("side", bottomStairConnectionsSide(block, stairs, slab))
                .connections("top", bottomStairConnectionsTop(block, stairs, slab))
                .build()
        ));
        parent = specification == BlockSpecification.GLASS || specification == BlockSpecification.GLASS_PILLAR ?
            Rechiseled.identifier("block/glass_inner_stairs") : new ResourceLocation("block/inner_stairs");
        this.addModel(modelIdentifier.withSuffix("_inner_bottom"), ModelInstance.of(
            DefaultModelTypes.CONNECTING,
            ConnectingModelDataBuilder.builder()
                .parent(parent)
                .texture("bottom", bottom)
                .texture("side", side)
                .texture("top", top)
                .connections("bottom", bottomStairConnectionsBottom(block, stairs, slab))
                .connections("side", bottomStairConnectionsSide(block, stairs, slab))
                .connections("top", bottomStairConnectionsTop(block, stairs, slab))
                .build()
        ));
        parent = specification == BlockSpecification.GLASS || specification == BlockSpecification.GLASS_PILLAR ?
            Rechiseled.identifier("block/glass_outer_stairs") : new ResourceLocation("block/outer_stairs");
        this.addModel(modelIdentifier.withSuffix("_outer_bottom"), ModelInstance.of(
            DefaultModelTypes.CONNECTING,
            ConnectingModelDataBuilder.builder()
                .parent(parent)
                .texture("bottom", bottom)
                .texture("side", side)
                .texture("top", top)
                .connections("bottom", bottomStairConnectionsBottom(block, stairs, slab))
                .connections("side", bottomStairConnectionsSide(block, stairs, slab))
                .connections("top", bottomStairConnectionsTop(block, stairs, slab))
                .build()
        ));
        parent = specification == BlockSpecification.GLASS || specification == BlockSpecification.GLASS_PILLAR ?
            Rechiseled.identifier("block/glass_stairs_top") : Rechiseled.identifier("block/stairs_top");
        this.addModel(modelIdentifier.withSuffix("_top"), ModelInstance.of(
            DefaultModelTypes.CONNECTING,
            ConnectingModelDataBuilder.builder()
                .parent(parent)
                .texture("bottom", bottom)
                .texture("side", side)
                .texture("top", top)
                .connections("bottom", topStairConnectionsBottom(block, stairs, slab))
                .connections("side", topStairConnectionsSide(block, stairs, slab))
                .connections("top", topStairConnectionsTop(block, stairs, slab))
                .build()
        ));
        parent = specification == BlockSpecification.GLASS || specification == BlockSpecification.GLASS_PILLAR ?
            Rechiseled.identifier("block/glass_inner_stairs_top") : Rechiseled.identifier("block/inner_stairs_top");
        this.addModel(modelIdentifier.withSuffix("_inner_top"), ModelInstance.of(
            DefaultModelTypes.CONNECTING,
            ConnectingModelDataBuilder.builder()
                .parent(parent)
                .texture("bottom", bottom)
                .texture("side", side)
                .texture("top", top)
                .connections("bottom", topStairConnectionsBottom(block, stairs, slab))
                .connections("side", topStairConnectionsSide(block, stairs, slab))
                .connections("top", topStairConnectionsTop(block, stairs, slab))
                .build()
        ));
        parent = specification == BlockSpecification.GLASS || specification == BlockSpecification.GLASS_PILLAR ?
            Rechiseled.identifier("block/glass_outer_stairs_top") : Rechiseled.identifier("block/outer_stairs_top");
        this.addModel(modelIdentifier.withSuffix("_outer_top"), ModelInstance.of(
            DefaultModelTypes.CONNECTING,
            ConnectingModelDataBuilder.builder()
                .parent(parent)
                .texture("bottom", bottom)
                .texture("side", side)
                .texture("top", top)
                .connections("bottom", topStairConnectionsBottom(block, stairs, slab))
                .connections("side", topStairConnectionsSide(block, stairs, slab))
                .connections("top", topStairConnectionsTop(block, stairs, slab))
                .build()
        ));
    }

    private void addSlabModels(BlockModelType modelType, Block block, Block stairs, Block slab, String texturePath){
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
        this.addModel(modelIdentifier.withSuffix("_double"), ModelInstance.of(
            DefaultModelTypes.CONNECTING,
            ConnectingModelDataBuilder.builder()
                .parent(new ResourceLocation("block/cube"))
                .texture("up", top)
                .texture("down", bottom)
                .texture("north", "#side")
                .texture("east", "#side")
                .texture("south", "#side")
                .texture("west", "#side")
                .texture("particle", "#side")
                .texture("side", side)
                .connections("north", "side")
                .connections("east", "side")
                .connections("south", "side")
                .connections("west", "side")
                .connections("side", blockConnectionsSide(block, stairs, slab))
                .connections("up", blockConnectionsTop(block, stairs, slab))
                .connections("down", blockConnectionsBottom(block, stairs, slab))
                .build()
        ));
        this.addModel(modelIdentifier.withSuffix("_bottom"), ModelInstance.of(
            DefaultModelTypes.CONNECTING,
            ConnectingModelDataBuilder.builder()
                .parent(new ResourceLocation("block/slab"))
                .texture("bottom", bottom)
                .texture("side", side)
                .texture("top", top)
                .connections("bottom", bottomSlabConnectionsBottom(block, stairs, slab))
                .connections("side", bottomSlabConnectionsSide(block, stairs, slab))
                .connections("top", bottomSlabConnectionsTop(block, stairs, slab))
                .build()
        ));
        this.addModel(modelIdentifier.withSuffix("_top"), ModelInstance.of(
            DefaultModelTypes.CONNECTING,
            ConnectingModelDataBuilder.builder()
                .parent(new ResourceLocation("block/slab_top"))
                .texture("bottom", bottom)
                .texture("side", side)
                .texture("top", top)
                .connections("bottom", topSlabConnectionsBottom(block, stairs, slab))
                .connections("side", topSlabConnectionsSide(block, stairs, slab))
                .connections("top", topSlabConnectionsTop(block, stairs, slab))
                .build()
        ));
    }

    private static ConnectionPredicate isBlock(Block block){
        if(block.getStateDefinition().getProperties().contains(RechiseledPillarBlock.AXIS_PROPERTY))
            //noinspection unchecked,SuspiciousNameCombination
            return DefaultConnectionPredicates.matchState(block, Pair.of(RechiseledPillarBlock.AXIS_PROPERTY, Direction.Axis.Y));
        return DefaultConnectionPredicates.matchBlock(block);
    }

    private static ConnectionPredicate isFullBlock(Block block, Block slab){
        if(slab == null)
            return isBlock(block);
        //noinspection unchecked
        return isBlock(block).or(DefaultConnectionPredicates.matchState(slab, Pair.of(RechiseledSlabBlock.TYPE, SlabType.DOUBLE)));
    }

    private static ConnectionPredicate isStairs(Block stairs){
        return stairs == null ? DefaultConnectionPredicates.or() : DefaultConnectionPredicates.matchBlock(stairs);
    }

    private static ConnectionPredicate isTopStairs(Block stairs){
        //noinspection unchecked,SuspiciousNameCombination
        return stairs == null ? DefaultConnectionPredicates.or() : DefaultConnectionPredicates.matchState(stairs, Pair.of(RechiseledStairBlock.HALF, Half.TOP));
    }

    private static ConnectionPredicate isBottomStairs(Block stairs){
        //noinspection unchecked,SuspiciousNameCombination
        return stairs == null ? DefaultConnectionPredicates.or() : DefaultConnectionPredicates.matchState(stairs, Pair.of(RechiseledStairBlock.HALF, Half.BOTTOM));
    }

    private static ConnectionPredicate isSlab(Block slab){
        return slab == null ? DefaultConnectionPredicates.or() : DefaultConnectionPredicates.matchBlock(slab);
    }

    private static ConnectionPredicate isTopSlab(Block slab){
        //noinspection unchecked,SuspiciousNameCombination
        return slab == null ? DefaultConnectionPredicates.or() : DefaultConnectionPredicates.matchState(slab, Pair.of(RechiseledSlabBlock.TYPE, SlabType.TOP));
    }

    private static ConnectionPredicate isBottomSlab(Block slab){
        //noinspection unchecked,SuspiciousNameCombination
        return slab == null ? DefaultConnectionPredicates.or() : DefaultConnectionPredicates.matchState(slab, Pair.of(RechiseledSlabBlock.TYPE, SlabType.BOTTOM));
    }

    private static ConnectionPredicate blockConnectionsSide(Block block, Block stairs, Block slab){
        return DefaultConnectionPredicates.or(
            isFullBlock(block, slab),
            isStairs(stairs),
            DefaultConnectionPredicates.isDirection(ConnectionDirection.TOP, ConnectionDirection.TOP_LEFT, ConnectionDirection.TOP_RIGHT)
                .and(isBottomSlab(slab)),
            DefaultConnectionPredicates.isDirection(ConnectionDirection.BOTTOM, ConnectionDirection.BOTTOM_LEFT, ConnectionDirection.BOTTOM_RIGHT)
                .and(isTopSlab(slab))
        );
    }

    private static ConnectionPredicate blockConnectionsTop(Block block, Block stairs, Block slab){
        return DefaultConnectionPredicates.or(
            isFullBlock(block, slab),
            isStairs(stairs),
            isTopSlab(slab)
        );
    }

    private static ConnectionPredicate blockConnectionsBottom(Block block, Block stairs, Block slab){
        return DefaultConnectionPredicates.or(
            isFullBlock(block, slab),
            isStairs(stairs),
            isBottomSlab(slab)
        );
    }

    private static ConnectionPredicate bottomSlabConnectionsSide(Block block, Block stairs, Block slab){
        return DefaultConnectionPredicates.or(
            DefaultConnectionPredicates.isDirection(ConnectionDirection.LEFT, ConnectionDirection.RIGHT)
                .and(isBottomSlab(slab).or(isBottomStairs(stairs))),
            DefaultConnectionPredicates.isDirection(ConnectionDirection.BOTTOM, ConnectionDirection.BOTTOM_LEFT, ConnectionDirection.BOTTOM_RIGHT)
                .and(isFullBlock(block, slab).or(isTopSlab(slab), isStairs(stairs)))
        );
    }

    private static ConnectionPredicate bottomSlabConnectionsTop(Block block, Block stairs, Block slab){
        return DefaultConnectionPredicates.or(
            isBottomSlab(slab),
            isBottomStairs(stairs)
        );
    }

    private static ConnectionPredicate bottomSlabConnectionsBottom(Block block, Block stairs, Block slab){
        return blockConnectionsBottom(block, stairs, slab);
    }

    private static ConnectionPredicate topSlabConnectionsSide(Block block, Block stairs, Block slab){
        return DefaultConnectionPredicates.or(
            DefaultConnectionPredicates.isDirection(ConnectionDirection.LEFT, ConnectionDirection.RIGHT)
                .and(isTopSlab(slab).or(isTopStairs(stairs))),
            DefaultConnectionPredicates.isDirection(ConnectionDirection.TOP, ConnectionDirection.TOP_LEFT, ConnectionDirection.TOP_RIGHT)
                .and(isFullBlock(block, slab).or(isBottomSlab(slab), isStairs(stairs)))
        );
    }

    private static ConnectionPredicate topSlabConnectionsTop(Block block, Block stairs, Block slab){
        return blockConnectionsTop(block, stairs, slab);
    }

    private static ConnectionPredicate topSlabConnectionsBottom(Block block, Block stairs, Block slab){
        return DefaultConnectionPredicates.or(
            isTopSlab(slab),
            isTopStairs(stairs)
        );
    }

    private static ConnectionPredicate bottomStairConnectionsSide(Block block, Block stairs, Block slab){
        return DefaultConnectionPredicates.or(
            isFullBlock(block, slab),
            isStairs(stairs),
            DefaultConnectionPredicates.isDirection(ConnectionDirection.LEFT, ConnectionDirection.TOP_LEFT, ConnectionDirection.TOP, ConnectionDirection.TOP_RIGHT, ConnectionDirection.RIGHT)
                .and(isBottomSlab(slab)),
            DefaultConnectionPredicates.isDirection(ConnectionDirection.BOTTOM, ConnectionDirection.BOTTOM_LEFT, ConnectionDirection.BOTTOM_RIGHT)
                .and(isTopSlab(slab))
        );
    }

    private static ConnectionPredicate bottomStairConnectionsTop(Block block, Block stairs, Block slab){
        return DefaultConnectionPredicates.or(
            isBlock(block),
            isStairs(stairs),
            isSlab(slab)
        );
    }

    private static ConnectionPredicate bottomStairConnectionsBottom(Block block, Block stairs, Block slab){
        return blockConnectionsBottom(block, stairs, slab);
    }

    private static ConnectionPredicate topStairConnectionsSide(Block block, Block stairs, Block slab){
        return DefaultConnectionPredicates.or(
            isFullBlock(block, slab),
            isStairs(stairs),
            DefaultConnectionPredicates.isDirection(ConnectionDirection.TOP, ConnectionDirection.TOP_LEFT, ConnectionDirection.TOP_RIGHT)
                .and(isBottomSlab(slab)),
            DefaultConnectionPredicates.isDirection(ConnectionDirection.LEFT, ConnectionDirection.BOTTOM_LEFT, ConnectionDirection.BOTTOM, ConnectionDirection.BOTTOM_RIGHT, ConnectionDirection.RIGHT)
                .and(isTopSlab(slab))
        );
    }

    private static ConnectionPredicate topStairConnectionsTop(Block block, Block stairs, Block slab){
        return blockConnectionsTop(block, stairs, slab);
    }

    private static ConnectionPredicate topStairConnectionsBottom(Block block, Block stairs, Block slab){
        return bottomStairConnectionsTop(block, stairs, slab);
    }

    @Override
    public String getName(){
        return "Registration Fusion Model Generator: " + this.registration.getModid();
    }
}
