package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.rechiseled.model.RechiseledBakedModel;
import com.supermartijn642.rechiseled.model.RechiseledConnectedBakedModel;
import com.supermartijn642.rechiseled.model.RechiseledModelData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.pipeline.QuadGatheringTransformer;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
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
        RechiseledModelData data = new RechiseledModelData();
        if(model instanceof RechiseledConnectedBakedModel){
            for(EnumFacing direction : EnumFacing.values())
                data.sides.put(direction, new RechiseledModelData.SideData(direction, capture::getBlock, pos, state.getBlock()));
        }

        renderLitItem(model, data);

        GlStateManager.popMatrix();
    }

    //
    // Below is all copied from ForgeHooksClient and edited to pass along the model data
    //

    private static class LightGatheringTransformer extends QuadGatheringTransformer {

        private static final VertexFormat FORMAT = new VertexFormat().addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.TEX_2S);

        int blockLight, skyLight;

        {this.setVertexFormat(FORMAT);}

        boolean hasLighting(){
            return this.dataLength[1] >= 2;
        }

        @Override
        protected void processQuad(){
            // Reset light data
            this.blockLight = 0;
            this.skyLight = 0;
            // Compute average light for all 4 vertices
            for(int i = 0; i < 4; i++){
                this.blockLight += (int)((this.quadData[1][i][0] * 0xFFFF) / 0x20);
                this.skyLight += (int)((this.quadData[1][i][1] * 0xFFFF) / 0x20);
            }
            // Values must be multiplied by 16, divided by 4 for average => x4
            this.blockLight *= 4;
            this.skyLight *= 4;
        }

        // Dummy overrides

        @Override
        public void setQuadTint(int tint){
        }

        @Override
        public void setQuadOrientation(EnumFacing orientation){
        }

        @Override
        public void setApplyDiffuseLighting(boolean diffuse){
        }

        @Override
        public void setTexture(TextureAtlasSprite texture){
        }
    }

    private static final LightGatheringTransformer lightGatherer = new LightGatheringTransformer();

    public static void renderLitItem(IBakedModel model, RechiseledModelData modelData){
        List<BakedQuad> allquads = new ArrayList<>();

        for(EnumFacing enumfacing : EnumFacing.values()){
            if(model instanceof RechiseledBakedModel)
                allquads.addAll(((RechiseledBakedModel)model).getQuads(null, enumfacing, 0, modelData));
            else
                allquads.addAll(model.getQuads(null, enumfacing, 0));
        }

        if(model instanceof RechiseledBakedModel)
            allquads.addAll(((RechiseledBakedModel)model).getQuads(null, null, 0, modelData));
        else
            allquads.addAll(model.getQuads(null, null, 0));

        if(allquads.isEmpty()) return;

        // Current list of consecutive quads with the same lighting
        List<BakedQuad> segment = new ArrayList<>();

        // Lighting of the current segment
        int segmentBlockLight = 0;
        int segmentSkyLight = 0;
        // Diffuse lighting state
        boolean segmentShading = true;
        // State changed by the current segment
        boolean segmentLightingDirty = false;
        boolean segmentShadingDirty = false;
        // If the current segment contains lighting data
        boolean hasLighting = false;

        for(int i = 0; i < allquads.size(); i++){
            BakedQuad q = allquads.get(i);

            // Lighting of the current quad
            int bl = 0;
            int sl = 0;

            // Fail-fast on ITEM, as it cannot have light data
            if(q.getFormat() != DefaultVertexFormats.ITEM && q.getFormat().hasUvOffset(1)){
                q.pipe(lightGatherer);
                if(lightGatherer.hasLighting()){
                    bl = lightGatherer.blockLight;
                    sl = lightGatherer.skyLight;
                }
            }

            boolean shade = q.shouldApplyDiffuseLighting();

            boolean lightingDirty = segmentBlockLight != bl || segmentSkyLight != sl;
            boolean shadeDirty = shade != segmentShading;

            // If lighting or color data has changed, draw the segment and flush it
            if(lightingDirty || shadeDirty){
                if(i > 0) // Make sure this isn't the first quad being processed
                {
                    drawSegment(segment, segmentBlockLight, segmentSkyLight, segmentShading, segmentLightingDirty && (hasLighting || segment.size() < i), segmentShadingDirty);
                }
                segmentBlockLight = bl;
                segmentSkyLight = sl;
                segmentShading = shade;
                segmentLightingDirty = lightingDirty;
                segmentShadingDirty = shadeDirty;
                hasLighting = segmentBlockLight > 0 || segmentSkyLight > 0 || !segmentShading;
            }

            segment.add(q);
        }

        drawSegment(segment, segmentBlockLight, segmentSkyLight, segmentShading, segmentLightingDirty && (hasLighting || segment.size() < allquads.size()), segmentShadingDirty);

        // Clean up render state if necessary
        if(hasLighting){
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, OpenGlHelper.lastBrightnessX, OpenGlHelper.lastBrightnessY);
            GlStateManager.enableLighting();
        }
    }

    private static void drawSegment(List<BakedQuad> segment, int bl, int sl, boolean shade, boolean updateLighting, boolean updateShading){
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

        float lastBl = OpenGlHelper.lastBrightnessX;
        float lastSl = OpenGlHelper.lastBrightnessY;

        if(updateShading){
            if(shade){
                // (Re-)enable lighting for normal look with shading
                GlStateManager.enableLighting();
            }else{
                // Disable lighting to simulate a lack of diffuse lighting
                GlStateManager.disableLighting();
            }
        }

        if(updateLighting){
            // Force lightmap coords to simulate synthetic lighting
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, Math.max(bl, lastBl), Math.max(sl, lastSl));
        }

        ClientUtils.getItemRenderer().renderQuads(bufferbuilder, segment, -1, ItemStack.EMPTY);
        Tessellator.getInstance().draw();

        // Preserve this as it represents the "world" lighting
        OpenGlHelper.lastBrightnessX = lastBl;
        OpenGlHelper.lastBrightnessY = lastSl;

        segment.clear();
    }
}
