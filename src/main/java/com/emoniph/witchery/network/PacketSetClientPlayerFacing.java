package com.emoniph.witchery.network;

import com.emoniph.witchery.Witchery;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSetClientPlayerFacing implements IMessage {

   private float pitch;
   private float yaw;


   public PacketSetClientPlayerFacing() {}

   public PacketSetClientPlayerFacing(EntityPlayer player) {
      this.pitch = player.rotationPitch;
      this.yaw = player.rotationYaw;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeFloat(this.pitch);
      buffer.writeFloat(this.yaw);
   }

   public void fromBytes(ByteBuf buffer) {
      this.pitch = buffer.readFloat();
      this.yaw = buffer.readFloat();
   }

   public static class Handler implements IMessageHandler<PacketSetClientPlayerFacing, IMessage> {

      public IMessage onMessage(PacketSetClientPlayerFacing message, MessageContext ctx) {
         EntityPlayer player = Witchery.proxy.getPlayer(ctx);
         player.setPositionAndRotation(player.posX, player.posY, player.posZ, message.yaw, message.pitch);
         return null;
      }
   }
}
