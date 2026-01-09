package com.supermartijn642.rechiseled.api.chiseling;

import com.supermartijn642.core.TextComponents;
import net.minecraft.network.chat.Component;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public enum ChiselingBlockShape {

    BLOCK("rechiseled.chiseling.shape.block", 1),
    STAIRS("rechiseled.chiseling.shape.stairs", 1),
    SLAB("rechiseled.chiseling.shape.slab", 2);

    private final Component name;
    private final int conversionFactor;

    ChiselingBlockShape(String translationKey, int conversionFactor){
        this.name = TextComponents.translation(translationKey).get();
        this.conversionFactor = conversionFactor;
    }

    public Component translation(){
        return this.name;
    }

    public int conversionFactor(){
        return this.conversionFactor;
    }
}
