package com.emoniph.witchery.util;

import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class TameableUtil {

   public static void setOwner(EntityTameable tameable, EntityPlayer owner) {
      if(tameable != null && owner != null) {
         tameable.func_152115_b(owner.getUniqueID().toString());
      }

   }

   public static void setOwnerByUsername(EntityTameable tameable, String ownerUsername) {
      EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(ownerUsername);
      setOwner(tameable, player);
   }

   public static boolean isOwner(EntityTameable tameable, EntityPlayer player) {
      return tameable.func_152114_e(player);
   }

   public static boolean hasOwner(EntityTameable tameable) {
      String id = tameable.func_152113_b();
      return id != null && !id.isEmpty();
   }

   public static EntityLivingBase getOwnerAccrossDimensions(EntityTameable tameable) {
      String id = tameable.func_152113_b();
      UUID uuid = UUID.fromString(id);
      return getPlayerByID(uuid);
   }

   public static EntityPlayerMP getPlayerByID(UUID uuid) {
      Iterator iterator = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();

      while(iterator.hasNext()) {
         EntityPlayerMP entityplayermp = (EntityPlayerMP)iterator.next();
         if(entityplayermp.getGameProfile().getId().equals(uuid)) {
            return entityplayermp;
         }
      }

      return null;
   }

   public static void cloneOwner(EntityTameable tameable, EntityTameable tameableToCopyFrom) {
      tameable.func_152115_b(tameableToCopyFrom.func_152113_b());
   }
}
