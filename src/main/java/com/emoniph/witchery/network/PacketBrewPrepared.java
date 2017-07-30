package com.emoniph.witchery.network;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketBrewPrepared implements IMessage {

   private int brewIndex;


   public PacketBrewPrepared() {}

   public PacketBrewPrepared(int brewIndex) {
      this.brewIndex = brewIndex;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.brewIndex);
   }

   public void fromBytes(ByteBuf buffer) {
      this.brewIndex = buffer.readInt();
   }

   public static class Handler implements IMessageHandler<PacketBrewPrepared, IMessage> {

      public IMessage onMessage(PacketBrewPrepared message, MessageContext ctx) {
         EntityPlayer player = Witchery.proxy.getPlayer(ctx);
         player.getEntityData().setInteger("WITCLastBrewIndex", message.brewIndex);
         SoundEffect.RANDOM_POP.playAtPlayer(player.worldObj, player, 1.0F);
         return null;
      }
   }
}
