package com.emoniph.witchery.brewing.potions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityAIVillagerMateNow extends EntityAIBase {

   private EntityVillager villagerObj;
   private EntityVillager mate;
   private World worldObj;
   private int matingTimeout;
   private boolean begin;
   Village villageObj;


   public EntityAIVillagerMateNow(EntityVillager p_i1634_1_) {
      this.villagerObj = p_i1634_1_;
      this.worldObj = p_i1634_1_.worldObj;
      this.setMutexBits(3);
   }

   public void beginMating() {
      this.begin = true;
   }

   public boolean shouldExecute() {
      if(this.villagerObj.getGrowingAge() == 0 && this.begin) {
         this.villageObj = this.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.villagerObj.posX), MathHelper.floor_double(this.villagerObj.posY), MathHelper.floor_double(this.villagerObj.posZ), 0);
         if(this.villageObj == null) {
            return false;
         } else if(!this.checkSufficientDoorsPresentForNewVillager()) {
            return false;
         } else {
            Entity entity = this.worldObj.findNearestEntityWithinAABB(EntityVillager.class, this.villagerObj.boundingBox.expand(8.0D, 3.0D, 8.0D), this.villagerObj);
            if(entity == null) {
               return false;
            } else {
               this.mate = (EntityVillager)entity;
               return this.mate.getGrowingAge() == 0;
            }
         }
      } else {
         return false;
      }
   }

   public void startExecuting() {
      this.matingTimeout = 300;
      this.villagerObj.setMating(true);
      this.begin = false;
   }

   public void resetTask() {
      this.villageObj = null;
      this.mate = null;
      this.villagerObj.setMating(false);
      this.begin = false;
   }

   public boolean continueExecuting() {
      boolean keepGoing = this.matingTimeout >= 0 && this.checkSufficientDoorsPresentForNewVillager() && this.villagerObj.getGrowingAge() == 0;
      return keepGoing;
   }

   public void updateTask() {
      --this.matingTimeout;
      this.villagerObj.getLookHelper().setLookPositionWithEntity(this.mate, 10.0F, 30.0F);
      if(this.villagerObj.getDistanceSqToEntity(this.mate) > 2.25D) {
         this.villagerObj.getNavigator().tryMoveToEntityLiving(this.mate, 0.25D);
      } else if(this.matingTimeout == 0 && this.mate.isMating()) {
         this.giveBirth();
      }

      if(this.villagerObj.getRNG().nextInt(35) == 0) {
         this.worldObj.setEntityState(this.villagerObj, (byte)12);
      }

   }

   private boolean checkSufficientDoorsPresentForNewVillager() {
      return true;
   }

   private void giveBirth() {
      EntityVillager entityvillager = this.villagerObj.createChild(this.mate);
      this.mate.setGrowingAge(500);
      this.villagerObj.setGrowingAge(500);
      entityvillager.setGrowingAge(-24000);
      entityvillager.setLocationAndAngles(this.villagerObj.posX, this.villagerObj.posY, this.villagerObj.posZ, 0.0F, 0.0F);
      this.worldObj.spawnEntityInWorld(entityvillager);
      this.worldObj.setEntityState(entityvillager, (byte)12);
   }
}
