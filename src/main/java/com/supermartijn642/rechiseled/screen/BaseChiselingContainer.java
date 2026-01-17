package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.gui.BaseContainer;
import com.supermartijn642.core.gui.BaseContainerType;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipeManager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Created 22/12/2021 by SuperMartijn642
 */
public abstract class BaseChiselingContainer extends BaseContainer {

    private final boolean isClient;
    public ChiselingRecipe currentRecipe = null;
    public ChiselingEntry currentEntry = null;
    public boolean connecting = false;

    public BaseChiselingContainer(BaseContainerType<?> type, Player player){
        super(type, player);
        //noinspection resource
        this.isClient = player.level().isClientSide();
        this.addSlots();
    }

    @Override
    protected void addSlots(Player player){
        //noinspection resource
        boolean isClient = player.level().isClientSide();
        this.addSlot(new DummySlot(0, 154, 102) {
            @Override
            public void set(ItemStack stack){
                BaseChiselingContainer.this.setCurrentStack(stack);
                BaseChiselingContainer.this.updateRecipe();
            }

            @Override
            public ItemStack getItem(){
                return BaseChiselingContainer.this.getCurrentStack();
            }

            @Override
            public boolean mayPlace(ItemStack stack){
                return ChiselingRecipeManager.get(isClient).getRecipeForItem(stack.getItem()) != null;
            }
        });
        this.addPlayerSlots(31, 144);
    }

    @Override
    public boolean stillValid(Player playerIn){
        return !this.shouldBeClosed();
    }

    protected void updateRecipe(){
        ItemStack stack = this.getCurrentStack();
        if(stack.isEmpty()){
            this.currentRecipe = null;
            this.currentEntry = null;
            this.connecting = false;
        }else{
            this.currentRecipe = ChiselingRecipeManager.get(this.isClient).getRecipeForItem(stack.getItem());
            if(this.currentRecipe != null){
                for(ChiselingEntry entry : this.currentRecipe.entries()){
                    if(entry.hasRegularItem(ChiselingBlockShape.BLOCK) && entry.getRegularItem(ChiselingBlockShape.BLOCK) == stack.getItem()){
                        this.currentEntry = entry;
                        this.connecting = false;
                        return;
                    }else if(entry.hasConnectingItem(ChiselingBlockShape.BLOCK) && entry.getConnectingItem(ChiselingBlockShape.BLOCK) == stack.getItem()){
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
        if(this.currentRecipe == null || index >= this.currentRecipe.entries().size())
            return;

        ChiselingEntry entry = this.currentRecipe.entries().get(index);
        Item item = (this.connecting && entry.hasConnectingItem(ChiselingBlockShape.BLOCK)) || !entry.hasRegularItem(ChiselingBlockShape.BLOCK) ? entry.getConnectingItem(ChiselingBlockShape.BLOCK) : entry.getRegularItem(ChiselingBlockShape.BLOCK);
        //noinspection DataFlowIssue
        ItemStack stack = new ItemStack(item, this.getCurrentStack().getCount());
        this.setCurrentStack(stack);
        this.updateRecipe();
    }

    public void toggleConnecting(){
        if(this.currentRecipe == null)
            return;

        if(this.connecting){
            if(this.currentEntry.hasRegularItem(ChiselingBlockShape.BLOCK)){
                //noinspection DataFlowIssue
                ItemStack stack = new ItemStack(this.currentEntry.getRegularItem(ChiselingBlockShape.BLOCK), this.getCurrentStack().getCount());
                this.setCurrentStack(stack);
                this.updateRecipe();
            }
        }else{
            if(this.currentEntry.hasConnectingItem(ChiselingBlockShape.BLOCK)){
                //noinspection DataFlowIssue
                ItemStack stack = new ItemStack(this.currentEntry.getConnectingItem(ChiselingBlockShape.BLOCK), this.getCurrentStack().getCount());
                this.setCurrentStack(stack);
                this.updateRecipe();
            }
        }
    }

    public void chiselAll(){
        if(this.currentRecipe == null)
            return;

        Inventory inventory = this.player.getInventory();
        for(int index = 0; index < inventory.getContainerSize(); index++){
            ItemStack stack = inventory.getItem(index);
            if(stack.isEmpty()) continue;
            Item item = this.connecting ? this.currentEntry.getConnectingItem(ChiselingBlockShape.BLOCK) : this.currentEntry.getRegularItem(ChiselingBlockShape.BLOCK);
            //noinspection DataFlowIssue
            if(stack.getCount() > item.getMaxStackSize() || (stack.hasTag() && !stack.getTag().isEmpty()))
                continue;

            for(ChiselingEntry entry : this.currentRecipe.entries()){
                if((entry.hasConnectingItem(ChiselingBlockShape.BLOCK) && stack.getItem() == entry.getConnectingItem(ChiselingBlockShape.BLOCK))
                    || (entry.hasRegularItem(ChiselingBlockShape.BLOCK) && stack.getItem() == entry.getRegularItem(ChiselingBlockShape.BLOCK))){
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
    public ItemStack quickMoveStack(Player player, int index){
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
                if(!slotStack.isEmpty() && ItemStack.isSameItemSameTags(stack, slotStack)){
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
