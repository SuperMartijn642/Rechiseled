package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.gui.ScreenUtils;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
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

        ScreenUtils.bindTexture(AtlasTexture.LOCATION_BLOCKS);
        ClientUtils.getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS).pushFilter(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlphaTest();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color4f(1, 1, 1, 1);

        if(doShading)
            RenderHelper.turnOnGui();
        else
            GlStateManager.disableLighting();

        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, 350);
        GlStateManager.scalef(1, -1, 1);
        GlStateManager.scaled(scale, scale, scale);

        GlStateManager.rotated(pitch, 1, 0, 0);
        GlStateManager.rotated(yaw, 0, 1, 0);

        IBakedModel model = ClientUtils.getItemRenderer().getItemModelShaper().getItemModel(item);
        if(model != null)
            ClientUtils.getItemRenderer().render(item.getDefaultInstance(), model);

        GlStateManager.enableDepthTest();
        if(doShading)
            GlStateManager.disableLighting();

        GlStateManager.popMatrix();

        ClientUtils.getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS).popFilter();
        GlStateManager.disableAlphaTest();
        GlStateManager.disableRescaleNormal();
    }
}
