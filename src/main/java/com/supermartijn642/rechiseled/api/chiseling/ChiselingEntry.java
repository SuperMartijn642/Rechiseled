package com.supermartijn642.rechiseled.api.chiseling;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

/**
 * A chiseling item consists of a regular and a connecting item for each {@link ChiselingBlockShape}.
 * The regular item is typically used for the variant <b>without</b> connecting textures and the connecting item is typically used for the variant <b>with</b> connecting textures.
 * <p>
 * Regular and connecting items may be absent for any shape, however the entry always has at least one item.
 * To get a guaranteed item from the entry, use {@link ChiselingEntry#getAnyItem()}.
 * <p>
 * Created 07/01/2026 by SuperMartijn642
 */
public interface ChiselingEntry {

    /**
     * The identifier of the plugin that added this entry.
     */
    ResourceLocation owner();

    /**
     * The identifier of the recipe this entry came from.
     */
    @Nullable ResourceLocation recipe();

    /**
     * Whether this entry has a regular or connecting item for the given shape.
     */
    boolean hasShape(ChiselingBlockShape shape);

    /**
     * Whether the entry has a regular item for the given shape.
     */
    boolean hasRegularItem(ChiselingBlockShape shape);

    /**
     * Whether the entry has a connecting item for the given shape.
     */
    boolean hasConnectingItem(ChiselingBlockShape shape);

    @Nullable
    ItemWithWorth getRegularItem(ChiselingBlockShape shape);

    @Nullable
    ItemWithWorth getConnectingItem(ChiselingBlockShape shape);

    /**
     * Returns any item for the given shape.
     */
    @Nullable
    ItemWithWorth getAnyItem(ChiselingBlockShape shape);

    /**
     * Returns any item from this entry. Result is never {@code null}.
     */
    ItemWithWorth getAnyItem();

    /**
     * Returns any regular item from this entry.
     */
    @Nullable
    ItemWithWorth getAnyRegularItem();

    /**
     * Returns any connecting item from this entry.
     */
    @Nullable
    ItemWithWorth getAnyConnectingItem();

    /**
     * Whether this entry contains the given item.
     */
    boolean contains(ItemLike item);
}
