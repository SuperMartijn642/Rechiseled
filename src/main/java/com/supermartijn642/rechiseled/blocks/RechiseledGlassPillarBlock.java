package com.supermartijn642.rechiseled.blocks;

import com.supermartijn642.core.block.BlockProperties;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

/**
 * Created 09/05/2023 by SuperMartijn642
 */
public class RechiseledGlassPillarBlock extends RechiseledPillarBlock {

    public RechiseledGlassPillarBlock(boolean connecting, BlockProperties properties){
        super(connecting, properties);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState otherState, Direction side){
        return otherState.is(this) || super.skipRendering(state, otherState, side);
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
