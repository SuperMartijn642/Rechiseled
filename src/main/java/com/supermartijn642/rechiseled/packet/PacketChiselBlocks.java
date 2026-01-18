package com.supermartijn642.rechiseled.packet;

import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Created 12/01/2026 by SuperMartijn642
 */
public class PacketChiselBlocks implements BasePacket {

    private BlockPos pos;
    private Direction side;
    private boolean isSneaking;

    public PacketChiselBlocks(BlockPos pos, Direction side, boolean isSneaking) {
        this.pos = pos;
        this.side = side;
        this.isSneaking = isSneaking;
    }

    public PacketChiselBlocks() {
    }

    @Override
    public void write(FriendlyByteBuf buffer){
        buffer.writeBlockPos(this.pos);
        buffer.writeEnum(this.side);
        buffer.writeBoolean(this.isSneaking);
    }

    @Override
    public void read(FriendlyByteBuf buffer){
        this.pos = buffer.readBlockPos();
        this.side = buffer.readEnum(Direction.class);
        this.isSneaking = buffer.readBoolean();
    }

    @Override
    public void handle(PacketContext context){
        Player player = context.getPlayer();
        if(player.getEyePosition().distanceToSqr(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) > 100)
            return;
        ItemStack stack = player.getMainHandItem();
        if(stack.getItem() == Rechiseled.chisel)
            Rechiseled.chisel.leftClickBlock(player, stack, this.pos, this.side, this.isSneaking);
    }
}
