package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.RenderUtils;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.Item;

/**
 * Created 22/01/2022 by SuperMartijn642
 */
public class ScreenItemRender {

    /**
     * Renders a given item as a 3d model
     */
    public static void drawItem(Item item, double x, double y, double scale, float yaw, float pitch, boolean doShading){
        scale /= Math.sqrt(2 + 1d / (16 * 16));

        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        RenderSystem.pushMatrix();
        RenderSystem.translated(x, y, 350);
        RenderSystem.scalef(1, -1, 1);
        RenderSystem.scaled(scale, scale, scale);

        MatrixStack matrixstack = new MatrixStack();
        matrixstack.mulPose(new Quaternion(pitch, yaw, 0, true));

        if(doShading)
            RenderSystem.enableLighting();

        IRenderTypeBuffer.Impl renderTypeBuffer = RenderUtils.getMainBufferSource();
        IBakedModel model = ClientUtils.getItemRenderer().getItemModelShaper().getItemModel(item);
        if(model != null)
            ClientUtils.getItemRenderer().render(item.getDefaultInstance(), ItemCameraTransforms.TransformType.GUI, false, matrixstack, renderTypeBuffer, 15728880, OverlayTexture.NO_OVERLAY, model);
        renderTypeBuffer.endBatch();

        RenderSystem.enableDepthTest();
        if(doShading)
            RenderSystem.disableLighting();

        RenderSystem.popMatrix();

        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
    }
}
