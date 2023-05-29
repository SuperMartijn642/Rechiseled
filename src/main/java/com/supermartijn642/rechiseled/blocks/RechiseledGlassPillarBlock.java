package com.supermartijn642.rechiseled.blocks;

import com.supermartijn642.core.block.BlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

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
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context){
        return Shapes.empty();
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos){
        return 1;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos){
        return true;
    }
}
