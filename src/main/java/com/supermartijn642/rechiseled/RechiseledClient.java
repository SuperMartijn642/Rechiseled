package com.supermartijn642.rechiseled;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.WidgetContainerScreen;
import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.rechiseled.model.RechiseledModelLoader;
import com.supermartijn642.rechiseled.screen.BaseChiselingContainer;
import com.supermartijn642.rechiseled.screen.BaseChiselingContainerScreen;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
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
        ClientRegistrationHandler handler = ClientRegistrationHandler.get("rechiseled");
        handler.registerContainerScreen(() -> Rechiseled.chisel_container, container -> new WidgetContainerScreen<BaseChiselingContainerScreen<BaseChiselingContainer>,ChiselContainer>(new BaseChiselingContainerScreen<>(TextComponents.item(Rechiseled.chisel).get()), container, false) {
            @Override
            public void render(int mouseX, int mouseY, float partialTicks){
                this.widget.offsetLeft = (this.width - this.widget.width()) / 2;
                this.widget.offsetTop = (this.height - this.widget.height()) / 2;
                super.render(mouseX, mouseY, partialTicks);
            }
        });
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent e){
        ModelLoaderRegistry.registerLoader(new ResourceLocation("rechiseled", "connecting_model"), new RechiseledModelLoader());
    }
}
