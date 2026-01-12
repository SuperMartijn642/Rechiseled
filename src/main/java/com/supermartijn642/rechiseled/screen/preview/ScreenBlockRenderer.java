package com.supermartijn642.rechiseled.screen.preview;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.supermartijn642.core.ClientUtils;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Quaternionf;

import java.util.Map;
import java.util.function.Function;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ScreenBlockRenderer {

    private static final RandomSource RANDOM_SOURCE = RandomSource.create();
    private static BlockCaptureLevel fakeLevel;

    public static void drawBlock(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, BlockCapture capture, double x, double y, double scale, float yaw, float pitch, boolean flatShading){
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

        Function<ChunkSectionLayer,VertexConsumer> blockBufferSource = layer -> bufferSource.getBuffer(layer == ChunkSectionLayer.TRANSLUCENT ? Sheets.translucentBlockItemSheet() : Sheets.cutoutBlockSheet());
        for(Map.Entry<BlockPos,BlockState> entry : capture.getBlocks()){
            BlockState state = entry.getValue();
            renderBlock(fakeLevel, entry.getKey(), state, poseStack, blockBufferSource);
        }

        poseStack.popPose();

        if(flatShading){
            ClientUtils.getMinecraft().gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_FLAT);
            bufferSource.endBatch();
            ClientUtils.getMinecraft().gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
        }

        fakeLevel.setCapture(null);
    }

    private static void renderBlock(BlockAndTintGetter level, BlockPos pos, BlockState state, PoseStack poseStack, Function<ChunkSectionLayer,VertexConsumer> bufferSource){
        poseStack.pushPose();
        poseStack.translate(pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5);

        BlockStateModel model = ClientUtils.getBlockRenderer().getBlockModel(state);
        ModelData modelData = model.getModelData(level, pos, state, ModelData.EMPTY);
        for(ChunkSectionLayer layer : model.getRenderTypes(state, RandomSource.create(), modelData))
            ModelBlockRenderer.renderModel(poseStack.last(), bufferSource.apply(layer), model, 1, 1, 1, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, modelData, layer);

        poseStack.popPose();
    }
}
