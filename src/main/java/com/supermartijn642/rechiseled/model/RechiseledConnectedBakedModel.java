package com.supermartijn642.rechiseled.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.List;
import java.util.Map;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledConnectedBakedModel extends RechiseledBakedModel {

    public RechiseledConnectedBakedModel(Map<EnumFacing,List<Tuple<BakedQuad,Boolean>>> quads, boolean ambientOcclusion, boolean gui3d, boolean customRenderer, TextureAtlasSprite particles, ItemOverrideList itemOverrides, ItemCameraTransforms transforms){
        super(quads, ambientOcclusion, gui3d, customRenderer, particles, itemOverrides, transforms);
    }

    @Override
    public RechiseledModelData getModelData(IBlockAccess world, BlockPos pos, IBlockState state){
        RechiseledModelData data = new RechiseledModelData();
        for(EnumFacing direction : EnumFacing.values())
            data.sides.put(direction, new RechiseledModelData.SideData(direction, world, pos, state.getBlock()));
        return data;
    }

    @Override
    protected int[] getUV(EnumFacing side, RechiseledModelData data){
        if(data == null)
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
                                uv = new int[]{7, 7};
                            else if(blocks.up_left && !blocks.up_right && blocks.down_left && blocks.down_right)
                                uv = new int[]{6, 7};
                            else if(blocks.up_left && blocks.up_right && !blocks.down_left && blocks.down_right)
                                uv = new int[]{7, 6};
                            else if(blocks.up_left && blocks.up_right && blocks.down_left && !blocks.down_right)
                                uv = new int[]{6, 6};
                            else{
                                if(!blocks.up_left && blocks.up_right && !blocks.down_right && blocks.down_left)
                                    uv = new int[]{0, 4};
                                else if(blocks.up_left && !blocks.up_right && blocks.down_right && !blocks.down_left)
                                    uv = new int[]{0, 5};
                                else if(!blocks.up_left && !blocks.up_right && blocks.down_right && blocks.down_left)
                                    uv = new int[]{3, 6};
                                else if(blocks.up_left && !blocks.up_right && !blocks.down_right && blocks.down_left)
                                    uv = new int[]{3, 7};
                                else if(blocks.up_left && blocks.up_right && !blocks.down_right && !blocks.down_left)
                                    uv = new int[]{2, 7};
                                else if(!blocks.up_left && blocks.up_right && blocks.down_right && !blocks.down_left)
                                    uv = new int[]{2, 6};
                                else{
                                    if(blocks.up_left)
                                        uv = new int[]{5, 7};
                                    else if(blocks.up_right)
                                        uv = new int[]{4, 7};
                                    else if(blocks.down_right)
                                        uv = new int[]{4, 6};
                                    else if(blocks.down_left)
                                        uv = new int[]{5, 6};
                                    else
                                        uv = new int[]{0, 6};
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
