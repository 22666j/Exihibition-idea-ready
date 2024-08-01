package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class S0APacketUseBed implements Packet {
   private int playerID;
   private BlockPos field_179799_b;

   public S0APacketUseBed() {
   }

   public S0APacketUseBed(EntityPlayer p_i45964_1_, BlockPos p_i45964_2_) {
      this.playerID = p_i45964_1_.getEntityId();
      this.field_179799_b = p_i45964_2_;
   }

   public void readPacketData(PacketBuffer data) throws IOException {
      this.playerID = data.readVarIntFromBuffer();
      this.field_179799_b = data.readBlockPos();
   }

   public void writePacketData(PacketBuffer data) throws IOException {
      data.writeVarIntToBuffer(this.playerID);
      data.writeBlockPos(this.field_179799_b);
   }

   public void func_180744_a(INetHandlerPlayClient p_180744_1_) {
      p_180744_1_.handleUseBed(this);
   }

   public EntityPlayer getPlayer(World worldIn) {
      return (EntityPlayer)worldIn.getEntityByID(this.playerID);
   }

   public BlockPos func_179798_a() {
      return this.field_179799_b;
   }

   public void processPacket(INetHandler handler) {
      this.func_180744_a((INetHandlerPlayClient)handler);
   }
}
