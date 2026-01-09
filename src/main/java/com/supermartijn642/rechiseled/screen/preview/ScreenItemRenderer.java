package com.supermartijn642.rechiseled.screen.preview;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.gui.ScreenUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

/**
 * Created 22/01/2022 by SuperMartijn642
 */
public class ScreenItemRenderer {

    /**
     * Renders a given item as a 3d model
     */
    public static void drawItem(ItemStack item, double x, double y, double scale, float yaw, float pitch, boolean flatShading){
        scale /= Math.sqrt(2 + 1d / (16 * 16));

        ScreenUtils.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        ClientUtils.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1, 1, 1, 1);

        if(!flatShading)
            RenderHelper.enableGUIStandardItemLighting();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 350);
        GlStateManager.scale(scale, -scale, scale);
        GlStateManager.rotate(pitch, 1, 0, 0);
        GlStateManager.rotate(yaw, 0, 1, 0);

        IBakedModel model = ClientUtils.getItemRenderer().getItemModelMesher().getItemModel(item);
        model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GUI, false);
        if(model != null)
            ClientUtils.getItemRenderer().renderItem(item, model);

        GlStateManager.popMatrix();

        GlStateManager.enableDepth();
        ClientUtils.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
    }
}
