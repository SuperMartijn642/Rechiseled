package com.supermartijn642.rechiseled.screen.preview;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;

/**
 * Created 22/01/2022 by SuperMartijn642
 */
public class ScreenItemRenderer {

    private static final PoseStack POSE_STACK = new PoseStack();

    /**
     * Renders a given item as a 3d model
     */
    public static void drawItem(Item item, double x, double y, double scale, float yaw, float pitch, boolean flatShading){
        scale /= Math.sqrt(2 + 1d / (16 * 16));

        RenderSystem.getModelViewStack().pushPose();
        RenderSystem.getModelViewStack().scale(1, -1, 1);
        RenderSystem.applyModelViewMatrix();

        POSE_STACK.pushPose();
        POSE_STACK.translate(x, -y, 350);
        POSE_STACK.scale((float)scale, (float)scale, (float)scale);
        POSE_STACK.mulPose(new Quaternionf().setAngleAxis(pitch / 180 * (float)Math.PI, 1, 0, 0));
        POSE_STACK.mulPose(new Quaternionf().setAngleAxis(yaw / 180 * (float)Math.PI, 0, 1, 0));

        MultiBufferSource.BufferSource bufferSource = RenderUtils.getMainBufferSource();
        BakedModel model = ClientUtils.getItemRenderer().getItemModelShaper().getItemModel(item);
        if(model != null)
            ClientUtils.getItemRenderer().render(item.getDefaultInstance(), ItemDisplayContext.GUI, false, POSE_STACK, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, model);


        POSE_STACK.popPose();

        RenderSystem.enableDepthTest();
        if(flatShading){
            Lighting.setupForFlatItems();
            bufferSource.endBatch();
            Lighting.setupFor3DItems();
        }else
            bufferSource.endBatch();

        RenderSystem.getModelViewStack().popPose();
        RenderSystem.applyModelViewMatrix();
    }
}
