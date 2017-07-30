package com.emoniph.witchery.network;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.ExtendedVillager;
import com.emoniph.witchery.network.PacketExtendedVillagerSync;
import com.emoniph.witchery.network.PacketPlayerStyle;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class PacketExtendedEntityRequestSyncToClient implements IMessage {

   private int entityId;


   public PacketExtendedEntityRequestSyncToClient() {}

   public PacketExtendedEntityRequestSyncToClient(EntityLivingBase villager) {
      this.entityId = villager.getEntityId();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.entityId);
   }

   public void fromBytes(ByteBuf buffer) {
      this.entityId = buffer.readInt();
   }

   public static class Handler implements IMessageHandler<PacketExtendedEntityRequestSyncToClient, IMessage> {

      public IMessage onMessage(PacketExtendedEntityRequestSyncToClient message, MessageContext ctx) {
         EntityPlayer player = Witchery.proxy.getPlayer(ctx);
         if(player != null) {
            Entity entity = player.worldObj.getEntityByID(message.entityId);
            if(entity instanceof EntityVillager) {
               ExtendedVillager ext = ExtendedVillager.get((EntityVillager)entity);
               if(ext != null) {
                  return new PacketExtendedVillagerSync(ext);
               }
            } else if(entity instanceof EntityPlayer) {
               return new PacketPlayerStyle((EntityPlayer)entity);
            }
         }

         return null;
      }
   }
}
