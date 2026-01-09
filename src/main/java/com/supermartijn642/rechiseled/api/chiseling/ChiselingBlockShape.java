package com.supermartijn642.rechiseled.api.chiseling;

import com.supermartijn642.core.TextComponents;
import net.minecraft.network.chat.Component;

/**
 * Created 07/01/2026 by SuperMartijn642
 */
public enum ChiselingBlockShape {

    BLOCK("rechiseled.chiseling.shape.block"),
    STAIRS("rechiseled.chiseling.shape.stairs"),
    SLAB("rechiseled.chiseling.shape.slab");

    private final Component name;

    ChiselingBlockShape(String translationKey){
        this.name = TextComponents.translation(translationKey).get();
    }

    public Component translation(){
        return this.name;
    }
}
