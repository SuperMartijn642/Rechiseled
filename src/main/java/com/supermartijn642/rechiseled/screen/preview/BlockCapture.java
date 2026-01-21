package com.supermartijn642.rechiseled.screen.preview;

import com.google.common.collect.Maps;
import com.supermartijn642.core.util.Holder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class BlockCapture {

    private static BlockCaptureLevel fakeLevel;

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

    public void updateShapes(){
        if(fakeLevel == null)
            fakeLevel = new BlockCaptureLevel();
        fakeLevel.setCapture(this);
        this.blocks.replaceAll((pos, state) -> {
            BlockPos.MutableBlockPos neighbor = new BlockPos.MutableBlockPos();
            Holder<IBlockState> updatedState = new Holder<>(state);
            fakeLevel.setSetBlockCallback((p, s) -> {
                if(pos.equals(p)){
                    updatedState.set(s);
                    return true;
                }
                return false;
            });
            try{
                for(EnumFacing direction : EnumFacing.values()){
                    neighbor.setPos(pos).move(direction);
                    Block neighborBlock = this.blocks.getOrDefault(neighbor, Blocks.AIR.getDefaultState()).getBlock();
                    updatedState.get().neighborChanged(fakeLevel, pos, neighborBlock, neighbor);
                    if(updatedState.get() == null || updatedState.get().getBlock() != state.getBlock())
                        return state;
                }
            }catch(Exception ignored){
                return state;
            }
            return updatedState.get();
        });
        fakeLevel.setCapture(null);
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
