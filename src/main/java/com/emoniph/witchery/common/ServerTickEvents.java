package com.emoniph.witchery.common;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.common.Shapeshift;
import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.network.PacketPlayerStyle;
import com.emoniph.witchery.util.Config;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class ServerTickEvents {

   public static final ArrayList TASKS = new ArrayList();


   @SubscribeEvent
   public void onServerTick(ServerTickEvent event) {
      if(event.side == Side.SERVER && event.phase == Phase.START && TASKS.size() > 0) {
         ArrayList completedTasks = new ArrayList();
         Iterator i$ = TASKS.iterator();

         ServerTickEvents.ServerTickTask task;
         while(i$.hasNext()) {
            task = (ServerTickEvents.ServerTickTask)i$.next();
            if(task.process()) {
               completedTasks.add(task);
            }
         }

         i$ = completedTasks.iterator();

         while(i$.hasNext()) {
            task = (ServerTickEvents.ServerTickTask)i$.next();
            TASKS.remove(task);
         }
      }

   }

   @SubscribeEvent
   public void onPlayerTick(PlayerTickEvent event) {
      if(event.side == Side.SERVER && !event.player.worldObj.isRemote) {
         if(event.phase == Phase.START) {
            Collection playerExt = event.player.getActivePotionEffects();
            ExtendedPlayer playerExt1 = ExtendedPlayer.get(event.player);
            if(playerExt1 != null) {
               playerExt1.updateWorship();
               if(playerExt.size() > 0) {
                  playerExt1.cacheIncurablePotionEffect(playerExt);
               }

               playerExt1.checkSleep(true);
            }
         } else if(event.phase == Phase.END) {
            ExtendedPlayer playerExt2 = ExtendedPlayer.get(event.player);
            if(playerExt2 != null) {
               playerExt2.restoreIncurablePotionEffects();
               playerExt2.checkSleep(false);
            }
         }
      }

   }

   @SubscribeEvent
   public void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
      EntityPlayer player = event.player;
      World world = event.player.worldObj;
      Shapeshift.INSTANCE.initCurrentShift(player);
      Infusion.syncPlayer(world, player);
      ExtendedPlayer.get(player).scheduleSync();
      Witchery.packetPipeline.sendToDimension(new PacketPlayerStyle(player), world.provider.dimensionId);
      if(player.dimension != Config.instance().dimensionDreamID && WorldProviderDreamWorld.getPlayerIsSpiritWalking(player) && !WorldProviderDreamWorld.getPlayerIsGhost(player)) {
         WorldProviderDreamWorld.setPlayerMustAwaken(player, true);
      } else if(player.dimension == Config.instance().dimensionDreamID && !WorldProviderDreamWorld.getPlayerIsSpiritWalking(player)) {
         WorldProviderDreamWorld.changeDimension(player, 0);
         WorldProviderDreamWorld.findTopAndSetPosition(player.worldObj, player);
      }

   }

   @SubscribeEvent
   public void onPlayerRespawn(PlayerRespawnEvent event) {
      if(!event.player.worldObj.isRemote) {
         NBTTagCompound nbtPlayer = Infusion.getNBT(event.player);
         if(nbtPlayer.hasKey("WITCPoSpawn")) {
            NBTTagList player = nbtPlayer.getTagList("WITCPoSpawn", 10);
            if(player.tagCount() > 0) {
               for(int world = 0; world < player.tagCount(); ++world) {
                  PotionEffect restoredEffect = PotionEffect.readCustomPotionEffectFromNBT(player.getCompoundTagAt(world));
                  if(!event.player.isPotionActive(restoredEffect.getPotionID())) {
                     event.player.addPotionEffect(restoredEffect);
                  }
               }
            }

            nbtPlayer.removeTag("WITCPoSpawn");
         }

         EntityPlayer var6 = event.player;
         World var7 = event.player.worldObj;
         Shapeshift.INSTANCE.initCurrentShift(var6);
         Infusion.syncPlayer(var7, var6);
         ExtendedPlayer.get(var6).scheduleSync();
         Witchery.packetPipeline.sendToDimension(new PacketPlayerStyle(var6), var7.provider.dimensionId);
      }

   }

   @SubscribeEvent
   public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
      if(!event.player.worldObj.isRemote) {
         EntityPlayer player = event.player;
         if(player != null && player.worldObj != null && !player.worldObj.isRemote) {
            long nextUpdate = MinecraftServer.getSystemTimeMillis() + 1500L;
            ExtendedPlayer.get(player).scheduleSync();
            Iterator nbtPlayer = player.worldObj.playerEntities.iterator();

            while(nbtPlayer.hasNext()) {
               Object obj = nbtPlayer.next();
               EntityPlayer otherPlayer = (EntityPlayer)obj;
               NBTTagCompound nbtOtherPlayer = Infusion.getNBT(otherPlayer);
               if(otherPlayer != player) {
                  nbtOtherPlayer.setLong("WITCResyncLook", nextUpdate);
               }
            }

            NBTTagCompound nbtPlayer1 = Infusion.getNBT(player);
            Witchery.packetPipeline.sendToDimension(new PacketPlayerStyle(player), player.worldObj.provider.dimensionId);
            if(player.dimension != Config.instance().dimensionDreamID && WorldProviderDreamWorld.getPlayerIsSpiritWalking(player) && !WorldProviderDreamWorld.getPlayerIsGhost(player)) {
               WorldProviderDreamWorld.setPlayerMustAwaken(player, true);
            }
         }
      }

   }


   public abstract static class ServerTickTask {

      protected final World world;


      public ServerTickTask(World world) {
         this.world = world;
      }

      public abstract boolean process();
   }
}
