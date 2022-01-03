package com.supermartijn642.rechiseled.screen;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

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

    public AABB getBounds(){
        if(this.blocks.isEmpty())
            return new AABB(0, 0, 0, 0, 0, 0);
        AABB bounds = new AABB(this.blocks.keySet().stream().findFirst().get());
        for(BlockPos pos : this.blocks.keySet())
            bounds = bounds.minmax(new AABB(pos));
        return bounds;
    }
}
