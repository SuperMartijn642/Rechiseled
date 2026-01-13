package com.supermartijn642.rechiseled.blocks;

import com.supermartijn642.core.block.BlockProperties;
import com.supermartijn642.core.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created 09/05/2023 by SuperMartijn642
 */
public class RechiseledGlassPillarBlock extends RechiseledPillarBlock {

    private Block stairs, slab;
    private boolean haveStairsAndSlabBeenSet = false;
    private volatile Map<Pair<BlockState,BlockState>,Boolean[]> shouldHideFaceCache = null;

    public RechiseledGlassPillarBlock(boolean connecting, BlockProperties properties){
        super(connecting, properties.noOcclusion().isSuffocating(Blocks::never));
    }

    public void setStairsAndSlab(Block stairs, Block slab){
        if(this.haveStairsAndSlabBeenSet)
            throw new IllegalStateException("Already set stairs and slab!");
        this.haveStairsAndSlabBeenSet = true;
        this.stairs = stairs;
        this.slab = slab;
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState otherState, Direction side){
        if(state.getValue(AXIS_PROPERTY) != Direction.Axis.Y)
            return state == otherState;
        if(!otherState.is(this) && !otherState.is(this.stairs) && !otherState.is(this.slab))
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
