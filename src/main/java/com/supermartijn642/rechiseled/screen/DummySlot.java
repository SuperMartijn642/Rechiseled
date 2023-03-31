package com.supermartijn642.rechiseled.screen;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Created 29/01/2023 by SuperMartijn642
 */
public class DummySlot extends Slot {

    private static final Container EMPTY_CONTAINER = new Container() {
        @Override
        public int getContainerSize(){
            return 0;
        }

        @Override
        public boolean isEmpty(){
            return true;
        }

        @Override
        public ItemStack getItem(int i){
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItem(int i, int j){
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItemNoUpdate(int i){
            return ItemStack.EMPTY;
        }

        @Override
        public void setItem(int i, ItemStack itemStack){
        }

        @Override
        public void setChanged(){
        }

        @Override
        public boolean stillValid(Player player){
            return false;
        }

        @Override
        public void clearContent(){
        }
    };

    public DummySlot(int index, int x, int y){
        super(EMPTY_CONTAINER, index, x, y);
    }

    @Override
    public ItemStack getItem(){
        return ItemStack.EMPTY;
    }

    @Override
    public void set(ItemStack itemStack){
    }

    @Override
    public void setChanged(){
    }

    @Override
    public int getMaxStackSize(){
        ItemStack stack = this.getItem();
        return stack.isEmpty() ? 64 : stack.getMaxStackSize();
    }

    @Override
    public ItemStack remove(int count){
        ItemStack stack = this.getItem();
        ItemStack result = stack.split(count);
        this.set(stack);
        return result;
    }
}
