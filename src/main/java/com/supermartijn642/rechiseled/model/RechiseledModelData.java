package com.supermartijn642.rechiseled.model;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Map;
import java.util.function.Function;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledModelData {

    public Map<EnumFacing,SideData> sides = Maps.newEnumMap(EnumFacing.class);

    public static class SideData {

        public boolean left;
        public boolean right;
        public boolean up;
        public boolean up_left;
        public boolean up_right;
        public boolean down;
        public boolean down_left;
        public boolean down_right;

        public SideData(EnumFacing side, Function<BlockPos,IBlockState> blockGetter, BlockPos pos, Block block){
            EnumFacing left;
            EnumFacing right;
            EnumFacing up;
            EnumFacing down;
            if(side.getAxis() == EnumFacing.Axis.Y){
                left = EnumFacing.WEST;
                right = EnumFacing.EAST;
                up = side == EnumFacing.UP ? EnumFacing.NORTH : EnumFacing.SOUTH;
                down = side == EnumFacing.UP ? EnumFacing.SOUTH : EnumFacing.NORTH;
            }else{
                left = side.rotateY();
                right = side.rotateYCCW();
                up = EnumFacing.UP;
                down = EnumFacing.DOWN;
            }

            this.left = isSameBlock(blockGetter, block, pos.offset(left));
            this.right = isSameBlock(blockGetter, block, pos.offset(right));
            this.up = isSameBlock(blockGetter, block, pos.offset(up));
            this.up_left = isSameBlock(blockGetter, block, pos.offset(up).offset(left));
            this.up_right = isSameBlock(blockGetter, block, pos.offset(up).offset(right));
            this.down = isSameBlock(blockGetter, block, pos.offset(down));
            this.down_left = isSameBlock(blockGetter, block, pos.offset(down).offset(left));
            this.down_right = isSameBlock(blockGetter, block, pos.offset(down).offset(right));
        }

        public SideData(EnumFacing side, IBlockAccess world, BlockPos pos, Block block){
            this(side, world::getBlockState, pos, block);
        }

        private static boolean isSameBlock(Function<BlockPos,IBlockState> blockGetter, Block block, BlockPos pos){
            return blockGetter.apply(pos).getBlock() == block;
        }
    }
}
