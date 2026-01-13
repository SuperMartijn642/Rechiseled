package com.supermartijn642.rechiseled.blocks;

import com.supermartijn642.core.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created 11/01/2026 by SuperMartijn642
 */
public class RechiseledGlassStairBlock extends RechiseledStairBlock {

    private Block block, slab;
    private boolean haveBlockAndSlabBeenSet = false;
    private volatile Map<Pair<BlockState,BlockState>,Boolean[]> shouldHideFaceCache = null;

    public RechiseledGlassStairBlock(boolean connecting, BlockState parent, Properties properties){
        super(connecting, parent, properties);
    }

    public void setBlockAndSlab(Block block, Block slab){
        if(this.haveBlockAndSlabBeenSet)
            throw new IllegalStateException("Already set block and slab!");
        this.haveBlockAndSlabBeenSet = true;
        this.block = block;
        this.slab = slab;
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState otherState, Direction side){
        if(!this.haveBlockAndSlabBeenSet)
            throw new IllegalStateException("Block and slab have not been set!");
        if(otherState.getBlock() != this && otherState.getBlock() != this.block && otherState.getBlock() != this.slab)
            return false;
        // Create cache if absent
        if(this.shouldHideFaceCache == null){
            synchronized(this){
                if(this.shouldHideFaceCache == null)
                    this.shouldHideFaceCache = new HashMap<>();
            }
        }
        // Get or calculate value
        return RechiseledGlassBlock.calculateShouldHideFace(state, otherState, side, this.shouldHideFaceCache);
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
