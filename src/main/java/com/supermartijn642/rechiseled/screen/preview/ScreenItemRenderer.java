package com.supermartijn642.rechiseled.screen.preview;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.ClientUtils;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;

/**
 * Created 22/01/2022 by SuperMartijn642
 */
public class ScreenItemRenderer {

    private static final ItemStackRenderState ITEM_STACK_RENDER_STATE = new ItemStackRenderState();

    /**
     * Renders a given item as a 3d model
     */
    public static void submitItem(PoseStack poseStack, SubmitNodeCollector output, Item item, double x, double y, double scale, float yaw, float pitch){
        scale /= Math.sqrt(2 + 1d / (16 * 16));
        scale *= 1.6;

        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale((float)scale, (float)-scale, (float)-scale);
        poseStack.mulPose(new Quaternionf().setAngleAxis(pitch / 180 * (float)Math.PI, 1, 0, 0));
        poseStack.mulPose(new Quaternionf().setAngleAxis(yaw / 180 * (float)Math.PI, 0, 1, 0));
        ClientUtils.getMinecraft().getItemModelResolver().updateForTopItem(ITEM_STACK_RENDER_STATE, item.getDefaultInstance(), ItemDisplayContext.GUI, null, null, 0);
        ITEM_STACK_RENDER_STATE.submit(poseStack, output, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
