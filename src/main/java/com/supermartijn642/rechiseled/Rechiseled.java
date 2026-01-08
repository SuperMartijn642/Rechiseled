package com.supermartijn642.rechiseled;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.gui.BaseContainerType;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.network.PacketChannel;
import com.supermartijn642.core.network.PacketDirection;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import com.supermartijn642.core.registry.RegistryEntryAcceptor;
import com.supermartijn642.rechiseled.api.blocks.RechiseledBlockType;
import com.supermartijn642.rechiseled.api.registration.RechiseledRegistration;
import com.supermartijn642.rechiseled.chiseling.PacketUpdateChiselingRecipes;
import com.supermartijn642.rechiseled.data.RechiseledItemModelGenerator;
import com.supermartijn642.rechiseled.data.RechiseledLanguageGenerator;
import com.supermartijn642.rechiseled.data.RechiseledRecipeGenerator;
import com.supermartijn642.rechiseled.data.RechiseledTextureProvider;
import com.supermartijn642.rechiseled.packet.PacketChiselAll;
import com.supermartijn642.rechiseled.packet.PacketSelectEntry;
import com.supermartijn642.rechiseled.packet.PacketToggleConnecting;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod(modid = Rechiseled.MODID, name = "@mod_name@", version = "@mod_version@", dependencies = "required-after:supermartijn642corelib@@core_library_dependency@;required-after-client:fusion@@fusion_dependency@")
public class Rechiseled {

    public static final String MODID = "rechiseled";

    public static ResourceLocation identifier(String path){
        return new ResourceLocation(MODID, path);
    }

    public static final RechiseledRegistration REGISTRATION = RechiseledRegistration.get(MODID);
    public static final PacketChannel CHANNEL = PacketChannel.create(MODID);
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @RegistryEntryAcceptor(namespace = MODID, identifier = "chisel", registry = RegistryEntryAcceptor.Registry.ITEMS)
    public static ChiselItem chisel;

    @RegistryEntryAcceptor(namespace = MODID, identifier = "chisel_container", registry = RegistryEntryAcceptor.Registry.MENU_TYPES)
    public static BaseContainerType<ChiselContainer> chisel_container;

    public static final CreativeItemGroup GROUP = CreativeItemGroup.create(MODID, () -> chisel)
        .filler(stackConsumer -> {
            List<Item> items = new LinkedList<>();
            items.add(chisel);
            for(RechiseledBlockType type : REGISTRATION.getAllBlockTypes()){
                items.add(type.getRegularItem());
                items.add(type.getConnectingItem());
            }
            items.stream().filter(Objects::nonNull).map(ItemStack::new).forEach(stackConsumer);
        });

    public Rechiseled(){
        CHANNEL.registerMessage(PacketSelectEntry.class, PacketSelectEntry::new, PacketDirection.CLIENT_TO_SERVER, true);
        CHANNEL.registerMessage(PacketToggleConnecting.class, PacketToggleConnecting::new, PacketDirection.CLIENT_TO_SERVER, true);
        CHANNEL.registerMessage(PacketChiselAll.class, PacketChiselAll::new, PacketDirection.CLIENT_TO_SERVER, true);
        CHANNEL.registerMessage(PacketUpdateChiselingRecipes.class, PacketUpdateChiselingRecipes::new, PacketDirection.SERVER_TO_CLIENT, false);

        register();
        if(CommonUtils.getEnvironmentSide().isClient())
            RechiseledClient.register();
        registerGenerators();
        RechiseledBlocks.init();
        RechiseledChiselingRecipes.init();
    }

    public static void register(){
        RegistrationHandler handler = RegistrationHandler.get(MODID);
        // Chisel item
        handler.registerItem("chisel", ChiselItem::new);
        // Container type
        handler.registerMenuType("chisel_container", BaseContainerType.create((container, buffer) -> buffer.writeBoolean(container.hand == EnumHand.MAIN_HAND), ((player, buffer) -> new ChiselContainer(player, buffer.readBoolean() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND))));
    }

    private static void registerGenerators(){
        GeneratorRegistrationHandler handler = GeneratorRegistrationHandler.get(MODID);
        handler.addGenerator(RechiseledTextureProvider::new);
        handler.addGenerator(RechiseledItemModelGenerator::new);
        handler.addGenerator(RechiseledLanguageGenerator::new);
        handler.addGenerator(RechiseledRecipeGenerator::new);
        REGISTRATION.registerDataProviders();
    }
}
