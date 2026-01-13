package com.supermartijn642.rechiseled.blocks;

import com.supermartijn642.core.block.BlockProperties;
import com.supermartijn642.core.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

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
        super(connecting, properties.noOcclusion().isSuffocating(Blocks::never));
    }

    @SuppressWarnings("unused")
    public RechiseledGlassBlock(boolean connecting, BlockBehaviour.Properties properties){
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
        if(!this.haveStairsAndSlabBeenSet)
            throw new IllegalStateException("Stairs and slab have not been set!");
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
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public static boolean calculateShouldHideFace(BlockState state, BlockState neighbor, Direction side, Map<Pair<BlockState,BlockState>,Boolean[]> cache){
        // Get value from cache
        Boolean[] booleans;
        synchronized(cache){
            booleans = cache.computeIfAbsent(Pair.of(state,neighbor), s -> new Boolean[6]);
        }
        // If not cached, calculate it
        if(booleans[side.ordinal()] == null){
            VoxelShape ourShape = state.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).getFaceShape(side);
            VoxelShape neighborShape = neighbor.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).getFaceShape(side.getOpposite());
            boolean shouldHideFace = !Shapes.joinIsNotEmpty(ourShape, neighborShape, BooleanOp.NOT_SAME);
            // Update cache
            synchronized(cache){
                booleans[side.ordinal()] = shouldHideFace;
            }
        }
        return booleans[side.ordinal()];
    }
}
