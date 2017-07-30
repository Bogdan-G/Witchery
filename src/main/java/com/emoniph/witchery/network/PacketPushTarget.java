package com.emoniph.witchery.network;

import com.emoniph.witchery.Witchery;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketPushTarget implements IMessage {

   private double motionX;
   private double motionY;
   private double motionZ;


   public PacketPushTarget() {}

   public PacketPushTarget(double motionX, double motionY, double motionZ) {
      this.motionX = motionX;
      this.motionY = motionY;
      this.motionZ = motionZ;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeDouble(this.motionX);
      buffer.writeDouble(this.motionY);
      buffer.writeDouble(this.motionZ);
   }

   public void fromBytes(ByteBuf buffer) {
      this.motionX = buffer.readDouble();
      this.motionY = buffer.readDouble();
      this.motionZ = buffer.readDouble();
   }

   public static class Handler implements IMessageHandler<PacketPushTarget, IMessage> {

      public IMessage onMessage(PacketPushTarget message, MessageContext ctx) {
         EntityPlayer player = Witchery.proxy.getPlayer(ctx);
         player.motionX = message.motionX;
         player.motionY = message.motionY;
         player.motionZ = message.motionZ;
         return null;
      }
   }
}
