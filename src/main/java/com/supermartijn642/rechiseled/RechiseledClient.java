package com.supermartijn642.rechiseled;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.WidgetContainerScreen;
import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.rechiseled.screen.BaseChiselingContainerScreen;
import net.fabricmc.api.ClientModInitializer;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledClient implements ClientModInitializer {

    @Override
    public void onInitializeClient(){
        ClientRegistrationHandler handler = ClientRegistrationHandler.get("rechiseled");
        handler.registerContainerScreen(() -> Rechiseled.chisel_container, container -> WidgetContainerScreen.of(new BaseChiselingContainerScreen<>(TextComponents.item(Rechiseled.chisel).get()), container, false));
    }
}
