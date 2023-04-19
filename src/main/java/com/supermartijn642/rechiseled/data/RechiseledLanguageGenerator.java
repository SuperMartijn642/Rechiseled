package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.LanguageGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created 7/8/2021 by SuperMartijn642
 */
public class RechiseledLanguageGenerator extends LanguageGenerator {

    private static final List<Pair<Supplier<Block>,String>> BLOCKS = new ArrayList<>();

    public static void addBlockTranslation(Supplier<Block> blockSupplier, String translation){
        BLOCKS.add(Pair.of(blockSupplier, translation));
    }

    public RechiseledLanguageGenerator(ResourceCache cache){
        super("rechiseled", cache, "en_us");
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
        this.translation("rechiseled.chiseling.select_block", "Select %s");
        this.translation("rechiseled.chiseling.preview", "Block Preview");

        // Jei
        this.translation("rechiseled.jei_category.title", "Chiseling");

        // Chisel item
        this.item(Rechiseled.chisel, "Chisel");

        // All the blocks
        for(Pair<Supplier<Block>,String> pair : BLOCKS)
            this.block(pair.left().get(), pair.right());
    }
}
