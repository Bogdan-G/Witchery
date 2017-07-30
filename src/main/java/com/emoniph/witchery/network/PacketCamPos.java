package com.emoniph.witchery.network;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.client.PlayerRender;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PacketCamPos implements IMessage {

   private boolean active;
   private boolean updatePosition;
   private int entityID;


   public PacketCamPos() {}

   public PacketCamPos(boolean active, boolean updatePosition, Entity entity) {
      this.active = active;
      this.updatePosition = updatePosition;
      this.entityID = entity != null?entity.getEntityId():0;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeBoolean(this.active);
      buffer.writeBoolean(this.updatePosition);
      buffer.writeInt(this.entityID);
   }

   public void fromBytes(ByteBuf buffer) {
      this.active = buffer.readBoolean();
      this.updatePosition = buffer.readBoolean();
      this.entityID = buffer.readInt();
   }

   public static class Handler implements IMessageHandler<PacketCamPos, IMessage> {

      public IMessage onMessage(PacketCamPos message, MessageContext ctx) {
         EntityPlayer player = Witchery.proxy.getPlayer(ctx);
         PlayerRender.moveCameraActive = message.active;
         if(message.active) {
            Minecraft.getMinecraft();
            PlayerRender.ticksSinceActive = Minecraft.getSystemTime();
            if(message.updatePosition) {
               PlayerRender.moveCameraToEntityID = message.entityID;
            }
         }

         return null;
      }
   }
}
