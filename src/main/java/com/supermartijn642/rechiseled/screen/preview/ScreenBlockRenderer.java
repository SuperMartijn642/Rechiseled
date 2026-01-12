package com.supermartijn642.rechiseled.screen.preview;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.RenderUtils;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ScreenBlockRenderer {

    private static final PoseStack POSE_STACK = new PoseStack();
    private static final Random RANDOM = new Random();
    private static final Set<Block> erroredBlocks = new HashSet<>();
    private static BlockCaptureLevel fakeLevel;

    public static void drawBlock(BlockCapture capture, double x, double y, double scale, float yaw, float pitch, boolean flatShading){
        AABB bounds = capture.getBounds();
        double span = Math.sqrt(bounds.getXsize() * bounds.getXsize() + bounds.getYsize() * bounds.getYsize() + bounds.getZsize() * bounds.getZsize());
        scale /= span;

        if(fakeLevel == null)
            fakeLevel = new BlockCaptureLevel();
        fakeLevel.setCapture(capture);

        RenderSystem.getModelViewStack().pushPose();
        RenderSystem.getModelViewStack().scale(1, -1, 1);
        RenderSystem.applyModelViewMatrix();

        POSE_STACK.pushPose();
        POSE_STACK.translate(x, -y, 350);
        POSE_STACK.scale((float)scale, (float)scale, (float)scale);
        POSE_STACK.mulPose(new Quaternion(pitch, yaw, 0, true));

        MultiBufferSource.BufferSource bufferSource = RenderUtils.getMainBufferSource();
        for(Map.Entry<BlockPos,BlockState> entry : capture.getBlocks()){
            BlockState state = entry.getValue();
            if(!erroredBlocks.contains(state.getBlock())){
                try{
                    renderBlock(fakeLevel, entry.getKey(), entry.getValue(), POSE_STACK, bufferSource);
                }catch(Exception e){
                    Rechiseled.LOGGER.error("Encountered an exception whilst rendering block '{}'!", Registry.BLOCK.getKey(state.getBlock()), e);
                    erroredBlocks.add(state.getBlock());
                }
            }
        }

        POSE_STACK.popPose();

        if(flatShading){
            Lighting.setupForFlatItems();
            bufferSource.endBatch();
            Lighting.setupFor3DItems();
        }else
            bufferSource.endBatch();

        RenderSystem.getModelViewStack().popPose();
        RenderSystem.applyModelViewMatrix();

        fakeLevel.setCapture(null);
    }

    private static void renderBlock(BlockAndTintGetter level, BlockPos pos, BlockState state, PoseStack poseStack, MultiBufferSource bufferSource){
        poseStack.pushPose();
        poseStack.translate(pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5);

        long seed = state.getSeed(pos);
        BlockRenderDispatcher blockRenderer = ClientUtils.getBlockRenderer();
        BakedModel model = blockRenderer.getBlockModel(state);
        IModelData modelData = model.getModelData(level, pos, state, EmptyModelData.INSTANCE);
        RenderType renderType = ItemBlockRenderTypes.getRenderType(state, true);
        RANDOM.setSeed(seed);
        blockRenderer.renderBatched(state, pos, level, poseStack, bufferSource.getBuffer(renderType), true, RANDOM, modelData);

        poseStack.popPose();
    }
}
