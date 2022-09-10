package com.supermartijn642.rechiseled;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.item.BaseItem;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselItem extends BaseItem {

    public ChiselItem(){
        super(ItemProperties.create().group(Rechiseled.GROUP));
    }

    @Override
    public ItemUseResult interact(ItemStack stack, PlayerEntity player, Hand hand, World level){
        if(!level.isClientSide)
            CommonUtils.openContainer(new ChiselContainer(player, hand));
        return ItemUseResult.success(stack);
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
