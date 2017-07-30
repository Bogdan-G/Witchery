package com.emoniph.witchery.infusion.infusions;

import com.emoniph.witchery.brewing.potions.PotionEnderInhibition;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.DimensionalLocation;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class InfusionOtherwhere extends Infusion {

   private static final String RECALL_LOCATON_KEY = "WITCRecall";
   private static final int SAVE_RECALL_POINT_THRESHOLD = 60;


   public InfusionOtherwhere(int infusionID) {
      super(infusionID);
   }

   public IIcon getPowerBarIcon(EntityPlayer player, int index) {
      return Blocks.portal.getIcon(0, 0);
   }

   public void onLeftClickEntity(ItemStack itemstack, World world, EntityPlayer player, Entity otherEntity) {
      if(!world.isRemote) {
         if(otherEntity instanceof EntityLivingBase) {
            EntityLivingBase otherLivingEntity = (EntityLivingBase)otherEntity;
            if(player.isSneaking()) {
               DimensionalLocation HIKE_HEIGHT = this.recallLocation(getNBT(player), "WITCRecall");
               if(HIKE_HEIGHT != null && HIKE_HEIGHT.dimension != Config.instance().dimensionDreamID && HIKE_HEIGHT.dimension != Config.instance().dimensionTormentID && HIKE_HEIGHT.dimension != Config.instance().dimensionMirrorID && world.provider.dimensionId != Config.instance().dimensionDreamID && world.provider.dimensionId != Config.instance().dimensionTormentID && world.provider.dimensionId != Config.instance().dimensionMirrorID && !PotionEnderInhibition.isActive(player, 2) && this.consumeCharges(world, player, 4, false)) {
                  if(player instanceof EntityPlayerMP && !isConnectionClosed((EntityPlayerMP)player)) {
                     player.fallDistance = 0.0F;
                     ItemGeneral.teleportToLocation(world, HIKE_HEIGHT.posX, HIKE_HEIGHT.posY, HIKE_HEIGHT.posZ, HIKE_HEIGHT.dimension, player, true);
                     otherLivingEntity.fallDistance = 0.0F;
                     if(!PotionEnderInhibition.isActive(otherLivingEntity, 2)) {
                        ItemGeneral.teleportToLocation(world, HIKE_HEIGHT.posX, HIKE_HEIGHT.posY, HIKE_HEIGHT.posZ, HIKE_HEIGHT.dimension, otherLivingEntity, true);
                     }
                  }
               } else {
                  world.playSoundAtEntity(player, "note.snare", 0.5F, 0.4F / ((float)Math.random() * 0.4F + 0.8F));
               }
            } else if(!PotionEnderInhibition.isActive(player, 2) && this.consumeCharges(world, player, 2, true)) {
               double HIKE_HEIGHT1 = 8.0D;
               MovingObjectPosition hitMOP = raytraceUpBlocks(world, player, true, 8.0D);
               double hikeModified = hitMOP == null?8.0D:Math.min((double)hitMOP.blockY - otherLivingEntity.posY - 2.0D, 8.0D);
               MovingObjectPosition hitMOP2 = raytraceUpBlocks(world, otherLivingEntity, true, 8.0D);
               double hikeModified2 = hitMOP2 == null?8.0D:Math.min((double)hitMOP2.blockY - otherLivingEntity.posY - 2.0D, 8.0D);
               if(player instanceof EntityPlayerMP && !isConnectionClosed((EntityPlayerMP)player) && hikeModified > 0.0D && hikeModified2 > 0.0D) {
                  ItemGeneral.teleportToLocation(world, player.posX, player.posY + hikeModified, player.posZ, player.dimension, player, true);
                  if(!PotionEnderInhibition.isActive(otherLivingEntity, 2)) {
                     ItemGeneral.teleportToLocation(world, otherLivingEntity.posX, otherLivingEntity.posY + hikeModified2, otherLivingEntity.posZ, otherLivingEntity.dimension, otherLivingEntity, true);
                  }
               }
            }
         }

      }
   }

   public void onUsingItemTick(ItemStack itemstack, World world, EntityPlayer player, int countdown) {
      int elapsedTicks = this.getMaxItemUseDuration(itemstack) - countdown;
      if(player.isSneaking() && elapsedTicks == 60) {
         if(!world.isRemote) {
            ChatUtil.sendTranslated(EnumChatFormatting.GRAY, player, "witchery.infuse.cansetrecall", new Object[0]);
         }

         player.worldObj.playSoundAtEntity(player, "note.pling", 0.5F, 0.4F / ((float)Math.random() * 0.4F + 0.8F));
      } else if(!player.isSneaking() && elapsedTicks > 0 && elapsedTicks % 20 == 0) {
         int MAX_TELEPORT_DISTANCE = 40 + 20 * (elapsedTicks / 20);
         MovingObjectPosition hitMOP = doCustomRayTrace(world, player, true, (double)MAX_TELEPORT_DISTANCE);
         if(hitMOP != null) {
            player.worldObj.playSoundAtEntity(player, "random.orb", 0.5F, 0.4F / ((float)Math.random() * 0.4F + 0.8F));
            if(!world.isRemote) {
               ChatUtil.sendTranslated(EnumChatFormatting.GRAY, player, "witchery.infuse.canteleport", new Object[0]);
            }
         } else {
            player.worldObj.playSoundAtEntity(player, "random.pop", 0.5F, 0.4F / ((float)Math.random() * 0.4F + 0.8F));
         }
      }

   }

   public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer player, int countdown) {
      if(!world.isRemote) {
         int elapsedTicks = this.getMaxItemUseDuration(itemstack) - countdown;
         if(player.isSneaking() && elapsedTicks >= 60) {
            this.storeLocation(getNBT(player), "WITCRecall", player);
            SoundEffect.RANDOM_FIZZ.playAtPlayer(world, player);
         } else if(player.isSneaking()) {
            DimensionalLocation MAX_TELEPORT_DISTANCE = this.recallLocation(getNBT(player), "WITCRecall");
            if(MAX_TELEPORT_DISTANCE != null && MAX_TELEPORT_DISTANCE.dimension != Config.instance().dimensionDreamID && MAX_TELEPORT_DISTANCE.dimension != Config.instance().dimensionTormentID && MAX_TELEPORT_DISTANCE.dimension != Config.instance().dimensionMirrorID && world.provider.dimensionId != Config.instance().dimensionDreamID && world.provider.dimensionId != Config.instance().dimensionTormentID && world.provider.dimensionId != Config.instance().dimensionMirrorID && !PotionEnderInhibition.isActive(player, 2) && this.consumeCharges(world, player, 2, false)) {
               if(player instanceof EntityPlayerMP && !isConnectionClosed((EntityPlayerMP)player)) {
                  player.fallDistance = 0.0F;
                  ItemGeneral.teleportToLocation(world, MAX_TELEPORT_DISTANCE.posX, MAX_TELEPORT_DISTANCE.posY, MAX_TELEPORT_DISTANCE.posZ, MAX_TELEPORT_DISTANCE.dimension, player, true);
                  Infusion.setCooldown(world, itemstack, 1500);
               }
            } else {
               world.playSoundAtEntity(player, "note.snare", 0.5F, 0.4F / ((float)Math.random() * 0.4F + 0.8F));
            }
         } else {
            int MAX_TELEPORT_DISTANCE1 = 40 + 20 * (elapsedTicks / 20);
            MovingObjectPosition hitMOP = doCustomRayTrace(world, player, true, (double)MAX_TELEPORT_DISTANCE1);
            if(hitMOP != null && !PotionEnderInhibition.isActive(player, 2) && this.consumeCharges(world, player, 1, false)) {
               ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, player, 0.5D, 2.0D, 16);
               teleportEntity(player, hitMOP);
               ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, player, 0.5D, 2.0D, 16);
               Infusion.setCooldown(world, itemstack, 1500);
            } else {
               world.playSoundAtEntity(player, "note.snare", 0.5F, 0.4F / ((float)Math.random() * 0.4F + 0.8F));
               if(hitMOP == null && !world.isRemote) {
                  ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.cannotteleport", new Object[0]);
               }
            }
         }

      }
   }

   private void storeLocation(NBTTagCompound nbt, String key, EntityPlayer player) {
      DimensionalLocation location = new DimensionalLocation(player);
      location.saveToNBT(nbt, key);
      if(!player.worldObj.isRemote) {
         ChatUtil.sendTranslated(EnumChatFormatting.GRAY, player, "witchery.infuse.setrecall", new Object[]{player.worldObj.provider.getDimensionName(), Integer.valueOf(MathHelper.floor_double(location.posX)).toString(), Integer.valueOf(MathHelper.floor_double(location.posY)).toString(), Integer.valueOf(MathHelper.floor_double(location.posZ)).toString()});
      }

   }

   private DimensionalLocation recallLocation(NBTTagCompound nbtTag, String key) {
      DimensionalLocation location = new DimensionalLocation(nbtTag, key);
      return !location.isValid?null:location;
   }

   public static void teleportEntity(EntityPlayer entityPlayer, MovingObjectPosition hitMOP) {
      if(hitMOP != null && entityPlayer instanceof EntityPlayerMP) {
         EntityPlayerMP player = (EntityPlayerMP)entityPlayer;
         if(!isConnectionClosed(player)) {
            switch(InfusionOtherwhere.NamelessClass1354619960.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[hitMOP.typeOfHit.ordinal()]) {
            case 1:
               player.setPositionAndUpdate(hitMOP.hitVec.xCoord, hitMOP.hitVec.yCoord, hitMOP.hitVec.zCoord);
               break;
            case 2:
               double hitx = hitMOP.hitVec.xCoord;
               double hity = hitMOP.hitVec.yCoord;
               double hitz = hitMOP.hitVec.zCoord;
               switch(hitMOP.sideHit) {
               case 0:
                  hity -= 2.0D;
               case 1:
               default:
                  break;
               case 2:
                  hitz -= 0.5D;
                  break;
               case 3:
                  hitz += 0.5D;
                  break;
               case 4:
                  hitx -= 0.5D;
                  break;
               case 5:
                  hitx += 0.5D;
               }

               player.fallDistance = 0.0F;
               player.setPositionAndUpdate(hitx, hity, hitz);
            }
         }
      }

   }

   public static MovingObjectPosition doCustomRayTrace(World world, EntityPlayer player, boolean collisionFlag, double reachDistance) {
      MovingObjectPosition pickedBlock = raytraceBlocks(world, player, collisionFlag, reachDistance);
      MovingObjectPosition pickedEntity = raytraceEntities(world, player, collisionFlag, reachDistance);
      if(pickedBlock == null) {
         return pickedEntity;
      } else if(pickedEntity == null) {
         return pickedBlock;
      } else {
         Vec3 playerPosition = Vec3.createVectorHelper(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
         double dBlock = pickedBlock.hitVec.distanceTo(playerPosition);
         double dEntity = pickedEntity.hitVec.distanceTo(playerPosition);
         return dEntity < dBlock?pickedEntity:pickedBlock;
      }
   }

   public static MovingObjectPosition raytraceEntities(World world, EntityPlayer player, boolean collisionFlag, double reachDistance) {
      MovingObjectPosition pickedEntity = null;
      Vec3 playerPosition = Vec3.createVectorHelper(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
      Vec3 playerLook = player.getLookVec();
      Vec3 playerViewOffset = Vec3.createVectorHelper(playerPosition.xCoord + playerLook.xCoord * reachDistance, playerPosition.yCoord + playerLook.yCoord * reachDistance, playerPosition.zCoord + playerLook.zCoord * reachDistance);
      double playerBorder = 1.1D * reachDistance;
      AxisAlignedBB boxToScan = player.boundingBox.expand(playerBorder, playerBorder, playerBorder);
      List entitiesHit = world.getEntitiesWithinAABBExcludingEntity(player, boxToScan);
      double closestEntity = reachDistance;
      if(entitiesHit != null && !entitiesHit.isEmpty()) {
         Iterator i$ = entitiesHit.iterator();

         while(i$.hasNext()) {
            Entity entityHit = (Entity)i$.next();
            if(entityHit != null && entityHit.canBeCollidedWith() && entityHit.boundingBox != null) {
               float border = entityHit.getCollisionBorderSize();
               AxisAlignedBB aabb = entityHit.boundingBox.expand((double)border, (double)border, (double)border);
               MovingObjectPosition hitMOP = aabb.calculateIntercept(playerPosition, playerViewOffset);
               if(hitMOP != null) {
                  if(aabb.isVecInside(playerPosition)) {
                     if(0.0D < closestEntity || closestEntity == 0.0D) {
                        pickedEntity = new MovingObjectPosition(entityHit);
                        if(pickedEntity != null) {
                           pickedEntity.hitVec = hitMOP.hitVec;
                           closestEntity = 0.0D;
                        }
                     }
                  } else {
                     double distance = playerPosition.distanceTo(hitMOP.hitVec);
                     if(distance < closestEntity || closestEntity == 0.0D) {
                        pickedEntity = new MovingObjectPosition(entityHit);
                        pickedEntity.hitVec = hitMOP.hitVec;
                        closestEntity = distance;
                     }
                  }
               }
            }
         }

         return pickedEntity;
      } else {
         return null;
      }
   }

   private static boolean isConnectionClosed(EntityPlayerMP player) {
      return !player.playerNetServerHandler.netManager.isChannelOpen();
   }

   public static MovingObjectPosition raytraceBlocks(World world, EntityPlayer player, boolean collisionFlag, double reachDistance) {
      Vec3 playerPosition = Vec3.createVectorHelper(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
      Vec3 playerLook = player.getLookVec();
      Vec3 playerViewOffset = Vec3.createVectorHelper(playerPosition.xCoord + playerLook.xCoord * reachDistance, playerPosition.yCoord + playerLook.yCoord * reachDistance, playerPosition.zCoord + playerLook.zCoord * reachDistance);
      return world.func_147447_a(playerPosition, playerViewOffset, collisionFlag, !collisionFlag, false);
   }

   private static MovingObjectPosition raytraceUpBlocks(World world, EntityLivingBase player, boolean collisionFlag, double reachDistance) {
      Vec3 playerPosition = Vec3.createVectorHelper(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
      Vec3 playerUp = Vec3.createVectorHelper(0.0D, 1.0D, 0.0D);
      Vec3 playerViewOffset = Vec3.createVectorHelper(playerPosition.xCoord + playerUp.xCoord * reachDistance, playerPosition.yCoord + playerUp.yCoord * reachDistance, playerPosition.zCoord + playerUp.zCoord * reachDistance);
      return world.func_147447_a(playerPosition, playerViewOffset, collisionFlag, !collisionFlag, false);
   }

   // $FF: synthetic class
   static class NamelessClass1354619960 {

      // $FF: synthetic field
      static final int[] $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType = new int[MovingObjectType.values().length];


      static {
         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.ENTITY.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.BLOCK.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
