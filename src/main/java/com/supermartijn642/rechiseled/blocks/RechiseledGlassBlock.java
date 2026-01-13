package com.supermartijn642.rechiseled.blocks;

import com.supermartijn642.core.block.BlockProperties;
import com.supermartijn642.core.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.EmptyBlockReader;
import net.minecraft.world.IBlockReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created 09/05/2023 by SuperMartijn642
 */
public class RechiseledGlassBlock extends RechiseledBlock {

    private Block stairs, slab;
    private boolean haveStairsAndSlabBeenSet = false;
    private volatile Map<Pair<BlockState,BlockState>,Boolean[]> shouldHideFaceCache = null;

    public RechiseledGlassBlock(boolean connecting, BlockProperties properties){
        super(connecting, properties.noOcclusion());
    }

    @SuppressWarnings("unused")
    public RechiseledGlassBlock(boolean connecting, Properties properties){
        super(connecting, properties.noOcclusion());
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
        if(!this.haveStairsAndSlabBeenSet)
            throw new IllegalStateException("Stairs and slab have not been set!");
        if(otherState.getBlock() != this && otherState.getBlock() != this.stairs && otherState.getBlock() != this.slab)
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

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public static boolean calculateShouldHideFace(BlockState state, BlockState neighbor, Direction side, Map<Pair<BlockState,BlockState>,Boolean[]> cache){
        // Get value from cache
        Boolean[] booleans;
        synchronized(cache){
            booleans = cache.computeIfAbsent(Pair.of(state, neighbor), s -> new Boolean[6]);
        }
        // If not cached, calculate it
        if(booleans[side.ordinal()] == null){
            VoxelShape ourShape = state.getShape(EmptyBlockReader.INSTANCE, BlockPos.ZERO).getFaceShape(side);
            VoxelShape neighborShape = neighbor.getShape(EmptyBlockReader.INSTANCE, BlockPos.ZERO).getFaceShape(side.getOpposite());
            boolean shouldHideFace = !VoxelShapes.joinIsNotEmpty(ourShape, neighborShape, IBooleanFunction.NOT_SAME);
            // Update cache
            synchronized(cache){
                booleans[side.ordinal()] = shouldHideFace;
            }
        }
        return booleans[side.ordinal()];
    }
}
