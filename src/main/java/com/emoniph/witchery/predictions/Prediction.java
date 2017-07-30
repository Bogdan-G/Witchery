package com.emoniph.witchery.predictions;

import com.emoniph.witchery.util.TimeUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public abstract class Prediction {

   public final int predictionID;
   public final double itemWeight;
   protected final String translationKey;
   protected final double selfFulfillmentProbabilityPerSec;


   public Prediction(int id, int itemWeight, double selfFulfillmentProbabilityPerSec, String translationKey) {
      this.predictionID = id;
      this.itemWeight = (double)itemWeight;
      this.translationKey = translationKey;
      this.selfFulfillmentProbabilityPerSec = selfFulfillmentProbabilityPerSec;
   }

   public boolean shouldTrySelfFulfill(World world, EntityPlayer player) {
      return world.rand.nextDouble() < this.selfFulfillmentProbabilityPerSec;
   }

   public boolean doSelfFulfillment(World world, EntityPlayer player) {
      return false;
   }

   public boolean checkIfFulfilled(World world, EntityPlayer player, LivingHurtEvent event, boolean isPastDue, boolean veryOld) {
      return false;
   }

   public boolean checkIfFulfilled(World world, EntityPlayer player, PlayerInteractEvent event, boolean isPastDue, boolean veryOld) {
      return false;
   }

   public boolean checkIfFulfilled(World world, EntityPlayer player, LivingUpdateEvent event, boolean isPastDue, boolean veryOld) {
      return false;
   }

   public boolean checkIfFulfilled(World worldObj, EntityPlayer player, HarvestDropsEvent event, boolean isPastDue, boolean veryOld) {
      return false;
   }

   public NBTTagCompound createTagCompound(World world) {
      NBTTagCompound compound = new NBTTagCompound();
      compound.setInteger("WITCPreID", this.predictionID);
      compound.setLong("WITCPreTime", TimeUtil.getServerTimeInTicks());
      return compound;
   }

   public String getTranslationKey() {
      return this.translationKey;
   }

   public boolean isPredictionPastDue(long predictionTime, long currentTime) {
      return currentTime - predictionTime > 9600L;
   }

   public boolean isPredictionPossible(World world, EntityPlayer player) {
      return true;
   }
}
