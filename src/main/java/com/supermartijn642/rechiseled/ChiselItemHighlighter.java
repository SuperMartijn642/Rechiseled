package com.supermartijn642.rechiseled;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.block.BlockShape;
import com.supermartijn642.core.render.RenderUtils;
import com.supermartijn642.core.render.RenderWorldEvent;
import com.supermartijn642.core.util.Pair;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderHighlightEvent;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created 12/01/2026 by SuperMartijn642
 */
public class ChiselItemHighlighter {

    // This is obviously not the correct way to handle extracted state, but Forge provides no way to add any custom level data
    private static BlockShape HIGHLIGHT_SHAPE;

    public static void registerListeners(){
        RenderHighlightEvent.Block.BUS.addListener((Consumer<RenderHighlightEvent.Block>)e -> extractChiselHighlight(e.getLevelRenderState()));
        RenderWorldEvent.EVENT_BUS.addListener(ChiselItemHighlighter::renderChiselHighlight);
    }

    private static void extractChiselHighlight(LevelRenderState levelRenderState){
        HIGHLIGHT_SHAPE = null;

        // Check whether the player is hitting a block with a chisel
        HitResult result = ClientUtils.getMinecraft().hitResult;
        if(result == null || result.getType() != HitResult.Type.BLOCK || !(result instanceof BlockHitResult))
            return;
        ItemStack chisel = ClientUtils.getPlayer().getMainHandItem();
        if(chisel.getItem() != Rechiseled.chisel)
            return;

        // Find chiselable blocks
        ItemStack storedStack = ChiselItem.getStoredStack(chisel);
        Level level = ClientUtils.getWorld();
        boolean isShiftDown = ClientUtils.getMinecraft().options.keyShift.isDown();
        List<Pair<BlockPos,BlockState>> chiselableBlocks = ChiselItem.findChiselableBlocks(level, ((BlockHitResult)result).getBlockPos(), ((BlockHitResult)result).getDirection(), storedStack, isShiftDown);
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
        if(!shape.isEmpty())
            HIGHLIGHT_SHAPE = shape;
    }

    public static void renderChiselHighlight(RenderWorldEvent e){
        BlockShape shape = HIGHLIGHT_SHAPE;
        if(shape == null)
            return;
        Vec3 camera = e.getCameraPos();
        e.submitFeatures((poseStack, output) -> {
            poseStack.pushPose();
            poseStack.translate(-camera.x, -camera.y, -camera.z);
            RenderUtils.submitShape(output, poseStack, shape, 1, 1, 1, 1, false);
            poseStack.popPose();
        });
    }
}
