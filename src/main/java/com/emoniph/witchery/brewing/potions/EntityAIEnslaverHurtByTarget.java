package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.PotionEnslaved;
import com.emoniph.witchery.util.EntityUtil;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIEnslaverHurtByTarget extends EntityAITarget {

   EntityCreature enslavedEntity;
   EntityLivingBase enslaversAttacker;
   private int enslaversRevengeTimer;


   public EntityAIEnslaverHurtByTarget(EntityCreature enslavedCreature) {
      super(enslavedCreature, false);
      this.enslavedEntity = enslavedCreature;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      if(!this.enslavedEntity.isPotionActive(Witchery.Potions.ENSLAVED)) {
         return false;
      } else {
         String ownerName = PotionEnslaved.getMobEnslaverName(this.enslavedEntity);
         if(ownerName != null && !ownerName.isEmpty()) {
            EntityPlayer enslaver = this.enslavedEntity.worldObj.getPlayerEntityByName(ownerName);
            if(enslaver == null) {
               return false;
            } else {
               this.enslaversAttacker = enslaver.getAITarget();
               int revengeTimer = enslaver.func_142015_aE();
               return revengeTimer == this.enslaversRevengeTimer?false:(this.enslaversAttacker == null?false:this.isSuitableTarget(this.enslaversAttacker, false));
            }
         } else {
            return false;
         }
      }
   }

   public void startExecuting() {
      EntityUtil.setTarget(super.taskOwner, this.enslaversAttacker);
      String enslaverName = PotionEnslaved.getMobEnslaverName(this.enslavedEntity);
      EntityPlayer enslaver = this.enslavedEntity.worldObj.getPlayerEntityByName(enslaverName);
      if(enslaver != null) {
         this.enslaversRevengeTimer = enslaver.func_142015_aE();
      }

      super.startExecuting();
   }
}
