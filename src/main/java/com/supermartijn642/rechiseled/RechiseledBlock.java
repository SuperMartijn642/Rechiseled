package com.supermartijn642.rechiseled;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.block.BaseBlock;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created 22/12/2021 by SuperMartijn642
 */
public class RechiseledBlock extends BaseBlock {

    public static final IUnlistedProperty<IBlockAccess> WORLD_PROPERTY = new IUnlistedProperty<IBlockAccess>() {
        @Override
        public String getName(){
            return "world";
        }

        @Override
        public boolean isValid(IBlockAccess value){
            return value != null;
        }

        @Override
        public Class<IBlockAccess> getType(){
            return IBlockAccess.class;
        }

        @Override
        public String valueToString(IBlockAccess value){
            return value.getWorldType().getName();
        }
    };
    public static final IUnlistedProperty<BlockPos> POSITION_PROPERTY = new IUnlistedProperty<BlockPos>() {
        @Override
        public String getName(){
            return "position";
        }

        @Override
        public boolean isValid(BlockPos value){
            return value != null;
        }

        @Override
        public Class<BlockPos> getType(){
            return BlockPos.class;
        }

        @Override
        public String valueToString(BlockPos value){
            return value.toString();
        }
    };

    public final boolean connecting;

    public RechiseledBlock(String registryName, boolean connecting, Properties properties){
        super(registryName, false, properties);
        this.connecting = connecting;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> text, ITooltipFlag flag){
        super.addInformation(stack, world, text, flag);
        if(this.connecting)
            text.add(TextComponents.translation("rechiseled.tooltip.connecting").color(TextFormatting.GRAY).format());
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos){
        IExtendedBlockState extendedBlockState = (IExtendedBlockState)state;
        extendedBlockState = extendedBlockState.withProperty(WORLD_PROPERTY, world);
        extendedBlockState = extendedBlockState.withProperty(POSITION_PROPERTY, pos);
        return extendedBlockState;
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer.Builder(this).add(WORLD_PROPERTY, POSITION_PROPERTY).build();
    }
}
