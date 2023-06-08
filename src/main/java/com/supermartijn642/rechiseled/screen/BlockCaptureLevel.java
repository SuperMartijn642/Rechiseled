package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.ClientUtils;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.lighting.WorldLightManager;

import javax.annotation.Nullable;

/**
 * Created 09/06/2023 by SuperMartijn642
 */
public class BlockCaptureLevel implements IBlockDisplayReader {

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
    public FluidState getFluidState(BlockPos pos){
        return this.capture.getBlock(pos).getFluidState();
    }

    @Override
    public float getShade(Direction direction, boolean bl){
        return ClientUtils.getWorld().getShade(direction, bl);
    }

    @Override
    public WorldLightManager getLightEngine(){
        return ClientUtils.getWorld().getLightEngine();
    }

    @Override
    public int getBlockTint(BlockPos pos, ColorResolver colorResolver){
        return 0;
    }
}
