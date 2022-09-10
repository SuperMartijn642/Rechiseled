package com.supermartijn642.rechiseled;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.item.BaseItem;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselItem extends BaseItem {

    public ChiselItem(){
        super(ItemProperties.create().group(Rechiseled.GROUP));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand){
        ItemStack stack = player.getItemInHand(hand);
        if(!world.isClientSide)
            CommonUtils.openContainer(new ChiselContainer(player, hand));
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
