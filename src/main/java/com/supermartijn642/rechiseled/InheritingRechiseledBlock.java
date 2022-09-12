package com.supermartijn642.rechiseled;

import com.supermartijn642.core.block.BlockProperties;
import net.minecraft.block.Block;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class InheritingRechiseledBlock extends RechiseledBlock {

    public InheritingRechiseledBlock(boolean connecting, Block parent){
        super(connecting, BlockProperties.copy(parent).lootTableFrom(null));
    }
}
