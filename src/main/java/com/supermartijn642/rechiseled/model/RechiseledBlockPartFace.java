package com.supermartijn642.rechiseled.model;

import net.minecraft.client.renderer.model.BlockFaceUV;
import net.minecraft.client.renderer.model.BlockPartFace;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledBlockPartFace extends BlockPartFace {

    public final boolean connecting;

    public RechiseledBlockPartFace(@Nullable Direction side, int tintIndex, String texture, BlockFaceUV uv, boolean connecting){
        super(side, tintIndex, texture, uv);
        this.connecting = connecting;
    }
}
