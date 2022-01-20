package com.supermartijn642.rechiseled;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselItem extends Item {

    public ChiselItem(){
        super(new Properties().tab(Rechiseled.GROUP));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand){
        ItemStack stack = player.getItemInHand(hand);
        if(!world.isClientSide){
            NetworkHooks.openGui((ServerPlayerEntity)player, new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName(){
                    return TextComponents.item(stack.getItem()).get();
                }

                @Nullable
                @Override
                public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player){
                    return new ChiselContainer(Rechiseled.chisel_container, id, player, hand);
                }
            }, buffer -> buffer.writeBoolean(hand == Hand.MAIN_HAND));
        }
        return ActionResult.newResult(ActionResultType.SUCCESS, stack);
    }

    public static ItemStack getStoredStack(ItemStack chisel){
        CompoundNBT tag = chisel.getOrCreateTag();
        return tag.contains("stack") ? ItemStack.of(tag.getCompound("stack")) : ItemStack.EMPTY;
    }

    public static void setStoredStack(ItemStack chisel, ItemStack stack){
        CompoundNBT tag = chisel.getOrCreateTag();
        if(stack == null || stack.isEmpty())
            tag.remove("stack");
        else
            tag.put("stack", stack.serializeNBT());
    }
}
