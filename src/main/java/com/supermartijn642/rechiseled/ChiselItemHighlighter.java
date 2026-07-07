package com.supermartijn642.rechiseled;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.block.BlockShape;
import com.supermartijn642.core.render.RenderUtils;
import com.supermartijn642.core.util.Pair;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created 12/01/2026 by SuperMartijn642
 */
public class ChiselItemHighlighter {

    private static final ContextKey<BlockShape> HIGHLIGHT_KEY = new ContextKey<>(Rechiseled.identifier("chisel_highlight"));

    public static void registerListeners(){
        NeoForge.EVENT_BUS.addListener((Consumer<ExtractLevelRenderStateEvent>)e -> extractChiselHighlight(e.getRenderState()));
        NeoForge.EVENT_BUS.addListener((Consumer<RenderLevelStageEvent.AfterOpaqueBlocks>)e -> renderChiselHighlight(e.getLevelRenderState(), e.getLevelRenderer().submitNodeStorage, e.getPoseStack()));
    }

    private static void extractChiselHighlight(LevelRenderState levelRenderState){
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
            levelRenderState.setRenderData(HIGHLIGHT_KEY, shape);
    }

    private static void renderChiselHighlight(LevelRenderState levelRenderState, SubmitNodeCollector output, PoseStack poseStack){
        BlockShape shape = levelRenderState.getRenderData(HIGHLIGHT_KEY);
        if(shape == null)
            return;
        Vec3 camera = levelRenderState.cameraRenderState.pos;
        poseStack.pushPose();
        poseStack.translate(-camera.x, -camera.y, -camera.z);
        RenderUtils.submitShape(output, poseStack, shape, 1, 1, 1, 1, false);
        poseStack.popPose();
    }
}
