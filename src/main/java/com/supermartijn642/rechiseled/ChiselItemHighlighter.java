package com.supermartijn642.rechiseled;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.block.BlockShape;
import com.supermartijn642.core.render.RenderUtils;
import com.supermartijn642.core.render.RenderWorldEvent;
import com.supermartijn642.core.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created 12/01/2026 by SuperMartijn642
 */
public class ChiselItemHighlighter {

    public static void registerListeners(){
        MinecraftForge.EVENT_BUS.addListener((Consumer<RenderWorldEvent>)e -> renderChiselHighlight(e.getPoseStack()));
    }

    private static void renderChiselHighlight(MatrixStack poseStack){
        // Check whether the player is hitting a block with a chisel
        RayTraceResult result = ClientUtils.getMinecraft().hitResult;
        if(result == null || result.getType() != RayTraceResult.Type.BLOCK || !(result instanceof BlockRayTraceResult))
            return;
        ItemStack chisel = ClientUtils.getPlayer().getMainHandItem();
        if(chisel.getItem() != Rechiseled.chisel)
            return;

        // Find chiselable blocks
        ItemStack storedStack = ChiselItem.getStoredStack(chisel);
        World level = ClientUtils.getWorld();
        boolean isShiftDown = ClientUtils.getMinecraft().options.keyShift.isDown();
        List<Pair<BlockPos,BlockState>> chiselableBlocks = ChiselItem.findChiselableBlocks(level, ((BlockRayTraceResult)result).getBlockPos(), ((BlockRayTraceResult)result).getDirection(), storedStack, isShiftDown);
        if(chiselableBlocks.isEmpty())
            return;

        // Gather shapes to be rendered
        BlockShape shape = BlockShape.empty();
        for(Pair<BlockPos,BlockState> pair : chiselableBlocks){
            BlockState state = pair.right();
            BlockPos pos = pair.left();
            shape = BlockShape.or(
                BlockShape.create(state.getShape(level, pos)).offset(pos),
                shape
            );
        }
        if(shape.isEmpty())
            return;

        // Render shape
        Vector3d camera = RenderUtils.getCameraPosition();
        poseStack.pushPose();
        poseStack.translate(-camera.x, -camera.y, -camera.z);
        RenderUtils.renderShape(poseStack, shape, 1, 1, 1, false);
        poseStack.popPose();
    }
}
