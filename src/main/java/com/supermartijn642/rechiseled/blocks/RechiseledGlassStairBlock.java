package com.supermartijn642.rechiseled.blocks;

import com.supermartijn642.core.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

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
        super(connecting, parent, properties.noOcclusion().isSuffocating(Blocks::never).isViewBlocking(Blocks::never));
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
        if(!otherState.is(this) && !otherState.is(this.block) && !otherState.is(this.slab))
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
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context){
        return Shapes.empty();
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos){
        return 1;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos){
        return true;
    }
}
