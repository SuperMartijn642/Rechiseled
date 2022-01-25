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
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledConnectedBakedModel extends RechiseledBakedModel {

    public RechiseledConnectedBakedModel(Map<Direction,List<Tuple<BakedQuad,Boolean>>> quads, boolean ambientOcclusion, boolean gui3d, boolean blockLighting, boolean customRenderer, TextureAtlasSprite particles, ItemOverrideList itemOverrides, ItemCameraTransforms transforms){
        super(quads, ambientOcclusion, gui3d, blockLighting, customRenderer, particles, itemOverrides, transforms);
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull IBlockDisplayReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData){
        RechiseledModelData data = new RechiseledModelData();
        for(Direction direction : Direction.values())
            data.sides.put(direction, new RechiseledModelData.SideData(direction, world, pos, state.getBlock()));
        return new ModelDataMap.Builder().withInitial(RechiseledModelData.PROPERTY, data).build();
    }

    @Override
    protected int[] getUV(Direction side, IModelData modelData){
        RechiseledModelData data;
        if(!modelData.hasProperty(RechiseledModelData.PROPERTY) || (data = modelData.getData(RechiseledModelData.PROPERTY)) == null)
            return new int[]{0, 0};

        RechiseledModelData.SideData blocks = data.sides.get(side);
        int[] uv;

        if(!blocks.left && !blocks.up && !blocks.right && !blocks.down) // all directions
            uv = new int[]{0, 0};
        else{ // one direction
            if(blocks.left && !blocks.up && !blocks.right && !blocks.down)
                uv = new int[]{3, 0};
            else if(!blocks.left && blocks.up && !blocks.right && !blocks.down)
                uv = new int[]{0, 3};
            else if(!blocks.left && !blocks.up && blocks.right && !blocks.down)
                uv = new int[]{1, 0};
            else if(!blocks.left && !blocks.up && !blocks.right && blocks.down)
                uv = new int[]{0, 1};
            else{ // two directions
                if(blocks.left && !blocks.up && blocks.right && !blocks.down)
                    uv = new int[]{2, 0};
                else if(!blocks.left && blocks.up && !blocks.right && blocks.down)
                    uv = new int[]{0, 2};
                else if(blocks.left && blocks.up && !blocks.right && !blocks.down){
                    if(blocks.up_left)
                        uv = new int[]{3, 3};
                    else
                        uv = new int[]{5, 1};
                }else if(!blocks.left && blocks.up && blocks.right && !blocks.down){
                    if(blocks.up_right)
                        uv = new int[]{1, 3};
                    else
                        uv = new int[]{4, 1};
                }else if(!blocks.left && !blocks.up && blocks.right && blocks.down){
                    if(blocks.down_right)
                        uv = new int[]{1, 1};
                    else
                        uv = new int[]{4, 0};
                }else if(blocks.left && !blocks.up && !blocks.right && blocks.down){
                    if(blocks.down_left)
                        uv = new int[]{3, 1};
                    else
                        uv = new int[]{5, 0};
                }else{ // three directions
                    if(!blocks.left){
                        if(blocks.up_right && blocks.down_right)
                            uv = new int[]{1, 2};
                        else if(blocks.up_right)
                            uv = new int[]{4, 2};
                        else if(blocks.down_right)
                            uv = new int[]{6, 2};
                        else
                            uv = new int[]{6, 0};
                    }else if(!blocks.up){
                        if(blocks.down_left && blocks.down_right)
                            uv = new int[]{2, 1};
                        else if(blocks.down_left)
                            uv = new int[]{7, 2};
                        else if(blocks.down_right)
                            uv = new int[]{5, 2};
                        else
                            uv = new int[]{7, 0};
                    }else if(!blocks.right){
                        if(blocks.up_left && blocks.down_left)
                            uv = new int[]{3, 2};
                        else if(blocks.up_left)
                            uv = new int[]{7, 3};
                        else if(blocks.down_left)
                            uv = new int[]{5, 3};
                        else
                            uv = new int[]{7, 1};
                    }else if(!blocks.down){
                        if(blocks.up_left && blocks.up_right)
                            uv = new int[]{2, 3};
                        else if(blocks.up_left)
                            uv = new int[]{4, 3};
                        else if(blocks.up_right)
                            uv = new int[]{6, 3};
                        else
                            uv = new int[]{6, 1};
                    }else{ // four directions
                        if(blocks.up_left && blocks.up_right && blocks.down_left && blocks.down_right)
                            uv = new int[]{2, 2};
                        else{
                            if(!blocks.up_left && blocks.up_right && blocks.down_left && blocks.down_right)
                                uv = new int[]{7, 5};
                            else if(blocks.up_left && !blocks.up_right && blocks.down_left && blocks.down_right)
                                uv = new int[]{6, 5};
                            else if(blocks.up_left && blocks.up_right && !blocks.down_left && blocks.down_right)
                                uv = new int[]{7, 4};
                            else if(blocks.up_left && blocks.up_right && blocks.down_left && !blocks.down_right)
                                uv = new int[]{6, 4};
                            else{
                                if(!blocks.up_left && blocks.up_right && !blocks.down_right && blocks.down_left)
                                    uv = new int[]{0, 4};
                                else if(blocks.up_left && !blocks.up_right && blocks.down_right && !blocks.down_left)
                                    uv = new int[]{0, 5};
                                else if(!blocks.up_left && !blocks.up_right && blocks.down_right && blocks.down_left)
                                    uv = new int[]{3, 4};
                                else if(blocks.up_left && !blocks.up_right && !blocks.down_right && blocks.down_left)
                                    uv = new int[]{3, 5};
                                else if(blocks.up_left && blocks.up_right && !blocks.down_right && !blocks.down_left)
                                    uv = new int[]{2, 5};
                                else if(!blocks.up_left && blocks.up_right && blocks.down_right && !blocks.down_left)
                                    uv = new int[]{2, 4};
                                else{
                                    if(blocks.up_left)
                                        uv = new int[]{5, 5};
                                    else if(blocks.up_right)
                                        uv = new int[]{4, 5};
                                    else if(blocks.down_right)
                                        uv = new int[]{4, 4};
                                    else if(blocks.down_left)
                                        uv = new int[]{5, 4};
                                    else
                                        uv = new int[]{1, 4};
                                }
                            }
                        }
                    }
                }
            }
        }

        return uv;
    }
}
