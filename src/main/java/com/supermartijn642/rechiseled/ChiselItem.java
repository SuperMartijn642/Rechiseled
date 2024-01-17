package com.supermartijn642.rechiseled;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.item.BaseItem;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.rechiseled.api.ChiselingWorld;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipes;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselItem extends BaseItem {

    public ChiselItem(){
        super(ItemProperties.create().maxStackSize(1).group(Rechiseled.GROUP));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand){
        ItemStack stack = player.getItemInHand(hand);
        if(!world.isClientSide) {
            if(player.isCrouching())
                pickItemFromWorld(player, hand, stack);

            ChiselContainer container = new ChiselContainer(player, hand);
            CommonUtils.openContainer(container);
        }
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        if(player.isCrouching())
            return true;

        ItemStack storedStack = getStoredStack(itemstack);
        if(storedStack.isEmpty())
            return true;

        ChiselingRecipe recipe = ChiselingRecipes.getRecipe(storedStack);
        if (recipe == null)
            return true;

        Level level = player.level();
        BlockState blockState = level.getBlockState(pos);
        if (!(blockState.getBlock().asItem() instanceof BlockItem blockItem))
            return true;

        if(recipe.contains(blockItem.getDefaultInstance())){
            if(!(storedStack.getItem() instanceof BlockItem stackBlockItem) || blockItem == stackBlockItem)
                return true;

            level.setBlock(pos, stackBlockItem.getBlock().defaultBlockState(), 1);
            level.playSound(null, pos, SoundEvents.CALCITE_HIT, SoundSource.BLOCKS);
            return true;
        }

        return true;
    }

    public static ItemStack getStoredStack(ItemStack chisel){
        CompoundTag tag = chisel.getOrCreateTag();
        return tag.contains("stack") ? ItemStack.of(tag.getCompound("stack")) : ItemStack.EMPTY;
    }

    public static void setStoredStack(ItemStack chisel, ItemStack stack){
        CompoundTag tag = chisel.getOrCreateTag();
        if(stack == null || stack.isEmpty())
            tag.remove("stack");
        else
            tag.put("stack", stack.serializeNBT());
    }

    public void pickItemFromWorld(Player player, InteractionHand hand, ItemStack chisel){
        if(chisel.getItem() != Rechiseled.chisel)
            return;

        BlockItem blockItem = ChiselingWorld.pickBlockItemFromWorld(player);
        if(blockItem == null)
            return;

        // find the recipe for the picked world block
        ChiselingRecipe recipeForBlock = null;
        List<ChiselingRecipe> allRecipes = ChiselingRecipes.getAllRecipes();

        ItemStack defaultInstance = blockItem.asItem().getDefaultInstance();
        for(int i = 0; i < allRecipes.size(); ++i){
            ChiselingRecipe recipe = allRecipes.get(i);

            if(!recipe.contains(defaultInstance))
                continue;

            recipeForBlock = recipe;
            break;
        }

        if(recipeForBlock == null)
            return;

        // update the stack type or swap it out with a valid stack from the players inventory
        ItemStack chiselStack = getStoredStack(chisel);
        if(chiselStack.getItem() == blockItem) // we already have the item and don't need to do anything
            return;

        NonNullList<ItemStack> playerItems = player.getInventory().items; // try to grab matching item first
        for(int i = 0; i < playerItems.size(); ++i){
            ItemStack inventoryStack = playerItems.get(i);
            Item item = inventoryStack.getItem();
            if(item != blockItem)
                continue;

            pickChiselItemFromInventory(chisel, chiselStack, inventoryStack, blockItem, player.getInventory(), i);

            return;
        }

        if(!chiselStack.isEmpty() && recipeForBlock.contains(chiselStack)){ // no stack in inventory, try setting the stack already in the chisel
            int indexOf = recipeForBlock.indexOf(defaultInstance);
            if(indexOf > -1){
                ItemStack newStack = new ItemStack(blockItem);
                newStack.setCount(chiselStack.getCount());

                setStoredStack(chisel, newStack);
                return;
            }

        }

        for(int i = 0; i < playerItems.size(); ++i){ // grab an item which is part of the same recipe
            ItemStack inventoryStack = playerItems.get(i);
            if (!recipeForBlock.contains(inventoryStack))
                continue;

            pickChiselItemFromInventory(chisel, chiselStack, inventoryStack, blockItem, player.getInventory(), i);
            return;
        }
    }

    private void pickChiselItemFromInventory(ItemStack chisel, ItemStack chiselStack, ItemStack inventoryStack, Item blockItem,
                                             Inventory playerInventory, int inventoryIndex){
        if(!chiselStack.isEmpty()){
            ItemStack newInventoryStack = chiselStack.copy();
            ItemStack newChiselStack = new ItemStack(blockItem);
            newChiselStack.setCount(inventoryStack.getCount());

            playerInventory.setItem(inventoryIndex, newInventoryStack);

            setStoredStack(chisel, newChiselStack);
        }else{
            ItemStack newChiselStack = new ItemStack(blockItem);
            newChiselStack.setCount(inventoryStack.getCount());
            playerInventory.setItem(inventoryIndex, ItemStack.EMPTY);

            setStoredStack(chisel, newChiselStack);
        }
    }
}
