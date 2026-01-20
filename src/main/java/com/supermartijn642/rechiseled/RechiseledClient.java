package com.supermartijn642.rechiseled;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.WidgetContainerScreen;
import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.core.render.CustomRendererBakedModelWrapper;
import com.supermartijn642.rechiseled.screen.BaseChiselingContainer;
import com.supermartijn642.rechiseled.screen.BaseChiselingContainerScreen;
import com.supermartijn642.rechiseled.screen.ChiselContainer;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledClient {

    public static void register(){
        ClientRegistrationHandler handler = ClientRegistrationHandler.get(Rechiseled.MODID);
        handler.registerContainerScreen(() -> Rechiseled.chisel_container, container -> {
            BaseChiselingContainerScreen<BaseChiselingContainer> widget = new BaseChiselingContainerScreen<>(TextComponents.item(Rechiseled.chisel).get());
            WidgetContainerScreen<?,ChiselContainer> screen = WidgetContainerScreen.of(widget, container, false);
            widget.setScreen(screen);
            return screen;
        });
        handler.registerItemModelOverwrite(() -> Rechiseled.chisel, CustomRendererBakedModelWrapper::wrap);
        handler.registerCustomItemRenderer(() -> Rechiseled.chisel, ChiselItemRenderer.INSTANCE);
    }
}
