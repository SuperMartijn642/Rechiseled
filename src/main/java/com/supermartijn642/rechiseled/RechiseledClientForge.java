package com.supermartijn642.rechiseled;

import com.supermartijn642.rechiseled.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RechiseledClientForge {

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock e){
        Player player = e.getEntity();

        if(player.isCreative())
            return;

        ItemStack stackInhand = e.getItemStack();
        if(stackInhand.isEmpty())
            return;

        if(!(stackInhand.getItem() instanceof ChiselItem))
            return;

        ItemStack storedStack = ChiselItem.getStoredStack(stackInhand);
        if(storedStack.isEmpty())
            return;

        ChiselingRecipe recipe = ChiselingRecipes.getRecipe(storedStack);
        if (recipe == null)
            return;

        BlockPos pos = e.getPos();
        Level level = e.getLevel();
        BlockState blockState = level.getBlockState(pos);
        if (!(blockState.getBlock().asItem() instanceof BlockItem blockItem))
            return;

        if(recipe.contains(blockItem.getDefaultInstance())){
            if(!(storedStack.getItem() instanceof BlockItem stackBlockItem) || blockItem == stackBlockItem)
                return;

            level.setBlock(pos, stackBlockItem.getBlock().defaultBlockState(), 1);
            level.playSound(player, pos, SoundEvents.CALCITE_HIT, SoundSource.BLOCKS);
        }
    }
}
