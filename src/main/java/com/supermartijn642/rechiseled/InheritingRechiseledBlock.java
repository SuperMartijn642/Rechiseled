package com.supermartijn642.rechiseled;

import com.supermartijn642.core.ToolType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class InheritingRechiseledBlock extends RechiseledBlock {

    private final Block parent;

    public InheritingRechiseledBlock(String registryName, boolean connecting, Block parent){
        super(registryName, connecting, Properties.create(parent.getMaterial(parent.getDefaultState())).sound(parent.getSoundType()).setLightLevel(state -> parent.getLightValue(parent.getDefaultState())).slipperiness(parent.slipperiness).harvestTool(parent.getHarvestTool(parent.getDefaultState()) == null ? null : ToolType.get(parent.getHarvestTool(parent.getDefaultState()))).harvestLevel(parent.getHarvestLevel(parent.getDefaultState())));
        this.parent = parent;
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos){
        return this.parent.getBlockHardness(this.parent.getDefaultState(), worldIn, pos);
    }

    @Override
    public float getExplosionResistance(Entity exploder){
        return this.parent.getExplosionResistance(exploder);
    }
}
