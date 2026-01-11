package com.supermartijn642.rechiseled.blocks.impl;

import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.block.BlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created 20/01/2026 by SuperMartijn642
 */
public class SlabBlock extends BaseBlock {

    public static final PropertyEnum<SlabType> TYPE = PropertyEnum.create("type", SlabType.class);
    private static final AxisAlignedBB BOTTOM_SHAPE = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    private static final AxisAlignedBB TOP_SHAPE = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);

    public SlabBlock(BlockProperties properties){
        super(false, properties);
        this.setDefaultState(this.getDefaultState().withProperty(TYPE, SlabType.BOTTOM));
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(TYPE).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        if(meta >= SlabType.values().length)
            return super.getStateFromMeta(meta);
        return this.getDefaultState().withProperty(TYPE, SlabType.values()[meta]);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess level, BlockPos pos){
        SlabType type = state.getValue(TYPE);
        return type == SlabType.DOUBLE ? Block.FULL_BLOCK_AABB : type == SlabType.BOTTOM ? BOTTOM_SHAPE : TOP_SHAPE;
    }

    @Override
    public boolean isTopSolid(IBlockState state){
        return state.getValue(TYPE) != SlabType.BOTTOM;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess level, IBlockState state, BlockPos pos, EnumFacing side){
        SlabType type = state.getValue(TYPE);
        if(type == SlabType.DOUBLE
            || (type == SlabType.TOP && side == EnumFacing.UP)
            || (type == SlabType.BOTTOM && side == EnumFacing.DOWN))
            return BlockFaceShape.SOLID;
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state){
        return state.getValue(TYPE) == SlabType.DOUBLE;
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess level, BlockPos pos, EnumFacing side){
        SlabType type = state.getValue(TYPE);
        return type == SlabType.DOUBLE
            || (type == SlabType.TOP && side == EnumFacing.UP)
            || (type == SlabType.BOTTOM && side == EnumFacing.DOWN);
    }

    @Override
    public IBlockState getStateForPlacement(World level, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        boolean top = side == EnumFacing.DOWN || (side != EnumFacing.UP && hitY > 0.5f);
        return this.getDefaultState().withProperty(TYPE, top ? SlabType.TOP : SlabType.BOTTOM);
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random){
        return state.getValue(TYPE) == SlabType.DOUBLE ? 2 : 1;
    }

    @Override
    public boolean isFullCube(IBlockState state){
        return state.getValue(TYPE) == SlabType.DOUBLE;
    }
}
