package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.rechiseled.ChiselItem;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselContainer extends BaseChiselingContainer {

    private final Hand hand;

    public ChiselContainer(ContainerType<?> type, int id, PlayerEntity player, Hand hand){
        super(type, id, player);
        this.hand = hand;
        super.updateRecipe();
    }

    @Override
    public ItemStack getCurrentStack(){
        ItemStack chisel = this.player.getItemInHand(this.hand);
        return chisel.getItem() == Rechiseled.chisel ? ChiselItem.getStoredStack(chisel) : ItemStack.EMPTY;
    }

    @Override
    public void setCurrentStack(ItemStack stack){
        ItemStack chisel = this.player.getItemInHand(this.hand);
        if(chisel.getItem() == Rechiseled.chisel)
            ChiselItem.setStoredStack(chisel, stack);
    }

    @Override
    public boolean shouldBeClosed(){
        ItemStack chisel = this.player.getItemInHand(this.hand);
        return chisel.getItem() != Rechiseled.chisel;
    }
}
