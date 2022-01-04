package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.RenderUtils;
import com.supermartijn642.rechiseled.model.RechiseledConnectedBakedModel;
import com.supermartijn642.rechiseled.model.RechiseledModelData;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ScreenBlockRenderer {

    public static void drawBlock(BlockCapture capture, double x, double y, double scale, float yaw, float pitch, boolean doShading){
        AxisAlignedBB bounds = capture.getBounds();
        double span = Math.sqrt(bounds.getXsize() * bounds.getXsize() + bounds.getYsize() * bounds.getYsize() + bounds.getZsize() * bounds.getZsize());
        scale /= span;

        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        RenderSystem.pushMatrix();
        RenderSystem.translated(x, y, 350);
        RenderSystem.scalef(1, -1, 1);
        RenderSystem.scaled(scale, scale, scale);

        MatrixStack matrixstack = new MatrixStack();
        matrixstack.mulPose(new Quaternion(pitch, yaw, 0, true));

        if(doShading)
            RenderSystem.enableLighting();

        IRenderTypeBuffer.Impl renderTypeBuffer = RenderUtils.getMainBufferSource();
        for(Map.Entry<BlockPos,BlockState> entry : capture.getBlocks())
            renderBlock(capture, entry.getKey(), entry.getValue(), matrixstack, renderTypeBuffer);
        renderTypeBuffer.endBatch();

        RenderSystem.enableDepthTest();
        if(doShading)
            RenderSystem.disableLighting();

        RenderSystem.popMatrix();

        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
    }

    private static void renderBlock(BlockCapture capture, BlockPos pos, BlockState state, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer){
        matrixStack.pushPose();
        matrixStack.translate(pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5);

        IBakedModel model = ClientUtils.getBlockRenderer().getBlockModel(state);
        IModelData modelData = EmptyModelData.INSTANCE;
        if(model instanceof RechiseledConnectedBakedModel){
            RechiseledModelData data = new RechiseledModelData();
            for(Direction direction : Direction.values())
                data.sides.put(direction, new RechiseledModelData.SideData(direction, capture::getBlock, pos, state.getBlock()));
            modelData = new ModelDataMap.Builder().withInitial(RechiseledModelData.PROPERTY, data).build();
        }

        RenderType renderType = RenderTypeLookup.getRenderType(state);
        renderModel(model, state, matrixStack, renderTypeBuffer.getBuffer(renderType), modelData);

        matrixStack.popPose();
    }

    private static void renderModel(IBakedModel modelIn, BlockState state, MatrixStack matrixStackIn, IVertexBuilder bufferIn, IModelData modelData){
        Random random = new Random();

        for(Direction direction : Direction.values()){
            random.setSeed(42L);
            renderQuads(matrixStackIn, bufferIn, modelIn.getQuads(state, direction, random, modelData));
        }

        random.setSeed(42L);
        renderQuads(matrixStackIn, bufferIn, modelIn.getQuads(state, null, random, modelData));
    }

    private static void renderQuads(MatrixStack matrixStackIn, IVertexBuilder bufferIn, List<BakedQuad> quadsIn){
        MatrixStack.Entry matrix = matrixStackIn.last();

        for(BakedQuad bakedquad : quadsIn)
            bufferIn.addVertexData(matrix, bakedquad, 1, 1, 1, 1, 15728880, OverlayTexture.NO_OVERLAY, false);
    }
}
