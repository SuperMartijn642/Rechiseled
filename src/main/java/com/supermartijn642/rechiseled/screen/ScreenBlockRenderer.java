package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.RenderUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.util.Map;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ScreenBlockRenderer {

    private static final PoseStack POSE_STACK = new PoseStack();

    public static void drawBlock(BlockCapture capture, double x, double y, double scale, float yaw, float pitch, boolean doShading){
        AABB bounds = capture.getBounds();
        double span = Math.sqrt(bounds.getXsize() * bounds.getXsize() + bounds.getYsize() * bounds.getYsize() + bounds.getZsize() * bounds.getZsize());
        scale /= span;

        RenderSystem.getModelViewStack().pushPose();
        RenderSystem.getModelViewStack().scale(1, -1, 1);
        RenderSystem.applyModelViewMatrix();

        POSE_STACK.pushPose();
        POSE_STACK.translate(x, -y, 350);
        POSE_STACK.scale((float)scale, (float)scale, (float)scale);
        POSE_STACK.mulPose(new Quaternionf().setAngleAxis(pitch / 180 * (float)Math.PI, 1, 0, 0));
        POSE_STACK.mulPose(new Quaternionf().setAngleAxis(yaw / 180 * (float)Math.PI, 0, 1, 0));

        if(doShading)
            Lighting.setupFor3DItems();

        BlockAndTintGetter captureReference = new BlockAndTintGetter() {
            @Override
            public float getShade(Direction direction, boolean bl){
                return 1;
            }

            @Override
            public LevelLightEngine getLightEngine(){
                return ClientUtils.getWorld().getLightEngine();
            }

            @Override
            public int getBrightness(LightLayer lightLayer, BlockPos blockPos){
                return 15;
            }

            @Override
            public int getBlockTint(BlockPos pos, ColorResolver colorResolver){
                //noinspection unchecked
                Registry<Biome> biomeRegistry = (Registry<Biome>)BuiltInRegistries.REGISTRY.get(Registries.BIOME.location());
                return biomeRegistry == null ? 0 : colorResolver.getColor(biomeRegistry.get(Biomes.PLAINS), pos.getX(), pos.getZ());
            }

            @Nullable
            @Override
            public BlockEntity getBlockEntity(BlockPos pos){
                return null;
            }

            @Override
            public BlockState getBlockState(BlockPos pos){
                return capture.getBlock(pos);
            }

            @Override
            public FluidState getFluidState(BlockPos pos){
                return capture.getBlock(pos).getFluidState();
            }

            @Override
            public int getHeight(){
                return -Integer.MAX_VALUE;
            }

            @Override
            public int getMinBuildHeight(){
                return Integer.MAX_VALUE;
            }
        };

        MultiBufferSource.BufferSource renderTypeBuffer = RenderUtils.getMainBufferSource();
        for(Map.Entry<BlockPos,BlockState> entry : capture.getBlocks())
            renderBlock(captureReference, entry.getKey(), entry.getValue(), POSE_STACK, renderTypeBuffer);
        renderTypeBuffer.endBatch();

        RenderSystem.enableDepthTest();
        if(doShading)
            Lighting.setupForFlatItems();

        POSE_STACK.popPose();

        RenderSystem.getModelViewStack().popPose();
        RenderSystem.applyModelViewMatrix();
    }

    private static void renderBlock(BlockAndTintGetter capture, BlockPos pos, BlockState state, PoseStack poseStack, MultiBufferSource renderTypeBuffer){
        poseStack.pushPose();
        poseStack.translate(pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5);

        BakedModel model = ClientUtils.getBlockRenderer().getBlockModel(state);
        RenderType renderType = ItemBlockRenderTypes.getRenderType(state, true);
        ClientUtils.getBlockRenderer().getModelRenderer().tesselateBlock(capture, model, state, pos, poseStack, renderTypeBuffer.getBuffer(renderType), true, RandomSource.create(42), 42, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }
}
