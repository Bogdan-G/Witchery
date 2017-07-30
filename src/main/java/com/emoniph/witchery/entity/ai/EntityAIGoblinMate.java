package com.emoniph.witchery.entity.ai;

import com.emoniph.witchery.entity.EntityGoblin;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityAIGoblinMate extends EntityAIBase {

   private EntityGoblin goblinObj;
   private EntityGoblin mate;
   private World worldObj;
   private int matingTimeout;
   Village villageObj;


   public EntityAIGoblinMate(EntityGoblin goblin) {
      this.goblinObj = goblin;
      this.worldObj = goblin.worldObj;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      if(this.goblinObj.getGrowingAge() != 0) {
         return false;
      } else if(this.goblinObj.getRNG().nextInt(500) != 0) {
         return false;
      } else {
         this.villageObj = this.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.goblinObj.posX), MathHelper.floor_double(this.goblinObj.posY), MathHelper.floor_double(this.goblinObj.posZ), 0);
         if(this.villageObj == null) {
            return false;
         } else if(!this.checkSufficientDoorsPresentForNewVillager()) {
            return false;
         } else {
            Entity entity = this.worldObj.findNearestEntityWithinAABB(EntityGoblin.class, this.goblinObj.boundingBox.expand(8.0D, 3.0D, 8.0D), this.goblinObj);
            if(entity == null) {
               return false;
            } else {
               this.mate = (EntityGoblin)entity;
               return this.mate.getGrowingAge() == 0;
            }
         }
      }
   }

   public void startExecuting() {
      this.matingTimeout = 300;
      this.goblinObj.setMating(true);
   }

   public void resetTask() {
      this.villageObj = null;
      this.mate = null;
      this.goblinObj.setMating(false);
   }

   public boolean continueExecuting() {
      return this.matingTimeout >= 0 && this.checkSufficientDoorsPresentForNewVillager() && this.goblinObj.getGrowingAge() == 0;
   }

   public void updateTask() {
      --this.matingTimeout;
      this.goblinObj.getLookHelper().setLookPositionWithEntity(this.mate, 10.0F, 30.0F);
      if(this.goblinObj.getDistanceSqToEntity(this.mate) > 2.25D) {
         this.goblinObj.getNavigator().tryMoveToEntityLiving(this.mate, 0.25D);
      } else if(this.matingTimeout == 0 && this.mate.isMating()) {
         this.giveBirth();
      }

      if(this.goblinObj.getRNG().nextInt(35) == 0) {
         this.worldObj.setEntityState(this.goblinObj, (byte)12);
      }

   }

   private boolean checkSufficientDoorsPresentForNewVillager() {
      if(!this.villageObj.isMatingSeason()) {
         return false;
      } else {
         int i = (int)((double)((float)this.villageObj.getNumVillageDoors()) * 0.35D);
         return this.getNumVillagers() < i;
      }
   }

   private int getNumVillagers() {
      if(this.worldObj != null && this.goblinObj != null) {
         List list = this.worldObj.getEntitiesWithinAABB(EntityGoblin.class, this.goblinObj.boundingBox.expand(32.0D, 3.0D, 32.0D));
         return list != null?list.size():0;
      } else {
         return 0;
      }
   }

   private void giveBirth() {
      EntityGoblin entityvillager = this.goblinObj.createChild(this.mate);
      this.mate.setGrowingAge(6000);
      this.goblinObj.setGrowingAge(6000);
      entityvillager.setGrowingAge(-24000);
      entityvillager.setLocationAndAngles(this.goblinObj.posX, this.goblinObj.posY, this.goblinObj.posZ, 0.0F, 0.0F);
      this.worldObj.spawnEntityInWorld(entityvillager);
      this.worldObj.setEntityState(entityvillager, (byte)12);
   }
}
