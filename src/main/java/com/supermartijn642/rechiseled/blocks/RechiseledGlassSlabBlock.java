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
 * Created 11/01/2026 by SuperMartijn642
 */
public class RechiseledGlassSlabBlock extends RechiseledSlabBlock {

    private Block block, stairs;
    private boolean haveBlockAndStairsBeenSet = false;

    public RechiseledGlassSlabBlock(boolean connecting, BlockProperties properties){
        super(connecting, properties);
    }

    public void setStairsAndSlab(Block block, Block stairs){
        if(this.haveBlockAndStairsBeenSet)
            throw new IllegalStateException("Already set block and stairs!");
        this.haveBlockAndStairsBeenSet = true;
        this.block = block;
        this.stairs = stairs;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess level, BlockPos pos, EnumFacing side){
        if(!this.haveBlockAndStairsBeenSet)
            throw new IllegalStateException("Block and stairs have not been set!");
        IBlockState otherState = level.getBlockState(pos.offset(side));
        if(otherState.getBlock() == this.block)
            return otherState.shouldSideBeRendered(level, pos.offset(side), side.getOpposite());
        if(otherState.getBlock() == this && side.getAxis().isHorizontal())
            return state.getValue(TYPE) != otherState.getValue(TYPE);
        if(otherState.getBlock() == this.stairs || otherState.getBlock() == this)
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
