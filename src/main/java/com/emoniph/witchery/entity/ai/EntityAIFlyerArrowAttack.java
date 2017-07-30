package com.emoniph.witchery.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class EntityAIFlyerArrowAttack extends EntityAIBase {

   private final EntityLiving entityHost;
   private final IRangedAttackMob rangedAttackEntityHost;
   private EntityLivingBase attackTarget;
   private int rangedAttackTime;
   private double entityMoveSpeed;
   private int field_75318_f;
   private int field_96561_g;
   private int maxRangedAttackTime;
   private float field_96562_i;
   private float field_82642_h;
   private static final String __OBFID = "CL_00001609";
   private int field_75445_i;
   private int failedPathFindingPenalty;


   public EntityAIFlyerArrowAttack(IRangedAttackMob par1IRangedAttackMob, double par2, int par4, float par5) {
      this(par1IRangedAttackMob, par2, par4, par4, par5);
   }

   public EntityAIFlyerArrowAttack(IRangedAttackMob par1IRangedAttackMob, double par2, int par4, int par5, float par6) {
      this.rangedAttackTime = -1;
      if(!(par1IRangedAttackMob instanceof EntityLivingBase)) {
         throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
      } else {
         this.rangedAttackEntityHost = par1IRangedAttackMob;
         this.entityHost = (EntityLiving)par1IRangedAttackMob;
         this.entityMoveSpeed = par2;
         this.field_96561_g = par4;
         this.maxRangedAttackTime = par5;
         this.field_96562_i = par6;
         this.field_82642_h = par6 * par6;
         this.setMutexBits(3);
      }
   }

   public boolean shouldExecute() {
      EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();
      if(entitylivingbase == null) {
         return false;
      } else {
         this.attackTarget = entitylivingbase;
         return true;
      }
   }

   public boolean continueExecuting() {
      return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
   }

   public void resetTask() {
      this.attackTarget = null;
      this.field_75318_f = 0;
      this.rangedAttackTime = -1;
      this.field_75445_i = 0;
   }

   public void updateTask() {
      double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.boundingBox.minY, this.attackTarget.posZ);
      boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);
      if(flag) {
         ++this.field_75318_f;
      } else {
         this.field_75318_f = 0;
      }

      if(d0 > (double)this.field_82642_h && --this.field_75445_i <= 0) {
         this.field_75445_i = this.failedPathFindingPenalty + 4 + this.entityHost.getRNG().nextInt(7);
         double f = this.attackTarget.posX - this.entityHost.posX;
         double d1 = this.attackTarget.posY - this.entityHost.posY;
         double d2 = this.attackTarget.posZ - this.entityHost.posZ;
         double d3 = f * f + d1 * d1 + d2 * d2;
         d3 = (double)MathHelper.sqrt_double(d3);
         if(this.isCourseTraversable(this.attackTarget.posX, this.attackTarget.posY, this.attackTarget.posZ, d3)) {
            this.entityHost.motionX += f / d3 * 0.15D;
            this.entityHost.motionY += d1 / d3 * 0.15D;
            this.entityHost.motionZ += d2 / d3 * 0.15D;
            this.failedPathFindingPenalty = 0;
         } else {
            this.failedPathFindingPenalty += 10;
         }

         this.entityHost.renderYawOffset = this.entityHost.rotationYaw = -((float)Math.atan2(this.entityHost.motionX, this.entityHost.motionZ)) * 180.0F / 3.1415927F;
         this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
      }

      this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
      float var12;
      if(--this.rangedAttackTime == 0) {
         if(d0 > (double)this.field_82642_h || !flag) {
            return;
         }

         var12 = MathHelper.sqrt_double(d0) / this.field_96562_i;
         float f1 = var12;
         if(var12 < 0.1F) {
            f1 = 0.1F;
         }

         if(f1 > 1.0F) {
            f1 = 1.0F;
         }

         this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, f1);
         this.rangedAttackTime = MathHelper.floor_float(var12 * (float)(this.maxRangedAttackTime - this.field_96561_g) + (float)this.field_96561_g);
      } else if(this.rangedAttackTime < 0) {
         var12 = MathHelper.sqrt_double(d0) / this.field_96562_i;
         this.rangedAttackTime = MathHelper.floor_float(var12 * (float)(this.maxRangedAttackTime - this.field_96561_g) + (float)this.field_96561_g);
      }

   }

   private boolean isCourseTraversable(double par1, double par3, double par5, double par7) {
      double d4 = (par1 - this.attackTarget.posX) / par7;
      double d5 = (par3 - this.attackTarget.posY) / par7;
      double d6 = (par5 - this.attackTarget.posZ) / par7;
      AxisAlignedBB axisalignedbb = this.attackTarget.boundingBox.copy();

      for(int i = 1; (double)i < par7; ++i) {
         axisalignedbb.offset(d4, d5, d6);
         if(!this.attackTarget.worldObj.getCollidingBoundingBoxes(this.attackTarget, axisalignedbb).isEmpty()) {
            return false;
         }
      }

      return true;
   }
}
