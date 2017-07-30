package com.emoniph.witchery.util;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBT {

   public static NBTTagCompound get(ItemStack stack) {
      if(!stack.hasTagCompound()) {
         stack.setTagCompound(new NBTTagCompound());
      }

      return stack.getTagCompound();
   }

   public static NBTTagCompound get(EntityItem entity) {
      return entity.getEntityData();
   }

   public static NBTTagCompound get(EntityLiving entity) {
      return entity.getEntityData();
   }

   public static NBTTagCompound get(EntityPlayer player) {
      NBTTagCompound nbtPlayer = player.getEntityData();
      NBTTagCompound nbtPersistant = nbtPlayer.getCompoundTag("PlayerPersisted");
      if(!nbtPlayer.hasKey("PlayerPersisted")) {
         nbtPlayer.setTag("PlayerPersisted", nbtPersistant);
      }

      return nbtPersistant;
   }
}
