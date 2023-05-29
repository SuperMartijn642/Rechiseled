package com.supermartijn642.rechiseled.model;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
@Deprecated
public class RechiseledBakedModel implements IDynamicBakedModel {

    private final Map<Direction,List<Tuple<BakedQuad,Boolean>>> quads;
    private final boolean ambientOcclusion;
    private final boolean gui3d;
    private final boolean blockLighting;
    private final boolean customRenderer;
    private final TextureAtlasSprite particles;
    private final ItemOverrides itemOverrides;
    private final ItemTransforms transforms;
    private final ChunkRenderTypeSet blockRenderTypes;
    private final List<RenderType> itemRenderTypes;
    private final List<RenderType> fabulousItemRenderTypes;

    public RechiseledBakedModel(Map<Direction,List<Tuple<BakedQuad,Boolean>>> quads, boolean ambientOcclusion, boolean gui3d, boolean blockLighting, boolean customRenderer, TextureAtlasSprite particles, ItemOverrides itemOverrides, ItemTransforms transforms, RenderTypeGroup renderTypes){
        this.quads = quads;
        this.ambientOcclusion = ambientOcclusion;
        this.gui3d = gui3d;
        this.blockLighting = blockLighting;
        this.customRenderer = customRenderer;
        this.particles = particles;
        this.itemOverrides = itemOverrides;
        this.transforms = transforms;
        this.blockRenderTypes = !renderTypes.isEmpty() ? ChunkRenderTypeSet.of(renderTypes.block()) : null;
        this.itemRenderTypes = !renderTypes.isEmpty() ? Collections.singletonList(renderTypes.entity()) : null;
        this.fabulousItemRenderTypes = !renderTypes.isEmpty() ? Collections.singletonList(renderTypes.entityFabulous()) : null;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, RenderType renderType){
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
                adjustVertexDataUV(vertexData, uv[0], uv[1], quad.getSprite(), DefaultVertexFormat.BLOCK);

                quads.add(new BakedQuad(vertexData, quad.getTintIndex(), quad.getDirection(), quad.getSprite(), quad.isShade()));
            }else
                quads.add(quad);
        }

        return quads;
    }

    protected int[] getUV(Direction side, ModelData modelData){
        return new int[]{0, 0};
    }

    private static int[] adjustVertexDataUV(int[] vertexData, int newU, int newV, TextureAtlasSprite sprite, VertexFormat vertexFormat){
        int vertexSize = vertexFormat.getIntegerSize();
        int vertices = vertexData.length / vertexSize;
        int uvOffset = findUVOffset(vertexFormat) / 4;

        for(int i = 0; i < vertices; i++){
            int offset = i * vertexSize + uvOffset;

            float width = sprite.getU1() - sprite.getU0();
            float u = (newU + (Float.intBitsToFloat(vertexData[offset]) - sprite.getU0()) / width) * 2;
            vertexData[offset] = Float.floatToRawIntBits(sprite.getU(u));

            float height = sprite.getV1() - sprite.getV0();
            float v = (newV + (Float.intBitsToFloat(vertexData[offset + 1]) - sprite.getV0()) / height) * 2;
            vertexData[offset + 1] = Float.floatToRawIntBits(sprite.getV(v));
        }
        return vertexData;
    }

    private static int findUVOffset(VertexFormat vertexFormat){
        int index;
        VertexFormatElement element = null;
        for(index = 0; index < vertexFormat.getElements().size(); index++){
            VertexFormatElement el = vertexFormat.getElements().get(index);
            if(el.getUsage() == VertexFormatElement.Usage.UV){
                element = el;
                break;
            }
        }
        if(index == vertexFormat.getElements().size() || element == null)
            throw new RuntimeException("Expected vertex format to have a UV attribute");
        if(element.getType() != VertexFormatElement.Type.FLOAT)
            throw new RuntimeException("Expected UV attribute to have data type FLOAT");
        if(element.getByteSize() < 4)
            throw new RuntimeException("Expected UV attribute to have at least 4 dimensions");
        return vertexFormat.getOffset(index);
    }

    @Nonnull
    @Override
    public ModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull ModelData tileData){
        return IDynamicBakedModel.super.getModelData(world, pos, state, tileData);
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data){
        if(this.blockRenderTypes != null)
            return this.blockRenderTypes;
        return IDynamicBakedModel.super.getRenderTypes(state, rand, data);
    }

    @Override
    public List<RenderType> getRenderTypes(ItemStack itemStack, boolean fabulous){
        if(!fabulous){
            if(this.itemRenderTypes != null)
                return this.itemRenderTypes;
        }else{
            if(this.fabulousItemRenderTypes != null)
                return this.fabulousItemRenderTypes;
        }
        return IDynamicBakedModel.super.getRenderTypes(itemStack, fabulous);
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
    public ItemOverrides getOverrides(){
        return this.itemOverrides;
    }

    @Override
    public ItemTransforms getTransforms(){
        return this.transforms;
    }
}
