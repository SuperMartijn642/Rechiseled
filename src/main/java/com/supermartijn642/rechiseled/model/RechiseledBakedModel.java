package com.supermartijn642.rechiseled.model;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledBakedModel implements IDynamicBakedModel {

    private final Map<Direction,List<Tuple<BakedQuad,Boolean>>> quads;
    private final boolean ambientOcclusion;
    private final boolean gui3d;
    private final boolean blockLighting;
    private final boolean customRenderer;
    private final TextureAtlasSprite particles;
    private final ItemOverrideList itemOverrides;
    private final ItemCameraTransforms transforms;

    public RechiseledBakedModel(Map<Direction,List<Tuple<BakedQuad,Boolean>>> quads, boolean ambientOcclusion, boolean gui3d, boolean blockLighting, boolean customRenderer, TextureAtlasSprite particles, ItemOverrideList itemOverrides, ItemCameraTransforms transforms){
        this.quads = quads;
        this.ambientOcclusion = ambientOcclusion;
        this.gui3d = gui3d;
        this.blockLighting = blockLighting;
        this.customRenderer = customRenderer;
        this.particles = particles;
        this.itemOverrides = itemOverrides;
        this.transforms = transforms;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData){
        List<BakedQuad> quads = new ArrayList<>();
        List<Tuple<BakedQuad,Boolean>> unconnectedQuads = this.quads.getOrDefault(side, Collections.emptyList());

        for(Tuple<BakedQuad,Boolean> entry : unconnectedQuads){
            BakedQuad quad = entry.getA();
            if(entry.getB()){
                int[] vertexData = quad.getVertices();
                // Make sure we don't change the original quad
                vertexData = Arrays.copyOf(vertexData, vertexData.length);

                // Adjust the uv
                int[] uv = this.getUV(quad.getDirection(), extraData);
                adjustVertexDataUV(vertexData, uv[0], uv[1], quad.getSprite());

                quads.add(new BakedQuad(vertexData, quad.getTintIndex(), quad.getDirection(), quad.getSprite(), quad.isShade()));
            }else
                quads.add(quad);
        }

        return quads;
    }

    protected int[] getUV(Direction side, IModelData modelData){
        return new int[]{0, 0};
    }

    private static int[] adjustVertexDataUV(int[] vertexData, int newU, int newV, TextureAtlasSprite sprite){
        for(int i = 0; i < 4; i++){
            float width = sprite.getU1() - sprite.getU0();
            float u = (newU + (Float.intBitsToFloat(vertexData[i * 8 + 4]) - sprite.getU0()) / width) * 2;
            vertexData[i * 8 + 4] = Float.floatToRawIntBits(sprite.getU(u));

            float height = sprite.getV1() - sprite.getV0();
            float v = (newV + (Float.intBitsToFloat(vertexData[i * 8 + 5]) - sprite.getV0()) / height) * 2;
            vertexData[i * 8 + 5] = Float.floatToRawIntBits(sprite.getV(v));
        }
        return vertexData;
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull IBlockDisplayReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData){
        return IDynamicBakedModel.super.getModelData(world, pos, state, tileData);
    }

    @Override
    public boolean useAmbientOcclusion(){
        return this.ambientOcclusion;
    }

    @Override
    public boolean isGui3d(){
        return this.gui3d;
    }

    @Override
    public boolean usesBlockLight(){
        return this.blockLighting;
    }

    @Override
    public boolean isCustomRenderer(){
        return this.customRenderer;
    }

    @Override
    public TextureAtlasSprite getParticleIcon(){
        return this.particles;
    }

    @Override
    public ItemOverrideList getOverrides(){
        return this.itemOverrides;
    }

    @Override
    public ItemCameraTransforms getTransforms(){
        return this.transforms;
    }
}
