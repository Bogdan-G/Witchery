package com.emoniph.witchery.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityFlyingMob extends EntityMob {

   public EntityFlyingMob(World par1World) {
      super(par1World);
   }

   protected void fall(float par1) {}

   protected void updateFallState(double par1, boolean par3) {}

   public void moveEntityWithHeading(float par1, float par2) {
      if(this.isInWater()) {
         this.moveFlying(par1, par2, 0.02F);
         this.moveEntity(super.motionX, super.motionY, super.motionZ);
         super.motionX *= 0.800000011920929D;
         super.motionY *= 0.800000011920929D;
         super.motionZ *= 0.800000011920929D;
      } else if(this.handleLavaMovement()) {
         this.moveFlying(par1, par2, 0.02F);
         this.moveEntity(super.motionX, super.motionY, super.motionZ);
         super.motionX *= 0.5D;
         super.motionY *= 0.5D;
         super.motionZ *= 0.5D;
      } else {
         float d0 = 0.91F;
         if(super.onGround) {
            d0 = 0.54600006F;
            Block f3 = super.worldObj.getBlock(MathHelper.floor_double(super.posX), MathHelper.floor_double(super.boundingBox.minY) - 1, MathHelper.floor_double(super.posZ));
            if(f3 != Blocks.air) {
               d0 = f3.slipperiness * 0.91F;
            }
         }

         float f31 = 0.16277136F / (d0 * d0 * d0);
         this.moveFlying(par1, par2, super.onGround?0.1F * f31:0.02F);
         d0 = 0.91F;
         if(super.onGround) {
            d0 = 0.54600006F;
            Block d1 = super.worldObj.getBlock(MathHelper.floor_double(super.posX), MathHelper.floor_double(super.boundingBox.minY) - 1, MathHelper.floor_double(super.posZ));
            if(d1 != Blocks.air) {
               d0 = d1.slipperiness * 0.91F;
            }
         }

         this.moveEntity(super.motionX, super.motionY, super.motionZ);
         super.motionX *= (double)d0;
         super.motionY *= (double)d0;
         super.motionZ *= (double)d0;
      }

      super.prevLimbSwingAmount = super.limbSwingAmount;
      double d01 = super.posX - super.prevPosX;
      double d11 = super.posZ - super.prevPosZ;
      float f4 = MathHelper.sqrt_double(d01 * d01 + d11 * d11) * 4.0F;
      if(f4 > 1.0F) {
         f4 = 1.0F;
      }

      super.limbSwingAmount += (f4 - super.limbSwingAmount) * 0.4F;
      super.limbSwing += super.limbSwingAmount;
   }

   public boolean isOnLadder() {
      return false;
   }
}
