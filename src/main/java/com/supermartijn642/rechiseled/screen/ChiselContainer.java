package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.rechiseled.ChiselItem;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselContainer extends BaseChiselingContainer {

    public final EnumHand hand;

    public ChiselContainer(EntityPlayer player, EnumHand hand){
        super(Rechiseled.chisel_container, player);
        this.hand = hand;
        super.updateRecipe();
    }

    @Override
    public ItemStack getCurrentStack(){
        ItemStack chisel = this.player.getHeldItem(this.hand);
        return chisel.getItem() == Rechiseled.chisel ? ChiselItem.getStoredStack(chisel) : ItemStack.EMPTY;
    }

    @Override
    public void setCurrentStack(ItemStack stack){
        ItemStack chisel = this.player.getHeldItem(this.hand);
        if(chisel.getItem() == Rechiseled.chisel)
            ChiselItem.setStoredStack(chisel, stack);
    }

    @Override
    public boolean shouldBeClosed(){
        ItemStack chisel = this.player.getHeldItem(this.hand);
        return chisel.getItem() != Rechiseled.chisel;
    }
}
