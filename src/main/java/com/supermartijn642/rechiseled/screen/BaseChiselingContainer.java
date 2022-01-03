package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.gui.BaseContainer;
import com.supermartijn642.rechiseled.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
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

    public BaseChiselingContainer(ContainerType<?> type, int id, PlayerEntity player){
        super(type, id, player);
        this.addSlots();
    }

    @Override
    protected void addSlots(PlayerEntity playerEntity){
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
                if(!currentStack.isEmpty() && !ItemStack.matches(currentStack, stack))
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
                return slot == 0 && ChiselingRecipes.getRecipe(BaseChiselingContainer.this.player.level.getRecipeManager(), stack) != null;
            }
        }, 0, 154, 102));
        this.addPlayerSlots(31, 144);
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn){
        return !this.shouldBeClosed();
    }

    protected void updateRecipe(){
        ItemStack stack = this.getCurrentStack();
        if(stack.isEmpty()){
            this.currentRecipe = null;
            this.currentEntry = null;
            this.connecting = false;
        }else{
            this.currentRecipe = ChiselingRecipes.getRecipe(BaseChiselingContainer.this.player.level.getRecipeManager(), stack);
            if(this.currentRecipe != null){
                for(ChiselingEntry entry : this.currentRecipe.getEntries()){
                    if(entry.hasRegularItem() && entry.getRegularItem() == stack.getItem()){
                        this.currentEntry = entry;
                        this.connecting = false;
                        return;
                    }else if(entry.hasConnectingItem() && entry.getConnectingItem() == stack.getItem()){
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
        Item item = (this.connecting && entry.hasConnectingItem()) || !entry.hasRegularItem() ? entry.getConnectingItem() : entry.getRegularItem();
        ItemStack stack = new ItemStack(item, this.getCurrentStack().getCount());
        this.setCurrentStack(stack);
        this.updateRecipe();
    }

    public void toggleConnecting(){
        if(this.currentRecipe == null)
            return;

        if(this.connecting){
            if(this.currentEntry.hasRegularItem()){
                ItemStack stack = new ItemStack(this.currentEntry.getRegularItem(), this.getCurrentStack().getCount());
                this.setCurrentStack(stack);
                this.updateRecipe();
            }
        }else{
            if(this.currentEntry.hasConnectingItem()){
                ItemStack stack = new ItemStack(this.currentEntry.getConnectingItem(), this.getCurrentStack().getCount());
                this.setCurrentStack(stack);
                this.updateRecipe();
            }
        }
    }

    public void chiselAll(){
        if(this.currentRecipe == null)
            return;

        PlayerInventory inventory = this.player.inventory;
        for(int index = 0; index < inventory.getContainerSize(); index++){
            ItemStack stack = inventory.getItem(index);
            Item item = this.connecting ? this.currentEntry.getConnectingItem() : this.currentEntry.getRegularItem();
            if(stack.getCount() > item.getMaxStackSize())
                continue;

            for(ChiselingEntry entry : this.currentRecipe.getEntries()){
                if((!stack.hasTag() || stack.getTag().isEmpty())
                    && ((entry.hasConnectingItem() && stack.getItem() == entry.getConnectingItem())
                    || (entry.hasRegularItem() && stack.getItem() == entry.getRegularItem()))){
                    stack = new ItemStack(item, stack.getCount());
                    inventory.setItem(index, stack);
                }
            }
        }
    }

    public abstract ItemStack getCurrentStack();

    public abstract void setCurrentStack(ItemStack stack);

    public abstract boolean shouldBeClosed();

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index){
        ItemStack stack = this.getSlot(index).getItem();
        if(stack.isEmpty())
            return stack;

        if(index == 0){
            if(!this.moveItemStackTo(stack, 1, this.slots.size(), true))
                return ItemStack.EMPTY;
        }else{
            if(!this.moveItemStackTo(stack, 0, 1, true))
                return ItemStack.EMPTY;
        }

        if(stack.isEmpty())
            this.slots.get(index).set(stack);
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

                Slot slot = this.slots.get(index);
                ItemStack slotStack = slot.getItem();
                if(!slotStack.isEmpty() && consideredTheSameItem(stack, slotStack)){
                    int sumCount = slotStack.getCount() + stack.getCount();
                    int maxSize = Math.min(slot.getMaxStackSize(), stack.getMaxStackSize());
                    if(sumCount <= maxSize){
                        stack.setCount(0);
                        slotStack.setCount(sumCount);
                        slot.set(slotStack);
                        changed = true;
                    }else if(slotStack.getCount() < maxSize){
                        stack.shrink(maxSize - slotStack.getCount());
                        slotStack.setCount(maxSize);
                        slot.set(slotStack);
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

                Slot slot = this.slots.get(index);
                ItemStack slotStack = slot.getItem();
                if(slotStack.isEmpty() && slot.mayPlace(stack)){
                    if(stack.getCount() > slot.getMaxStackSize())
                        slot.set(stack.split(slot.getMaxStackSize()));
                    else
                        slot.set(stack.split(stack.getCount()));

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
