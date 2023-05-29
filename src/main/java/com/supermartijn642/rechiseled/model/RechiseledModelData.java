package com.supermartijn642.rechiseled.model;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.function.Function;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
@Deprecated
public class RechiseledModelData {

    public Map<Direction,SideData> sides = Maps.newEnumMap(Direction.class);

    public static class SideData {

        public boolean left;
        public boolean right;
        public boolean up;
        public boolean up_left;
        public boolean up_right;
        public boolean down;
        public boolean down_left;
        public boolean down_right;

        public SideData(Direction side, Function<BlockPos,BlockState> blockGetter, BlockPos pos, Block block){
            Direction left;
            Direction right;
            Direction up;
            Direction down;
            if(side.getAxis() == Direction.Axis.Y){
                left = Direction.WEST;
                right = Direction.EAST;
                up = side == Direction.UP ? Direction.NORTH : Direction.SOUTH;
                down = side == Direction.UP ? Direction.SOUTH : Direction.NORTH;
            }else{
                left = side.getClockWise();
                right = side.getCounterClockWise();
                up = Direction.UP;
                down = Direction.DOWN;
            }

            this.left = isSameBlock(blockGetter, block, pos.relative(left));
            this.right = isSameBlock(blockGetter, block, pos.relative(right));
            this.up = isSameBlock(blockGetter, block, pos.relative(up));
            this.up_left = isSameBlock(blockGetter, block, pos.relative(up).relative(left));
            this.up_right = isSameBlock(blockGetter, block, pos.relative(up).relative(right));
            this.down = isSameBlock(blockGetter, block, pos.relative(down));
            this.down_left = isSameBlock(blockGetter, block, pos.relative(down).relative(left));
            this.down_right = isSameBlock(blockGetter, block, pos.relative(down).relative(right));
        }

        public SideData(Direction side, BlockGetter world, BlockPos pos, Block block){
            this(side, world::getBlockState, pos, block);
        }

        private static boolean isSameBlock(Function<BlockPos,BlockState> blockGetter, Block block, BlockPos pos){
            return blockGetter.apply(pos).getBlock() == block;
        }
    }
}
