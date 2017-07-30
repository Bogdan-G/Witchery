package com.emoniph.witchery.network;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.common.Shapeshift;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketHowl implements IMessage {

   public void toBytes(ByteBuf buffer) {}

   public void fromBytes(ByteBuf buffer) {}

   public static class Handler implements IMessageHandler<PacketHowl, IMessage> {

      public IMessage onMessage(PacketHowl message, MessageContext ctx) {
         EntityPlayer player = Witchery.proxy.getPlayer(ctx);
         Shapeshift.INSTANCE.checkForHowling(player, ExtendedPlayer.get(player));
         return null;
      }
   }
}
