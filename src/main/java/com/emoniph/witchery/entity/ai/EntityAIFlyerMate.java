package com.emoniph.witchery.entity.ai;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIFlyerMate extends EntityAIBase {

   private EntityAnimal theAnimal;
   World theWorld;
   private EntityAnimal targetMate;
   int spawnBabyDelay;
   int updateDelay = 0;
   double moveSpeed;


   public EntityAIFlyerMate(EntityAnimal par1EntityAnimal, double par2) {
      this.theAnimal = par1EntityAnimal;
      this.theWorld = par1EntityAnimal.worldObj;
      this.moveSpeed = par2;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      if(!this.theAnimal.isInLove()) {
         return false;
      } else {
         this.targetMate = this.getNearbyMate();
         return this.targetMate != null;
      }
   }

   public boolean continueExecuting() {
      return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
   }

   public void resetTask() {
      this.targetMate = null;
      this.spawnBabyDelay = 0;
      this.updateDelay = 0;
   }

   public void updateTask() {
      if(--this.updateDelay <= 0) {
         double d0 = this.targetMate.posX - this.theAnimal.posX;
         double d1 = this.targetMate.posY - this.theAnimal.posY;
         double d2 = this.targetMate.posZ - this.theAnimal.posZ;
         double d3 = d0 * d0 + d1 * d1 + d2 * d2;
         d3 = (double)MathHelper.sqrt_double(d3);
         if(this.isCourseTraversable(this.targetMate.posX, this.targetMate.posY, this.targetMate.posZ, d3)) {
            this.theAnimal.motionX += d0 / d3 * 0.25D;
            this.theAnimal.motionY += d1 / d3 * 0.25D;
            this.theAnimal.motionZ += d2 / d3 * 0.25D;
         }

         this.updateDelay = 10;
      }

      this.theAnimal.renderYawOffset = this.theAnimal.rotationYaw = -((float)Math.atan2(this.theAnimal.motionX, this.theAnimal.motionZ)) * 180.0F / 3.1415927F;
      ++this.spawnBabyDelay;
      if(this.spawnBabyDelay >= 60 && this.theAnimal.getDistanceSqToEntity(this.targetMate) < 9.0D) {
         this.spawnBaby();
      }

   }

   private boolean isCourseTraversable(double par1, double par3, double par5, double par7) {
      double d4 = (par1 - this.theAnimal.posX) / par7;
      double d5 = (par3 - this.theAnimal.posY) / par7;
      double d6 = (par5 - this.theAnimal.posZ) / par7;
      AxisAlignedBB axisalignedbb = this.theAnimal.boundingBox.copy();

      for(int i = 1; (double)i < par7; ++i) {
         axisalignedbb.offset(d4, d5, d6);
         if(!this.theAnimal.worldObj.getCollidingBoundingBoxes(this.theAnimal, axisalignedbb).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private EntityAnimal getNearbyMate() {
      float f = 8.0F;
      List list = this.theWorld.getEntitiesWithinAABB(this.theAnimal.getClass(), this.theAnimal.boundingBox.expand((double)f, (double)f, (double)f));
      double d0 = Double.MAX_VALUE;
      EntityAnimal entityanimal = null;
      Iterator iterator = list.iterator();

      while(iterator.hasNext()) {
         EntityAnimal entityanimal1 = (EntityAnimal)iterator.next();
         if(this.theAnimal.canMateWith(entityanimal1) && this.theAnimal.getDistanceSqToEntity(entityanimal1) < d0) {
            entityanimal = entityanimal1;
            d0 = this.theAnimal.getDistanceSqToEntity(entityanimal1);
         }
      }

      return entityanimal;
   }

   private void spawnBaby() {
      EntityAgeable entityageable = this.theAnimal.createChild(this.targetMate);
      if(entityageable != null) {
         this.theAnimal.setGrowingAge(6000);
         this.targetMate.setGrowingAge(6000);
         this.theAnimal.resetInLove();
         this.targetMate.resetInLove();
         entityageable.setGrowingAge(-24000);
         entityageable.setLocationAndAngles(this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, 0.0F, 0.0F);
         this.theWorld.spawnEntityInWorld(entityageable);
         Random random = this.theAnimal.getRNG();

         for(int i = 0; i < 7; ++i) {
            double d0 = random.nextGaussian() * 0.02D;
            double d1 = random.nextGaussian() * 0.02D;
            double d2 = random.nextGaussian() * 0.02D;
            this.theWorld.spawnParticle("heart", this.theAnimal.posX + (double)(random.nextFloat() * this.theAnimal.width * 2.0F) - (double)this.theAnimal.width, this.theAnimal.posY + 0.5D + (double)(random.nextFloat() * this.theAnimal.height), this.theAnimal.posZ + (double)(random.nextFloat() * this.theAnimal.width * 2.0F) - (double)this.theAnimal.width, d0, d1, d2);
         }

         this.theWorld.spawnEntityInWorld(new EntityXPOrb(this.theWorld, this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, random.nextInt(7) + 1));
      }

   }
}
