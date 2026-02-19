package com.supermartijn642.rechiseled;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.item.BaseItem;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.rechiseled.api.chiseling.*;
import com.supermartijn642.rechiseled.packet.PacketChiselBlocks;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselItem extends BaseItem {

    public static ItemStack getStoredStack(ItemStack chisel){
        CompoundTag tag = chisel.getOrCreateTag();
        if(!tag.contains("stack"))
            return ItemStack.EMPTY;
        ItemStack stack = ItemStack.of(tag.getCompound("stack"));
        if(tag.contains("stackCount"))
            stack.setCount(tag.getInt("stackCount"));
        return stack;
    }

    public static void setStoredStack(ItemStack chisel, ItemStack stack){
        CompoundTag tag = chisel.getOrCreateTag();
        if(stack == null || stack.isEmpty()){
            tag.remove("stack");
            tag.remove("stackCount");
        }else{
            tag.put("stack", stack.serializeNBT());
            tag.putInt("stackCount", stack.getCount());
        }
    }

    public ChiselItem(){
        super(ItemProperties.create().maxStackSize(1).group(Rechiseled.GROUP));
    }

    @Override
    public ItemUseResult interact(ItemStack stack, Player player, InteractionHand hand, Level level){
        if(!level.isClientSide())
            CommonUtils.openContainer(new ChiselContainer(player, hand));
        return ItemUseResult.success(stack);
    }

    @Override
    protected void appendItemInformation(ItemStack stack, @Nullable BlockGetter level, Consumer<Component> info, boolean advanced){
        ItemStack storedStack = getStoredStack(stack);
        if(!storedStack.isEmpty())
            info.accept(TextComponents.item(storedStack.getItem()).color(ChatFormatting.GRAY).italic().get());
        super.appendItemInformation(stack, level, info, advanced);
    }

    public boolean leftClickBlock(Player player, ItemStack stack, BlockPos pos, Direction side, boolean isShift){
        Level level = player.level();
        List<Pair<BlockPos,BlockState>> chiselableBlocks = findChiselableBlocks(level, pos, side, getStoredStack(stack), isShift);
        if(chiselableBlocks.isEmpty())
            return false;
        if(level.isClientSide()){
            Rechiseled.CHANNEL.sendToServer(new PacketChiselBlocks(pos, side, isShift));
            player.playSound(level.getBlockState(pos).getSoundType().getHitSound());
        }else{
            for(Pair<BlockPos,BlockState> block : chiselableBlocks)
                level.setBlock(block.left(), block.right(), Block.UPDATE_ALL);
        }
        return true;
    }

    public static List<Pair<BlockPos,BlockState>> findChiselableBlocks(Level level, BlockPos targetedPos, Direction side, ItemStack filter, boolean isShiftDown){
        // Find appropriate chiseling recipe
        if(!filter.isEmpty() && !(filter.getItem() instanceof BlockItem))
            return List.of();
        ChiselingRecipe recipe = filter.isEmpty() ? null : ChiselingRecipeManager.get(level).getRecipeForItem(filter.getItem());
        if(!filter.isEmpty() && recipe == null)
            return List.of();
        BlockState targetedBlock = level.getBlockState(targetedPos);
        if(recipe == null){
            recipe = ChiselingRecipeManager.get(level).getRecipeForItem(targetedBlock.getBlock().asItem());
            if(recipe == null)
                return List.of();
        }else if(recipe != ChiselingRecipeManager.get(level).getRecipeForItem(targetedBlock.getBlock().asItem()))
            return List.of();

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
                return List.of();
        }

        // Loop over blocks in 3 by 3 around side
        List<Pair<BlockPos,BlockState>> chiselableBlocks = new ArrayList<>();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
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
                    if(occlusionShape == Shapes.block())
                        continue;
                    if(!occlusionShape.isEmpty() && !Shapes.joinIsNotEmpty(Shapes.block(), occlusionShape, BooleanOp.ONLY_FIRST))
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
                        chiselableBlocks.add(Pair.of(pos.immutable(), filterBlock.withPropertiesOf(state)));
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
                    chiselableBlocks.add(Pair.of(pos.immutable(), block.withPropertiesOf(state)));
                }
            }
        }
        return chiselableBlocks;
    }
}
