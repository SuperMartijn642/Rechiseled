package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.ClientUtils;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;

/**
 * Created 22/01/2022 by SuperMartijn642
 */
public class ScreenItemRender {

    /**
     * Renders a given item as a 3d model
     */
    public static void drawItem(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Item item, double x, double y, double scale, float yaw, float pitch, boolean doShading){
        scale /= Math.sqrt(2 + 1d / (16 * 16));
        scale *= 1.6;

        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale((float)scale, (float)-scale, (float)scale);
        ClientUtils.getItemRenderer().renderStatic(
            item.getDefaultInstance(),
            ItemDisplayContext.GUI,
            LightTexture.FULL_BRIGHT,
            OverlayTexture.NO_OVERLAY,
            poseStack,
            bufferSource,
            null,
            0
        );
        poseStack.popPose();
    }
}
