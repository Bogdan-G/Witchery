package com.emoniph.witchery.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITemptWithPlayer extends EntityAIBase {

   private EntityCreature temptedEntity;
   private double field_75282_b;
   private double field_75283_c;
   private double field_75280_d;
   private double field_75281_e;
   private double field_75278_f;
   private double field_75279_g;
   private EntityPlayer temptingPlayer;
   private int delayTemptCounter;
   private boolean field_75287_j;
   private boolean field_75286_m;


   public EntityAITemptWithPlayer(EntityCreature par1EntityCreature, double par2) {
      this.temptedEntity = par1EntityCreature;
      this.field_75282_b = par2;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      if(this.delayTemptCounter > 0) {
         --this.delayTemptCounter;
         return false;
      } else {
         this.temptingPlayer = this.temptedEntity.worldObj.getClosestPlayerToEntity(this.temptedEntity, 10.0D);
         return this.temptingPlayer != null;
      }
   }

   public boolean continueExecuting() {
      return this.shouldExecute();
   }

   public void startExecuting() {
      this.field_75283_c = this.temptingPlayer.posX;
      this.field_75280_d = this.temptingPlayer.posY;
      this.field_75281_e = this.temptingPlayer.posZ;
      this.field_75287_j = true;
      this.field_75286_m = this.temptedEntity.getNavigator().getAvoidsWater();
      this.temptedEntity.getNavigator().setAvoidsWater(false);
   }

   public void resetTask() {
      this.temptingPlayer = null;
      this.temptedEntity.getNavigator().clearPathEntity();
      this.delayTemptCounter = 100;
      this.field_75287_j = false;
      this.temptedEntity.getNavigator().setAvoidsWater(this.field_75286_m);
   }

   public void updateTask() {
      this.temptedEntity.getLookHelper().setLookPositionWithEntity(this.temptingPlayer, 30.0F, (float)this.temptedEntity.getVerticalFaceSpeed());
      if(this.temptedEntity.getDistanceSqToEntity(this.temptingPlayer) < 6.25D) {
         this.temptedEntity.getNavigator().clearPathEntity();
      } else {
         this.temptedEntity.getNavigator().tryMoveToEntityLiving(this.temptingPlayer, this.field_75282_b);
      }

   }

   public boolean func_75277_f() {
      return this.field_75287_j;
   }
}
