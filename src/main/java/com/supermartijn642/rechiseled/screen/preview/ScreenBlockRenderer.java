package com.supermartijn642.rechiseled.screen.preview;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.core.render.RenderUtils;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
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

    private static final MatrixStack POSE_STACK = new MatrixStack();
    private static final Random RANDOM = new Random();
    private static final Set<Block> erroredBlocks = new HashSet<>();
    private static BlockCaptureLevel fakeLevel;

    public static void drawBlock(BlockCapture capture, double x, double y, double scale, float yaw, float pitch, boolean flatShading){
        AxisAlignedBB bounds = capture.getBounds();
        double span = Math.sqrt(bounds.getXsize() * bounds.getXsize() + bounds.getYsize() * bounds.getYsize() + bounds.getZsize() * bounds.getZsize());
        scale /= span;

        if(fakeLevel == null)
            fakeLevel = new BlockCaptureLevel();
        fakeLevel.setCapture(capture);

        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        RenderSystem.pushMatrix();
        RenderSystem.translated(x, y, 350);
        RenderSystem.scaled(scale, -scale, scale);

        POSE_STACK.pushPose();
        POSE_STACK.mulPose(new Quaternion(pitch, yaw, 0, true));

        IRenderTypeBuffer.Impl bufferSource = RenderUtils.getMainBufferSource();
        for(Map.Entry<BlockPos,BlockState> entry : capture.getBlocks()){
            BlockState state = entry.getValue();
            if(!erroredBlocks.contains(state.getBlock())){
                try{
                    renderBlock(fakeLevel, entry.getKey(), state, POSE_STACK, bufferSource);
                }catch(Exception e){
                    Rechiseled.LOGGER.error("Encountered an exception whilst rendering block '{}'!", Registries.BLOCKS.getIdentifier(state.getBlock()), e);
                    erroredBlocks.add(state.getBlock());
                }
            }
        }

        if(flatShading){
            RenderHelper.setupForFlatItems();
            bufferSource.endBatch();
            RenderHelper.setupFor3DItems();
        }else
            bufferSource.endBatch();

        POSE_STACK.popPose();
        RenderSystem.popMatrix();

        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();

        fakeLevel.setCapture(null);
    }

    private static void renderBlock(ILightReader level, BlockPos pos, BlockState state, MatrixStack poseStack, IRenderTypeBuffer bufferSource){
        poseStack.pushPose();
        poseStack.translate(pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5);

        long seed = state.getSeed(pos);
        BlockRendererDispatcher blockRenderer = ClientUtils.getBlockRenderer();
        IBakedModel model = blockRenderer.getBlockModel(state);
        IModelData modelData = model.getModelData(level, pos, state, EmptyModelData.INSTANCE);
        RenderType renderType = RenderTypeLookup.getRenderType(state);
        RANDOM.setSeed(seed);
        blockRenderer.renderModel(state, pos, level, poseStack, bufferSource.getBuffer(renderType), true, RANDOM, modelData);

        poseStack.popPose();
    }
}
