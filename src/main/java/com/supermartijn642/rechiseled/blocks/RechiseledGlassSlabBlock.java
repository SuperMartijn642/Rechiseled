package com.supermartijn642.rechiseled.blocks;


import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/**
 * Created 11/01/2026 by SuperMartijn642
 */
public class RechiseledGlassSlabBlock extends RechiseledSlabBlock {

    public RechiseledGlassSlabBlock(boolean connecting, Properties properties){
        super(connecting, properties);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState otherState, Direction side){
        return otherState.getBlock() == this || super.skipRendering(state, otherState, side); // TODO
    }

    @Override
    public float getShadeBrightness(BlockState state, IBlockReader level, BlockPos pos){
        return 1;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader level, BlockPos pos){
        return true;
    }

    @Override
    public boolean isSuffocating(BlockState state, IBlockReader level, BlockPos pos){
        return false;
    }

    @Override
    public boolean isRedstoneConductor(BlockState state, IBlockReader level, BlockPos pos){
        return false;
    }

    @Override
    public boolean isValidSpawn(BlockState state, IBlockReader level, BlockPos pos, EntityType<?> entityType){
        return false;
    }
}
