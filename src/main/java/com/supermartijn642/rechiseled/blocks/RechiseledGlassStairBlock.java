package com.supermartijn642.rechiseled.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

/**
 * Created 11/01/2026 by SuperMartijn642
 */
public class RechiseledGlassStairBlock extends RechiseledStairBlock {

    public RechiseledGlassStairBlock(boolean connecting, BlockState parent, Properties properties){
        super(connecting, parent, properties);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState otherState, Direction side){
        return otherState.is(this) || super.skipRendering(state, otherState, side); // TODO
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context){
        return VoxelShapes.empty();
    }

    @Override
    public float getShadeBrightness(BlockState state, IBlockReader level, BlockPos pos){
        return 1;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader level, BlockPos pos){
        return true;
    }
}
