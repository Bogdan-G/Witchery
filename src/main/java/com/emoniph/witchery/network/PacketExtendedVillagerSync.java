package com.emoniph.witchery.network;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.ExtendedVillager;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class PacketExtendedVillagerSync implements IMessage {

   private int entityId;
   private int blood;
   private boolean sleeping;


   public PacketExtendedVillagerSync() {}

   public PacketExtendedVillagerSync(ExtendedVillager extendedVillager) {
      this.entityId = extendedVillager.getVillager().getEntityId();
      this.blood = extendedVillager.getBlood();
      this.sleeping = extendedVillager.isSleeping();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.entityId);
      buffer.writeInt(this.blood);
      buffer.writeBoolean(this.sleeping);
   }

   public void fromBytes(ByteBuf buffer) {
      this.entityId = buffer.readInt();
      this.blood = buffer.readInt();
      this.sleeping = buffer.readBoolean();
   }

   public static class Handler implements IMessageHandler<PacketExtendedVillagerSync, IMessage> {

      public IMessage onMessage(PacketExtendedVillagerSync message, MessageContext ctx) {
         EntityPlayer player = Witchery.proxy.getPlayer(ctx);
         if(player != null) {
            Entity entity = player.worldObj.getEntityByID(message.entityId);
            if(entity instanceof EntityVillager) {
               ExtendedVillager ext = ExtendedVillager.get((EntityVillager)entity);
               if(ext != null) {
                  ext.synced = true;
                  ext.setBlood(message.blood);
                  ext.setSleeping(message.sleeping);
               }
            }
         }

         return null;
      }
   }
}
