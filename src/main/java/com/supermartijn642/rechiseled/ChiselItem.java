package com.supermartijn642.rechiseled;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselItem extends Item {

    public ChiselItem(){
        this.setUnlocalizedName("rechiseled.chisel");
        this.setCreativeTab(Rechiseled.GROUP);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
        ItemStack stack = player.getHeldItem(hand);
        if(!world.isRemote)
            player.openGui(Rechiseled.instance, hand == EnumHand.MAIN_HAND ? 0 : 1, world, 0, 0, 0);
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

    public static ItemStack getStoredStack(ItemStack chisel){
        NBTTagCompound tag = chisel.hasTagCompound() ? chisel.getTagCompound() : new NBTTagCompound();
        return tag.hasKey("stack") ? new ItemStack(tag.getCompoundTag("stack")) : ItemStack.EMPTY;
    }

    public static void setStoredStack(ItemStack chisel, ItemStack stack){
        NBTTagCompound tag = chisel.hasTagCompound() ? chisel.getTagCompound() : new NBTTagCompound();
        if(stack == null || stack.isEmpty())
            tag.removeTag("stack");
        else
            tag.setTag("stack", stack.serializeNBT());
        chisel.setTagCompound(tag);
    }
}
