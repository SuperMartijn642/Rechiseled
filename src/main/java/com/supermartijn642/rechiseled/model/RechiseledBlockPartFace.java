package com.supermartijn642.rechiseled.model;

import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledBlockPartFace extends BlockPartFace {

    public final boolean connecting;

    public RechiseledBlockPartFace(@Nullable EnumFacing side, int tintIndex, String texture, BlockFaceUV uv, boolean connecting){
        super(side, tintIndex, texture, uv);
        this.connecting = connecting;
    }
}
