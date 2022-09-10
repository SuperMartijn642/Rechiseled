package com.supermartijn642.rechiseled;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.WidgetContainerScreen;
import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.rechiseled.model.RechiseledModelLoader;
import com.supermartijn642.rechiseled.screen.BaseChiselingContainerScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RechiseledClient {

    public static void register(){
        ClientRegistrationHandler handler = ClientRegistrationHandler.get("rechiseled");
        handler.registerContainerScreen(() -> Rechiseled.chisel_container, container -> new WidgetContainerScreen<>(new BaseChiselingContainerScreen<>(TextComponents.item(Rechiseled.chisel).get()), container, false){
            @Override
            public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks){
                this.widget.offsetLeft = (this.width - this.widget.width()) / 2;
                this.widget.offsetTop = (this.height - this.widget.height()) / 2;
                super.render(poseStack, mouseX, mouseY, partialTicks);
            }
        });
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelEvent.RegisterGeometryLoaders e){
        e.register("connecting_model", new RechiseledModelLoader());
    }
}
