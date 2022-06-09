package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.RenderUtils;
import com.supermartijn642.rechiseled.model.RechiseledConnectedBakedModel;
import com.supermartijn642.rechiseled.model.RechiseledModelData;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

import java.util.List;
import java.util.Map;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ScreenBlockRenderer {

    public static void drawBlock(BlockCapture capture, double x, double y, double scale, float yaw, float pitch, boolean doShading){
        AABB bounds = capture.getBounds();
        double span = Math.sqrt(bounds.getXsize() * bounds.getXsize() + bounds.getYsize() * bounds.getYsize() + bounds.getZsize() * bounds.getZsize());
        scale /= span;

        RenderSystem.getModelViewStack().pushPose();
        RenderSystem.getModelViewStack().scale(1, -1, 1);
        RenderSystem.applyModelViewMatrix();

        PoseStack poseStack = new PoseStack();
        poseStack.translate(x, -y, 350);
        poseStack.scale((float)scale, (float)scale, (float)scale);
        poseStack.mulPose(new Quaternion(pitch, yaw, 0, true));

        if(doShading)
            Lighting.setupFor3DItems();


        MultiBufferSource.BufferSource renderTypeBuffer = RenderUtils.getMainBufferSource();
        for(Map.Entry<BlockPos,BlockState> entry : capture.getBlocks())
            renderBlock(capture, entry.getKey(), entry.getValue(), poseStack, renderTypeBuffer);
        renderTypeBuffer.endBatch();

        RenderSystem.enableDepthTest();
        if(doShading)
            Lighting.setupForFlatItems();

        RenderSystem.getModelViewStack().popPose();
        RenderSystem.applyModelViewMatrix();
    }

    private static void renderBlock(BlockCapture capture, BlockPos pos, BlockState state, PoseStack matrixStack, MultiBufferSource renderTypeBuffer){
        matrixStack.pushPose();
        matrixStack.translate(pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5);

        BakedModel model = ClientUtils.getBlockRenderer().getBlockModel(state);
        IModelData modelData = EmptyModelData.INSTANCE;
        if(model instanceof RechiseledConnectedBakedModel){
            RechiseledModelData data = new RechiseledModelData();
            for(Direction direction : Direction.values())
                data.sides.put(direction, new RechiseledModelData.SideData(direction, capture::getBlock, pos, state.getBlock()));
            modelData = new ModelDataMap.Builder().withInitial(RechiseledModelData.PROPERTY, data).build();
        }

        RenderType renderType = ItemBlockRenderTypes.getRenderType(state, true);
        renderModel(model, state, matrixStack, renderTypeBuffer.getBuffer(renderType), modelData);

        matrixStack.popPose();
    }

    private static void renderModel(BakedModel modelIn, BlockState state, PoseStack matrixStackIn, VertexConsumer bufferIn, IModelData modelData){
        RandomSource random = RandomSource.create();

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
