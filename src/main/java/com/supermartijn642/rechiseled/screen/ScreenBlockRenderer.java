package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.RenderUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ScreenBlockRenderer {

    private static final PoseStack POSE_STACK = new PoseStack();
    private static BlockCaptureLevel fakeLevel;

    public static void drawBlock(BlockCapture capture, double x, double y, double scale, float yaw, float pitch, boolean doShading){
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

        if(doShading)
            Lighting.setupFor3DItems();


        MultiBufferSource.BufferSource renderTypeBuffer = RenderUtils.getMainBufferSource();
        for(Map.Entry<BlockPos,BlockState> entry : capture.getBlocks())
            renderBlock(entry.getKey(), entry.getValue(), POSE_STACK, renderTypeBuffer);
        renderTypeBuffer.endBatch();

        RenderSystem.enableDepthTest();
        if(doShading)
            Lighting.setupForFlatItems();

        POSE_STACK.popPose();

        RenderSystem.getModelViewStack().popPose();
        RenderSystem.applyModelViewMatrix();

        fakeLevel.setCapture(null);
    }

    private static void renderBlock(BlockPos pos, BlockState state, PoseStack matrixStack, MultiBufferSource renderTypeBuffer){
        matrixStack.pushPose();
        matrixStack.translate(pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5);

        BakedModel model = ClientUtils.getBlockRenderer().getBlockModel(state);
        IModelData modelData = model.getModelData(fakeLevel, pos, state, EmptyModelData.INSTANCE);
        RenderType renderType = ItemBlockRenderTypes.getRenderType(state, true);
        renderModel(model, state, matrixStack, renderTypeBuffer.getBuffer(renderType), modelData);

        matrixStack.popPose();
    }

    private static void renderModel(BakedModel modelIn, BlockState state, PoseStack matrixStackIn, VertexConsumer bufferIn, IModelData modelData){
        Random random = new Random();

        for(Direction direction : Direction.values()){
            random.setSeed(42L);
            renderQuads(matrixStackIn, bufferIn, modelIn.getQuads(state, direction, random, modelData));
        }

        random.setSeed(42L);
        renderQuads(matrixStackIn, bufferIn, modelIn.getQuads(state, null, random, modelData));
    }

    private static void renderQuads(PoseStack matrixStackIn, VertexConsumer bufferIn, List<BakedQuad> quadsIn){
        PoseStack.Pose matrix = matrixStackIn.last();

        for(BakedQuad bakedquad : quadsIn)
            bufferIn.putBulkData(matrix, bakedquad, 1, 1, 1, 1, 15728880, OverlayTexture.NO_OVERLAY, false);
    }
}
