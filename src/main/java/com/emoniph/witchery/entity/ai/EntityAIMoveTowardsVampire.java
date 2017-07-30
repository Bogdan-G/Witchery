package com.emoniph.witchery.entity.ai;

import com.emoniph.witchery.common.ExtendedPlayer;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class EntityAIMoveTowardsVampire extends EntityAIBase {

   private EntityCreature theEntity;
   private EntityLivingBase targetEntity;
   private double movePosX;
   private double movePosY;
   private double movePosZ;
   private double speed;
   private float maxTargetDistance;
   private float minTargetDistance;


   public EntityAIMoveTowardsVampire(EntityCreature par1EntityCreature, double par2, float min, float max) {
      this.theEntity = par1EntityCreature;
      this.speed = par2;
      this.minTargetDistance = min;
      this.maxTargetDistance = max;
      this.setMutexBits(1);
   }

   private EntityLivingBase getDistanceSqToPartner() {
      double R = (double)this.maxTargetDistance;
      AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(this.theEntity.posX - R, this.theEntity.posY - R, this.theEntity.posZ - R, this.theEntity.posX + R, this.theEntity.posY + R, this.theEntity.posZ + R);
      List mogs = this.theEntity.worldObj.getEntitiesWithinAABB(EntityPlayer.class, bb);
      double minDistance = Double.MAX_VALUE;
      EntityPlayer target = null;
      Iterator i$ = mogs.iterator();

      while(i$.hasNext()) {
         EntityPlayer player = (EntityPlayer)i$.next();
         if(ExtendedPlayer.get(player).getVampireLevel() >= 8) {
            double distance = this.theEntity.getDistanceSqToEntity(player);
            if(distance < minDistance) {
               minDistance = distance;
               target = player;
            }
         }
      }

      return target;
   }

   public boolean shouldExecute() {
      this.targetEntity = this.getDistanceSqToPartner();
      if(this.targetEntity == null) {
         return false;
      } else {
         double dist = this.targetEntity.getDistanceSqToEntity(this.theEntity);
         return dist > (double)(this.maxTargetDistance * this.maxTargetDistance)?false:dist >= (double)(this.minTargetDistance * this.minTargetDistance);
      }
   }

   public boolean continueExecuting() {
      if(this.theEntity.ticksExisted % 20 == 0) {
         this.theEntity.getNavigator().tryMoveToXYZ(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ, this.speed);
      }

      return true;
   }

   public void resetTask() {
      this.targetEntity = null;
   }

   public void startExecuting() {
      this.theEntity.getNavigator().tryMoveToXYZ(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ, this.speed);
   }
}
