package com.supermartijn642.rechiseled.blocks.impl;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * Created 20/01/2026 by SuperMartijn642
 */
public enum SlabType implements IStringSerializable {

    BOTTOM, TOP, DOUBLE;

    private final String name = this.name().toLowerCase(Locale.ROOT);

    @Override
    public String getName(){
        return this.name;
    }
}
