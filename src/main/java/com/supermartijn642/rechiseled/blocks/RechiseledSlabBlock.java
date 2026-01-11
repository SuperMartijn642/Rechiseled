package com.supermartijn642.rechiseled.blocks;

import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;

/**
 * Created 11/01/2026 by SuperMartijn642
 */
public class RechiseledSlabBlock extends SlabBlock {

    public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;

    private final boolean connecting;

    public RechiseledSlabBlock(boolean connecting, Properties properties){
        super(properties);
        this.connecting = connecting;
    }

    public boolean isConnecting(){
        return this.connecting;
    }
}
