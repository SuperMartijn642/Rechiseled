package com.supermartijn642.rechiseled.blocks;

import com.supermartijn642.core.block.BlockProperties;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created 04/05/2023 by SuperMartijn642
 */
public class RechiseledPillarBlock extends RechiseledBlock {

    public static final PropertyEnum<EnumFacing.Axis> AXIS_PROPERTY = BlockRotatedPillar.AXIS;

    public RechiseledPillarBlock(boolean connecting, BlockProperties properties){
        super(connecting, properties);
        this.setDefaultState(this.getDefaultState().withProperty(AXIS_PROPERTY, EnumFacing.Axis.Y));
    }

    @Override
    public IBlockState getStateForPlacement(World level, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand){
        return this.getDefaultState().withProperty(AXIS_PROPERTY, side.getAxis());
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rotation){
        if(rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90){
            EnumFacing.Axis axis = state.getValue(AXIS_PROPERTY);
            if(axis != EnumFacing.Axis.Y)
                return state.withProperty(AXIS_PROPERTY, axis == EnumFacing.Axis.X ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
        }
        return state;
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, AXIS_PROPERTY);
    }

    public IBlockState getStateFromMeta(int meta){
        EnumFacing.Axis axis = (meta & 4) == 4 ? EnumFacing.Axis.X
            : (meta & 8) == 8 ? EnumFacing.Axis.Z : EnumFacing.Axis.Y;
        return this.getDefaultState().withProperty(AXIS_PROPERTY, axis);
    }

    public int getMetaFromState(IBlockState state){
        EnumFacing.Axis axis = state.getValue(AXIS_PROPERTY);
        return axis == EnumFacing.Axis.X ? 4 : axis == EnumFacing.Axis.Z ? 8 : 0;
    }
}
