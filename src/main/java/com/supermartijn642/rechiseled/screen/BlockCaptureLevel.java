package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;

/**
 * Created 09/06/2023 by SuperMartijn642
 */
public class BlockCaptureLevel implements IBlockAccess {

    private BlockCapture capture;

    protected BlockCaptureLevel(){
    }

    public void setCapture(BlockCapture capture){
        this.capture = capture;
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
    public IBlockState getBlockState(BlockPos pos){
        return this.capture.getBlock(pos);
    }

    @Override
    public boolean isAirBlock(BlockPos pos){
        IBlockState state = this.capture.getBlock(pos);
        return state.getBlock().isAir(state, this, pos);
    }

    @Override
    public Biome getBiome(BlockPos pos){
        return ClientUtils.getWorld().getBiome(pos);
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
        return this.capture.getBlock(pos).isSideSolid(this, pos, side);
    }

}
