package com.emoniph.witchery.network;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PacketParticles implements IMessage {

   private ParticleEffect particleEffect;
   private SoundEffect soundEffect;
   private double x;
   private double y;
   private double z;
   private double width;
   private double height;
   private int color;


   public PacketParticles() {}

   public PacketParticles(ParticleEffect particleEffect, SoundEffect soundEffect, double x, double y, double z, double width, double height, int color) {
      this.particleEffect = particleEffect;
      this.soundEffect = soundEffect != null?soundEffect:SoundEffect.NONE;
      this.x = x;
      this.y = y;
      this.z = z;
      this.width = width;
      this.height = height;
      this.color = color;
   }

   public PacketParticles(ParticleEffect particleEffect, SoundEffect soundEffect, Entity targetEntity, double width, double height) {
      this(particleEffect, soundEffect, targetEntity.posX, targetEntity.posY, targetEntity.posZ, width, height, 16777215);
   }

   public PacketParticles(ParticleEffect particleEffect, SoundEffect soundEffect, Entity targetEntity, double width, double height, int color) {
      this(particleEffect, soundEffect, targetEntity.posX, targetEntity.posY, targetEntity.posZ, width, height, color);
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.particleEffect.ordinal());
      buffer.writeInt(this.soundEffect.ordinal());
      buffer.writeDouble(this.x);
      buffer.writeDouble(this.y);
      buffer.writeDouble(this.z);
      buffer.writeDouble(this.width);
      buffer.writeDouble(this.height);
      buffer.writeInt(this.color);
   }

   public void fromBytes(ByteBuf buffer) {
      int ordinalParticle = buffer.readInt();
      this.particleEffect = ParticleEffect.values()[ordinalParticle];
      int ordinalSound = buffer.readInt();
      this.soundEffect = SoundEffect.values()[ordinalSound];
      this.x = buffer.readDouble();
      this.y = buffer.readDouble();
      this.z = buffer.readDouble();
      this.width = buffer.readDouble();
      this.height = buffer.readDouble();
      this.color = buffer.readInt();
   }

   public static class Handler implements IMessageHandler<PacketParticles, IMessage> {

      public IMessage onMessage(PacketParticles message, MessageContext ctx) {
         EntityPlayer player = Witchery.proxy.getPlayer(ctx);
         World world = player.worldObj;
         double x = message.x;
         double y = message.y;
         double z = message.z;
         double width = message.width;
         double height = message.height;
         SoundEffect sound = message.soundEffect;
         int color = message.color;
         ParticleEffect particle = message.particleEffect;
         Witchery.proxy.showParticleEffect(world, x, y, z, width, height, sound, color, particle);
         return null;
      }
   }
}
