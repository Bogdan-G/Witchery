package com.emoniph.witchery.entity.ai;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class EntityAIMoveTowardsEntityClass extends EntityAIBase {

   private EntityCreature theEntity;
   private EntityLivingBase targetEntity;
   private Class targetType;
   private double movePosX;
   private double movePosY;
   private double movePosZ;
   private double speed;
   private float maxTargetDistance;
   private float minTargetDistance;


   public EntityAIMoveTowardsEntityClass(EntityCreature par1EntityCreature, Class targetType, double par2, float par4, float par5) {
      this.theEntity = par1EntityCreature;
      this.targetType = targetType;
      this.speed = par2;
      this.minTargetDistance = par4;
      this.maxTargetDistance = par5;
      this.setMutexBits(1);
   }

   private EntityLiving getDistanceSqToPartner() {
      double R = (double)this.maxTargetDistance;
      AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(this.theEntity.posX - R, this.theEntity.posY - R, this.theEntity.posZ - R, this.theEntity.posX + R, this.theEntity.posY + R, this.theEntity.posZ + R);
      List mogs = this.theEntity.worldObj.getEntitiesWithinAABB(this.targetType, bb);
      double minDistance = Double.MAX_VALUE;
      EntityLiving target = null;
      Iterator i$ = mogs.iterator();

      while(i$.hasNext()) {
         Object obj = i$.next();
         EntityLiving mog = (EntityLiving)obj;
         double distance = this.theEntity.getDistanceSqToEntity(mog);
         if(distance < minDistance) {
            minDistance = distance;
            target = mog;
         }
      }

      return target;
   }

   public boolean shouldExecute() {
      if(this.theEntity.worldObj.rand.nextInt(20) != 0) {
         return false;
      } else {
         this.targetEntity = this.getDistanceSqToPartner();
         if(this.targetEntity == null) {
            return false;
         } else {
            double dist = this.targetEntity.getDistanceSqToEntity(this.theEntity);
            if(dist > (double)(this.maxTargetDistance * this.maxTargetDistance)) {
               return false;
            } else if(dist < (double)(this.minTargetDistance * this.minTargetDistance)) {
               return false;
            } else {
               Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, Vec3.createVectorHelper(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ));
               if(vec3 == null) {
                  return false;
               } else {
                  this.movePosX = vec3.xCoord;
                  this.movePosY = vec3.yCoord;
                  this.movePosZ = vec3.zCoord;
                  return true;
               }
            }
         }
      }
   }

   public boolean continueExecuting() {
      return !this.theEntity.getNavigator().noPath() && this.targetEntity.isEntityAlive() && this.targetEntity.getDistanceSqToEntity(this.theEntity) < (double)(this.maxTargetDistance * this.maxTargetDistance);
   }

   public void resetTask() {
      this.targetEntity = null;
   }

   public void startExecuting() {
      this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
   }
}
