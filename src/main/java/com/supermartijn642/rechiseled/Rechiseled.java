package com.supermartijn642.rechiseled;

import com.supermartijn642.core.network.PacketChannel;
import com.supermartijn642.rechiseled.chiseling.PacketChiselingRecipes;
import com.supermartijn642.rechiseled.data.*;
import com.supermartijn642.rechiseled.packet.PacketChiselAll;
import com.supermartijn642.rechiseled.packet.PacketSelectEntry;
import com.supermartijn642.rechiseled.packet.PacketToggleConnecting;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("rechiseled")
public class Rechiseled {

    public static final PacketChannel CHANNEL = PacketChannel.create("rechiseled");

    public static final CreativeModeTab GROUP = new CreativeModeTab("rechiseled") {
        @Override
        public ItemStack makeIcon(){
            return new ItemStack(chisel);
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> stacks){
            List<Item> items = new LinkedList<>();
            items.add(chisel);
            for(RechiseledBlockType type : RechiseledBlockType.values()){
                if(type.hasCreatedRegularBlock())
                    items.add(type.getRegularItem());
                if(type.hasCreatedConnectingBlock())
                    items.add(type.getConnectingItem());
            }
            items.stream().map(ItemStack::new).forEach(stacks::add);
        }
    };

    @ObjectHolder(value = "rechiseled:chisel", registryName = "minecraft:item")
    public static ChiselItem chisel;

    @ObjectHolder(value = "rechiseled:chisel_container", registryName = "minecraft:menu")
    public static MenuType<ChiselContainer> chisel_container;

    public Rechiseled(){
        CHANNEL.registerMessage(PacketSelectEntry.class, PacketSelectEntry::new, true);
        CHANNEL.registerMessage(PacketToggleConnecting.class, PacketToggleConnecting::new, true);
        CHANNEL.registerMessage(PacketChiselAll.class, PacketChiselAll::new, true);
        CHANNEL.registerMessage(PacketChiselingRecipes.class, PacketChiselingRecipes::new, true);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {

        @SubscribeEvent
        public static void onRegisterEvent(RegisterEvent e){
            if(e.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS))
                onBlockRegistry(Objects.requireNonNull(e.getForgeRegistry()));
            else if(e.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS))
                onItemRegistry(Objects.requireNonNull(e.getForgeRegistry()));
            else if(e.getRegistryKey().equals(ForgeRegistries.Keys.MENU_TYPES))
                onContainerRegistry(Objects.requireNonNull(e.getForgeRegistry()));
        }

        public static void onBlockRegistry(IForgeRegistry<Block> registry){
            for(RechiseledBlockType type : RechiseledBlockType.values()){
                type.createBlocks();
                if(type.hasCreatedRegularBlock())
                    registry.register(type.regularRegistryName, type.getRegularBlock());
                if(type.hasCreatedConnectingBlock())
                    registry.register(type.connectingRegistryName, type.getConnectingBlock());
            }
        }

        public static void onItemRegistry(IForgeRegistry<Item> registry){
            registry.register("chisel", new ChiselItem());

            for(RechiseledBlockType type : RechiseledBlockType.values()){
                type.createItems();
                if(type.hasCreatedRegularBlock())
                    registry.register(type.regularRegistryName, type.getRegularItem());
                if(type.hasCreatedConnectingBlock())
                    registry.register(type.connectingRegistryName, type.getConnectingItem());
            }
        }

        public static void onContainerRegistry(IForgeRegistry<MenuType<?>> registry){
            registry.register("chisel_container", IForgeMenuType.create(
                (id, inventory, data) -> new ChiselContainer(chisel_container, id, inventory.player, data.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND)
            ));
        }

        @SubscribeEvent
        public static void onGatherData(GatherDataEvent e){
            e.getGenerator().addProvider(e.includeClient(), new RechiseledTextureProvider(e));
            e.getGenerator().addProvider(e.includeClient(), new RechiseledConnectingBlockModelProvider(e));
            e.getGenerator().addProvider(e.includeClient(), new RechiseledItemModelProvider(e));
            e.getGenerator().addProvider(e.includeClient(), new RechiseledBlockStateProvider(e));
            e.getGenerator().addProvider(e.includeClient(), new RechiseledLanguageProvider(e));
            e.getGenerator().addProvider(e.includeServer(), new RechiseledLootTableProvider(e));
            e.getGenerator().addProvider(e.includeServer(), new RechiseledAdvancementProvider(e));
            e.getGenerator().addProvider(e.includeServer(), new RechiseledChiselingRecipeProvider(e));
            e.getGenerator().addProvider(e.includeServer(), new RechiseledRecipeProvider(e));
            e.getGenerator().addProvider(e.includeServer(), new RechiseledBlockTagsProvider(e));
        }
    }
}
