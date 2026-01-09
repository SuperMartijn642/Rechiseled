package com.supermartijn642.rechiseled.screen.preview;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.ClientUtils;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Quaternionf;

import java.util.Map;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ScreenBlockRenderer {

    private static BlockCaptureLevel fakeLevel;

    public static void drawBlock(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, BlockCapture capture, double x, double y, double scale, float yaw, float pitch, boolean doShading){
        AABB bounds = capture.getBounds();
        double span = Math.sqrt(bounds.getXsize() * bounds.getXsize() + bounds.getYsize() * bounds.getYsize() + bounds.getZsize() * bounds.getZsize());
        scale /= span;

        if(fakeLevel == null)
            fakeLevel = new BlockCaptureLevel();
        fakeLevel.setCapture(capture);

        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale((float)scale, (float)-scale, (float)-scale);
        poseStack.mulPose(new Quaternionf().setAngleAxis(pitch / 180 * (float)Math.PI, 1, 0, 0));
        poseStack.mulPose(new Quaternionf().setAngleAxis(yaw / 180 * (float)Math.PI, 0, 1, 0));

        for(Map.Entry<BlockPos,BlockState> entry : capture.getBlocks())
            renderBlock(entry.getKey(), entry.getValue(), poseStack, bufferSource);

        poseStack.popPose();

        fakeLevel.setCapture(null);
    }

    private static void renderBlock(BlockPos pos, BlockState state, PoseStack poseStack, MultiBufferSource bufferSource){
        poseStack.pushPose();
        poseStack.translate(pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5);

        BlockStateModel model = ClientUtils.getBlockRenderer().getBlockModel(state);
        ModelData modelData = model.getModelData(fakeLevel, pos, state, ModelData.EMPTY);
        for(ChunkSectionLayer layer : model.getRenderTypes(state, RandomSource.create(), modelData))
            ModelBlockRenderer.renderModel(poseStack.last(), bufferSource.getBuffer(RenderTypeHelper.getEntityRenderType(layer)), model, 1, 1, 1, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, modelData, layer);

        poseStack.popPose();
    }
}
