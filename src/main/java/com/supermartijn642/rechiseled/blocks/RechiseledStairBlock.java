package com.supermartijn642.rechiseled.blocks;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.translation.I18n;

/**
 * Created 11/01/2026 by SuperMartijn642
 */
public class RechiseledStairBlock extends BlockStairs {

    public static final PropertyDirection FACING = BlockStairs.FACING;
    public static final PropertyEnum<BlockStairs.EnumHalf> HALF = BlockStairs.HALF;
    public static final PropertyEnum<BlockStairs.EnumShape> SHAPE = BlockStairs.SHAPE;

    private final boolean connecting;

    public RechiseledStairBlock(boolean connecting, IBlockState parent){
        super(parent);
        this.connecting = connecting;
    }

    public boolean isConnecting(){
        return this.connecting;
    }

    public String getLocalizedName(){
        //noinspection deprecation
        return I18n.translateToLocal(this.getUnlocalizedName()).trim();
    }

    public String getUnlocalizedName(){
        return this.getRegistryName().getResourceDomain() + ".block." + this.getRegistryName().getResourcePath();
    }
}
