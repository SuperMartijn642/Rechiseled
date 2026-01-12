package com.supermartijn642.rechiseled.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.ScheduledTick;

/**
 * Created 12/01/2026 by SuperMartijn642
 */
public class EmptyLevelTickAccess implements LevelTickAccess<Object> {

    public static final EmptyLevelTickAccess INSTANCE = new EmptyLevelTickAccess();

    @Override
    public boolean willTickThisTick(BlockPos pos, Object object){
        return false;
    }

    @Override
    public boolean hasScheduledTick(BlockPos pos, Object object){
        return false;
    }

    @Override
    public void schedule(ScheduledTick<Object> scheduledTick){
    }

    @Override
    public int count(){
        return 0;
    }
}
