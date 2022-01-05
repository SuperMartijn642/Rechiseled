package com.supermartijn642.rechiseled.screen;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class BlockCapture {

    private final Map<BlockPos,IBlockState> blocks = Maps.newHashMap();

    public BlockCapture(){
    }

    public BlockCapture(IBlockState state){
        this.putBlock(BlockPos.ORIGIN, state);
    }

    public BlockCapture(Block block){
        this.putBlock(BlockPos.ORIGIN, block);
    }

    public void putBlock(BlockPos pos, IBlockState state){
        if(state == null || state.getBlock() == Blocks.AIR)
            this.blocks.remove(pos);
        else
            this.blocks.put(pos, state);
    }

    public void putBlock(BlockPos pos, Block block){
        this.putBlock(pos, block.getDefaultState());
    }

    public boolean isAir(BlockPos pos){
        return !this.blocks.containsKey(pos);
    }

    public IBlockState getBlock(BlockPos pos){
        return this.blocks.getOrDefault(pos, Blocks.AIR.getDefaultState());
    }

    public Iterable<Map.Entry<BlockPos,IBlockState>> getBlocks(){
        return this.blocks.entrySet();
    }

    public AxisAlignedBB getBounds(){
        if(this.blocks.isEmpty())
            return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
        AxisAlignedBB bounds = new AxisAlignedBB(this.blocks.keySet().stream().findFirst().get());
        for(BlockPos pos : this.blocks.keySet())
            bounds = bounds.union(new AxisAlignedBB(pos));
        return bounds;
    }
}
