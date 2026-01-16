package com.supermartijn642.rechiseled.screen.preview;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.RenderUtils;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ScreenBlockRenderer {

    private static final RandomSource RANDOM_SOURCE = RandomSource.create();
    private static final Set<Block> erroredBlocks = new HashSet<>();
    private static BlockCaptureLevel fakeLevel;

    public static void drawBlock(PoseStack poseStack, BlockCapture capture, double x, double y, double scale, float yaw, float pitch, boolean flatShading){
        AABB bounds = capture.getBounds();
        double span = Math.sqrt(bounds.getXsize() * bounds.getXsize() + bounds.getYsize() * bounds.getYsize() + bounds.getZsize() * bounds.getZsize());
        scale /= span;

        if(fakeLevel == null)
            fakeLevel = new BlockCaptureLevel();
        fakeLevel.setCapture(capture);

        poseStack.pushPose();
        poseStack.translate(x, y, 350);
        poseStack.mulPoseMatrix(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
        poseStack.scale((float)scale, (float)scale, (float)scale);
        poseStack.mulPose(new Quaternionf().setAngleAxis(pitch / 180 * (float)Math.PI, 1, 0, 0));
        poseStack.mulPose(new Quaternionf().setAngleAxis(yaw / 180 * (float)Math.PI, 0, 1, 0));

        MultiBufferSource.BufferSource bufferSource = RenderUtils.getMainBufferSource();
        for(Map.Entry<BlockPos,BlockState> entry : capture.getBlocks()){
            BlockState state = entry.getValue();
            if(!erroredBlocks.contains(state.getBlock())){
                try{
                    renderBlock(fakeLevel, entry.getKey(), entry.getValue(), poseStack, bufferSource);
                }catch(Exception e){
                    Rechiseled.LOGGER.error("Encountered an exception whilst rendering block '{}'!", BuiltInRegistries.BLOCK.getKey(state.getBlock()), e);
                    erroredBlocks.add(state.getBlock());
                }
            }
        }

        poseStack.popPose();

        if(flatShading){
            Lighting.setupForFlatItems();
            bufferSource.endBatch();
            Lighting.setupFor3DItems();
        }

        fakeLevel.setCapture(null);
    }

    private static void renderBlock(BlockAndTintGetter level, BlockPos pos, BlockState state, PoseStack poseStack, MultiBufferSource bufferSource){
        poseStack.pushPose();
        poseStack.translate(pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5);

        long seed = state.getSeed(pos);
        BlockRenderDispatcher blockRenderer = ClientUtils.getBlockRenderer();
        BakedModel model = blockRenderer.getBlockModel(state);
        ModelData modelData = model.getModelData(level, pos, state, ModelData.EMPTY);
        RANDOM_SOURCE.setSeed(seed);
        ChunkRenderTypeSet renderTypes = model.getRenderTypes(state, RANDOM_SOURCE, modelData);
        for(RenderType renderType : renderTypes){
            RenderType itemRenderType = renderType == RenderType.translucent() ? Sheets.translucentItemSheet() : Sheets.cutoutBlockSheet();
            blockRenderer.renderBatched(state, pos, level, poseStack, bufferSource.getBuffer(itemRenderType), true, RANDOM_SOURCE, modelData, renderType);
        }

        poseStack.popPose();
    }
}
