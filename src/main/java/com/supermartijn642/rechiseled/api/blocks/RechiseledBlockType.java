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

    boolean hasStairs();

    boolean hasRegularStairs();

    Block getRegularStairs();

    ItemBlock getRegularStairsItem();

    boolean hasConnectingStairs();

    Block getConnectingStairs();

    ItemBlock getConnectingStairsItem();

    boolean hasSlabs();

    boolean hasRegularSlab();

    Block getRegularSlab();

    ItemBlock getRegularSlabItem();

    boolean hasConnectingSlab();

    Block getConnectingSlab();

    ItemBlock getConnectingSlabItem();
}
