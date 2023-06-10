package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.ClientUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

/**
 * Created 10/06/2023 by SuperMartijn642
 */
public class BlockCaptureLevel implements BlockAndTintGetter {

    private BlockCapture capture;

    public void setCapture(BlockCapture capture){
        this.capture = capture;
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos pos){
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
    public float getShade(Direction side, boolean bl){
        return ClientUtils.getWorld().getShade(side, bl);
    }

    @Override
    public int getBrightness(LightLayer lightLayer, BlockPos pos){
        return 15;
    }

    @Override
    public LevelLightEngine getLightEngine(){
        return ClientUtils.getWorld().getLightEngine();
    }

    @Override
    public int getBlockTint(BlockPos pos, ColorResolver colorResolver){
        //noinspection unchecked
        Registry<Biome> biomeRegistry = (Registry<Biome>)BuiltInRegistries.REGISTRY.get(Registries.BIOME.location());
        return biomeRegistry == null ? 0 : colorResolver.getColor(biomeRegistry.get(Biomes.PLAINS), pos.getX(), pos.getZ());
    }

    @Override
    public int getHeight(){
        return -Integer.MAX_VALUE;
    }

    @Override
    public int getMinBuildHeight(){
        return Integer.MAX_VALUE;
    }
}
