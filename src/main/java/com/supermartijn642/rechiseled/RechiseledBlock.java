package com.supermartijn642.rechiseled;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.block.BaseBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created 22/12/2021 by SuperMartijn642
 */
public class RechiseledBlock extends BaseBlock {

    public final boolean connecting;

    public RechiseledBlock(String registryName, boolean connecting, Properties properties){
        super(registryName, false, properties);
        this.connecting = connecting;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> text, ITooltipFlag flag){
        super.appendHoverText(stack, world, text, flag);
        if(this.connecting)
            text.add(TextComponents.translation("rechiseled.tooltip.connecting").color(TextFormatting.GRAY).get());
    }
}
