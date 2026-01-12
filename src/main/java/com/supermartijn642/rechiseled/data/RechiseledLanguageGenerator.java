package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.LanguageGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.rechiseled.Rechiseled;

/**
 * Created 7/8/2021 by SuperMartijn642
 */
public class RechiseledLanguageGenerator extends LanguageGenerator {

    public RechiseledLanguageGenerator(ResourceCache cache){
        super(Rechiseled.MODID, cache, "en_us");
    }

    @Override
    public void generate(){
        // Item group
        this.itemGroup(Rechiseled.GROUP, "Rechiseled");

        // Screen
        this.translation("rechiseled.tooltip.connecting", "Connecting");
        this.translation("rechiseled.chiseling.preview.mode_0", "Preview 1 by 1");
        this.translation("rechiseled.chiseling.preview.mode_1", "Preview 3 by 1");
        this.translation("rechiseled.chiseling.preview.mode_2", "Preview 3 by 3");
        this.translation("rechiseled.chiseling.connecting", "Connected textures: %s");
        this.translation("rechiseled.chiseling.connecting.on", "On");
        this.translation("rechiseled.chiseling.connecting.off", "Off");
        this.translation("rechiseled.chiseling.chisel_all", "Chisel all");
        this.translation("rechiseled.chiseling.chisel_all.shift", "%s for all shapes");
        this.translation("rechiseled.chiseling.chisel_all.items", "%s items");
        this.translation("rechiseled.chiseling.select_block", "Select %s");
        this.translation("rechiseled.chiseling.preview", "Block Preview");
        this.translation("rechiseled.chiseling.select_shape", "Shape: %s");
        this.translation("rechiseled.chiseling.filter", "Filter options");
        this.translation("rechiseled.chiseling.filter.clear", "Right-click to clear");
        this.translation("rechiseled.chiseling.filter.show_blocks", "show blocks");
        this.translation("rechiseled.chiseling.filter.show_stairs", "show stairs");
        this.translation("rechiseled.chiseling.filter.show_slabs", "show slabs");
        this.translation("rechiseled.chiseling.filter.show_non_connecting", "show non-connecting");
        this.translation("rechiseled.chiseling.scrollbar", "Scrollbar");
        this.translation("rechiseled.chiseling.entry.recipe", "Recipe: %s");
        this.translation("rechiseled.chiseling.entry.owner", "Plugin: %s");

        // Jei & rei
        this.translation("rechiseled.recipe_category.title", "Chiseling");
        this.translation("rechiseled.recipe_category.conversion_value", "Conversion value: %s");

        // Chisel item
        this.item(Rechiseled.chisel, "Chisel");

        // Chiseling shapes
        this.translation("rechiseled.chiseling.shape.block", "Block");
        this.translation("rechiseled.chiseling.shape.stairs", "Stairs");
        this.translation("rechiseled.chiseling.shape.slab", "Slab");
    }
}
