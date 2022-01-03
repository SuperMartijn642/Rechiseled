package com.supermartijn642.rechiseled;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class InheritingRechiseledBlock extends RechiseledBlock {

    public InheritingRechiseledBlock(String registryName, boolean connecting, Block parent){
        super(registryName, connecting, Properties.copy(parent));
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos){
        return super.getLightValue(state, world, pos);
    }
}
