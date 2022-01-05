package com.supermartijn642.rechiseled;

import com.supermartijn642.core.network.PacketChannel;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipe;
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
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ObjectHolder;

import java.util.LinkedList;
import java.util.List;

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
                if(!type.useParent)
                    items.add(type.getRegularItem());
                items.add(type.getConnectingItem());
            }
            items.stream().map(ItemStack::new).forEach(stacks::add);
        }
    };

    @ObjectHolder("rechiseled:chisel")
    public static ChiselItem chisel;

    @ObjectHolder("rechiseled:chisel_container")
    public static MenuType<ChiselContainer> chisel_container;

    public Rechiseled(){
        CHANNEL.registerMessage(PacketSelectEntry.class, PacketSelectEntry::new, true);
        CHANNEL.registerMessage(PacketToggleConnecting.class, PacketToggleConnecting::new, true);
        CHANNEL.registerMessage(PacketChiselAll.class, PacketChiselAll::new, true);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {

        @SubscribeEvent
        public static void onBlockRegistry(RegistryEvent.Register<Block> e){
            for(RechiseledBlockType type : RechiseledBlockType.values()){
                type.createBlocks();
                if(type.getRegularBlock() != null)
                    e.getRegistry().register(type.getRegularBlock());
                if(type.getConnectingBlock() != null)
                    e.getRegistry().register(type.getConnectingBlock());
            }
        }

        @SubscribeEvent
        public static void onItemRegistry(RegistryEvent.Register<Item> e){
            e.getRegistry().register(new ChiselItem().setRegistryName("chisel"));

            for(RechiseledBlockType type : RechiseledBlockType.values()){
                type.createItems();
                if(type.getRegularItem() != null)
                    e.getRegistry().register(type.getRegularItem());
                if(type.getConnectingItem() != null)
                    e.getRegistry().register(type.getConnectingItem());
            }
        }

        @SubscribeEvent
        public static void onContainerRegistry(RegistryEvent.Register<MenuType<?>> e){
            e.getRegistry().register(IForgeContainerType.create(
                (id, inventory, data) -> new ChiselContainer(chisel_container, id, inventory.player, data.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND)
            ).setRegistryName("chisel_container"));
        }

        @SubscribeEvent
        public static void onRecipeSerializerRegistry(RegistryEvent.Register<RecipeSerializer<?>> e){
            e.getRegistry().register(ChiselingRecipe.SERIALIZER.setRegistryName("chiseling"));
        }

        @SubscribeEvent
        public static void onGatherData(GatherDataEvent e){
            e.getGenerator().addProvider(new RechiseledBlockModelProvider(e));
            e.getGenerator().addProvider(new RechiseledItemModelProvider(e));
            e.getGenerator().addProvider(new RechiseledBlockStateProvider(e));
            e.getGenerator().addProvider(new RechiseledLanguageProvider(e));
            e.getGenerator().addProvider(new RechiseledLootTableProvider(e));
            e.getGenerator().addProvider(new RechiseledAdvancementProvider(e));
            e.getGenerator().addProvider(new RechiseledChiselingRecipeProvider(e));
            e.getGenerator().addProvider(new RechiseledRecipeProvider(e));
            e.getGenerator().addProvider(new RechiseledBlockTagsProvider(e));
        }
    }
}
