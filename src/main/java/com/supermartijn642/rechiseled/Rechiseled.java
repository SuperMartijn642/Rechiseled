package com.supermartijn642.rechiseled;

import com.supermartijn642.core.network.PacketChannel;
import com.supermartijn642.rechiseled.chiseling.PacketChiselingRecipes;
import com.supermartijn642.rechiseled.data.*;
import com.supermartijn642.rechiseled.packet.PacketChiselAll;
import com.supermartijn642.rechiseled.packet.PacketSelectEntry;
import com.supermartijn642.rechiseled.packet.PacketToggleConnecting;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ObjectHolder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("rechiseled")
public class Rechiseled {

    public static final PacketChannel CHANNEL = PacketChannel.create("rechiseled");

    public static final ItemGroup GROUP = new ItemGroup("rechiseled") {
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

    @ObjectHolder("rechiseled:chisel")
    public static ChiselItem chisel;

    @ObjectHolder("rechiseled:chisel_container")
    public static ContainerType<ChiselContainer> chisel_container;

    public Rechiseled(){
        CHANNEL.registerMessage(PacketSelectEntry.class, PacketSelectEntry::new, true);
        CHANNEL.registerMessage(PacketToggleConnecting.class, PacketToggleConnecting::new, true);
        CHANNEL.registerMessage(PacketChiselAll.class, PacketChiselAll::new, true);
        CHANNEL.registerMessage(PacketChiselingRecipes.class, PacketChiselingRecipes::new, false);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {

        @SubscribeEvent
        public static void onBlockRegistry(RegistryEvent.Register<Block> e){
            for(RechiseledBlockType type : RechiseledBlockType.values()){
                type.createBlocks();
                if(type.hasCreatedRegularBlock())
                    e.getRegistry().register(type.getRegularBlock());
                if(type.hasCreatedConnectingBlock())
                    e.getRegistry().register(type.getConnectingBlock());
            }
        }

        @SubscribeEvent
        public static void onItemRegistry(RegistryEvent.Register<Item> e){
            e.getRegistry().register(new ChiselItem().setRegistryName("chisel"));

            for(RechiseledBlockType type : RechiseledBlockType.values()){
                type.createItems();
                if(type.hasCreatedRegularBlock())
                    e.getRegistry().register(type.getRegularItem());
                if(type.hasCreatedConnectingBlock())
                    e.getRegistry().register(type.getConnectingItem());
            }
        }

        @SubscribeEvent
        public static void onContainerRegistry(RegistryEvent.Register<ContainerType<?>> e){
            e.getRegistry().register(IForgeContainerType.create(
                (id, inventory, data) -> new ChiselContainer(chisel_container, id, inventory.player, data.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND)
            ).setRegistryName("chisel_container"));
        }

        @SubscribeEvent
        public static void onGatherData(GatherDataEvent e){
            ExistingFileHelper existingFileHelper = new TrackingExistingFileHelper(e.getExistingFileHelper());
            e.getGenerator().addProvider(new RechiseledTextureProvider(e, existingFileHelper));
            e.getGenerator().addProvider(new RechiseledConnectingBlockModelProvider(e, existingFileHelper));
            e.getGenerator().addProvider(new RechiseledItemModelProvider(e, existingFileHelper));
            e.getGenerator().addProvider(new RechiseledBlockStateProvider(e, existingFileHelper));
            e.getGenerator().addProvider(new RechiseledLanguageProvider(e));
            e.getGenerator().addProvider(new RechiseledLootTableProvider(e));
            e.getGenerator().addProvider(new RechiseledAdvancementProvider(e));
            e.getGenerator().addProvider(new RechiseledChiselingRecipeProvider(e));
            e.getGenerator().addProvider(new RechiseledRecipeProvider(e));
        }
    }
}
