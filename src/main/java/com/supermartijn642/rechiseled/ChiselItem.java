package com.supermartijn642.rechiseled;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselItem extends Item {

    public ChiselItem(){
        super(new Properties().tab(Rechiseled.GROUP));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand){
        ItemStack stack = player.getItemInHand(hand);
        if(!world.isClientSide){
            NetworkHooks.openGui((ServerPlayer)player, new MenuProvider() {
                @Override
                public Component getDisplayName(){
                    return TextComponents.item(stack.getItem()).get();
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player){
                    return new ChiselContainer(Rechiseled.chisel_container, id, player, hand);
                }
            }, buffer -> buffer.writeBoolean(hand == InteractionHand.MAIN_HAND));
        }
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
    }

    public static ItemStack getStoredStack(ItemStack chisel){
        CompoundTag tag = chisel.getOrCreateTag();
        return tag.contains("stack") ? ItemStack.of(tag.getCompound("stack")) : ItemStack.EMPTY;
    }

    public static void setStoredStack(ItemStack chisel, ItemStack stack){
        CompoundTag tag = chisel.getOrCreateTag();
        if(stack == null || stack.isEmpty())
            tag.remove("stack");
        else
            tag.put("stack", stack.serializeNBT());
    }
}
