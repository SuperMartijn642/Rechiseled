package com.supermartijn642.rechiseled.blocks;

import com.supermartijn642.core.block.BlockProperties;
import com.supermartijn642.rechiseled.blocks.impl.SlabBlock;

/**
 * Created 11/01/2026 by SuperMartijn642
 */
public class RechiseledSlabBlock extends SlabBlock {

    private final boolean connecting;

    public RechiseledSlabBlock(boolean connecting, BlockProperties properties){
        super(properties);
        this.connecting = connecting;
    }

    public boolean isConnecting(){
        return this.connecting;
    }
}
