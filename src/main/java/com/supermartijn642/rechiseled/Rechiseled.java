package com.supermartijn642.rechiseled;

import com.supermartijn642.core.gui.BaseContainerType;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.network.PacketChannel;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import com.supermartijn642.core.registry.RegistryEntryAcceptor;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeLoader;
import com.supermartijn642.rechiseled.chiseling.PacketChiselingRecipes;
import com.supermartijn642.rechiseled.data.*;
import com.supermartijn642.rechiseled.packet.PacketChiselAll;
import com.supermartijn642.rechiseled.packet.PacketSelectEntry;
import com.supermartijn642.rechiseled.packet.PacketToggleConnecting;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedList;
import java.util.List;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class Rechiseled implements ModInitializer {

    public static final PacketChannel CHANNEL = PacketChannel.create("rechiseled");

    @RegistryEntryAcceptor(namespace = "rechiseled", identifier = "chisel", registry = RegistryEntryAcceptor.Registry.ITEMS)
    public static ChiselItem chisel;

    @RegistryEntryAcceptor(namespace = "rechiseled", identifier = "chisel_container", registry = RegistryEntryAcceptor.Registry.MENU_TYPES)
    public static BaseContainerType<ChiselContainer> chisel_container;

    public static final CreativeItemGroup GROUP = CreativeItemGroup.create("rechiseled", () -> chisel)
        .filler(stackConsumer -> {
            List<Item> items = new LinkedList<>();
            items.add(chisel);
            for(RechiseledBlockType type : RechiseledBlockType.values()){
                if(type.hasCreatedRegularBlock())
                    items.add(type.getRegularItem());
                if(type.hasCreatedConnectingBlock())
                    items.add(type.getConnectingItem());
            }
            items.stream().map(ItemStack::new).forEach(stackConsumer);
        });

    @Override
    public void onInitialize(){
        CHANNEL.registerMessage(PacketSelectEntry.class, PacketSelectEntry::new, true);
        CHANNEL.registerMessage(PacketToggleConnecting.class, PacketToggleConnecting::new, true);
        CHANNEL.registerMessage(PacketChiselAll.class, PacketChiselAll::new, true);
        CHANNEL.registerMessage(PacketChiselingRecipes.class, PacketChiselingRecipes::new, true);

        ChiselingRecipeLoader.addListeners();

        register();
        registerGenerators();
    }

    public static void register(){
        RegistrationHandler handler = RegistrationHandler.get("rechiseled");
        // Blocks
        handler.registerBlockCallback(helper -> {
            for(RechiseledBlockType type : RechiseledBlockType.values()){
                type.createBlocks();
                if(type.hasCreatedRegularBlock())
                    helper.register(type.regularRegistryName, type.getRegularBlock());
                if(type.hasCreatedConnectingBlock())
                    helper.register(type.connectingRegistryName, type.getConnectingBlock());
            }
        });
        // Items
        handler.registerItem("chisel", ChiselItem::new);
        handler.registerItemCallback(helper -> {
            for(RechiseledBlockType type : RechiseledBlockType.values()){
                type.createItems();
                if(type.hasCreatedRegularBlock())
                    helper.register(type.regularRegistryName, type.getRegularItem());
                if(type.hasCreatedConnectingBlock())
                    helper.register(type.connectingRegistryName, type.getConnectingItem());
            }
        });
        // Container type
        handler.registerMenuType("chisel_container", BaseContainerType.create((container, buffer) -> buffer.writeBoolean(container.hand == InteractionHand.MAIN_HAND), ((player, buffer) -> new ChiselContainer(player, buffer.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND))));
    }

    private static void registerGenerators(){
        GeneratorRegistrationHandler handler = GeneratorRegistrationHandler.get("rechiseled");
        handler.addProvider(RechiseledTextureProvider::new);
        handler.addProvider(RechiseledChiselingRecipeProvider::new);
        handler.addProvider(RechiseledConnectingBlockModelProvider::new);
        handler.addGenerator(RechiseledBlockStateGenerator::new);
        handler.addGenerator(RechiseledBlockTagsGenerator::new);
        handler.addGenerator(RechiseledItemModelGenerator::new);
        handler.addGenerator(RechiseledLanguageGenerator::new);
        handler.addGenerator(RechiseledLootTableGenerator::new);
        handler.addGenerator(RechiseledRecipeGenerator::new);
        handler.addGenerator(RechiseledAtlasSourceGenerator::new);
    }
}
