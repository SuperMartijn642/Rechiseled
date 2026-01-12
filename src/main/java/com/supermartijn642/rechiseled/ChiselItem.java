package com.supermartijn642.rechiseled;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.item.BaseItem;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.rechiseled.api.chiseling.*;
import com.supermartijn642.rechiseled.packet.PacketChiselBlocks;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.*;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselItem extends BaseItem {

    public static ItemStack getStoredStack(ItemStack chisel){
        CompoundNBT tag = chisel.getOrCreateTag();
        return tag.contains("stack") ? ItemStack.of(tag.getCompound("stack")) : ItemStack.EMPTY;
    }

    public static void setStoredStack(ItemStack chisel, ItemStack stack){
        CompoundNBT tag = chisel.getOrCreateTag();
        if(stack == null || stack.isEmpty())
            tag.remove("stack");
        else
            tag.put("stack", stack.serializeNBT());
    }

    public ChiselItem(){
        super(ItemProperties.create().maxStackSize(1).group(Rechiseled.GROUP));
    }

    @Override
    public ItemUseResult interact(ItemStack stack, PlayerEntity player, Hand hand, World level){
        if(!level.isClientSide)
            CommonUtils.openContainer(new ChiselContainer(player, hand));
        return ItemUseResult.success(stack);
    }

    public boolean leftClickBlock(PlayerEntity player, ItemStack stack, BlockPos pos, Direction side, boolean isShift){
        World level = player.level;
        List<Pair<BlockPos,BlockState>> chiselableBlocks = findChiselableBlocks(level, pos, side, getStoredStack(stack), isShift);
        if(chiselableBlocks.isEmpty())
            return false;
        if(level.isClientSide()){
            Rechiseled.CHANNEL.sendToServer(new PacketChiselBlocks(pos, side, isShift));
            player.playSound(level.getBlockState(pos).getSoundType().getHitSound(), 1, 1);
        }else{
            for(Pair<BlockPos,BlockState> block : chiselableBlocks)
                level.setBlock(block.left(), block.right(), Constants.BlockFlags.DEFAULT);
        }
        return true;
    }

    public static List<Pair<BlockPos,BlockState>> findChiselableBlocks(World level, BlockPos targetedPos, Direction side, ItemStack filter, boolean isShiftDown){
        // Find appropriate chiseling recipe
        if(!filter.isEmpty() && !(filter.getItem() instanceof BlockItem))
            return Collections.emptyList();
        ChiselingRecipe recipe = filter.isEmpty() ? null : ChiselingRecipeManager.get(true).getRecipeForItem(filter.getItem());
        if(!filter.isEmpty() && recipe == null)
            return Collections.emptyList();
        BlockState targetedBlock = ClientUtils.getWorld().getBlockState(targetedPos);
        if(recipe == null){
            recipe = ChiselingRecipeManager.get(true).getRecipeForItem(targetedBlock.getBlock().asItem());
            if(recipe == null)
                return Collections.emptyList();
        }else if(recipe != ChiselingRecipeManager.get(true).getRecipeForItem(targetedBlock.getBlock().asItem()))
            return Collections.emptyList();

        // Get the shape we want to convert
        Item item = targetedBlock.getBlock().asItem();
        ChiselingBlockShape targetShape = null;
        loop:
        for(ChiselingEntry entry : recipe.entries()){
            if(entry.contains(item)){
                for(ChiselingBlockShape shape : ChiselingBlockShape.values()){
                    if((entry.hasRegularItem(shape) && entry.getRegularItem(shape).item() == item)
                        || (entry.hasConnectingItem(shape) && entry.getConnectingItem(shape).item() == item)){
                        targetShape = shape;
                        break loop;
                    }
                }
            }
        }

        // Get filter properties
        Block filterBlock = null;
        if(!filter.isEmpty()){
            item = filter.getItem();
            loop:
            for(ChiselingEntry entry : recipe.entries()){
                if(entry.contains(item)){
                    for(ChiselingBlockShape shape : ChiselingBlockShape.values()){
                        if(entry.hasRegularItem(shape) && entry.getRegularItem(shape).item() == item
                            && entry.hasRegularItem(targetShape) && entry.getRegularItem(targetShape).item() instanceof BlockItem){
                            filterBlock = ((BlockItem)entry.getRegularItem(targetShape).item()).getBlock();
                            break loop;
                        }
                        if(entry.hasConnectingItem(shape) && entry.getConnectingItem(shape).item() == item
                            && entry.hasConnectingItem(targetShape) && entry.getConnectingItem(targetShape).item() instanceof BlockItem){
                            filterBlock = ((BlockItem)entry.getConnectingItem(targetShape).item()).getBlock();
                            break loop;
                        }
                    }
                }
            }
            if(filterBlock == null)
                return Collections.emptyList();
        }

        // Loop over blocks in 3 by 3 around side
        List<Pair<BlockPos,BlockState>> chiselableBlocks = new ArrayList<>();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        int xRange = isShiftDown || side.getAxis() == Direction.Axis.X ? 0 : 1;
        int yRange = isShiftDown || side.getAxis() == Direction.Axis.Y ? 0 : 1;
        int zRange = isShiftDown || side.getAxis() == Direction.Axis.Z ? 0 : 1;
        for(int x = -xRange; x <= xRange; x++){
            for(int y = -yRange; y <= yRange; y++){
                for(int z = -zRange; z <= zRange; z++){
                    // Get the block sate
                    pos.set(targetedPos.getX() + x, targetedPos.getY() + y, targetedPos.getZ() + z);
                    BlockState state = level.getBlockState(pos);
                    if(state.getBlock() == filterBlock)
                        continue;
                    ItemWithWorth worth = recipe.getWorth(state.getBlock());
                    if(worth == null)
                        continue;
                    // Check block is not hidden behind another block
                    pos.set(targetedPos.getX() + x + side.getStepX(), targetedPos.getY() + y + side.getStepY(), targetedPos.getZ() + z + side.getStepZ());
                    VoxelShape occlusionShape = level.getBlockState(pos).getFaceOcclusionShape(level, pos, side.getOpposite());
                    if(occlusionShape == VoxelShapes.block())
                        continue;
                    if(!occlusionShape.isEmpty() && !VoxelShapes.joinIsNotEmpty(VoxelShapes.block(), occlusionShape, IBooleanFunction.ONLY_FIRST))
                        continue;
                    pos.set(targetedPos.getX() + x, targetedPos.getY() + y, targetedPos.getZ() + z);
                    // Find shape of state
                    ChiselingBlockShape shape = null;
                    loop:
                    for(ChiselingEntry entry : recipe.entries()){
                        if(entry.contains(state.getBlock())){
                            for(ChiselingBlockShape s : ChiselingBlockShape.values()){
                                if((entry.hasRegularItem(s) && entry.getRegularItem(s).item() == state.getBlock().asItem())
                                    || (entry.hasConnectingItem(s) && entry.getConnectingItem(s).item() == state.getBlock().asItem())){
                                    shape = s;
                                    break loop;
                                }
                            }
                        }
                    }
                    if(targetShape != null && shape != targetShape)
                        continue;
                    // If there is a filter, use that
                    if(filterBlock != null){
                        chiselableBlocks.add(Pair.of(pos.immutable(), blockWithPropertiesOf(filterBlock, state)));
                        continue;
                    }
                    // Find a random entry with the same shape and worth as the state
                    List<Block> validBlocks = new ArrayList<>();
                    for(ChiselingEntry entry : recipe.entries()){
                        if(entry.contains(state.getBlock()))
                            continue;
                        if(entry.hasRegularItem(shape) && entry.getRegularItem(shape).worth() == worth.worth()){
                            Item regularItem = entry.getRegularItem(shape).item();
                            if(regularItem instanceof BlockItem)
                                validBlocks.add(((BlockItem)regularItem).getBlock());
                        }
                        if(entry.hasConnectingItem(shape) && entry.getConnectingItem(shape).worth() == worth.worth()){
                            Item connectingItem = entry.getConnectingItem(shape).item();
                            if(connectingItem instanceof BlockItem)
                                validBlocks.add(((BlockItem)connectingItem).getBlock());
                        }
                    }
                    if(validBlocks.isEmpty())
                        continue;
                    Block block = validBlocks.get(level.random.nextInt(validBlocks.size()));
                    chiselableBlocks.add(Pair.of(pos.immutable(), blockWithPropertiesOf(block, state)));
                }
            }
        }
        return chiselableBlocks;
    }

    private static BlockState blockWithPropertiesOf(Block block, BlockState copyState){
        Collection<Property<?>> properties = block.getStateDefinition().getProperties();
        BlockState state = block.defaultBlockState();
        for(Map.Entry<Property<?>,Comparable<?>> entry : copyState.getValues().entrySet()){
            Property<?> property = entry.getKey();
            if(properties.contains(property))
                //noinspection unchecked,rawtypes
                state = state.setValue((Property)property, (Comparable)entry.getValue());
        }
        return state;
    }
}
