package com.emoniph.witchery.network;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.ExtendedPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PacketPartialExtendedPlayerSync implements IMessage {

   private int entityId;
   private int blood;


   public PacketPartialExtendedPlayerSync() {}

   public PacketPartialExtendedPlayerSync(ExtendedPlayer playerEx, EntityPlayer player) {
      this.entityId = player.getEntityId();
      this.blood = playerEx.getHumanBlood();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.entityId);
      buffer.writeInt(this.blood);
   }

   public void fromBytes(ByteBuf buffer) {
      this.entityId = buffer.readInt();
      this.blood = buffer.readInt();
   }

   public static class Handler implements IMessageHandler<PacketPartialExtendedPlayerSync, IMessage> {

      public IMessage onMessage(PacketPartialExtendedPlayerSync message, MessageContext ctx) {
         EntityPlayer player = Witchery.proxy.getPlayer(ctx);
         if(player != null) {
            Entity entity = player.worldObj.getEntityByID(message.entityId);
            if(entity instanceof EntityPlayer) {
               ExtendedPlayer ext = ExtendedPlayer.get((EntityPlayer)entity);
               if(ext != null) {
                  ext.setHumanBlood(message.blood);
               }
            }
         }

         return null;
      }
   }
}
