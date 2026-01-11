package com.supermartijn642.rechiseled.api.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

/**
 * Created 26/04/2023 by SuperMartijn642
 */
public interface RechiseledBlockType {

    boolean hasRegularVariant();

    Block getRegularBlock();

    BlockItem getRegularItem();

    boolean hasConnectingVariant();

    Block getConnectingBlock();

    BlockItem getConnectingItem();

    boolean hasStairs();

    boolean hasRegularStairs();

    Block getRegularStairs();

    BlockItem getRegularStairsItem();

    boolean hasConnectingStairs();

    Block getConnectingStairs();

    BlockItem getConnectingStairsItem();

    boolean hasSlabs();

    boolean hasRegularSlab();

    Block getRegularSlab();

    BlockItem getRegularSlabItem();

    boolean hasConnectingSlab();

    Block getConnectingSlab();

    BlockItem getConnectingSlabItem();
}
