package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.ModelGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.rechiseled.Rechiseled;

/**
 * Created 7/8/2021 by SuperMartijn642
 */
public class RechiseledItemModelGenerator extends ModelGenerator {

    public RechiseledItemModelGenerator(ResourceCache cache){
        super(Rechiseled.MODID, cache);
    }

    @Override
    public void generate(){
        // Chisel
        this.itemHandheld(Rechiseled.chisel, Rechiseled.identifier("item/chisel"));
    }

    @Override
    public String getName(){
        return this.modName + " Item Model Generator";
    }
}
