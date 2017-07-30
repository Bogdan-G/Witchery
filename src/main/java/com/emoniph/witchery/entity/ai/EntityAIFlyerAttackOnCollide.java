package com.emoniph.witchery.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIFlyerAttackOnCollide extends EntityAIBase {

   World worldObj;
   EntityCreature attacker;
   int attackTick;
   double speedTowardsTarget;
   boolean longMemory;
   PathEntity entityPathEntity;
   Class classTarget;
   private int field_75445_i;
   private int failedPathFindingPenalty;


   public EntityAIFlyerAttackOnCollide(EntityCreature par1EntityCreature, Class par2Class, double par3, boolean par5) {
      this(par1EntityCreature, par3, par5);
      this.classTarget = par2Class;
   }

   public EntityAIFlyerAttackOnCollide(EntityCreature par1EntityCreature, double par2, boolean par4) {
      this.attacker = par1EntityCreature;
      this.worldObj = par1EntityCreature.worldObj;
      this.speedTowardsTarget = par2;
      this.longMemory = par4;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
      return entitylivingbase == null?false:(!entitylivingbase.isEntityAlive()?false:this.classTarget == null || this.classTarget.isAssignableFrom(entitylivingbase.getClass()));
   }

   public boolean continueExecuting() {
      EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
      return entitylivingbase == null?false:(!entitylivingbase.isEntityAlive()?false:(!this.longMemory?!this.attacker.getNavigator().noPath():this.attacker.isWithinHomeDistance(MathHelper.floor_double(entitylivingbase.posX), MathHelper.floor_double(entitylivingbase.posY), MathHelper.floor_double(entitylivingbase.posZ))));
   }

   public void startExecuting() {
      this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
      this.field_75445_i = 0;
   }

   public void resetTask() {
      this.attacker.getNavigator().clearPathEntity();
   }

   public void updateTask() {
      EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
      this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
      double d0;
      if((this.longMemory || this.attacker.getEntitySenses().canSee(entitylivingbase)) && --this.field_75445_i <= 0) {
         this.field_75445_i = this.failedPathFindingPenalty + 4 + this.attacker.getRNG().nextInt(7);
         d0 = entitylivingbase.posX - this.attacker.posX;
         double d1 = entitylivingbase.posY - this.attacker.posY;
         double d2 = entitylivingbase.posZ - this.attacker.posZ;
         double d3 = d0 * d0 + d1 * d1 + d2 * d2;
         d3 = (double)MathHelper.sqrt_double(d3);
         if(this.isCourseTraversable(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, d3)) {
            this.attacker.motionX += d0 / d3 * 0.15D;
            this.attacker.motionY += d1 / d3 * 0.15D;
            this.attacker.motionZ += d2 / d3 * 0.15D;
            this.failedPathFindingPenalty = 0;
         } else {
            this.failedPathFindingPenalty += 10;
         }

         this.attacker.renderYawOffset = this.attacker.rotationYaw = -((float)Math.atan2(this.attacker.motionX, this.attacker.motionZ)) * 180.0F / 3.1415927F;
      }

      this.attackTick = Math.max(this.attackTick - 1, 0);
      d0 = (double)(this.attacker.width * 2.0F * this.attacker.width * 2.0F + entitylivingbase.width);
      if(this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.boundingBox.minY, entitylivingbase.posZ) <= d0 && this.attackTick <= 0) {
         this.attackTick = 20;
         if(this.attacker.getHeldItem() != null) {
            this.attacker.swingItem();
         }

         this.attacker.attackEntityAsMob(entitylivingbase);
      }

   }

   private boolean isCourseTraversable(double par1, double par3, double par5, double par7) {
      double d4 = (par1 - this.attacker.posX) / par7;
      double d5 = (par3 - this.attacker.posY) / par7;
      double d6 = (par5 - this.attacker.posZ) / par7;
      AxisAlignedBB axisalignedbb = this.attacker.boundingBox.copy();

      for(int i = 1; (double)i < par7; ++i) {
         axisalignedbb.offset(d4, d5, d6);
         if(!this.attacker.worldObj.getCollidingBoundingBoxes(this.attacker, axisalignedbb).isEmpty()) {
            return false;
         }
      }

      return true;
   }
}
