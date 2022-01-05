package com.supermartijn642.rechiseled.model;

import com.supermartijn642.rechiseled.RechiseledBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledBakedModel implements IBakedModel {

    private final Map<EnumFacing,List<Tuple<BakedQuad,Boolean>>> quads;
    private final boolean ambientOcclusion;
    private final boolean gui3d;
    private final boolean customRenderer;
    private final TextureAtlasSprite particles;
    private final ItemOverrideList itemOverrides;
    private final ItemCameraTransforms transforms;

    public RechiseledBakedModel(Map<EnumFacing,List<Tuple<BakedQuad,Boolean>>> quads, boolean ambientOcclusion, boolean gui3d, boolean customRenderer, TextureAtlasSprite particles, ItemOverrideList itemOverrides, ItemCameraTransforms transforms){
        this.quads = quads;
        this.ambientOcclusion = ambientOcclusion;
        this.gui3d = gui3d;
        this.customRenderer = customRenderer;
        this.particles = particles;
        this.itemOverrides = itemOverrides;
        this.transforms = transforms;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand){
        RechiseledModelData modelData = null;
        if(state instanceof IExtendedBlockState){
            IBlockAccess world = ((IExtendedBlockState)state).getValue(RechiseledBlock.WORLD_PROPERTY);
            BlockPos pos = ((IExtendedBlockState)state).getValue(RechiseledBlock.POSITION_PROPERTY);
            if(world != null && pos != null)
                modelData = this.getModelData(world, pos, state);
        }
        return this.getQuads(state, side, rand, modelData);
    }

    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand, RechiseledModelData modelData){
        List<BakedQuad> quads = new ArrayList<>();
        List<Tuple<BakedQuad,Boolean>> unconnectedQuads = this.quads.getOrDefault(side, Collections.emptyList());

        for(Tuple<BakedQuad,Boolean> entry : unconnectedQuads){
            BakedQuad quad = entry.getFirst();
            if(entry.getSecond()){
                int[] vertexData = quad.getVertexData();
                // Make sure we don't change the original quad
                vertexData = Arrays.copyOf(vertexData, vertexData.length);

                // Adjust the uv
                int[] uv = this.getUV(quad.getFace(), modelData);
                adjustVertexDataUV(vertexData, uv[0], uv[1], quad.getSprite());

                quads.add(new BakedQuad(vertexData, quad.getTintIndex(), quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat()));
            }else
                quads.add(quad);
        }

        return quads;
    }

    protected int[] getUV(EnumFacing side, RechiseledModelData modelData){
        return new int[]{0, 0};
    }

    protected RechiseledModelData getModelData(IBlockAccess world, BlockPos pos, IBlockState state){
        return null;
    }

    private static int[] adjustVertexDataUV(int[] vertexData, int newU, int newV, TextureAtlasSprite sprite){
        for(int i = 0; i < 4; i++){
            float width = sprite.getMaxU() - sprite.getMinU();
            float u = (newU + (Float.intBitsToFloat(vertexData[i * 7 + 4]) - sprite.getMinU()) / width) * 2;
            vertexData[i * 7 + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU(u));

            float height = sprite.getMaxV() - sprite.getMinV();
            float v = (newV + (Float.intBitsToFloat(vertexData[i * 7 + 5]) - sprite.getMinV()) / height) * 2;
            vertexData[i * 7 + 5] = Float.floatToRawIntBits(sprite.getInterpolatedV(v));
        }
        return vertexData;
    }

    @Override
    public boolean isAmbientOcclusion(){
        return this.ambientOcclusion;
    }

    @Override
    public boolean isGui3d(){
        return this.gui3d;
    }

    @Override
    public boolean isBuiltInRenderer(){
        return this.customRenderer;
    }

    @Override
    public TextureAtlasSprite getParticleTexture(){
        return this.particles;
    }

    @Override
    public ItemOverrideList getOverrides(){
        return this.itemOverrides;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms(){
        return this.transforms;
    }
}
