package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.ItemInfoGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.rechiseled.ChiselItemRenderer;
import com.supermartijn642.rechiseled.Rechiseled;

/**
 * Created 04/04/2025 by SuperMartijn642
 */
public class RechiseledItemInfoGenerator extends ItemInfoGenerator {

    public RechiseledItemInfoGenerator(ResourceCache cache){
        super("rechiseled", cache);
    }

    @Override
    public void generate(){
        this.info(Rechiseled.chisel).model(
            this.compositeModel()
                .addModel(this.model("item/chisel"))
                .addModel(new ChiselItemRenderer())
        );
    }
}
