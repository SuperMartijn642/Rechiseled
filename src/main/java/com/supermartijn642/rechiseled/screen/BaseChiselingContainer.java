package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.gui.BaseContainer;
import com.supermartijn642.core.gui.BaseContainerType;
import com.supermartijn642.core.gui.CustomSlot;
import com.supermartijn642.rechiseled.Rechiseled;
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
    public ChiselingBlockShape shape = null;
    public boolean connecting = false;

    public BaseChiselingContainer(BaseContainerType<?> type, Player player){
        super(type, player);
        //noinspection resource
        this.isClient = player.level.isClientSide();
        this.addSlots();
    }

    @Override
    protected void addSlots(Player player){
        //noinspection resource
        boolean isClient = player.level.isClientSide();
        this.addSlot(
            CustomSlot.builder()
                .position(181, 108)
                .size(26)
                .setter(BaseChiselingContainer.this::setCurrentStack)
                .getter(this::getCurrentStack)
                .filter(stack -> ChiselingRecipeManager.get(isClient).getRecipeForItem(stack.getItem()) != null)
                .onChange((oldStack, newStack) -> this.findRecipe())
                .build()
                .getVanillaSlot()
        );
        this.addPlayerSlots(50, 161);
    }

    @Override
    public boolean stillValid(Player playerIn){
        return !this.shouldBeClosed();
    }

    protected void findRecipe(){
        Item item = this.getCurrentStack().getItem();
        // Check if the current recipe is still applicable
        ChiselingRecipe recipe = ChiselingRecipeManager.get(this.isClient).getRecipeForItem(item);
        if(recipe != null && this.currentRecipe == recipe && this.currentRecipe.contains(item) && this.currentEntry.contains(item)
            && (this.connecting ? this.currentEntry.getConnectingItem(this.shape) == item : this.currentEntry.getRegularItem(this.shape) == item))
            return;

        // Find a matching recipe
        if(recipe != null){
            this.currentRecipe = recipe;
            for(ChiselingEntry entry : this.currentRecipe.entries()){
                if(entry.contains(item)){
                    for(ChiselingBlockShape shape : ChiselingBlockShape.values()){
                        if(this.connecting && entry.getConnectingItem(shape) == item){
                            this.currentEntry = entry;
                            this.shape = shape;
                            return;
                        }else if(entry.getRegularItem(shape) == item){
                            this.currentEntry = entry;
                            this.shape = shape;
                            this.connecting = false;
                            return;
                        }else if(!this.connecting && entry.getConnectingItem(shape) == item){
                            this.currentEntry = entry;
                            this.shape = shape;
                            this.connecting = true;
                            return;
                        }
                    }
                }
            }
        }
        this.currentRecipe = null;
        this.currentEntry = null;
        this.shape = null;
        this.connecting = false;
    }

    public void setCurrentEntry(int index, ChiselingBlockShape shape, boolean connecting){
        if(this.currentRecipe == null || index >= this.currentRecipe.entries().size())
            return;

        ChiselingEntry entry = this.currentRecipe.entries().get(index);
        if(connecting ? !entry.hasConnectingItem(shape) : !entry.hasRegularItem(shape))
            return;

        ItemStack currentStack = this.getCurrentStack();
        float conversionFactor = (float)shape.conversionFactor() / this.shape.conversionFactor();
        int convertedAmount = (int)Math.floor(conversionFactor * currentStack.getCount());
        if(convertedAmount <= 0)
            return;
        int leftover = currentStack.getCount() - Math.round(convertedAmount / conversionFactor);

        this.currentEntry = entry;
        this.shape = shape;
        this.connecting = connecting;
        Item item = connecting ? entry.getConnectingItem(shape) : entry.getRegularItem(shape);
        //noinspection DataFlowIssue
        this.setCurrentStack(new ItemStack(item, convertedAmount));
        if(leftover > 0){
            currentStack = currentStack.copy();
            currentStack.setCount(leftover);
            leftover = this.player.getInventory().addResource(currentStack);
            if(leftover > 0){
                currentStack = currentStack.copy();
                currentStack.setCount(leftover);
                this.player.drop(currentStack, true, true);
            }
        }
    }

    public void chiselAll(boolean includeAllShapes){
        if(this.currentRecipe == null)
            return;

        Item targetItem = this.connecting ? this.currentEntry.getConnectingItem(this.shape) : this.currentEntry.getRegularItem(this.shape);
        assert targetItem != null;

        // Find all space for overflow
        int availableSpace = 0;
        Inventory inventory = this.player.getInventory();
        for(int index = 0; index < inventory.getContainerSize(); index++){
            ItemStack stack = inventory.getItem(index);
            if(stack.isEmpty() || stack.getItem() == targetItem)
                availableSpace += Math.max(0, targetItem.getMaxStackSize() - stack.getCount());
        }

        // Replace stacks
        int overflow = 0;
        for(int index = 0; index < inventory.getContainerSize(); index++){
            ItemStack stack = inventory.getItem(index);
            if(stack.isEmpty() || stack.getItem() == targetItem || !this.currentRecipe.contains(stack.getItem()))
                continue;
            //noinspection DataFlowIssue
            if(stack.hasTag() && !stack.getTag().isEmpty()) // Safety check to prevent overwriting important items
                continue;

            // Find entry and shape of the stack
            ChiselingEntry stackEntry = null;
            for(ChiselingEntry entry : this.currentRecipe.entries()){
                if(entry.contains(stack.getItem()))
                    stackEntry = entry;
            }
            assert stackEntry != null;
            ChiselingBlockShape stackShape = null;
            for(ChiselingBlockShape shape : ChiselingBlockShape.values()){
                if(stackEntry.getRegularItem(shape) == stack.getItem() || stackEntry.getConnectingItem(shape) == stack.getItem())
                    stackShape = shape;
            }
            assert stackShape != null;
            if(!includeAllShapes && stackShape != this.shape)
                continue;

            // Calculate how much of the stack can be converted
            float conversionFactor = (float)this.shape.conversionFactor() / stackShape.conversionFactor();
            int convertedAmount = (int)Math.floor(stack.getCount() * conversionFactor);
            boolean canConvertEntireStack = conversionFactor >= 1 || stack.getCount() * conversionFactor < 10e-7; // Check that there's no partial items left over
            if(convertedAmount - targetItem.getMaxStackSize() > availableSpace){
                canConvertEntireStack = false;
                convertedAmount = (int)(Math.floor(availableSpace / conversionFactor) * conversionFactor);
            }
            if(convertedAmount == 0)
                continue;

            // Convert stack and add overflow
            if(canConvertEntireStack){
                int newStackSize = Math.min(convertedAmount, targetItem.getMaxStackSize());
                inventory.setItem(index, new ItemStack(targetItem, newStackSize));
                convertedAmount -= newStackSize;
            }else{
                int removedStackSize = (int)(convertedAmount / conversionFactor);
                stack = stack.copy();
                stack.setCount(stack.getCount() - removedStackSize);
                inventory.setItem(index, stack);
            }
            overflow += convertedAmount;
            availableSpace -= convertedAmount;
        }

        // Put overflow into player inventory
        if(overflow > 0){
            do{
                int remaining = inventory.addResource(new ItemStack(targetItem, overflow));
                if(remaining == overflow)
                    break;
                overflow = remaining;
            }while(overflow > 0);
            if(overflow > 0){
                Rechiseled.LOGGER.error("Failed to insert stacks into player inventory despite the fact there should be sufficient space!");
                this.player.drop(new ItemStack(targetItem, overflow), true, true);
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
