package com.supermartijn642.rechiseled;

import com.supermartijn642.core.gui.BaseContainerType;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.network.PacketChannel;
import com.supermartijn642.core.network.PacketDirection;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import com.supermartijn642.core.registry.RegistryEntryAcceptor;
import com.supermartijn642.rechiseled.api.blocks.RechiseledBlockType;
import com.supermartijn642.rechiseled.api.registration.RechiseledRegistration;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeDatapackPlugin;
import com.supermartijn642.rechiseled.chiseling.PacketUpdateChiselingRecipes;
import com.supermartijn642.rechiseled.data.RechiseledItemModelGenerator;
import com.supermartijn642.rechiseled.data.RechiseledLanguageGenerator;
import com.supermartijn642.rechiseled.data.RechiseledRecipeGenerator;
import com.supermartijn642.rechiseled.data.RechiseledTextureProvider;
import com.supermartijn642.rechiseled.packet.PacketChiselAll;
import com.supermartijn642.rechiseled.packet.PacketSelectEntry;
import com.supermartijn642.rechiseled.packet.PacketToggleConnecting;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class Rechiseled implements ModInitializer {

    public static final String MODID = "rechiseled";

    public static ResourceLocation identifier(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static final RechiseledRegistration REGISTRATION = RechiseledRegistration.get(MODID);
    public static final PacketChannel CHANNEL = PacketChannel.create(MODID);
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

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

    @Override
    public void onInitialize(){
        CHANNEL.registerMessage(PacketSelectEntry.class, PacketSelectEntry::new, PacketDirection.CLIENT_TO_SERVER, true);
        CHANNEL.registerMessage(PacketToggleConnecting.class, PacketToggleConnecting::new, PacketDirection.CLIENT_TO_SERVER, true);
        CHANNEL.registerMessage(PacketChiselAll.class, PacketChiselAll::new, PacketDirection.CLIENT_TO_SERVER, true);
        CHANNEL.registerMessage(PacketUpdateChiselingRecipes.class, PacketUpdateChiselingRecipes::new, PacketDirection.SERVER_TO_CLIENT, true);

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(ChiselingRecipeDatapackPlugin.INSTANCE);

        register();
        registerGenerators();
        RechiseledBlocks.init();
        RechiseledChiselingRecipes.init();
    }

    public static void register(){
        RegistrationHandler handler = RegistrationHandler.get(MODID);
        // Chisel item
        handler.registerItem("chisel", ChiselItem::new);
        // Container type
        handler.registerMenuType("chisel_container", BaseContainerType.create((container, buffer) -> buffer.writeBoolean(container.hand == InteractionHand.MAIN_HAND), ((player, buffer) -> new ChiselContainer(player, buffer.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND))));
        // Chisel item held stack data component
        handler.registerDataComponentType("chisel_held_stack", ChiselItem.HELD_STACK);
    }

    private static void registerGenerators(){
        GeneratorRegistrationHandler handler = GeneratorRegistrationHandler.get(MODID);
        handler.addProvider(RechiseledTextureProvider::new);
        handler.addGenerator(RechiseledItemModelGenerator::new);
        handler.addGenerator(RechiseledLanguageGenerator::new);
        handler.addGenerator(RechiseledRecipeGenerator::new);
        REGISTRATION.registerDataProviders();
    }
}
