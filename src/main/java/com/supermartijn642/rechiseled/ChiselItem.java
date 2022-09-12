package com.supermartijn642.rechiseled;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.item.BaseItem;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselItem extends BaseItem {

    public ChiselItem(){
        super(ItemProperties.create().group(Rechiseled.GROUP));
    }

    @Override
    public ItemUseResult interact(ItemStack stack, EntityPlayer player, EnumHand hand, World level){
        if(!level.isRemote)
            CommonUtils.openContainer(new ChiselContainer(player, hand));
        return ItemUseResult.success(stack);
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
