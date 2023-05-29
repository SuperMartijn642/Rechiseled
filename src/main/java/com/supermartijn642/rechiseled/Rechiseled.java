package com.supermartijn642.rechiseled;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.gui.BaseContainerType;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.network.PacketChannel;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import com.supermartijn642.core.registry.RegistryEntryAcceptor;
import com.supermartijn642.rechiseled.api.blocks.RechiseledBlockType;
import com.supermartijn642.rechiseled.api.registration.RechiseledRegistration;
import com.supermartijn642.rechiseled.chiseling.PacketChiselingRecipes;
import com.supermartijn642.rechiseled.data.RechiseledItemModelGenerator;
import com.supermartijn642.rechiseled.data.RechiseledLanguageGenerator;
import com.supermartijn642.rechiseled.data.RechiseledRecipeGenerator;
import com.supermartijn642.rechiseled.data.RechiseledTextureProvider;
import com.supermartijn642.rechiseled.packet.PacketChiselAll;
import com.supermartijn642.rechiseled.packet.PacketSelectEntry;
import com.supermartijn642.rechiseled.packet.PacketToggleConnecting;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("rechiseled")
public class Rechiseled {

    public static final RechiseledRegistration REGISTRATION = RechiseledRegistration.get("rechiseled");
    public static final PacketChannel CHANNEL = PacketChannel.create("rechiseled");

    @RegistryEntryAcceptor(namespace = "rechiseled", identifier = "chisel", registry = RegistryEntryAcceptor.Registry.ITEMS)
    public static ChiselItem chisel;

    @RegistryEntryAcceptor(namespace = "rechiseled", identifier = "chisel_container", registry = RegistryEntryAcceptor.Registry.MENU_TYPES)
    public static BaseContainerType<ChiselContainer> chisel_container;

    public static final CreativeItemGroup GROUP = CreativeItemGroup.create("rechiseled", () -> chisel)
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
        CHANNEL.registerMessage(PacketSelectEntry.class, PacketSelectEntry::new, true);
        CHANNEL.registerMessage(PacketToggleConnecting.class, PacketToggleConnecting::new, true);
        CHANNEL.registerMessage(PacketChiselAll.class, PacketChiselAll::new, true);
        CHANNEL.registerMessage(PacketChiselingRecipes.class, PacketChiselingRecipes::new, true);

        register();
        if(CommonUtils.getEnvironmentSide().isClient())
            RechiseledClient.register();
        registerGenerators();
        RechiseledBlocks.init();
        RechiseledChiselingRecipes.init();
    }

    public static void register(){
        RegistrationHandler handler = RegistrationHandler.get("rechiseled");
        // Chisel item
        handler.registerItem("chisel", ChiselItem::new);
        // Container type
        handler.registerMenuType("chisel_container", BaseContainerType.create((container, buffer) -> buffer.writeBoolean(container.hand == InteractionHand.MAIN_HAND), ((player, buffer) -> new ChiselContainer(player, buffer.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND))));
    }

    private static void registerGenerators(){
        GeneratorRegistrationHandler handler = GeneratorRegistrationHandler.get("rechiseled");
        handler.addProvider(RechiseledTextureProvider::new);
        handler.addGenerator(RechiseledItemModelGenerator::new);
        handler.addGenerator(RechiseledLanguageGenerator::new);
        handler.addGenerator(RechiseledRecipeGenerator::new);
        REGISTRATION.registerDataProviders();
    }
}
