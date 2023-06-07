package com.supermartijn642.rechiseled.api.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

/**
 * Created 26/04/2023 by SuperMartijn642
 */
public interface RechiseledBlockType {

    boolean hasRegularVariant();

    Block getRegularBlock();

    ItemBlock getRegularItem();

    boolean hasConnectingVariant();

    Block getConnectingBlock();

    ItemBlock getConnectingItem();
}
