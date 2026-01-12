package com.supermartijn642.rechiseled.screen.preview;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.rechiseled.util.EmptyChunkSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.particles.IParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraft.world.storage.IWorldInfo;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Created 09/06/2023 by SuperMartijn642
 */
public class BlockCaptureLevel implements IWorld {

    private BlockCapture capture;

    protected BlockCaptureLevel(){
    }

    public void setCapture(BlockCapture capture){
        this.capture = capture;
    }

    private static final DifficultyInstance DIFFICULTY_INSTANCE = new DifficultyInstance(Difficulty.NORMAL, 0, 0, 0);
    private static final Random RANDOM = new Random();

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
        return 1;
    }

    @Override
    public int getBrightness(LightType p_226658_1_, BlockPos p_226658_2_){
        return 15;
    }

    @Override
    public WorldLightManager getLightEngine(){
        return null;
    }

    @Override
    public ITickList<Block> getBlockTicks(){
        return EmptyTickList.empty();
    }

    @Override
    public ITickList<Fluid> getLiquidTicks(){
        return EmptyTickList.empty();
    }

    @Override
    public IWorldInfo getLevelData(){
        return ClientUtils.getWorld().getLevelData();
    }

    @Override
    public DifficultyInstance getCurrentDifficultyAt(BlockPos blockPos){
        return DIFFICULTY_INSTANCE;
    }

    @Override
    public AbstractChunkProvider getChunkSource(){
        return EmptyChunkSource.INSTANCE;
    }

    @Override
    public Random getRandom(){
        return RANDOM;
    }

    @Override
    public void playSound(@Nullable PlayerEntity player, BlockPos blockPos, SoundEvent soundEvent, SoundCategory soundCategory, float v, float v1){

    }

    @Override
    public void addParticle(IParticleData iParticleData, double v, double v1, double v2, double v3, double v4, double v5){

    }

    @Override
    public void levelEvent(@Nullable PlayerEntity playerEntity, int i, BlockPos blockPos, int i1){

    }

    @Override
    public DynamicRegistries registryAccess(){
        return ClientUtils.getWorld().registryAccess();
    }

    @Override
    public WorldBorder getWorldBorder(){
        return new WorldBorder();
    }

    @Override
    public List<Entity> getEntities(@Nullable Entity entity, AxisAlignedBB axisAlignedBB, @Nullable Predicate<? super Entity> predicate){
        return Collections.emptyList();
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<? extends T> aClass, AxisAlignedBB axisAlignedBB, @Nullable Predicate<? super T> predicate){
        return Collections.emptyList();
    }

    @Override
    public List<? extends PlayerEntity> players(){
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public IChunk getChunk(int x, int y, ChunkStatus chunkStatus, boolean b){
        return new EmptyChunk(null, new ChunkPos(x, y));
    }

    @Override
    public int getHeight(Heightmap.Type type, int i, int i1){
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
    public Biome getUncachedNoiseBiome(int i, int i1, int i2){
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
    public boolean setBlock(BlockPos blockPos, BlockState blockState, int i, int i1){
        return false;
    }

    @Override
    public boolean removeBlock(BlockPos blockPos, boolean b){
        return false;
    }

    @Override
    public boolean destroyBlock(BlockPos blockPos, boolean b, @Nullable Entity entity, int i){
        return false;
    }

    @Override
    public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> predicate){
        return predicate.test(this.capture.getBlock(pos));
    }
}
