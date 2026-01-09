package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.AtlasSourceGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.screen.*;
import com.supermartijn642.rechiseled.screen.preview.PreviewMode;
import com.supermartijn642.rechiseled.screen.preview.PreviewModeButtonWidget;

/**
 * Created 08/07/2025 by SuperMartijn642
 */
public class RechiseledAtlasSourceGenerator extends AtlasSourceGenerator {

    public RechiseledAtlasSourceGenerator(ResourceCache cache){
        super(Rechiseled.MODID, cache);
    }

    @Override
    public void generate(){
        this.guiAtlas()
            .texture(BaseChiselingContainerScreen.BACKGROUND)
            .texture(ChiselAllWidget.CHISEL_TEXTURE)
            .texture(ChiselAllWidget.GREY_BUTTONS)
            .texture(ConnectingToggleWidget.ICON_CONNECTED_ON)
            .texture(ConnectingToggleWidget.ICON_CONNECTED_OFF)
            .texture(ConnectingToggleWidget.SMALL_GREY_BUTTONS)
            .texture(EntryButtonWidget.TEXTURE)
            .texture(PreviewModeButtonWidget.GREY_BUTTONS)
            .texture(ToggleRotationButton.TEXTURE)
            .texture(ShapeSelectionWidget.SMALL_GREY_BUTTONS)
            .texture(ShapeSelectionWidget.BLOCK_ICON)
            .texture(ShapeSelectionWidget.STAIRS_ICON)
            .texture(ShapeSelectionWidget.SLAB_ICON)
            .texture(FilterOptionsWidget.BUTTONS)
            .texture(FilterOptionsWidget.MARKER)
            .texture(FilterOptionsWidget.CHECKMARK);
        for(PreviewMode mode : PreviewMode.values())
            this.guiAtlas().texture(mode.icon(false)).texture(mode.icon(true));
    }
}
