package com.supermartijn642.rechiseled;

import net.minecraft.block.Block;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class InheritingRechiseledBlock extends RechiseledBlock {

    public InheritingRechiseledBlock(String registryName, boolean connecting, Block parent){
        super(registryName, connecting, Properties.copy(parent));
    }
}
