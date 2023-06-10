package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.gui.ScreenUtils;
import net.minecraft.world.item.Item;

/**
 * Created 22/01/2022 by SuperMartijn642
 */
public class ScreenItemRender {

    /**
     * Renders a given item as a 3d model
     */
    public static void drawItem(PoseStack poseStack, Item item, double x, double y, double scale, float yaw, float pitch, boolean doShading){
        scale /= Math.sqrt(2 + 1d / (16 * 16));
        scale /= 10;

        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale((float)scale, (float)scale, (float)scale);
        poseStack.translate(-8, -8, 0);
        ScreenUtils.drawItem(poseStack, item.getDefaultInstance(), null, 0, 0);
        poseStack.popPose();
    }
}
