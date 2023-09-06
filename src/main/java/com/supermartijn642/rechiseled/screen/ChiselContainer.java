package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.rechiseled.ChiselItem;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselContainer extends BaseChiselingContainer {

    public final InteractionHand hand;

    public ChiselContainer(Player player, InteractionHand hand){
        super(Rechiseled.chisel_container, player);
        this.hand = hand;
        super.updateRecipe();
    }

    @Override
    public ItemStack getCurrentStack(){
        if(this.hand == null) // Ledger accesses slot contents immediately when added, so the 'hand' won't have been set yet
            return ItemStack.EMPTY;
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
