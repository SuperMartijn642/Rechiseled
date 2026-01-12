package com.supermartijn642.rechiseled.util;

import net.minecraft.world.IBlockReader;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.lighting.WorldLightManager;

import javax.annotation.Nullable;

/**
 * Created 17/01/2026 by SuperMartijn642
 */
public class EmptyChunkSource extends AbstractChunkProvider {

    public static final AbstractChunkProvider INSTANCE = new EmptyChunkSource();

    @Override
    public @Nullable Chunk getChunk(int i, int j, ChunkStatus chunkStatus, boolean bl){
        return null;
    }


    @Override
    public String gatherStats(){
        return "";
    }

    @Override
    public WorldLightManager getLightEngine(){
        return null;
    }

    @Override
    public IBlockReader getLevel(){
        return null;
    }
}
