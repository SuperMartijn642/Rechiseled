package com.supermartijn642.rechiseled.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraft.world.ticks.TickPriority;

/**
 * Created 12/01/2026 by SuperMartijn642
 */
public class EmptyScheduledTickAccess implements ScheduledTickAccess {

    public static final EmptyScheduledTickAccess INSTANCE = new EmptyScheduledTickAccess();
    private static final LevelTickAccess<?> dummyTickAccess = new LevelTickAccess<>() {
        @Override
        public boolean willTickThisTick(BlockPos pos, Object object){
            return false;
        }

        @Override
        public void schedule(ScheduledTick<Object> scheduledTick){
        }

        @Override
        public boolean hasScheduledTick(BlockPos pos, Object object){
            return false;
        }

        @Override
        public int count(){
            return 0;
        }
    };

    @Override
    public <T> ScheduledTick<T> createTick(BlockPos blockPos, T object, int i, TickPriority tickPriority){
        return null;
    }

    @Override
    public <T> ScheduledTick<T> createTick(BlockPos blockPos, T object, int i){
        return null;
    }

    @Override
    public LevelTickAccess<Block> getBlockTicks(){
        //noinspection unchecked
        return (LevelTickAccess<Block>)dummyTickAccess;
    }

    @Override
    public LevelTickAccess<Fluid> getFluidTicks(){
        //noinspection unchecked
        return (LevelTickAccess<Fluid>)dummyTickAccess;
    }
}
