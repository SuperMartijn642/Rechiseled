package com.supermartijn642.rechiseled.util;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;

/**
 * Created 17/01/2026 by SuperMartijn642
 */
public class EmptyChunkSource extends ChunkSource {

    public static final ChunkSource INSTANCE = new EmptyChunkSource();

    @Override
    public @Nullable ChunkAccess getChunk(int i, int j, ChunkStatus chunkStatus, boolean bl){
        return null;
    }

    @Override
    public void tick(BooleanSupplier booleanSupplier, boolean bl){
    }

    @Override
    public String gatherStats(){
        return "";
    }

    @Override
    public int getLoadedChunksCount(){
        return 0;
    }

    @Override
    public LevelLightEngine getLightEngine(){
        return null;
    }

    @Override
    public BlockGetter getLevel(){
        return null;
    }
}
