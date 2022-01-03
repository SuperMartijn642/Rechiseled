package com.supermartijn642.rechiseled.screen;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class BlockCapture {

    private final Map<BlockPos,BlockState> blocks = Maps.newHashMap();

    public BlockCapture(){
    }

    public BlockCapture(Block block){
        this.putBlock(BlockPos.ZERO, block);
    }

    public void putBlock(BlockPos pos, BlockState state){
        if(state == null || state.isAir())
            this.blocks.remove(pos);
        else
            this.blocks.put(pos, state);
    }

    public void putBlock(BlockPos pos, Block block){
        this.putBlock(pos, block.defaultBlockState());
    }

    public boolean isAir(BlockPos pos){
        return !this.blocks.containsKey(pos);
    }

    public BlockState getBlock(BlockPos pos){
        return this.blocks.getOrDefault(pos, Blocks.AIR.defaultBlockState());
    }

    public Iterable<Map.Entry<BlockPos,BlockState>> getBlocks(){
        return this.blocks.entrySet();
    }

    public AxisAlignedBB getBounds(){
        if(this.blocks.isEmpty())
            return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
        AxisAlignedBB bounds = new AxisAlignedBB(this.blocks.keySet().stream().findFirst().get());
        for(BlockPos pos : this.blocks.keySet())
            bounds = bounds.minmax(new AxisAlignedBB(pos));
        return bounds;
    }
}
