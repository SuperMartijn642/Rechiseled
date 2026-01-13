package com.supermartijn642.rechiseled.blocks;

import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.block.BlockProperties;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Created 22/12/2021 by SuperMartijn642
 */
public class RechiseledBlock extends BaseBlock {

    private final boolean connecting;

    public RechiseledBlock(boolean connecting, BlockProperties properties){
        super(false, properties);
        this.connecting = connecting;
    }

    public RechiseledBlock(boolean connecting, BlockBehaviour.Properties properties){
        super(false, properties);
        this.connecting = connecting;
    }

    public boolean isConnecting(){
        return this.connecting;
    }
}
