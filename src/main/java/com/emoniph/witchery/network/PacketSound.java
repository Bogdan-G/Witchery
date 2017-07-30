package com.emoniph.witchery.network;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSound implements IMessage {

   private SoundEffect effect;
   private double x;
   private double y;
   private double z;
   private float volume;
   private float pitch;


   public PacketSound() {}

   public PacketSound(SoundEffect soundEffect, Entity location) {
      this(soundEffect, location, -1.0F, -1.0F);
   }

   public PacketSound(SoundEffect soundEffect, Entity location, float volume, float pitch) {
      this.effect = soundEffect;
      this.x = location.posX;
      this.y = location.posY;
      this.z = location.posZ;
      this.volume = volume;
      this.pitch = pitch;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.effect.ordinal());
      buffer.writeDouble(this.x);
      buffer.writeDouble(this.y);
      buffer.writeDouble(this.z);
      buffer.writeFloat(this.volume);
      buffer.writeFloat(this.pitch);
   }

   public void fromBytes(ByteBuf buffer) {
      this.effect = SoundEffect.values()[buffer.readInt()];
      this.x = buffer.readDouble();
      this.y = buffer.readDouble();
      this.z = buffer.readDouble();
      this.volume = buffer.readFloat();
      this.pitch = buffer.readFloat();
   }

   public static class Handler implements IMessageHandler<PacketSound, IMessage> {

      public IMessage onMessage(PacketSound message, MessageContext ctx) {
         EntityPlayer player = Witchery.proxy.getPlayer(ctx);
         if(message.volume == -1.0F) {
            message.volume = 0.5F;
         }

         if(message.pitch == -1.0F) {
            message.pitch = 0.4F / ((float)player.worldObj.rand.nextDouble() * 0.4F + 0.8F);
         }

         player.worldObj.playSound(message.x, message.y, message.z, message.effect.toString(), message.volume, message.pitch, false);
         return null;
      }
   }
}
