package com.supermartijn642.rechiseled.api.registration;

import com.supermartijn642.rechiseled.api.blocks.RechiseledBlockBuilder;
import com.supermartijn642.rechiseled.api.blocks.RechiseledBlockType;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Allows for easy registration of new blocks for chiseling variants.
 * Data providers for models, block states, translations, tags, loot tables, and chiseling recipes will automatically be registered.
 * <p>
 * Created 26/04/2023 by SuperMartijn642
 */
public interface RechiseledRegistration {

    /**
     * Gets a {@link RechiseledRegistration} for the given modid.
     * If called multiple times with the same modid, the same instance will be returned.
     */
    static RechiseledRegistration get(String modid){
        return RechiseledRegistrationImpl.get(modid);
    }

    /**
     * Creates a new block builder.
     * @param identifier identifier used to register the block
     */
    RechiseledBlockBuilder block(String identifier);

    /**
     * Adds a new entry to a chiseling recipe.
     * @param recipe the chiseling recipe to add the entry to
     * @throws IllegalArgumentException when all item inputs are {@code null}
     * @see com.supermartijn642.rechiseled.api.BaseChiselingRecipes
     */
    void chiselingEntry(ResourceLocation recipe, Supplier<ItemLike> regularBlock, Supplier<ItemLike> regularStairs, Supplier<ItemLike> regularSlab, Supplier<ItemLike> connectingBlock, Supplier<ItemLike> connectingStairs, Supplier<ItemLike> connectingSlab);

    /**
     * Adds a new entry to a chiseling recipe with just the block shape.
     * @param recipe          the chiseling recipe to add the entry to
     * @param regularBlock    the regular block of the entry
     * @param connectingBlock the connecting block of the entry
     * @throws IllegalArgumentException when both item inputs are {@code null}
     * @see #chiselingEntry(ResourceLocation, Supplier, Supplier, Supplier, Supplier, Supplier, Supplier) method for adding an entry with other block shapes
     * @see com.supermartijn642.rechiseled.api.BaseChiselingRecipes
     */
    void chiselingEntry(ResourceLocation recipe, Supplier<ItemLike> regularBlock, Supplier<ItemLike> connectingBlock);

    /**
     * Creates an item group containing all the blocks created using this registration.
     * @param translation English translation for the item group's title
     * @throws IllegalStateException when an item group for this registration has already been created
     */
    CreativeModeTab itemGroup(Supplier<ItemLike> icon, String translation);

    /**
     * @return all block types created by this registration
     */
    Collection<RechiseledBlockType> getAllBlockTypes();

    /**
     * Registers all data providers.
     */
    void registerDataProviders();
}
