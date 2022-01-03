package com.supermartijn642.rechiseled.model;

import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.core.Direction;

import javax.annotation.Nullable;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledBlockPartFace extends BlockElementFace {

    public final boolean connecting;

    public RechiseledBlockPartFace(@Nullable Direction side, int tintIndex, String texture, BlockFaceUV uv, boolean connecting){
        super(side, tintIndex, texture, uv);
        this.connecting = connecting;
    }
}
