package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.AtlasSourceGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.screen.*;

import java.util.Arrays;

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
            .texture(ConnectingToggleWidget.GREY_BUTTONS)
            .texture(EntryButtonWidget.TEXTURE)
            .texture(PreviewModeButtonWidget.GREY_BUTTONS)
            .texture(ToggleRotationButton.TEXTURE);
        Arrays.stream(PreviewModeButtonWidget.ICONS).flatMap(Arrays::stream).forEach(this.guiAtlas()::texture);
    }
}
