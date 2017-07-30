package com.emoniph.witchery.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIFlyerWander extends EntityAIBase {

   private double xPosition;
   private double yPosition;
   private double zPosition;
   private double speed;
   World worldObj;
   public int courseChangeCooldown;
   public double waypointX;
   public double waypointY;
   public double waypointZ;
   public double fleeDistance;
   EntityLiving living;


   public EntityAIFlyerWander(EntityLiving par1EntityCreature, double par2, double fleeDistance) {
      this.living = par1EntityCreature;
      this.worldObj = this.living.worldObj;
      this.speed = par2;
      this.fleeDistance = fleeDistance;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      boolean isTame = this.living instanceof EntityTameable && ((EntityTameable)this.living).isTamed();
      return !isTame && this.living.worldObj.getClosestPlayerToEntity(this.living, this.fleeDistance) != null?true:(this.living.getAge() >= 100?false:this.living.getRNG().nextInt(this.living.worldObj.provider.isDaytime()?300:100) == 0 && (!(this.living instanceof EntityTameable) || !((EntityTameable)this.living).isSitting()));
   }

   public boolean continueExecuting() {
      return this.living instanceof EntityTameable && !((EntityTameable)this.living).isSitting() || this.living.getRNG().nextInt(40) != 0;
   }

   public void startExecuting() {}

   public void updateTask() {
      double d0 = this.waypointX - this.living.posX;
      double d1 = this.waypointY - this.living.posY;
      double d2 = this.waypointZ - this.living.posZ;
      double d3 = d0 * d0 + d1 * d1 + d2 * d2;
      if(d3 < 1.0D || d3 > 3600.0D) {
         float distance = this.living instanceof EntityTameable && ((EntityTameable)this.living).isTamed()?2.0F:6.0F;
         this.waypointX = this.living.posX + (double)((this.worldObj.rand.nextFloat() * 8.0F - 4.0F) * distance);
         this.waypointY = this.living.posY + (double)((this.worldObj.rand.nextFloat() * 2.0F - 1.0F) * distance);
         this.waypointZ = this.living.posZ + (double)((this.worldObj.rand.nextFloat() * 8.0F - 4.0F) * distance);
      }

      if(this.courseChangeCooldown-- <= 0) {
         this.courseChangeCooldown += this.worldObj.rand.nextInt(2) + 2;
         d3 = (double)MathHelper.sqrt_double(d3);
         if(this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3)) {
            this.living.motionX += d0 / d3 * 0.1D;
            this.living.motionY += d1 / d3 * 0.1D;
            this.living.motionZ += d2 / d3 * 0.1D;
         } else {
            this.waypointX = this.living.posX;
            this.waypointY = this.living.posY;
            this.waypointZ = this.living.posZ;
         }
      }

      this.living.renderYawOffset = this.living.rotationYaw = -((float)Math.atan2(this.living.motionX, this.living.motionZ)) * 180.0F / 3.1415927F;
   }

   private boolean isCourseTraversable(double par1, double par3, double par5, double par7) {
      double d4 = (par1 - this.living.posX) / par7;
      double d5 = (par3 - this.living.posY) / par7;
      double d6 = (par5 - this.living.posZ) / par7;
      AxisAlignedBB axisalignedbb = this.living.boundingBox.copy();

      for(int i = 1; (double)i < par7; ++i) {
         axisalignedbb.offset(d4, d5, d6);
         if(!this.living.worldObj.getCollidingBoundingBoxes(this.living, axisalignedbb).isEmpty()) {
            return false;
         }
      }

      return true;
   }
}
