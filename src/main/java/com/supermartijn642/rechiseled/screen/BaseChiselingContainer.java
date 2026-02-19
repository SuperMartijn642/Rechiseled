package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.gui.BaseContainer;
import com.supermartijn642.core.gui.BaseContainerType;
import com.supermartijn642.core.gui.CustomSlot;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.*;
import com.supermartijn642.rechiseled.api.chiseling.conversion.ChiselingConversionHelper;
import com.supermartijn642.rechiseled.api.chiseling.conversion.ConversionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
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
        this.isClient = player.level().isClientSide();
        this.addSlots();
    }

    @Override
    protected void addSlots(Player player){
        //noinspection resource
        boolean isClient = player.level().isClientSide();
        this.addSlot(
            CustomSlot.builder()
                .position(181, 108)
                .size(26)
                .setter(this::setCurrentStack)
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
            && (this.connecting ? this.currentEntry.getConnectingItem(this.shape).item() == item : this.currentEntry.getRegularItem(this.shape).item() == item))
            return;

        // Find a matching recipe
        if(recipe != null){
            this.currentRecipe = recipe;
            for(ChiselingEntry entry : this.currentRecipe.entries()){
                if(entry.contains(item)){
                    for(ChiselingBlockShape shape : ChiselingBlockShape.values()){
                        if(this.connecting && entry.hasConnectingItem(shape) && entry.getConnectingItem(shape).item() == item){
                            this.currentEntry = entry;
                            this.shape = shape;
                            return;
                        }else if(entry.hasRegularItem(shape) && entry.getRegularItem(shape).item() == item){
                            this.currentEntry = entry;
                            this.shape = shape;
                            this.connecting = false;
                            return;
                        }else if(!this.connecting && entry.hasConnectingItem(shape) && entry.getConnectingItem(shape).item() == item){
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
        ItemWithWorth currentWorth = this.currentRecipe.getWorth(currentStack.getItem());
        ItemWithWorth target = connecting ? entry.getConnectingItem(shape) : entry.getRegularItem(shape);
        ConversionResult conversion = ChiselingConversionHelper.convert(currentStack.getCount(), currentWorth, target);
        if(conversion.numberOfConversions() <= 0)
            return;

        this.currentEntry = entry;
        this.shape = shape;
        this.connecting = connecting;
        this.setCurrentStack(new ItemStack(target.item(), conversion.result()));
        if(conversion.leftover() > 0){
            currentStack = currentStack.copyWithCount(conversion.leftover());
            int leftover = this.player.getInventory().addResource(currentStack);
            if(leftover > 0)
                this.player.drop(currentStack.copyWithCount(leftover), true, true);
        }
    }

    public void chiselAll(boolean includeAllShapes){
        if(this.currentRecipe == null)
            return;

        ItemWithWorth target = this.connecting ? this.currentEntry.getConnectingItem(this.shape) : this.currentEntry.getRegularItem(this.shape);
        assert target != null;
        Item targetItem = target.item();

        // Find all space for overflow
        int availableSpace = 0;
        Inventory inventory = this.player.getInventory();
        for(int index = 0; index < inventory.getContainerSize(); index++){
            ItemStack stack = inventory.getItem(index);
            if(stack.isEmpty() || stack.getItem() == targetItem)
                //noinspection deprecation
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
                if((stackEntry.hasRegularItem(shape) && stackEntry.getRegularItem(shape).item() == stack.getItem())
                    || (stackEntry.hasConnectingItem(shape) && stackEntry.getConnectingItem(shape).item() == stack.getItem()))
                    stackShape = shape;
            }
            assert stackShape != null;
            if(!includeAllShapes && stackShape != this.shape)
                continue;

            // Calculate how much of the stack can be converted
            ItemWithWorth stackWorth = this.currentRecipe.getWorth(stack.getItem());
            //noinspection deprecation
            ConversionResult conversion = ChiselingConversionHelper.convert(stack.getCount(), stackWorth, target, availableSpace + targetItem.getMaxStackSize());
            boolean canConvertEntireStack = conversion.leftover() <= 0;
            if(!canConvertEntireStack)
                //noinspection deprecation
                conversion = ChiselingConversionHelper.convert(stack.getCount(), stackWorth, target, availableSpace + targetItem.getMaxStackSize());
            if(conversion.numberOfConversions() <= 0)
                continue;

            // Convert stack and add overflow
            if(canConvertEntireStack){
                //noinspection deprecation
                int newStackSize = Math.min(conversion.result(), targetItem.getMaxStackSize());
                inventory.setItem(index, new ItemStack(targetItem, newStackSize));
                overflow += conversion.result() - newStackSize;
                availableSpace -= conversion.result() - newStackSize;
            }else{
                inventory.setItem(index, stack.copyWithCount(conversion.leftover()));
                overflow += conversion.result();
                availableSpace -= conversion.result();
            }
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

        // Create a copy as the stack will be modified
        stack = stack.copy();
        // Transfer stack from chisel to inventory or vice versa
        if(index == 0){
            if(!this.moveItemStackTo(stack, 1, this.slots.size(), true))
                return ItemStack.EMPTY;
        }else{
            if(!this.moveItemStackTo(stack, 0, 1, true))
                return ItemStack.EMPTY;
        }

        // Update slot
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
                    slot.set(stack.split(Math.min(slot.getMaxStackSize(), stack.getMaxStackSize())));
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

    @Override
    public void clicked(int slotIndex, int inventoryIndex, ClickType clickType, Player player){
        // Prevent swapping oversized stacks from the chisel to the hotbar
        if(clickType == ClickType.SWAP && slotIndex == 0 && (inventoryIndex >= 0 && inventoryIndex < 9 || inventoryIndex == 40)){
            ItemStack currentStack = this.getCurrentStack();
            Inventory inventory = player.getInventory();
            if(!currentStack.isEmpty() && (currentStack.getCount() > currentStack.getMaxStackSize() || currentStack.getCount() > inventory.getMaxStackSize())){
                ItemStack inventoryStack = inventory.getItem(inventoryIndex);
                if(!inventoryStack.isEmpty() && !ItemStack.isSameItemSameTags(currentStack, inventoryStack))
                    return;
                int maxSize = Math.min(currentStack.getMaxStackSize(), inventory.getMaxStackSize());
                int transfer = maxSize - inventoryStack.getCount();
                if(transfer <= 0)
                    return;
                currentStack.shrink(transfer);
                this.setCurrentStack(currentStack);
                inventory.setItem(inventoryIndex, currentStack.copyWithCount(maxSize));
                return;
            }
        }
        super.clicked(slotIndex, inventoryIndex, clickType, player);
    }
}
