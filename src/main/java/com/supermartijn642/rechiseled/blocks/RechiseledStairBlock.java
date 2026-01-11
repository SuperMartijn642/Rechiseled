package com.supermartijn642.rechiseled.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;

/**
 * Created 11/01/2026 by SuperMartijn642
 */
public class RechiseledStairBlock extends StairsBlock {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
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
