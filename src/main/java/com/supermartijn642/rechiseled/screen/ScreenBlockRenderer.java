package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.gui.ScreenUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.QuadGatheringTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class ScreenBlockRenderer {

    private static BlockCaptureLevel fakeLevel;

    public static void drawBlock(BlockCapture capture, double x, double y, double scale, float yaw, float pitch, boolean doShading){
        AxisAlignedBB bounds = capture.getBounds();
        double span = Math.sqrt(bounds.getXsize() * bounds.getXsize() + bounds.getYsize() * bounds.getYsize() + bounds.getZsize() * bounds.getZsize());
        scale /= span;

        if(fakeLevel == null)
            fakeLevel = new BlockCaptureLevel();
        fakeLevel.setCapture(capture);

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

        for(Map.Entry<BlockPos,BlockState> entry : capture.getBlocks())
            renderBlock(entry.getKey(), entry.getValue());

        GlStateManager.enableDepthTest();
        if(doShading)
            GlStateManager.disableLighting();

        GlStateManager.popMatrix();

        ClientUtils.getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS).popFilter();
        GlStateManager.disableAlphaTest();
        GlStateManager.disableRescaleNormal();

        fakeLevel.setCapture(null);
    }

    private static void renderBlock(BlockPos pos, BlockState state){
        GlStateManager.pushMatrix();
        GlStateManager.translated(pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5);

        IBakedModel model = ClientUtils.getBlockRenderer().getBlockModel(state);
        IModelData modelData = model.getModelData(fakeLevel, pos, state, EmptyModelData.INSTANCE);
        renderLitItem(model, modelData);

        GlStateManager.popMatrix();
    }

    //
    // Below is all copied from ForgeHooksClient and edited to pass along the model data
    //

    private static class LightGatheringTransformer extends QuadGatheringTransformer {

        private static final VertexFormat FORMAT = new VertexFormat().addElement(DefaultVertexFormats.ELEMENT_UV0).addElement(DefaultVertexFormats.ELEMENT_UV1);

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
        public void setQuadOrientation(Direction orientation){
        }

        @Override
        public void setApplyDiffuseLighting(boolean diffuse){
        }

        @Override
        public void setTexture(TextureAtlasSprite texture){
        }
    }

    private static final LightGatheringTransformer lightGatherer = new LightGatheringTransformer();

    public static void renderLitItem(IBakedModel model, IModelData modelData){
        List<BakedQuad> allquads = new ArrayList<>();
        Random random = new Random();
        long seed = 42L;

        for(Direction enumfacing : Direction.values()){
            random.setSeed(seed);
            allquads.addAll(model.getQuads(null, enumfacing, random, modelData));
        }

        random.setSeed(seed);
        allquads.addAll(model.getQuads(null, null, random, modelData));

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
            if(q.getFormat() != DefaultVertexFormats.BLOCK_NORMALS && q.getFormat().hasUv(1)){
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
            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, GLX.lastBrightnessX, GLX.lastBrightnessY);
            GlStateManager.enableLighting();
        }
    }

    private static void drawSegment(List<BakedQuad> segment, int bl, int sl, boolean shade, boolean updateLighting, boolean updateShading){
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        bufferbuilder.begin(GL_QUADS, DefaultVertexFormats.BLOCK_NORMALS);

        float lastBl = GLX.lastBrightnessX;
        float lastSl = GLX.lastBrightnessY;

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
            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, Math.max(bl, lastBl), Math.max(sl, lastSl));
        }

        ClientUtils.getItemRenderer().renderQuadList(bufferbuilder, segment, -1, ItemStack.EMPTY);
        Tessellator.getInstance().end();

        // Preserve this as it represents the "world" lighting
        GLX.lastBrightnessX = lastBl;
        GLX.lastBrightnessY = lastSl;

        segment.clear();
    }
}
