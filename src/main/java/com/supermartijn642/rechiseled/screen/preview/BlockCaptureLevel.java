package com.supermartijn642.rechiseled.screen.preview;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.rechiseled.util.EmptyChunkSource;
import com.supermartijn642.rechiseled.util.EmptyLevelTickAccess;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.LevelTickAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created 09/06/2023 by SuperMartijn642
 */
public class BlockCaptureLevel implements LevelAccessor {

    private BlockCapture capture;

    public void setCapture(BlockCapture capture){
        this.capture = capture;
    }

    private static final DifficultyInstance DIFFICULTY_INSTANCE = new DifficultyInstance(Difficulty.NORMAL, 0, 0, 0);
    private static final RandomSource RANDOM_SOURCE = RandomSource.create();

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
    public long nextSubTickCount(){
        return 0;
    }

    @Override
    public LevelTickAccess<Block> getBlockTicks(){
        //noinspection unchecked,rawtypes
        return (LevelTickAccess)EmptyLevelTickAccess.INSTANCE;
    }

    @Override
    public LevelTickAccess<Fluid> getFluidTicks(){
        //noinspection unchecked,rawtypes
        return (LevelTickAccess)EmptyLevelTickAccess.INSTANCE;
    }

    @Override
    public LevelData getLevelData(){
        return ClientUtils.getWorld().getLevelData();
    }

    @Override
    public DifficultyInstance getCurrentDifficultyAt(BlockPos blockPos){
        return DIFFICULTY_INSTANCE;
    }

    @Override
    public @Nullable MinecraftServer getServer(){
        return null;
    }

    @Override
    public ChunkSource getChunkSource(){
        return EmptyChunkSource.INSTANCE;
    }

    @Override
    public boolean hasChunk(int i, int j){
        return false;
    }

    @Override
    public RandomSource getRandom(){
        return RANDOM_SOURCE;
    }

    @Override
    public void playSound(@Nullable Player player, BlockPos blockPos, SoundEvent soundEvent, SoundSource soundSource, float f, float g){
    }

    @Override
    public void addParticle(ParticleOptions particleOptions, double d, double e, double f, double g, double h, double i){
    }

    @Override
    public void levelEvent(@Nullable Player player, int i, BlockPos blockPos, int j){
    }

    @Override
    public void gameEvent(GameEvent gameEvent, Vec3 vec3, GameEvent.Context context){
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
        Registry<Biome> biomeRegistry = (Registry<Biome>)BuiltInRegistries.REGISTRY.get(Registries.BIOME.location());
        return biomeRegistry == null ? 0 : colorResolver.getColor(biomeRegistry.get(Biomes.PLAINS), pos.getX(), pos.getZ());
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
    public int getMinBuildHeight(){
        return -Integer.MAX_VALUE;
    }

    @Override
    public WorldBorder getWorldBorder(){
        return ClientUtils.getWorld().getWorldBorder();
    }

    @Override
    public List<Entity> getEntities(@Nullable Entity entity, AABB aABB, Predicate<? super Entity> predicate){
        return List.of();
    }

    @Override
    public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity,T> entityTypeTest, AABB aABB, Predicate<? super T> predicate){
        return List.of();
    }

    @Override
    public List<? extends Player> players(){
        return List.of();
    }

    @Override
    public List<VoxelShape> getEntityCollisions(@Nullable Entity entity, AABB aABB){
        return List.of();
    }

    @Override
    public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> predicate){
        return predicate.test(this.capture.getBlock(pos));
    }

    @Override
    public boolean isFluidAtPosition(BlockPos blockPos, Predicate<FluidState> predicate){
        return predicate.test(Fluids.EMPTY.defaultFluidState());
    }

    @Override
    public boolean setBlock(BlockPos blockPos, BlockState blockState, int i, int j){
        return false;
    }

    @Override
    public boolean removeBlock(BlockPos blockPos, boolean bl){
        return false;
    }

    @Override
    public boolean destroyBlock(BlockPos blockPos, boolean bl, @Nullable Entity entity, int i){
        return false;
    }
}
