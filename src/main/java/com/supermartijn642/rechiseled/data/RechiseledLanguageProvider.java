package com.supermartijn642.rechiseled.data;

import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.RechiseledBlockType;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

/**
 * Created 7/8/2021 by SuperMartijn642
 */
public class RechiseledLanguageProvider extends LanguageProvider {

    public RechiseledLanguageProvider(GatherDataEvent e){
        super(e.getGenerator(), "rechiseled", "en_us");
    }

    @Override
    protected void addTranslations(){
        this.add("itemGroup.rechiseled", "Rechiseled");

        this.add("rechiseled.tooltip.connecting", "Connecting");
        this.add("rechiseled.chiseling.preview.mode_0", "Preview 1 by 1");
        this.add("rechiseled.chiseling.preview.mode_1", "Preview 3 by 1");
        this.add("rechiseled.chiseling.preview.mode_2", "Preview 3 by 3");
        this.add("rechiseled.chiseling.connecting", "Connected textures: %s");
        this.add("rechiseled.chiseling.connecting.on", "On");
        this.add("rechiseled.chiseling.connecting.off", "Off");
        this.add("rechiseled.chiseling.chisel_all", "Chisel all");
        this.add("rechiseled.chiseling.select_block", "Select %s");
        this.add("rechiseled.chiseling.preview", "Block Preview");

        this.add("rechiseled.jei_category.title", "Chiseling");

        this.add(Rechiseled.chisel, "Chisel");

        for(RechiseledBlockType type : RechiseledBlockType.values()){
            if(type.hasCreatedRegularBlock())
                this.add(type.getRegularBlock(), type.englishTranslation);
            if(type.hasCreatedConnectingBlock())
                this.add(type.getConnectingBlock(), type.englishTranslation);
        }
    }
}
