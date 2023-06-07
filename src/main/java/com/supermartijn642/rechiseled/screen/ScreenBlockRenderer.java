package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.gui.ScreenUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;

import java.util.Map;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ScreenBlockRenderer {

    public static void drawBlock(BlockCapture capture, double x, double y, double scale, float yaw, float pitch, boolean doShading){
        AxisAlignedBB bounds = capture.getBounds();
        double sizeX = bounds.maxX - bounds.minX, sizeY = bounds.maxY - bounds.minY, sizeZ = bounds.maxZ - bounds.minZ;
        double span = Math.sqrt(sizeX * sizeX + sizeY * sizeY + sizeZ * sizeZ);
        scale /= span;

        ScreenUtils.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        ClientUtils.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1, 1, 1, 1);

        if(doShading)
            RenderHelper.enableGUIStandardItemLighting();
        else
            GlStateManager.disableLighting();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 350);
        GlStateManager.scale(1, -1, 1);
        GlStateManager.scale(scale, scale, scale);

        GlStateManager.rotate(pitch, 1, 0, 0);
        GlStateManager.rotate(yaw, 0, 1, 0);

        for(Map.Entry<BlockPos,IBlockState> entry : capture.getBlocks())
            renderBlock(capture, entry.getKey(), entry.getValue());

        GlStateManager.enableDepth();
        if(doShading)
            GlStateManager.disableLighting();

        GlStateManager.popMatrix();

        ClientUtils.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
    }

    private static void renderBlock(BlockCapture capture, BlockPos pos, IBlockState state){
        GlStateManager.pushMatrix();
        GlStateManager.translate(pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5);

        IBakedModel model = ClientUtils.getBlockRenderer().getModelForState(state);
        ForgeHooksClient.renderLitItem(ClientUtils.getItemRenderer(), model, 0, ItemStack.EMPTY);

        GlStateManager.popMatrix();
    }
}
