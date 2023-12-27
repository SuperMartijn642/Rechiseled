package com.supermartijn642.rechiseled;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.WidgetContainerScreen;
import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.core.render.CustomRendererBakedModelWrapper;
import com.supermartijn642.rechiseled.screen.BaseChiselingContainerScreen;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledClient {

    public static void register(){
        ClientRegistrationHandler handler = ClientRegistrationHandler.get("rechiseled");
        handler.registerContainerScreen(() -> Rechiseled.chisel_container, container -> WidgetContainerScreen.of(new BaseChiselingContainerScreen<>(TextComponents.item(Rechiseled.chisel).get()), container, false));
        handler.registerItemModelOverwrite(() -> Rechiseled.chisel, CustomRendererBakedModelWrapper::wrap);
        handler.registerCustomItemRenderer(() -> Rechiseled.chisel, ChiselItemRenderer::new);
    }
}
