package com.supermartijn642.rechiseled.blocks;


import com.supermartijn642.core.block.BlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created 11/01/2026 by SuperMartijn642
 */
public class RechiseledGlassSlabBlock extends RechiseledSlabBlock {

    public RechiseledGlassSlabBlock(boolean connecting, BlockProperties properties){
        super(connecting, properties);
    }

    @Override
    public boolean causesSuffocation(IBlockState state){
        return false;
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type){
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state){
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer(){
        return BlockRenderLayer.TRANSLUCENT;
    }
}
