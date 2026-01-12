package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.gui.BaseContainer;
import com.supermartijn642.core.gui.BaseContainerType;
import com.supermartijn642.core.gui.CustomSlot;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.*;
import com.supermartijn642.rechiseled.api.chiseling.conversion.ChiselingConversionHelper;
import com.supermartijn642.rechiseled.api.chiseling.conversion.ConversionResult;
import com.supermartijn642.rechiseled.api.util.ItemWithMeta;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created 22/12/2021 by SuperMartijn642
 */
public abstract class BaseChiselingContainer extends BaseContainer {

    private final boolean isClient;
    public ChiselingRecipe currentRecipe = null;
    public ChiselingEntry currentEntry = null;
    public ChiselingBlockShape shape = null;
    public boolean connecting = false;

    public BaseChiselingContainer(BaseContainerType<?> type, EntityPlayer player){
        super(type, player);
        this.isClient = player.world.isRemote;
        this.addSlots();
    }

    @Override
    protected void addSlots(EntityPlayer player){
        boolean isClient = player.world.isRemote;
        this.addSlot(
            CustomSlot.builder()
                .position(181, 108)
                .size(26)
                .setter(BaseChiselingContainer.this::setCurrentStack)
                .getter(this::getCurrentStack)
                .filter(stack -> ChiselingRecipeManager.get(isClient).getRecipeForItem(ItemWithMeta.fromStack(stack)) != null)
                .onChange((oldStack, newStack) -> this.findRecipe())
                .build()
                .getVanillaSlot()
        );
        this.addPlayerSlots(50, 161);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn){
        return !this.shouldBeClosed();
    }

    protected void findRecipe(){
        ItemWithMeta item = ItemWithMeta.fromStack(this.getCurrentStack());
        // Check if the current recipe is still applicable
        ChiselingRecipe recipe = ChiselingRecipeManager.get(this.isClient).getRecipeForItem(item);
        if(recipe != null && this.currentRecipe == recipe && this.currentRecipe.contains(item) && this.currentEntry.contains(item)
            && (this.connecting ? this.currentEntry.getConnectingItem(this.shape).item().equals(item) : this.currentEntry.getRegularItem(this.shape).item().equals(item)))
            return;

        // Find a matching recipe
        if(recipe != null){
            this.currentRecipe = recipe;
            for(ChiselingEntry entry : this.currentRecipe.entries()){
                if(entry.contains(item)){
                    for(ChiselingBlockShape shape : ChiselingBlockShape.values()){
                        if(this.connecting && entry.hasConnectingItem(shape) && entry.getConnectingItem(shape).item().equals(item)){
                            this.currentEntry = entry;
                            this.shape = shape;
                            return;
                        }else if(entry.hasRegularItem(shape) && entry.getRegularItem(shape).item().equals(item)){
                            this.currentEntry = entry;
                            this.shape = shape;
                            this.connecting = false;
                            return;
                        }else if(!this.connecting && entry.getConnectingItem(shape).item().equals(item)){
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
        if(this.currentRecipe == null || this.currentEntry == null || index >= this.currentRecipe.entries().size())
            return;

        ChiselingEntry entry = this.currentRecipe.entries().get(index);
        if(connecting ? !entry.hasConnectingItem(shape) : !entry.hasRegularItem(shape))
            return;

        ItemStack currentStack = this.getCurrentStack();
        ItemWithWorth currentWorth = this.currentRecipe.getWorth(ItemWithMeta.fromStack(currentStack));
        ItemWithWorth target = connecting ? entry.getConnectingItem(shape) : entry.getRegularItem(shape);
        ConversionResult conversion = ChiselingConversionHelper.convert(currentStack.getCount(), currentWorth, target);
        if(conversion.numberOfConversions() <= 0)
            return;

        this.currentEntry = entry;
        this.shape = shape;
        this.connecting = connecting;
        this.setCurrentStack(target.item().toStack(conversion.result()));
        if(conversion.leftover() > 0){
            currentStack = currentStack.copy();
            currentStack.setCount(conversion.leftover());
            int leftover = this.player.inventory.storePartialItemStack(currentStack);
            if(leftover > 0){
                currentStack = currentStack.copy();
                currentStack.setCount(leftover);
                this.player.dropItem(currentStack, true, true);
            }
        }
    }

    public void chiselAll(boolean includeAllShapes){
        if(this.currentRecipe == null)
            return;

        ItemWithWorth target = this.connecting ? this.currentEntry.getConnectingItem(this.shape) : this.currentEntry.getRegularItem(this.shape);
        assert target != null;
        ItemWithMeta targetItem = target.item();

        // Find all space for overflow
        int availableSpace = 0;
        InventoryPlayer inventory = this.player.inventory;
        for(int index = 0; index < inventory.getSizeInventory(); index++){
            ItemStack stack = inventory.getStackInSlot(index);
            if(stack.isEmpty() || targetItem.matches(stack))
                availableSpace += Math.max(0, targetItem.item().getItemStackLimit() - stack.getCount());
        }

        // Replace stacks
        int overflow = 0;
        for(int index = 0; index < inventory.getSizeInventory(); index++){
            ItemStack stack = inventory.getStackInSlot(index);
            ItemWithMeta stackType = ItemWithMeta.fromStack(stack);
            if(stack.isEmpty() || targetItem.matches(stack) || !this.currentRecipe.contains(stackType))
                continue;
            //noinspection DataFlowIssue
            if(stack.hasTagCompound() && !stack.getTagCompound().hasNoTags()) // Safety check to prevent overwriting important items
                continue;

            // Find entry and shape of the stack
            ChiselingEntry stackEntry = null;
            for(ChiselingEntry entry : this.currentRecipe.entries()){
                if(entry.contains(stackType))
                    stackEntry = entry;
            }
            assert stackEntry != null;
            ChiselingBlockShape stackShape = null;
            for(ChiselingBlockShape shape : ChiselingBlockShape.values()){
                if((stackEntry.hasRegularItem(shape) && stackEntry.getRegularItem(shape).item().equals(stackType))
                    || (stackEntry.hasConnectingItem(shape) && stackEntry.getConnectingItem(shape).item().equals(stackType)))
                    stackShape = shape;
            }
            assert stackShape != null;
            if(!includeAllShapes && stackShape != this.shape)
                continue;

            // Calculate how much of the stack can be converted
            ItemWithWorth stackWorth = this.currentRecipe.getWorth(stackType);
            ConversionResult conversion = ChiselingConversionHelper.convert(stack.getCount(), stackWorth, target, availableSpace + targetItem.item().getItemStackLimit());
            boolean canConvertEntireStack = conversion.leftover() <= 0;
            if(!canConvertEntireStack)
                conversion = ChiselingConversionHelper.convert(stack.getCount(), stackWorth, target, availableSpace + targetItem.item().getItemStackLimit());
            if(conversion.numberOfConversions() <= 0)
                continue;

            // Convert stack and add overflow
            if(canConvertEntireStack){
                int newStackSize = Math.min(conversion.result(), targetItem.item().getItemStackLimit());
                inventory.setInventorySlotContents(index, targetItem.toStack(newStackSize));
                overflow += conversion.result() - newStackSize;
                availableSpace -= conversion.result() - newStackSize;
            }else{
                stack = stack.copy();
                stack.setCount(conversion.leftover());
                inventory.setInventorySlotContents(index, stack);
                overflow += conversion.result();
                availableSpace -= conversion.result();
            }
        }

        // Put overflow into player inventory
        if(overflow > 0){
            do{
                int remaining = inventory.storePartialItemStack(targetItem.toStack(overflow));
                if(remaining == overflow)
                    break;
                overflow = remaining;
            }while(overflow > 0);
            if(overflow > 0){
                Rechiseled.LOGGER.error("Failed to insert stacks into player inventory despite the fact there should be sufficient space!");
                this.player.dropItem(targetItem.toStack(overflow), true, true);
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
