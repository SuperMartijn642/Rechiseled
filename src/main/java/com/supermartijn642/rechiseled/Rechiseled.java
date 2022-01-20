package com.supermartijn642.rechiseled;

import com.supermartijn642.core.network.PacketChannel;
import com.supermartijn642.rechiseled.packet.PacketChiselAll;
import com.supermartijn642.rechiseled.packet.PacketSelectEntry;
import com.supermartijn642.rechiseled.packet.PacketToggleConnecting;
import com.supermartijn642.rechiseled.screen.RechiseledGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.LinkedList;
import java.util.List;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod(modid = Rechiseled.MODID, name = Rechiseled.NAME, version = Rechiseled.VERSION, dependencies = Rechiseled.DEPENDENCIES)
public class Rechiseled {

    public static final String MODID = "rechiseled";
    public static final String NAME = "Rechiseled";
    public static final String VERSION = "1.0.6";
    public static final String DEPENDENCIES = "required-after:supermartijn642corelib@[1.0.16,);required-after:supermartijn642configlib@[1.0.9,)";

    public static final PacketChannel CHANNEL = PacketChannel.create("rechiseled");

    public static final CreativeTabs GROUP = new CreativeTabs("rechiseled") {
        @Override
        public ItemStack getTabIconItem(){
            return new ItemStack(chisel);
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> stacks){
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

    @Mod.Instance
    public static Rechiseled instance;

    @GameRegistry.ObjectHolder("rechiseled:chisel")
    public static ChiselItem chisel;

    public Rechiseled(){
        CHANNEL.registerMessage(PacketSelectEntry.class, PacketSelectEntry::new, true);
        CHANNEL.registerMessage(PacketToggleConnecting.class, PacketToggleConnecting::new, true);
        CHANNEL.registerMessage(PacketChiselAll.class, PacketChiselAll::new, true);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new RechiseledGuiHandler());
    }

    @Mod.EventBusSubscriber
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
    }
}
