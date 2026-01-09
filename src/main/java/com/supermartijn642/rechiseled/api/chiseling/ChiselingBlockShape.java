package com.supermartijn642.rechiseled.api.chiseling;

import com.supermartijn642.core.TextComponents;
import net.minecraft.util.text.ITextComponent;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public enum ChiselingBlockShape {

    BLOCK("rechiseled.chiseling.shape.block", 1),
    STAIRS("rechiseled.chiseling.shape.stairs", 1),
    SLAB("rechiseled.chiseling.shape.slab", 2);

    private final ITextComponent name;
    private final int conversionFactor;

    ChiselingBlockShape(String translationKey, int conversionFactor){
        this.name = TextComponents.translation(translationKey).get();
        this.conversionFactor = conversionFactor;
    }

    public ITextComponent translation(){
        return this.name;
    }

    public int conversionFactor(){
        return this.conversionFactor;
    }
}
