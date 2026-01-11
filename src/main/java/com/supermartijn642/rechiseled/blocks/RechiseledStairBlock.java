package com.supermartijn642.rechiseled.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

/**
 * Created 11/01/2026 by SuperMartijn642
 */
public class RechiseledStairBlock extends StairBlock {

    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;

    private final boolean connecting;

    public RechiseledStairBlock(boolean connecting, BlockState parent, Properties properties){
        super(parent, properties);
        this.connecting = connecting;
    }

    public boolean isConnecting(){
        return this.connecting;
    }
}
