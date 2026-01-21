package com.supermartijn642.rechiseled.screen.preview;

import com.supermartijn642.core.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;

import javax.annotation.Nullable;
import java.util.function.BiPredicate;

/**
 * Created 09/06/2023 by SuperMartijn642
 */
public class BlockCaptureLevel extends World {

    private BlockCapture capture;
    private BiPredicate<BlockPos,IBlockState> setBlockCallback;

    protected BlockCaptureLevel(){
        super(null, null, new WorldProvider() {
            @Override
            public DimensionType getDimensionType(){
                return ClientUtils.getWorld().provider.getDimensionType();
            }
        }, null, true);
    }

    public void setCapture(BlockCapture capture){
        this.capture = capture;
    }

    public void setSetBlockCallback(BiPredicate<BlockPos,IBlockState> callback){
        this.setBlockCallback = callback;
    }

    @Override
    public IBlockState getBlockState(BlockPos pos){
        return this.capture.getBlock(pos);
    }

    @Override
    public boolean setBlockState(BlockPos pos, IBlockState state, int flags){
        return this.setBlockCallback != null && this.setBlockCallback.test(pos, state);
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos){
        return null;
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue){
        return 15728880;
    }

    @Override
    public boolean isAirBlock(BlockPos pos){
        IBlockState state = this.capture.getBlock(pos);
        return state.getBlock().isAir(state, this, pos);
    }

    @Override
    protected boolean isChunkLoaded(int x, int z, boolean allowEmpty){
        return true;
    }

    @Override
    public Biome getBiome(BlockPos pos){
        return ClientUtils.getWorld().getBiome(pos);
    }

    @Override
    protected IChunkProvider createChunkProvider(){
        return null;
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction){
        return 0;
    }

    @Override
    public WorldType getWorldType(){
        return ClientUtils.getWorld().getWorldType();
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default){
        IBlockState block = this.capture.getBlock(pos);
        return block == null ? _default : block.isSideSolid(this, pos, side);
    }

}
