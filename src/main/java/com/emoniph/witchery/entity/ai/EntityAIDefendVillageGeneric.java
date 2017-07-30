package com.emoniph.witchery.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.village.Village;

public class EntityAIDefendVillageGeneric extends EntityAITarget {

   EntityAIDefendVillageGeneric.IVillageGuard defender;
   EntityLivingBase villageAgressorTarget;


   public EntityAIDefendVillageGeneric(EntityAIDefendVillageGeneric.IVillageGuard guard) {
      super(guard.getCreature(), false, true);
      this.defender = guard;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      Village village = this.defender.getVillage();
      if(village == null) {
         return false;
      } else {
         this.villageAgressorTarget = village.findNearestVillageAggressor(this.defender.getCreature());
         if(!this.isSuitableTarget(this.villageAgressorTarget, false)) {
            if(super.taskOwner.getRNG().nextInt(20) == 0) {
               this.villageAgressorTarget = village.func_82685_c(this.defender.getCreature());
               return this.isSuitableTarget(this.villageAgressorTarget, false);
            } else {
               return false;
            }
         } else {
            return true;
         }
      }
   }

   public void startExecuting() {
      this.defender.getCreature().setAttackTarget(this.villageAgressorTarget);
      super.startExecuting();
   }

   public interface IVillageGuard {

      Village getVillage();

      EntityCreature getCreature();
   }
}
