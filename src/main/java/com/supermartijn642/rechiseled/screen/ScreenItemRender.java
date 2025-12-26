package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import com.supermartijn642.core.ClientUtils;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;

/**
 * Created 22/01/2022 by SuperMartijn642
 */
public class ScreenItemRender {

    private static final ItemStackRenderState ITEM_STACK_RENDER_STATE = new ItemStackRenderState();
    private static FeatureRenderDispatcher featureRenderDispatcher;

    /**
     * Renders a given item as a 3d model
     */
    public static void drawItem(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Item item, double x, double y, double scale, float yaw, float pitch, boolean doShading){
        scale /= Math.sqrt(2 + 1d / (16 * 16));
        scale *= 1.6;

        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale((float)scale, (float)-scale, (float)scale);
        setupFeatureRenderer(bufferSource);
        ClientUtils.getMinecraft().getItemModelResolver().updateForTopItem(ITEM_STACK_RENDER_STATE, item.getDefaultInstance(), ItemDisplayContext.GUI, null, ClientUtils.getPlayer(), 0);
        ITEM_STACK_RENDER_STATE.submit(poseStack, featureRenderDispatcher.getSubmitNodeStorage(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
        featureRenderDispatcher.renderAllFeatures();
        poseStack.popPose();
    }

    private static void setupFeatureRenderer(MultiBufferSource.BufferSource bufferSource){
        if(featureRenderDispatcher == null){
            featureRenderDispatcher = new FeatureRenderDispatcher(
                new SubmitNodeStorage(),
                ClientUtils.getBlockRenderer(),
                bufferSource,
                ClientUtils.getMinecraft().getAtlasManager(),
                new OutlineBufferSource() {
                    @Override
                    public VertexConsumer getBuffer(RenderType renderType){
                        return VertexMultiConsumer.create(new VertexConsumer[0]); // Discard everything
                    }
                },
                MultiBufferSource.immediate(ByteBufferBuilder.exactlySized(0)),
                ClientUtils.getFontRenderer()
            );
        }else
            featureRenderDispatcher.bufferSource = bufferSource;
    }
}
