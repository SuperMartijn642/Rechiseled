package com.supermartijn642.rechiseled.screen.preview;

import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.resources.ResourceLocation;

/**
 * Created 08/01/2026 by SuperMartijn642
 */
public enum PreviewMode {

    SINGLE(Rechiseled.identifier("screen/icon_1x1_grey"), Rechiseled.identifier("screen/icon_1x1")),
    ROW(Rechiseled.identifier("screen/icon_3x1_grey"), Rechiseled.identifier("screen/icon_3x1")),
    PANEL(Rechiseled.identifier("screen/icon_3x3_grey"), Rechiseled.identifier("screen/icon_3x3"));

    private final ResourceLocation selectedIcon, unselectedIcon;

    PreviewMode(ResourceLocation selectedIcon, ResourceLocation unselectedIcon){
        this.selectedIcon = selectedIcon;
        this.unselectedIcon = unselectedIcon;
    }

    public ResourceLocation icon(boolean selected){
        return selected ? this.selectedIcon : this.unselectedIcon;
    }
}
