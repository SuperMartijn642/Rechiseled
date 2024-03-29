package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;

/**
 * Created 22/01/2022 by SuperMartijn642
 */
public class ScreenItemRender {

    /**
     * Renders a given item as a 3d model
     */
    public static void drawItem(Item item, double x, double y, double scale, float yaw, float pitch, boolean doShading){
        scale /= Math.sqrt(2 + 1d / (16 * 16));

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
        BakedModel model = ClientUtils.getItemRenderer().getItemModelShaper().getItemModel(item);
        if(model != null)
            ClientUtils.getItemRenderer().render(item.getDefaultInstance(), ItemTransforms.TransformType.GUI, false, poseStack, renderTypeBuffer, 15728880, OverlayTexture.NO_OVERLAY, model);
        renderTypeBuffer.endBatch();

        RenderSystem.enableDepthTest();
        if(doShading)
            Lighting.setupForFlatItems();

        RenderSystem.getModelViewStack().popPose();
        RenderSystem.applyModelViewMatrix();
    }
}
