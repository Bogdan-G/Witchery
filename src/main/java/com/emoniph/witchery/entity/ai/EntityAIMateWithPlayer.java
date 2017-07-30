package com.emoniph.witchery.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityAIMateWithPlayer extends EntityAIBase {

   private EntityVillager villagerObj;
   private EntityPlayer mate;
   private World worldObj;
   private int matingTimeout;
   private boolean forceExecute;


   public EntityAIMateWithPlayer(EntityVillager par1EntityVillager) {
      this.villagerObj = par1EntityVillager;
      this.worldObj = par1EntityVillager.worldObj;
      this.setMutexBits(3);
   }

   public void forceTask(EntityPlayer player) {
      this.forceExecute = true;
      this.mate = player;
   }

   public boolean shouldExecute() {
      if(this.villagerObj.getGrowingAge() != 0) {
         return false;
      } else if(!this.forceExecute && this.villagerObj.getRNG().nextInt(500) != 0) {
         return false;
      } else if(!this.forceExecute) {
         Entity entity = this.worldObj.findNearestEntityWithinAABB(EntityPlayer.class, this.villagerObj.boundingBox.expand(16.0D, 3.0D, 16.0D), this.villagerObj);
         if(entity == null) {
            return false;
         } else {
            this.mate = (EntityPlayer)entity;
            return true;
         }
      } else {
         this.forceExecute = false;
         return true;
      }
   }

   public void startExecuting() {
      this.matingTimeout = 1000;
      this.villagerObj.setMating(true);
   }

   public void resetTask() {
      this.mate = null;
      this.villagerObj.setMating(false);
   }

   public boolean continueExecuting() {
      return this.matingTimeout >= 0 && this.villagerObj.getGrowingAge() == 0;
   }

   public void updateTask() {
      if(this.matingTimeout > 0) {
         --this.matingTimeout;
      }

      this.villagerObj.getLookHelper().setLookPositionWithEntity(this.mate, 10.0F, 30.0F);
      if(this.villagerObj.getDistanceSqToEntity(this.mate) > 2.25D) {
         this.villagerObj.getNavigator().tryMoveToEntityLiving(this.mate, 0.3D);
      } else if(this.matingTimeout > 0 && this.villagerObj.getDistanceSqToEntity(this.mate) <= 2.25D) {
         this.matingTimeout = 0;
         this.giveBirth();
      }

      if(this.villagerObj.getRNG().nextInt(20) == 0) {
         this.worldObj.setEntityState(this.villagerObj, (byte)12);
      }

   }

   private void giveBirth() {
      EntityVillager entityvillager = this.villagerObj.createChild(this.villagerObj);
      this.villagerObj.setGrowingAge(6000);
      entityvillager.setGrowingAge(-24000);
      entityvillager.setLocationAndAngles(this.villagerObj.posX, this.villagerObj.posY, this.villagerObj.posZ, 0.0F, 0.0F);
      this.worldObj.spawnEntityInWorld(entityvillager);
      this.worldObj.setEntityState(entityvillager, (byte)12);
   }
}
