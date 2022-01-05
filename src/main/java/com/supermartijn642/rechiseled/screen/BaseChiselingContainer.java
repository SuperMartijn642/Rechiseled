package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.gui.BaseContainer;
import com.supermartijn642.rechiseled.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Created 22/12/2021 by SuperMartijn642
 */
public abstract class BaseChiselingContainer extends BaseContainer {

    public ChiselingRecipe currentRecipe = null;
    public ChiselingEntry currentEntry = null;
    public boolean connecting = false;

    public BaseChiselingContainer(EntityPlayer player){
        super(player);
        this.addSlots();
    }

    @Override
    protected void addSlots(EntityPlayer playerEntity){
        this.addSlot(new SlotItemHandler(new IItemHandlerModifiable() {
            @Override
            public void setStackInSlot(int slot, @Nonnull ItemStack stack){
                if(slot == 0){
                    BaseChiselingContainer.this.setCurrentStack(stack);
                    BaseChiselingContainer.this.updateRecipe();
                }
            }

            @Override
            public int getSlots(){
                return 1;
            }

            @Nonnull
            @Override
            public ItemStack getStackInSlot(int slot){
                return slot == 0 ? BaseChiselingContainer.this.getCurrentStack() : ItemStack.EMPTY;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate){
                if(slot != 0 || stack.isEmpty())
                    return stack.copy();

                ItemStack currentStack = BaseChiselingContainer.this.getCurrentStack();
                if(!currentStack.isEmpty() && !ItemStack.areItemStackTagsEqual(currentStack, stack))
                    return stack.copy();

                int count = Math.min(stack.getCount(), stack.getMaxStackSize() - currentStack.getCount());
                if(!simulate){
                    ItemStack newStack = stack.copy();
                    newStack.setCount(currentStack.getCount() + count);
                    BaseChiselingContainer.this.setCurrentStack(newStack);
                }
                stack = stack.copy();
                stack.shrink(count);
                return stack;
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate){
                if(slot != 0 || amount <= 0)
                    return ItemStack.EMPTY;

                ItemStack currentStack = BaseChiselingContainer.this.getCurrentStack();
                int count = Math.min(amount, currentStack.getCount());
                if(!simulate){
                    ItemStack newStack = currentStack.copy();
                    newStack.shrink(count);
                    BaseChiselingContainer.this.setCurrentStack(newStack);
                    BaseChiselingContainer.this.updateRecipe();
                }
                currentStack = currentStack.copy();
                currentStack.setCount(count);
                return currentStack;
            }

            @Override
            public int getSlotLimit(int slot){
                if(slot != 0)
                    return 0;
                ItemStack currentStack = BaseChiselingContainer.this.getCurrentStack();
                return currentStack.isEmpty() ? 64 : currentStack.getMaxStackSize();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack){
                return slot == 0 && ChiselingRecipes.getRecipe(stack) != null;
            }
        }, 0, 154, 102));
        this.addPlayerSlots(31, 144);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn){
        return !this.shouldBeClosed();
    }

    protected void updateRecipe(){
        ItemStack stack = this.getCurrentStack();
        if(stack.isEmpty()){
            this.currentRecipe = null;
            this.currentEntry = null;
            this.connecting = false;
        }else{
            this.currentRecipe = ChiselingRecipes.getRecipe(stack);
            if(this.currentRecipe != null){
                for(ChiselingEntry entry : this.currentRecipe.getEntries()){
                    if(entry.hasRegularItem() && entry.getRegularItem() == stack.getItem() && (!entry.getRegularItem().getHasSubtypes() || entry.getRegularItemData() == stack.getMetadata())){
                        this.currentEntry = entry;
                        this.connecting = false;
                        return;
                    }else if(entry.hasConnectingItem() && entry.getConnectingItem() == stack.getItem() && (!entry.getConnectingItem().getHasSubtypes() || entry.getConnectingItemData() == stack.getMetadata())){
                        this.currentEntry = entry;
                        this.connecting = true;
                        return;
                    }
                }
            }else{
                this.currentEntry = null;
                this.connecting = false;
            }
        }
    }

    public void setCurrentEntry(int index){
        if(this.currentRecipe == null || index >= this.currentRecipe.getEntries().size())
            return;

        ChiselingEntry entry = this.currentRecipe.getEntries().get(index);
        ItemStack stack = (this.connecting && entry.hasConnectingItem()) || !entry.hasRegularItem() ? entry.getConnectingItemStack() : entry.getRegularItemStack();
        stack.setCount(this.getCurrentStack().getCount());
        this.setCurrentStack(stack);
        this.updateRecipe();
    }

    public void toggleConnecting(){
        if(this.currentRecipe == null)
            return;

        if(this.connecting){
            if(this.currentEntry.hasRegularItem()){
                ItemStack stack = new ItemStack(this.currentEntry.getRegularItem(), this.getCurrentStack().getCount(), this.currentEntry.getRegularItemData());
                this.setCurrentStack(stack);
                this.updateRecipe();
            }
        }else{
            if(this.currentEntry.hasConnectingItem()){
                ItemStack stack = new ItemStack(this.currentEntry.getConnectingItem(), this.getCurrentStack().getCount(), this.currentEntry.getConnectingItemData());
                this.setCurrentStack(stack);
                this.updateRecipe();
            }
        }
    }

    public void chiselAll(){
        if(this.currentRecipe == null)
            return;

        InventoryPlayer inventory = this.player.inventory;
        for(int index = 0; index < inventory.getSizeInventory(); index++){
            ItemStack stack = inventory.getStackInSlot(index);
            Item item = this.connecting ? this.currentEntry.getConnectingItem() : this.currentEntry.getRegularItem();
            int data = this.connecting ? this.currentEntry.getConnectingItemData() : this.currentEntry.getRegularItemData();
            if(stack.getCount() > item.getItemStackLimit())
                continue;

            for(ChiselingEntry entry : this.currentRecipe.getEntries()){
                if((!stack.hasTagCompound() || stack.getTagCompound().hasNoTags())
                    && ((entry.hasConnectingItem() && stack.getItem() == entry.getConnectingItem() && (!entry.getConnectingItem().getHasSubtypes() || entry.getConnectingItemData() == stack.getMetadata()))
                    || (entry.hasRegularItem() && stack.getItem() == entry.getRegularItem() && (!entry.getRegularItem().getHasSubtypes() || entry.getRegularItemData() == stack.getMetadata())))){
                    stack = new ItemStack(item, stack.getCount(), data);
                    inventory.setInventorySlotContents(index, stack);
                }
            }
        }
    }

    public abstract ItemStack getCurrentStack();

    public abstract void setCurrentStack(ItemStack stack);

    public abstract boolean shouldBeClosed();

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index){
        ItemStack stack = this.getSlot(index).getStack();
        if(stack.isEmpty())
            return stack;

        if(index == 0){
            if(!this.moveItemStackTo(stack, 1, this.inventorySlots.size(), true))
                return ItemStack.EMPTY;
        }else{
            if(!this.moveItemStackTo(stack, 0, 1, true))
                return ItemStack.EMPTY;
        }

        if(stack.isEmpty())
            this.inventorySlots.get(index).putStack(stack);
        return stack;
    }

    protected boolean moveItemStackTo(ItemStack stack, int minSlot, int maxSlot, boolean reversed){
        boolean changed = false;
        int index = minSlot;
        if(reversed)
            index = maxSlot - 1;

        if(stack.isStackable()){
            while(!stack.isEmpty()){
                if(reversed){
                    if(index < minSlot){
                        break;
                    }
                }else if(index >= maxSlot){
                    break;
                }

                Slot slot = this.inventorySlots.get(index);
                ItemStack slotStack = slot.getStack();
                if(!slotStack.isEmpty() && slotStack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == slotStack.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, slotStack)){
                    int sumCount = slotStack.getCount() + stack.getCount();
                    int maxSize = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());
                    if(sumCount <= maxSize){
                        stack.setCount(0);
                        slotStack.setCount(sumCount);
                        slot.putStack(slotStack);
                        changed = true;
                    }else if(slotStack.getCount() < maxSize){
                        stack.shrink(maxSize - slotStack.getCount());
                        slotStack.setCount(maxSize);
                        slot.putStack(slotStack);
                        changed = true;
                    }
                }

                if(reversed)
                    index--;
                else
                    index++;
            }
        }

        if(!stack.isEmpty()){
            if(reversed)
                index = maxSlot - 1;
            else
                index = minSlot;

            while(true){
                if(reversed){
                    if(index < minSlot){
                        break;
                    }
                }else if(index >= maxSlot){
                    break;
                }

                Slot slot = this.inventorySlots.get(index);
                ItemStack slotStack = slot.getStack();
                if(slotStack.isEmpty() && slot.isItemValid(stack)){
                    if(stack.getCount() > slot.getSlotStackLimit())
                        slot.putStack(stack.splitStack(slot.getSlotStackLimit()));
                    else
                        slot.putStack(stack.splitStack(stack.getCount()));

                    changed = true;
                    break;
                }

                if(reversed)
                    index--;
                else
                    index++;
            }
        }

        return changed;
    }
}
