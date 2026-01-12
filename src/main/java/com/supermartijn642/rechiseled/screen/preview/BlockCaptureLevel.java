package com.supermartijn642.rechiseled.screen.preview;

import com.supermartijn642.core.ClientUtils;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created 09/06/2023 by SuperMartijn642
 */
public class BlockCaptureLevel implements LevelReader {

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
        return 1;
    }

    @Override
    public int getBrightness(LightLayer lightLayer, BlockPos pos){
        return 15;
    }

    @Override
    public LevelLightEngine getLightEngine(){
        return null;
    }

    @Override
    public @Nullable ChunkAccess getChunk(int i, int j, ChunkStatus chunkStatus, boolean bl){
        return null;
    }

    @Override
    public boolean hasChunk(int i, int j){
        return false;
    }

    @Override
    public int getHeight(Heightmap.Types types, int i, int j){
        return 0;
    }

    @Override
    public int getSkyDarken(){
        return 0;
    }

    @Override
    public BiomeManager getBiomeManager(){
        return null;
    }

    @Override
    public int getBlockTint(BlockPos pos, ColorResolver colorResolver){
        //noinspection unchecked
        Registry<Biome> biomeRegistry = (Registry<Biome>)BuiltInRegistries.REGISTRY.getOptional(Registries.BIOME.location()).orElse(null);
        return biomeRegistry == null ? 0 : colorResolver.getColor(biomeRegistry.get(Biomes.PLAINS).orElseThrow().value(), pos.getX(), pos.getZ());
    }

    @Override
    public Holder<Biome> getUncachedNoiseBiome(int i, int j, int k){
        return ClientUtils.getWorld().getUncachedNoiseBiome(0, 0, 0);
    }

    @Override
    public boolean isClientSide(){
        return true;
    }

    @Override
    public int getSeaLevel(){
        return 0;
    }

    @Override
    public DimensionType dimensionType(){
        return ClientUtils.getWorld().dimensionType();
    }

    @Override
    public int getHeight(){
        return Integer.MAX_VALUE;
    }

    @Override
    public RegistryAccess registryAccess(){
        return ClientUtils.getWorld().registryAccess();
    }

    @Override
    public FeatureFlagSet enabledFeatures(){
        return ClientUtils.getWorld().enabledFeatures();
    }

    @Override
    public int getMinY(){
        return -Integer.MAX_VALUE;
    }

    @Override
    public WorldBorder getWorldBorder(){
        return ClientUtils.getWorld().getWorldBorder();
    }

    @Override
    public List<VoxelShape> getEntityCollisions(@Nullable Entity entity, AABB aABB){
        return List.of();
    }
}
