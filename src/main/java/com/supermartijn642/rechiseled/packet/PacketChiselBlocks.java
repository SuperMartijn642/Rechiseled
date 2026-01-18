package com.supermartijn642.rechiseled.packet;

import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Created 12/01/2026 by SuperMartijn642
 */
public class PacketChiselBlocks implements BasePacket {

    private BlockPos pos;
    private EnumFacing side;
    private boolean isSneaking;

    public PacketChiselBlocks(BlockPos pos, EnumFacing side, boolean isSneaking){
        this.pos = pos;
        this.side = side;
        this.isSneaking = isSneaking;
    }

    public PacketChiselBlocks(){
    }

    @Override
    public void write(PacketBuffer buffer){
        buffer.writeBlockPos(this.pos);
        buffer.writeEnumValue(this.side);
        buffer.writeBoolean(this.isSneaking);
    }

    @Override
    public void read(PacketBuffer buffer){
        this.pos = buffer.readBlockPos();
        this.side = buffer.readEnumValue(EnumFacing.class);
        this.isSneaking = buffer.readBoolean();
    }

    @Override
    public void handle(PacketContext context){
        EntityPlayer player = context.getPlayer();
        if(player.getPositionEyes(0).squareDistanceTo(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) > 100)
            return;
        ItemStack stack = player.getHeldItemMainhand();
        if(stack.getItem() == Rechiseled.chisel)
            Rechiseled.chisel.leftClickBlock(player, stack, this.pos, this.side, this.isSneaking);
    }
}
