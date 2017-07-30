package com.emoniph.witchery.network;

import com.emoniph.witchery.network.PacketBrewPrepared;
import com.emoniph.witchery.network.PacketCamPos;
import com.emoniph.witchery.network.PacketClearFallDamage;
import com.emoniph.witchery.network.PacketExtendedEntityRequestSyncToClient;
import com.emoniph.witchery.network.PacketExtendedPlayerSync;
import com.emoniph.witchery.network.PacketExtendedVillagerSync;
import com.emoniph.witchery.network.PacketHowl;
import com.emoniph.witchery.network.PacketItemUpdate;
import com.emoniph.witchery.network.PacketPartialExtendedPlayerSync;
import com.emoniph.witchery.network.PacketParticles;
import com.emoniph.witchery.network.PacketPlayerStyle;
import com.emoniph.witchery.network.PacketPlayerSync;
import com.emoniph.witchery.network.PacketPushTarget;
import com.emoniph.witchery.network.PacketSelectPlayerAbility;
import com.emoniph.witchery.network.PacketSetClientPlayerFacing;
import com.emoniph.witchery.network.PacketSound;
import com.emoniph.witchery.network.PacketSpellPrepared;
import com.emoniph.witchery.network.PacketSyncEntitySize;
import com.emoniph.witchery.network.PacketSyncMarkupBook;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class PacketPipeline {

   private SimpleNetworkWrapper CHANNEL;


   public void preInit() {
      this.CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel("witchery".toLowerCase());
   }

   public void init() {
      this.CHANNEL.registerMessage((Class)PacketBrewPrepared.Handler.class, (Class)PacketBrewPrepared.class, 1, Side.SERVER);
      this.CHANNEL.registerMessage((Class)PacketParticles.Handler.class, (Class)PacketParticles.class, 2, Side.CLIENT);
      this.CHANNEL.registerMessage((Class)PacketCamPos.Handler.class, (Class)PacketCamPos.class, 3, Side.CLIENT);
      this.CHANNEL.registerMessage((Class)PacketItemUpdate.Handler.class, (Class)PacketItemUpdate.class, 4, Side.SERVER);
      this.CHANNEL.registerMessage((Class)PacketPlayerStyle.Handler.class, (Class)PacketPlayerStyle.class, 5, Side.CLIENT);
      this.CHANNEL.registerMessage((Class)PacketPlayerSync.Handler.class, (Class)PacketPlayerSync.class, 6, Side.CLIENT);
      this.CHANNEL.registerMessage((Class)PacketPushTarget.Handler.class, (Class)PacketPushTarget.class, 7, Side.CLIENT);
      this.CHANNEL.registerMessage((Class)PacketSound.Handler.class, (Class)PacketSound.class, 8, Side.CLIENT);
      this.CHANNEL.registerMessage((Class)PacketSpellPrepared.Handler.class, (Class)PacketSpellPrepared.class, 9, Side.SERVER);
      this.CHANNEL.registerMessage((Class)PacketClearFallDamage.Handler.class, (Class)PacketClearFallDamage.class, 10, Side.SERVER);
      this.CHANNEL.registerMessage((Class)PacketSyncEntitySize.Handler.class, (Class)PacketSyncEntitySize.class, 11, Side.CLIENT);
      this.CHANNEL.registerMessage((Class)PacketSyncMarkupBook.Handler.class, (Class)PacketSyncMarkupBook.class, 12, Side.SERVER);
      this.CHANNEL.registerMessage((Class)PacketExtendedPlayerSync.Handler.class, (Class)PacketExtendedPlayerSync.class, 13, Side.CLIENT);
      this.CHANNEL.registerMessage((Class)PacketHowl.Handler.class, (Class)PacketHowl.class, 14, Side.SERVER);
      this.CHANNEL.registerMessage((Class)PacketExtendedVillagerSync.Handler.class, (Class)PacketExtendedVillagerSync.class, 15, Side.CLIENT);
      this.CHANNEL.registerMessage((Class)PacketSelectPlayerAbility.Handler.class, (Class)PacketSelectPlayerAbility.class, 16, Side.SERVER);
      this.CHANNEL.registerMessage((Class)PacketExtendedEntityRequestSyncToClient.Handler.class, (Class)PacketExtendedEntityRequestSyncToClient.class, 17, Side.SERVER);
      this.CHANNEL.registerMessage((Class)PacketPartialExtendedPlayerSync.Handler.class, (Class)PacketPartialExtendedPlayerSync.class, 18, Side.CLIENT);
      this.CHANNEL.registerMessage((Class)PacketSetClientPlayerFacing.Handler.class, (Class)PacketSetClientPlayerFacing.class, 19, Side.CLIENT);
   }

   public void sendTo(IMessage message, EntityPlayer player) {
      if(player instanceof EntityPlayerMP) {
         this.CHANNEL.sendTo(message, (EntityPlayerMP)player);
      }

   }

   public void sendTo(IMessage message, EntityPlayerMP player) {
      this.CHANNEL.sendTo(message, player);
   }

   public void sendToServer(IMessage message) {
      this.CHANNEL.sendToServer(message);
   }

   public void sendToAllAround(IMessage message, TargetPoint targetPoint) {
      this.CHANNEL.sendToAllAround(message, targetPoint);
   }

   public void sendToAll(IMessage message) {
      this.CHANNEL.sendToAll(message);
   }

   public void sendToDimension(IMessage message, int dimensionId) {
      this.CHANNEL.sendToDimension(message, dimensionId);
   }

   public void sendTo(Packet packet, EntityPlayer player) {
      if(player instanceof EntityPlayerMP) {
         EntityPlayerMP mp = (EntityPlayerMP)player;
         mp.playerNetServerHandler.sendPacket(packet);
      }

   }

   public void sendToDimension(Packet packet, World world) {
      Iterator i$ = world.playerEntities.iterator();

      while(i$.hasNext()) {
         Object obj = i$.next();
         if(obj instanceof EntityPlayerMP) {
            EntityPlayerMP mp = (EntityPlayerMP)obj;
            mp.playerNetServerHandler.sendPacket(packet);
         }
      }

   }

   public void sendToAll(Packet packet) {
      WorldServer[] arr$ = MinecraftServer.getServer().worldServers;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         WorldServer world = arr$[i$];
         this.sendToDimension(packet, world);
      }

   }

   public void sendToAllAround(Packet packet, World world, TargetPoint point) {
      double RANGE_SQ = point.range * point.range;
      Iterator i$ = world.playerEntities.iterator();

      while(i$.hasNext()) {
         Object obj = i$.next();
         if(obj instanceof EntityPlayerMP) {
            EntityPlayerMP mp = (EntityPlayerMP)obj;
            if(mp.getDistanceSq(point.x, point.y, point.z) <= RANGE_SQ) {
               mp.playerNetServerHandler.sendPacket(packet);
            }
         }
      }

   }
}
