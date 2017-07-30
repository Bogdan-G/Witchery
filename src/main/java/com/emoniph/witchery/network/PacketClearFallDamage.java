package com.emoniph.witchery.network;

import com.emoniph.witchery.Witchery;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketClearFallDamage implements IMessage {

   public void toBytes(ByteBuf buffer) {}

   public void fromBytes(ByteBuf buffer) {}

   public static class Handler implements IMessageHandler<PacketClearFallDamage, IMessage> {

      public IMessage onMessage(PacketClearFallDamage message, MessageContext ctx) {
         EntityPlayer player = Witchery.proxy.getPlayer(ctx);
         if(player != null) {
            player.fallDistance = 0.0F;
         }

         return null;
      }
   }
}
