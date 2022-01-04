package com.supermartijn642.rechiseled;

import com.supermartijn642.rechiseled.model.RechiseledModelLoader;
import com.supermartijn642.rechiseled.screen.BaseChiselingContainerScreen;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.ModelLoaderRegistry2;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RechiseledClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent e){
        ModelLoaderRegistry2.registerLoader(new ResourceLocation("rechiseled", "connecting_model"), new RechiseledModelLoader());
        ScreenManager.register(Rechiseled.chisel_container, (ScreenManager.IScreenFactory<ChiselContainer,BaseChiselingContainerScreen<ChiselContainer>>)((container, inventory, name) -> new BaseChiselingContainerScreen<>(container, name)));
    }
}
