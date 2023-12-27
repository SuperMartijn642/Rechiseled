package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.RecipeGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.rechiseled.Rechiseled;
import net.neoforged.neoforge.common.Tags;

/**
 * Created 26/12/2021 by SuperMartijn642
 */
public class RechiseledRecipeGenerator extends RecipeGenerator {

    public RechiseledRecipeGenerator(ResourceCache cache){
        super("rechiseled", cache);
    }

    @Override
    public void generate(){
        this.shaped(Rechiseled.chisel)
            .pattern(" A")
            .pattern("B ")
            .input('A', Tags.Items.INGOTS_IRON)
            .input('B', Tags.Items.RODS_WOODEN)
            .unlockedBy(Tags.Items.INGOTS_IRON);
    }
}
