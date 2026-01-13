package com.supermartijn642.rechiseled.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created 11/01/2026 by SuperMartijn642
 */
public class RechiseledGlassStairBlock extends RechiseledStairBlock {

    private Block block, slab;
    private boolean haveBlockAndSlabBeenSet = false;

    public RechiseledGlassStairBlock(boolean connecting, IBlockState parent){
        super(connecting, parent);
    }

    public void setBlockAndSlab(Block block, Block slab){
        if(this.haveBlockAndSlabBeenSet)
            throw new IllegalStateException("Already set block and slab!");
        this.haveBlockAndSlabBeenSet = true;
        this.block = block;
        this.slab = slab;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess level, BlockPos pos, EnumFacing side){
        if(!this.haveBlockAndSlabBeenSet)
            throw new IllegalStateException("Block and slab have not been set!");
        IBlockState otherState = level.getBlockState(pos.offset(side));
        if(otherState.getBlock() == this.block)
            return otherState.shouldSideBeRendered(level, pos.offset(side), side.getOpposite());
        if(otherState.getBlock() == this || otherState.getBlock() == this.slab)
            return state.getBlockFaceShape(level, pos, side) != BlockFaceShape.SOLID
                || otherState.getBlockFaceShape(level, pos.offset(side), side.getOpposite()) != BlockFaceShape.SOLID;
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
