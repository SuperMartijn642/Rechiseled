package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.ClientUtils;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.IFluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;

/**
 * Created 09/06/2023 by SuperMartijn642
 */
public class BlockCaptureLevel implements IEnviromentBlockReader {

    private BlockCapture capture;

    protected BlockCaptureLevel(){
    }

    public void setCapture(BlockCapture capture){
        this.capture = capture;
    }

    @Nullable
    @Override
    public TileEntity getBlockEntity(BlockPos pos){
        return null;
    }

    @Override
    public BlockState getBlockState(BlockPos pos){
        return this.capture.getBlock(pos);
    }

    @Override
    public IFluidState getFluidState(BlockPos pos){
        return this.capture.getBlock(pos).getFluidState();
    }


    @Override
    public Biome getBiome(BlockPos pos){
        return ClientUtils.getWorld().getBiome(pos);
    }

    @Override
    public int getBrightness(LightType lightType, BlockPos pos){
        return 0;
    }
}
