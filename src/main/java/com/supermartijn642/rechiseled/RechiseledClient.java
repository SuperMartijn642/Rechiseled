package com.supermartijn642.rechiseled;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.WidgetContainerScreen;
import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.rechiseled.model.RechiseledModelLoader;
import com.supermartijn642.rechiseled.screen.BaseChiselingContainerScreen;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class RechiseledClient {

    public static void register(){
        ClientRegistrationHandler handler = ClientRegistrationHandler.get("rechiseled");
        handler.registerContainerScreen(() -> Rechiseled.chisel_container, container -> new WidgetContainerScreen<>(new BaseChiselingContainerScreen<>(TextComponents.item(Rechiseled.chisel).get()), container, false));
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent e){
        ModelLoaderRegistry.registerLoader(new RechiseledModelLoader());
    }
}
