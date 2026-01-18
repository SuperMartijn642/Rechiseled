package com.supermartijn642.rechiseled;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.block.BlockShape;
import com.supermartijn642.core.render.RenderUtils;
import com.supermartijn642.core.render.RenderWorldEvent;
import com.supermartijn642.core.util.Pair;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

/**
 * Created 12/01/2026 by SuperMartijn642
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ChiselItemHighlighter {

    @SubscribeEvent
    public static void onRenderWorld(RenderWorldEvent event){
        renderChiselHighlight();
    }

    private static void renderChiselHighlight(){
        // Check whether the player is hitting a block with a chisel
        RayTraceResult result = ClientUtils.getMinecraft().objectMouseOver;
        if(result == null || result.typeOfHit != RayTraceResult.Type.BLOCK)
            return;
        EntityPlayer player = ClientUtils.getPlayer();
        ItemStack chisel = player.getHeldItemMainhand();
        if(chisel.getItem() != Rechiseled.chisel)
            return;

        // Find chiselable blocks
        ItemStack storedStack = ChiselItem.getStoredStack(chisel);
        World level = ClientUtils.getWorld();
        boolean isShiftDown = ClientUtils.getMinecraft().gameSettings.keyBindSneak.isKeyDown();
        List<Pair<BlockPos,IBlockState>> chiselableBlocks = ChiselItem.findChiselableBlocks(player, level, result.getBlockPos(), result.sideHit, storedStack, isShiftDown);
        if(chiselableBlocks.isEmpty())
            return;

        // Gather shapes to be rendered
        BlockShape shape = BlockShape.empty();
        for(Pair<BlockPos,IBlockState> pair : chiselableBlocks){
            IBlockState state = pair.right();
            BlockPos pos = pair.left();
            shape = BlockShape.or(
                BlockShape.create(state.getSelectedBoundingBox(level, pos).grow(0.0020000000949949026D)),
                shape
            );
        }
        if(shape.isEmpty())
            return;

        // Render shape
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        Vec3d camera = RenderUtils.getCameraPosition();
        buffer.setTranslation(-camera.x, -camera.y, -camera.z);
        RenderUtils.renderShape(shape, 1, 1, 1, false);
        buffer.setTranslation(0, 0, 0);
    }
}
