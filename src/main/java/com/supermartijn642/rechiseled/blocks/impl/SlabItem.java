package com.supermartijn642.rechiseled.blocks.impl;

import com.supermartijn642.core.item.BaseBlockItem;
import com.supermartijn642.core.item.ItemProperties;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created 24/01/2026 by SuperMartijn642
 */
public class SlabItem extends BaseBlockItem {

    public SlabItem(Block block, ItemProperties properties){
        super(block, properties);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World level, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
        ItemStack stack = player.getHeldItem(hand);
        if(stack.isEmpty() || !player.canPlayerEdit(pos.offset(side), side, stack))
            return EnumActionResult.FAIL;

        // Check if the clicked on block is the same as this item and the click on side is opposite to the slab's half
        IBlockState state = level.getBlockState(pos);
        if(state.getBlock() == this.block && state.getValue(SlabBlock.TYPE) != SlabType.DOUBLE
            && (side == EnumFacing.UP ^ state.getValue(SlabBlock.TYPE) == SlabType.TOP)){
            IBlockState newState = this.block.getDefaultState().withProperty(SlabBlock.TYPE, SlabType.DOUBLE);
            AxisAlignedBB collisionBox = newState.getCollisionBoundingBox(level, pos);
            // Check collision box and place block
            if(collisionBox != Block.NULL_AABB && level.checkNoEntityCollision(collisionBox.offset(pos))
                && level.setBlockState(pos, newState, Constants.BlockFlags.DEFAULT_AND_RERENDER)){
                // Play sound
                SoundType soundtype = this.block.getSoundType(newState, level, pos, player);
                level.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1) / 2, soundtype.getPitch() * 0.8f);
                // Remove item
                stack.shrink(1);
                if(player instanceof EntityPlayerMP)
                    CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
            }
            return EnumActionResult.SUCCESS;
        }

        // If the neighboring block to the side that was clicked on is a half slab of the same block as this item, place a double slab
        BlockPos offset = pos.offset(side);
        state = level.getBlockState(offset);
        if(state.getBlock() == this.block && state.getValue(SlabBlock.TYPE) != SlabType.DOUBLE){
            IBlockState newState = this.block.getDefaultState().withProperty(SlabBlock.TYPE, SlabType.DOUBLE);
            AxisAlignedBB collisionBox = newState.getCollisionBoundingBox(level, offset);
            // Check collision box and place block
            if(collisionBox != Block.NULL_AABB && level.checkNoEntityCollision(collisionBox.offset(offset)) && level.setBlockState(offset, newState, 11)){
                // Play sound
                SoundType soundtype = this.block.getSoundType(newState, level, offset, player);
                level.playSound(player, offset, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1) / 2, soundtype.getPitch() * 0.8f);
                // Remove item
                stack.shrink(1);
            }
            return EnumActionResult.SUCCESS;
        }

        // Try to place block normally
        return super.onItemUse(player, level, pos, hand, side, hitX, hitY, hitZ);
    }

    @SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack){
        IBlockState state = worldIn.getBlockState(pos);
        if(state.getBlock() == this.block && state.getValue(SlabBlock.TYPE) != SlabType.DOUBLE
            && (side == EnumFacing.UP ^ state.getValue(SlabBlock.TYPE) == SlabType.TOP))
            return true;
        state = worldIn.getBlockState(pos.offset(side));
        if(state.getBlock() == this.block && state.getValue(SlabBlock.TYPE) != SlabType.DOUBLE)
            return true;
        return super.canPlaceBlockOnSide(worldIn, pos, side, player, stack);
    }
}
