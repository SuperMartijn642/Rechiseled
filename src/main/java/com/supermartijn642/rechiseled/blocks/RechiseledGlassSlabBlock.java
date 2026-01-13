package com.supermartijn642.rechiseled.blocks;


import com.supermartijn642.core.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created 11/01/2026 by SuperMartijn642
 */
public class RechiseledGlassSlabBlock extends RechiseledSlabBlock {

    private Block block, stairs;
    private boolean haveBlockAndStairsBeenSet = false;
    private volatile Map<Pair<BlockState,BlockState>,Boolean[]> shouldHideFaceCache = null;

    public RechiseledGlassSlabBlock(boolean connecting, Properties properties){
        super(connecting, properties);
    }

    public void setStairsAndSlab(Block block, Block stairs){
        if(this.haveBlockAndStairsBeenSet)
            throw new IllegalStateException("Already set block and stairs!");
        this.haveBlockAndStairsBeenSet = true;
        this.block = block;
        this.stairs = stairs;
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState otherState, Direction side){
        if(!this.haveBlockAndStairsBeenSet)
            throw new IllegalStateException("Block and stairs have not been set!");
        if(!otherState.is(this) && !otherState.is(this.block) && !otherState.is(this.stairs))
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
