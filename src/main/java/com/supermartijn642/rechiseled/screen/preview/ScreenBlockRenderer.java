package com.supermartijn642.rechiseled.screen.preview;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.rechiseled.Rechiseled;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;

import java.util.*;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ScreenBlockRenderer {

    private static final Matrix4fc IDENTITY_MATRIX = new Matrix4f().identity();
    private static final RandomSource RANDOM_SOURCE = RandomSource.create();

    private static final Set<Block> erroredBlocks = new HashSet<>();
    private static BlockCaptureLevel fakeLevel;

    public static void updateState(RenderState state, BlockCapture capture){
        AABB bounds = capture.getBounds();
        state.span = Math.sqrt(bounds.getXsize() * bounds.getXsize() + bounds.getYsize() * bounds.getYsize() + bounds.getZsize() * bounds.getZsize());

        if(fakeLevel == null)
            fakeLevel = new BlockCaptureLevel();
        fakeLevel.setCapture(capture);

        if(state.positions.length < capture.size()){
            state.positions = new BlockPos[capture.size()];
            state.blockRenderStates = Arrays.copyOf(state.blockRenderStates, capture.size());
        }

        int count = 0;
        for(Map.Entry<BlockPos,BlockState> entry : capture.getBlocks()){
            BlockState blockState = entry.getValue();
            if(blockState.getRenderShape() != RenderShape.MODEL)
                continue;
            if(erroredBlocks.contains(blockState.getBlock()))
                continue;
            BlockPos pos = entry.getKey();
            state.positions[count] = pos;
            BlockModelRenderState blockRenderState = state.blockRenderStates[count];
            if(blockRenderState == null)
                blockRenderState = state.blockRenderStates[count] = new BlockModelRenderState();
            try{
                updateBlockState(fakeLevel, pos, blockState, blockRenderState);
            }catch(Exception e){
                Rechiseled.LOGGER.error("Encountered an exception whilst rendering block '{}'!", BuiltInRegistries.BLOCK.getKey(blockState.getBlock()), e);
                erroredBlocks.add(blockState.getBlock());
                continue;
            }
            count++;
        }
        state.count = count;
    }

    private static void updateBlockState(BlockAndTintGetter level, BlockPos pos, BlockState blockState, BlockModelRenderState blockRenderState){
        BlockStateModel model = ClientUtils.getMinecraft().getModelManager().getBlockStateModelSet().get(blockState);
        long seed = blockState.getSeed(pos);
        List<BlockStateModelPart> parts = blockRenderState.setupModel(IDENTITY_MATRIX, model.hasMaterialFlag(level, pos, blockState, BakedQuad.FLAG_TRANSLUCENT));
        RANDOM_SOURCE.setSeed(seed);
        model.collectParts(level, pos, blockState, RANDOM_SOURCE, parts);
        IntList tintLayers = blockRenderState.tintLayers();
        for(BlockTintSource tintSource : ClientUtils.getMinecraft().getBlockColors().getTintSources(blockState))
            tintLayers.add(tintSource.colorInWorld(blockState, level, pos));
    }

    public static void submit(SubmitNodeCollector output, PoseStack poseStack, RenderState state, double x, double y, double scale, float yaw, float pitch){
        scale /= state.span;

        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale((float)scale, (float)-scale, (float)-scale);
        poseStack.mulPose(new Quaternionf().setAngleAxis(pitch / 180 * (float)Math.PI, 1, 0, 0));
        poseStack.mulPose(new Quaternionf().setAngleAxis(yaw / 180 * (float)Math.PI, 0, 1, 0));

        for(int i = 0; i < state.count; i++){
            BlockPos pos = state.positions[i];
            BlockModelRenderState blockRenderState = state.blockRenderStates[i];
            poseStack.pushPose();
            poseStack.translate(pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5);
            blockRenderState.submit(poseStack, output, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    public static class RenderState {
        private double span;
        private int count;
        private BlockPos[] positions = new BlockPos[0];
        private BlockModelRenderState[] blockRenderStates = new BlockModelRenderState[0];
    }
}
