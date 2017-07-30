package com.emoniph.witchery.infusion;

import com.emoniph.witchery.infusion.InfusedBrewGraveEffect;
import com.emoniph.witchery.infusion.InfusedBrewSoaringEffect;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.TimeUtil;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public abstract class InfusedBrewEffect {

   public static final ArrayList brewList = new ArrayList();
   public static final InfusedBrewEffect Soaring = new InfusedBrewSoaringEffect(1, 144000L);
   public static final InfusedBrewEffect Grave = new InfusedBrewGraveEffect(2, 144000L);
   public final int id;
   public final long durationTicks;
   public final int imageMapX;
   public final int imageMapY;
   private static String BREW_TYPE_KEY = "WITCInfusedBrewType";
   private static String BREW_START_KEY = "WITCInfusedBrewStart";
   private static String BREW_MINS_LEFT_KEY = "WITCInfusedBrewMinesLeft";


   protected InfusedBrewEffect(int id, long durationMS, int imageX, int imageY) {
      this.id = id;
      this.durationTicks = durationMS;
      this.imageMapX = imageX;
      this.imageMapY = imageY;

      while(brewList.size() <= id) {
         brewList.add((Object)null);
      }

      brewList.set(id, this);
   }

   public void drunk(World world, EntityPlayer player, ItemStack itemstack) {
      setActiveBrew(this, player, true);
      this.immediateEffect(world, player, itemstack);
   }

   public abstract void immediateEffect(World var1, EntityPlayer var2, ItemStack var3);

   public abstract void regularEffect(World var1, EntityPlayer var2);

   public boolean tryUseEffect(EntityPlayer player, MovingObjectPosition mop) {
      return this.isActive(player);
   }

   public boolean isActive(EntityPlayer player) {
      return getActiveBrew(player) == this;
   }

   public static InfusedBrewEffect getActiveBrew(EntityPlayer player) {
      if(player != null) {
         NBTTagCompound nbtPlayer = Infusion.getNBT(player);
         return getActiveBrew(nbtPlayer);
      } else {
         return null;
      }
   }

   public static InfusedBrewEffect getActiveBrew(NBTTagCompound nbtPlayer) {
      if(nbtPlayer != null) {
         int brewID = nbtPlayer.getInteger(BREW_TYPE_KEY);
         if(brewID > 0) {
            return (InfusedBrewEffect)brewList.get(brewID);
         }
      }

      return null;
   }

   public static long getActiveBrewStartTime(NBTTagCompound nbtPlayer) {
      if(nbtPlayer != null) {
         long startTime = nbtPlayer.getLong(BREW_START_KEY);
         return startTime;
      } else {
         return 0L;
      }
   }

   public static String getMinutesRemaining(World world, NBTTagCompound nbtPlayer, InfusedBrewEffect effect) {
      if(nbtPlayer != null) {
         long minsLeft = nbtPlayer.getLong(BREW_MINS_LEFT_KEY);
         return String.format("%d", new Object[]{Long.valueOf(minsLeft)});
      } else {
         return "";
      }
   }

   public static void setActiveBrew(InfusedBrewEffect brew, EntityPlayer player, boolean sync) {
      if(player != null) {
         NBTTagCompound nbtPlayer = Infusion.getNBT(player);
         setActiveBrew(player.worldObj, player, nbtPlayer, brew, sync);
      }

   }

   public static void setActiveBrew(World world, EntityPlayer player, NBTTagCompound nbtPlayer, InfusedBrewEffect brew, boolean sync) {
      if(nbtPlayer != null && !world.isRemote) {
         nbtPlayer.setInteger(BREW_TYPE_KEY, brew.id);
         nbtPlayer.setLong(BREW_START_KEY, TimeUtil.getServerTimeInTicks());
         if(sync) {
            Infusion.syncPlayer(world, player);
         }
      }

   }

   public static void setActiveBrewInfo(NBTTagCompound nbtPlayer, int brewID, long startTime) {
      nbtPlayer.setInteger(BREW_TYPE_KEY, brewID);
      nbtPlayer.setLong(BREW_MINS_LEFT_KEY, startTime);
   }

   public static void checkActiveEffects(World world, EntityPlayer player, NBTTagCompound nbtPlayer, boolean sync, long currentTime) {
      if(nbtPlayer != null && !world.isRemote) {
         InfusedBrewEffect activeEffect = getActiveBrew(nbtPlayer);
         if(activeEffect != null) {
            long startTime = nbtPlayer.getLong(BREW_START_KEY);
            if(currentTime > startTime + activeEffect.durationTicks) {
               nbtPlayer.removeTag(BREW_START_KEY);
               nbtPlayer.removeTag(BREW_TYPE_KEY);
               Infusion.syncPlayer(world, player);
               return;
            }

            activeEffect.regularEffect(world, player);
            if(sync) {
               Infusion.syncPlayer(world, player);
            }
         }
      }

   }

}
