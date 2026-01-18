package com.supermartijn642.rechiseled;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.BaseContainer;
import com.supermartijn642.core.gui.WidgetContainerScreen;
import com.supermartijn642.core.gui.widget.Widget;
import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.core.render.CustomRendererBakedModelWrapper;
import com.supermartijn642.rechiseled.model.RechiseledModelLoader;
import com.supermartijn642.rechiseled.screen.BaseChiselingContainer;
import com.supermartijn642.rechiseled.screen.BaseChiselingContainerScreen;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RechiseledClient {

    public static void register(){
        ChiselItemHighlighter.registerListeners();

        ClientRegistrationHandler handler = ClientRegistrationHandler.get(Rechiseled.MODID);
        handler.registerContainerScreen(() -> Rechiseled.chisel_container, container -> {
            BaseChiselingContainerScreen<BaseChiselingContainer> widget = new BaseChiselingContainerScreen<>(TextComponents.item(Rechiseled.chisel).get());
            WidgetContainerScreen<?,ChiselContainer> screen = WidgetContainerScreen.of(widget, container, false);
            widget.setScreen(screen);
            return screen;
        });
        handler.registerItemModelOverwrite(() -> Rechiseled.chisel, CustomRendererBakedModelWrapper::wrap);
        handler.registerCustomItemRenderer(() -> Rechiseled.chisel, ChiselItemRenderer::new);
    }

    /**
     * TODO: remove in 1.2
     */
    @SubscribeEvent
    @Deprecated
    public static void onModelRegistry(ModelRegistryEvent e){
        ModelLoaderRegistry.registerLoader(new ResourceLocation("rechiseled", "connecting_model"), new RechiseledModelLoader());
    }
}
