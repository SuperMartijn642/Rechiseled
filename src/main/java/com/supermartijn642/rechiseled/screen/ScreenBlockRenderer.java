package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.RenderUtils;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.Map;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ScreenBlockRenderer {

    private static final RandomSource RANDOM = RandomSource.create();
    private static BlockCaptureLevel fakeLevel;

    public static void drawBlock(PoseStack poseStack, BlockCapture capture, double x, double y, double scale, float yaw, float pitch, boolean doShading){
        AABB bounds = capture.getBounds();
        double span = Math.sqrt(bounds.getXsize() * bounds.getXsize() + bounds.getYsize() * bounds.getYsize() + bounds.getZsize() * bounds.getZsize());
        scale /= span;

        if(fakeLevel == null)
            fakeLevel = new BlockCaptureLevel();
        fakeLevel.setCapture(capture);

        poseStack.pushPose();
        poseStack.translate(x, y, 350);
        poseStack.mulPose(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
        poseStack.scale((float)scale, (float)scale, (float)scale);
        poseStack.mulPose(new Quaternionf().setAngleAxis(pitch / 180 * (float)Math.PI, 1, 0, 0));
        poseStack.mulPose(new Quaternionf().setAngleAxis(yaw / 180 * (float)Math.PI, 0, 1, 0));

        if(doShading)
            Lighting.setupForEntityInInventory(new Quaternionf().rotateX((float)(Math.PI / 2)).rotateZ((float)(Math.PI)));

        MultiBufferSource.BufferSource renderTypeBuffer = RenderUtils.getMainBufferSource();
        for(Map.Entry<BlockPos,BlockState> entry : capture.getBlocks())
            renderBlock(entry.getKey(), entry.getValue(), poseStack, renderTypeBuffer);
        renderTypeBuffer.endBatch();

        if(doShading)
            Lighting.setupForFlatItems();

        poseStack.popPose();

        fakeLevel.setCapture(null);
    }

    private static void renderBlock(BlockPos pos, BlockState state, PoseStack poseStack, MultiBufferSource renderTypeBuffer){
        poseStack.pushPose();
        poseStack.translate(pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5);

        BlockStateModel model = ClientUtils.getBlockRenderer().getBlockModel(state);
        ModelBlockRenderer.renderModel(poseStack.last(), renderTypeBuffer, model, 1, 1, 1, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, fakeLevel, pos, state);

        poseStack.popPose();
    }
}
