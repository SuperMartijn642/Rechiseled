package com.supermartijn642.rechiseled;

import com.google.common.collect.ImmutableSet;
import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.item.BaseItem;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.rechiseled.api.chiseling.*;
import com.supermartijn642.rechiseled.api.util.ItemWithMeta;
import com.supermartijn642.rechiseled.blocks.impl.SlabBlock;
import com.supermartijn642.rechiseled.blocks.impl.SlabType;
import com.supermartijn642.rechiseled.packet.PacketChiselBlocks;
import com.supermartijn642.rechiseled.screen.ChiselContainer;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ChiselItem extends BaseItem {

    public static ItemStack getStoredStack(ItemStack chisel){
        NBTTagCompound tag = chisel.getTagCompound();
        if(tag == null || !tag.hasKey("stack"))
            return ItemStack.EMPTY;
        ItemStack stack = new ItemStack(tag.getCompoundTag("stack"));
        if(tag.hasKey("stackCount"))
            stack.setCount(tag.getInteger("stackCount"));
        return stack;
    }

    public static void setStoredStack(ItemStack chisel, ItemStack stack){
        NBTTagCompound tag = chisel.hasTagCompound() ? chisel.getTagCompound() : new NBTTagCompound();
        if(stack == null || stack.isEmpty()){
            tag.removeTag("stack");
            tag.removeTag("stackCount");
        }else{
            tag.setTag("stack", stack.serializeNBT());
            tag.setInteger("stackCount", stack.getCount());
        }
        chisel.setTagCompound(tag);
    }

    public ChiselItem(){
        super(ItemProperties.create().maxStackSize(1).group(Rechiseled.GROUP));
    }

    @Override
    public ItemUseResult interact(ItemStack stack, EntityPlayer player, EnumHand hand, World level){
        if(!level.isRemote)
            CommonUtils.openContainer(new ChiselContainer(player, hand));
        return ItemUseResult.success(stack);
    }

    @Override
    protected void appendItemInformation(ItemStack stack, @Nullable IBlockAccess level, Consumer<ITextComponent> info, boolean advanced){
        ItemStack storedStack = getStoredStack(stack);
        if(!storedStack.isEmpty())
            info.accept(TextComponents.item(storedStack.getItem()).color(TextFormatting.GRAY).italic().get());
        super.appendItemInformation(stack, level, info, advanced);
    }

    public boolean leftClickBlock(EntityPlayer player, ItemStack stack, BlockPos pos, EnumFacing side, boolean isShift){
        World level = player.world;
        List<Pair<BlockPos,IBlockState>> chiselableBlocks = findChiselableBlocks(player, level, pos, side, getStoredStack(stack), isShift);
        if(chiselableBlocks.isEmpty())
            return false;
        if(level.isRemote){
            Rechiseled.CHANNEL.sendToServer(new PacketChiselBlocks(pos, side, isShift));
            IBlockState state = level.getBlockState(pos);
            player.playSound(state.getBlock().getSoundType(state, level, pos, player).getHitSound(), 1, 1);
        }else{
            for(Pair<BlockPos,IBlockState> block : chiselableBlocks)
                level.setBlockState(block.left(), block.right(), Constants.BlockFlags.DEFAULT);
        }
        return true;
    }

    public static List<Pair<BlockPos,IBlockState>> findChiselableBlocks(EntityPlayer player, World level, BlockPos targetedPos, EnumFacing side, ItemStack filter, boolean isShiftDown){
        // Find appropriate chiseling recipe
        if(!filter.isEmpty() && !(filter.getItem() instanceof ItemBlock))
            return Collections.emptyList();
        ChiselingRecipe recipe = filter.isEmpty() ? null : ChiselingRecipeManager.get(level).getRecipeForItem(ItemWithMeta.fromStack(filter));
        if(!filter.isEmpty() && recipe == null)
            return Collections.emptyList();
        IBlockState targetedBlock = level.getBlockState(targetedPos);
        if(recipe == null){
            recipe = ChiselingRecipeManager.get(level).getRecipeForItem(ItemWithMeta.of(targetedBlock.getBlock()));
            if(recipe == null)
                return Collections.emptyList();
        }else if(recipe != ChiselingRecipeManager.get(level).getRecipeForItem(ItemWithMeta.of(targetedBlock.getBlock())))
            return Collections.emptyList();

        // Get the shape we want to convert
        //noinspection deprecation
        ItemWithMeta item = ItemWithMeta.fromStack(targetedBlock.getBlock().getItem(level, targetedPos, targetedBlock));
        ChiselingBlockShape targetShape = null;
        loop:
        for(ChiselingEntry entry : recipe.entries()){
            if(entry.contains(item)){
                for(ChiselingBlockShape shape : ChiselingBlockShape.values()){
                    if((entry.hasRegularItem(shape) && entry.getRegularItem(shape).item().equals(item))
                        || (entry.hasConnectingItem(shape) && entry.getConnectingItem(shape).item().equals(item))){
                        targetShape = shape;
                        break loop;
                    }
                }
            }
        }

        // Get filter properties
        Block filterBlock = null;
        if(!filter.isEmpty()){
            item = ItemWithMeta.fromStack(filter);
            loop:
            for(ChiselingEntry entry : recipe.entries()){
                if(entry.contains(item)){
                    for(ChiselingBlockShape shape : ChiselingBlockShape.values()){
                        if(entry.hasRegularItem(shape) && entry.getRegularItem(shape).item().equals(item)
                            && entry.hasRegularItem(targetShape) && entry.getRegularItem(targetShape).item().item() instanceof ItemBlock){
                            filterBlock = ((ItemBlock)entry.getRegularItem(targetShape).item().item()).getBlock();
                            break loop;
                        }
                        if(entry.hasConnectingItem(shape) && entry.getConnectingItem(shape).item().equals(item)
                            && entry.hasConnectingItem(targetShape) && entry.getConnectingItem(targetShape).item().item() instanceof ItemBlock){
                            filterBlock = ((ItemBlock)entry.getConnectingItem(targetShape).item().item()).getBlock();
                            break loop;
                        }
                    }
                }
            }
            if(filterBlock == null)
                return Collections.emptyList();
        }

        // Loop over blocks in 3 by 3 around side
        List<Pair<BlockPos,IBlockState>> chiselableBlocks = new ArrayList<>();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int xRange = isShiftDown || side.getAxis() == EnumFacing.Axis.X ? 0 : 1;
        int yRange = isShiftDown || side.getAxis() == EnumFacing.Axis.Y ? 0 : 1;
        int zRange = isShiftDown || side.getAxis() == EnumFacing.Axis.Z ? 0 : 1;
        for(int x = -xRange; x <= xRange; x++){
            for(int y = -yRange; y <= yRange; y++){
                for(int z = -zRange; z <= zRange; z++){
                    // Get the block sate
                    pos.setPos(targetedPos.getX() + x, targetedPos.getY() + y, targetedPos.getZ() + z);
                    IBlockState state = level.getBlockState(pos);
                    if(state.getBlock() == filterBlock)
                        continue;
                    //noinspection deprecation
                    ItemWithMeta stateItem = ItemWithMeta.fromStack(state.getBlock().getItem(level, pos, state));
                    ItemWithWorth worth = recipe.getWorth(stateItem);
                    if(worth == null)
                        continue;
                    // Check block is not hidden behind another block
                    pos.setPos(targetedPos.getX() + x + side.getFrontOffsetX(), targetedPos.getY() + y + side.getFrontOffsetY(), targetedPos.getZ() + z + side.getFrontOffsetZ());
                    if(level.getBlockState(pos).doesSideBlockRendering(level, pos, side.getOpposite()))
                        continue;
                    pos.setPos(targetedPos.getX() + x, targetedPos.getY() + y, targetedPos.getZ() + z);
                    // Find shape of state
                    ChiselingBlockShape shape = null;
                    loop:
                    for(ChiselingEntry entry : recipe.entries()){
                        if(entry.contains(stateItem)){
                            for(ChiselingBlockShape s : ChiselingBlockShape.values()){
                                if((entry.hasRegularItem(s) && entry.getRegularItem(s).item().equals(stateItem))
                                    || (entry.hasConnectingItem(s) && entry.getConnectingItem(s).item().equals(stateItem))){
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
                        IBlockState newState = filterBlock.getStateForPlacement(level, pos, side, 0.5f, 0.5f, 0.5f, filter.getMetadata(), player, EnumHand.MAIN_HAND);
                        chiselableBlocks.add(Pair.of(pos.toImmutable(), stateWithPropertiesOf(newState, state)));
                        continue;
                    }
                    // Find a random entry with the same shape and worth as the state
                    List<ItemWithMeta> validBlocks = new ArrayList<>();
                    for(ChiselingEntry entry : recipe.entries()){
                        if(entry.contains(stateItem))
                            continue;
                        if(entry.hasRegularItem(shape) && entry.getRegularItem(shape).worth() == worth.worth()){
                            ItemWithMeta regularItem = entry.getRegularItem(shape).item();
                            if(regularItem.item() instanceof ItemBlock)
                                validBlocks.add(regularItem);
                        }
                        if(entry.hasConnectingItem(shape) && entry.getConnectingItem(shape).worth() == worth.worth()){
                            ItemWithMeta connectingItem = entry.getConnectingItem(shape).item();
                            if(connectingItem.item() instanceof ItemBlock)
                                validBlocks.add(connectingItem);
                        }
                    }
                    if(validBlocks.isEmpty())
                        continue;
                    ItemWithMeta choice = validBlocks.get(level.rand.nextInt(validBlocks.size()));
                    IBlockState newState = ((ItemBlock)choice.item()).getBlock().getStateForPlacement(level, pos, side, 0.5f, 0.5f, 0.5f, choice.meta(), player, EnumHand.MAIN_HAND);
                    chiselableBlocks.add(Pair.of(pos.toImmutable(), stateWithPropertiesOf(newState, state)));
                }
            }
        }
        return chiselableBlocks;
    }

    private static final Set<IProperty<?>> PROPERTIES_TO_COPY = ImmutableSet.of(
        BlockHorizontal.FACING,
        BlockRotatedPillar.AXIS,
        BlockStairs.HALF, BlockStairs.SHAPE,
        BlockSlab.HALF,
        SlabBlock.TYPE,
        BlockFence.NORTH, BlockFence.SOUTH, BlockFence.EAST, BlockFence.WEST,
        BlockPane.NORTH, BlockPane.SOUTH, BlockPane.EAST, BlockPane.WEST
    );

    private static IBlockState stateWithPropertiesOf(IBlockState state, IBlockState copyState){
        Collection<IProperty<?>> properties = state.getPropertyKeys();
        if(copyState.getPropertyKeys().contains(BlockSlab.HALF) && properties.contains(SlabBlock.TYPE))
            state = state.withProperty(SlabBlock.TYPE, copyState.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.BOTTOM ? SlabType.BOTTOM : SlabType.TOP);
        for(Map.Entry<IProperty<?>,Comparable<?>> entry : copyState.getProperties().entrySet()){
            IProperty<?> property = entry.getKey();
            if(PROPERTIES_TO_COPY.contains(property) && properties.contains(property))
                //noinspection unchecked,rawtypes
                state = state.withProperty((IProperty)property, (Comparable)entry.getValue());
        }
        return state;
    }
}
