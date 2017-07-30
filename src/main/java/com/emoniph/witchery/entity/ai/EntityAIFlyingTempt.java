package com.emoniph.witchery.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class EntityAIFlyingTempt extends EntityAIBase {

   private EntityCreature temptedEntity;
   private double field_75282_b;
   private double targetX;
   private double targetY;
   private double targetZ;
   private double field_75278_f;
   private double field_75279_g;
   private EntityPlayer temptingPlayer;
   private int delayTemptCounter;
   private boolean isRunning;
   private ItemStack[] breedingFood;
   private boolean scaredByPlayerMovement;
   private boolean field_75286_m;


   public EntityAIFlyingTempt(EntityCreature par1EntityCreature, double par2, ItemStack[] par4, boolean par5) {
      this.temptedEntity = par1EntityCreature;
      this.field_75282_b = par2;
      this.breedingFood = par4;
      this.scaredByPlayerMovement = par5;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      boolean isTame = this.temptedEntity instanceof EntityTameable && ((EntityTameable)this.temptedEntity).isTamed();
      if(isTame) {
         return false;
      } else if(this.delayTemptCounter > 0) {
         --this.delayTemptCounter;
         return false;
      } else {
         this.temptingPlayer = this.temptedEntity.worldObj.getClosestPlayerToEntity(this.temptedEntity, 10.0D);
         if(this.temptingPlayer == null) {
            return false;
         } else {
            ItemStack itemstack = this.temptingPlayer.getCurrentEquippedItem();
            return itemstack == null?false:this.isBreedingFood(itemstack);
         }
      }
   }

   private boolean isBreedingFood(ItemStack stack) {
      ItemStack[] arr$ = this.breedingFood;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ItemStack possibleFoods = arr$[i$];
         if(possibleFoods.isItemEqual(stack)) {
            return true;
         }
      }

      return false;
   }

   public boolean continueExecuting() {
      if(this.scaredByPlayerMovement) {
         if(this.temptedEntity.getDistanceSqToEntity(this.temptingPlayer) < 36.0D) {
            if(this.temptingPlayer.getDistanceSq(this.targetX, this.targetY, this.targetZ) > 0.010000000000000002D) {
               return false;
            }
         } else {
            this.targetX = this.temptingPlayer.posX;
            this.targetY = this.temptingPlayer.posY;
            this.targetZ = this.temptingPlayer.posZ;
         }

         this.field_75278_f = (double)this.temptingPlayer.rotationPitch;
         this.field_75279_g = (double)this.temptingPlayer.rotationYaw;
      }

      return this.shouldExecute();
   }

   public void startExecuting() {
      this.isRunning = true;
   }

   public void resetTask() {
      this.temptingPlayer = null;
      this.delayTemptCounter = 100;
      this.isRunning = false;
   }

   public void updateTask() {
      if(this.temptedEntity.getDistanceSqToEntity(this.temptingPlayer) >= 3.0D) {
         double d0 = this.targetX - this.temptedEntity.posX;
         double d1 = this.targetY - this.temptedEntity.posY;
         double d2 = this.targetZ - this.temptedEntity.posZ;
         double d3 = d0 * d0 + d1 * d1 + d2 * d2;
         d3 = (double)MathHelper.sqrt_double(d3);
         if(this.isCourseTraversable(this.targetX, this.targetY, this.targetZ, d3)) {
            this.temptedEntity.motionX += d0 / d3 * 0.05D;
            if(this.temptedEntity.posY < this.targetY + 1.0D) {
               this.temptedEntity.motionY += d1 / d3 * 0.05D + 0.025D;
            } else {
               this.temptedEntity.motionY += d1 / d3 * 0.05D;
            }

            this.temptedEntity.motionZ += d2 / d3 * 0.05D;
         }

         this.temptedEntity.renderYawOffset = this.temptedEntity.rotationYaw = -((float)Math.atan2(this.temptedEntity.motionX, this.temptedEntity.motionZ)) * 180.0F / 3.1415927F;
      }

   }

   private boolean isCourseTraversable(double par1, double par3, double par5, double par7) {
      double d4 = (par1 - this.temptedEntity.posX) / par7;
      double d5 = (par3 - this.temptedEntity.posY) / par7;
      double d6 = (par5 - this.temptedEntity.posZ) / par7;
      AxisAlignedBB axisalignedbb = this.temptedEntity.boundingBox.copy();

      for(int i = 1; (double)i < par7; ++i) {
         axisalignedbb.offset(d4, d5, d6);
         if(!this.temptedEntity.worldObj.getCollidingBoundingBoxes(this.temptedEntity, axisalignedbb).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public boolean isRunning() {
      return this.isRunning;
   }
}
