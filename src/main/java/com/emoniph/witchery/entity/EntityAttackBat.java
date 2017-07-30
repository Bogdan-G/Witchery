package com.emoniph.witchery.entity;

import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.mojang.authlib.GameProfile;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class EntityAttackBat extends EntityBat {

   private EntityPlayer ownerPlayer;
   private GameProfile owner;


   public EntityAttackBat(World world) {
      super(world);
   }

   protected void collideWithNearbyEntities() {
      List list = super.worldObj.getEntitiesWithinAABBExcludingEntity(this, super.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
      if(list != null && !list.isEmpty()) {
         for(int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity)list.get(i);
            if(entity.canBePushed()) {
               this.collideWithEntity(entity);
            }
         }
      }

   }

   protected void collideWithEntity(Entity entity) {
      if(!super.worldObj.isRemote && entity instanceof EntityLivingBase) {
         EntityLivingBase target = (EntityLivingBase)entity;
         if(this.ownerPlayer == null) {
            this.ownerPlayer = this.getOwner();
         }

         if(target != this.ownerPlayer && !(target instanceof EntityBat) && !target.isDead) {
            target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.ownerPlayer), 4.0F);
            ParticleEffect.REDDUST.send(SoundEffect.WITCHERY_RANDOM_DRINK, entity, (double)entity.width, (double)entity.height, 16);
            this.setDead();
         }
      }

   }

   protected void updateAITasks() {
      if(!super.worldObj.isRemote) {
         boolean done = false;
         if(super.ticksExisted > 300) {
            ParticleEffect.SMOKE.send(SoundEffect.NONE, this, (double)super.width, (double)super.height, 16);
            this.setDead();
         } else {
            if(this.ownerPlayer == null) {
               this.ownerPlayer = this.getOwner();
            }

            if(this.ownerPlayer != null && this.ownerPlayer.dimension == super.dimension) {
               MovingObjectPosition mop = InfusionOtherwhere.raytraceEntities(super.worldObj, this.ownerPlayer, true, 32.0D);
               if(mop != null && mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase && !(mop.entityHit instanceof EntityBat)) {
                  double d0 = mop.entityHit.posX - super.posX;
                  double d1 = mop.entityHit.posY - super.posY;
                  double d2 = mop.entityHit.posZ - super.posZ;
                  double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                  d3 = (double)MathHelper.sqrt_double(d3);
                  if(this.isCourseTraversable(mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, d3)) {
                     super.motionX += d0 / d3 * 0.1D;
                     super.motionY += d1 / d3 * 0.1D;
                     super.motionZ += d2 / d3 * 0.1D;
                     float f = (float)(Math.atan2(super.motionZ, super.motionX) * 180.0D / 3.141592653589793D) - 90.0F;
                     float f1 = MathHelper.wrapAngleTo180_float(f - super.rotationYaw);
                     super.moveForward = 0.5F;
                     super.rotationYaw += f1;
                     done = true;
                  }

                  super.renderYawOffset = super.rotationYaw = -((float)Math.atan2(super.motionX, super.motionZ)) * 180.0F / 3.1415927F;
               }
            }
         }

         if(!done) {
            super.updateAITasks();
         }
      }

   }

   private boolean isCourseTraversable(double par1, double par3, double par5, double par7) {
      double d4 = (par1 - super.posX) / par7;
      double d5 = (par3 - super.posY) / par7;
      double d6 = (par5 - super.posZ) / par7;
      AxisAlignedBB axisalignedbb = super.boundingBox.copy();

      for(int i = 1; (double)i < par7; ++i) {
         axisalignedbb.offset(d4, d5, d6);
         if(!super.worldObj.getCollidingBoundingBoxes(this, axisalignedbb).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public void setOwner(EntityPlayer player) {
      this.owner = player.getGameProfile();
   }

   public EntityPlayer getOwner() {
      return this.owner != null?super.worldObj.func_152378_a(this.owner.getId()):null;
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      if(this.owner != null) {
         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
         NBTUtil.func_152460_a(nbttagcompound1, this.owner);
         nbtRoot.setTag("Owner", nbttagcompound1);
      }

   }

   public void readFromNBT(NBTTagCompound nbtRoot) {
      super.readFromNBT(nbtRoot);
      if(nbtRoot.hasKey("Owner", 10)) {
         this.owner = NBTUtil.func_152459_a(nbtRoot.getCompoundTag("Owner"));
      }

   }
}
