package com.supermartijn642.rechiseled.blocks;

import com.supermartijn642.core.block.BlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created 09/05/2023 by SuperMartijn642
 */
public class RechiseledGlassBlock extends RechiseledBlock {

    private Block stairs, slab;
    private boolean haveStairsAndSlabBeenSet = false;

    public RechiseledGlassBlock(boolean connecting, BlockProperties properties){
        super(connecting, properties);
    }

    public void setStairsAndSlab(Block stairs, Block slab){
        if(this.haveStairsAndSlabBeenSet)
            throw new IllegalStateException("Already set stairs and slab!");
        this.haveStairsAndSlabBeenSet = true;
        this.stairs = stairs;
        this.slab = slab;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess level, BlockPos pos, EnumFacing side){
        if(!this.haveStairsAndSlabBeenSet)
            throw new IllegalStateException("Stairs and slab have not been set!");
        IBlockState otherState = level.getBlockState(pos.offset(side));
        if(otherState.getBlock() == this)
            return false;
        if(otherState.getBlock() == this.stairs || otherState.getBlock() == this.slab)
            return otherState.getBlockFaceShape(level, pos.offset(side), side.getOpposite()) != BlockFaceShape.SOLID;
        return super.shouldSideBeRendered(state, level, pos, side);
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
