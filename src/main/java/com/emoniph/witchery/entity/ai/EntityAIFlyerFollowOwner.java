package com.emoniph.witchery.entity.ai;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.item.ItemGeneral;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class EntityAIFlyerFollowOwner extends EntityAIBase {

   private EntityTameable thePet;
   private EntityLivingBase theOwner;
   World theWorld;
   private double field_75336_f;
   private int field_75343_h;
   float maxDist;
   float minDist;
   private boolean field_75344_i;


   public EntityAIFlyerFollowOwner(EntityTameable par1EntityTameable, double par2, float par4, float par5) {
      this.thePet = par1EntityTameable;
      this.theWorld = par1EntityTameable.worldObj;
      this.field_75336_f = par2;
      this.minDist = par4;
      this.maxDist = par5;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      Object entitylivingbase = this.thePet.getOwner();
      if(entitylivingbase == null && Familiar.couldBeFamiliar(this.thePet)) {
         entitylivingbase = Familiar.getOwnerForFamiliar(this.thePet).getCurrentOwner();
      }

      if(entitylivingbase == null) {
         return false;
      } else if(this.thePet.isSitting()) {
         return false;
      } else if(this.thePet.dimension == ((Entity)entitylivingbase).dimension && this.thePet.getDistanceSqToEntity((Entity)entitylivingbase) >= (double)(this.minDist * this.minDist)) {
         this.theOwner = (EntityLivingBase)entitylivingbase;
         return true;
      } else {
         return false;
      }
   }

   public boolean continueExecuting() {
      return this.thePet.getDistanceSqToEntity(this.theOwner) > (double)(this.maxDist * this.maxDist) && !this.thePet.isSitting();
   }

   public void startExecuting() {
      this.field_75343_h = 0;
   }

   public void resetTask() {
      this.theOwner = null;
   }

   public void updateTask() {
      if(!this.thePet.isSitting() && --this.field_75343_h <= 0) {
         this.field_75343_h = 10;
         if(this.thePet.dimension == this.theOwner.dimension && this.thePet.getDistanceSqToEntity(this.theOwner) < 256.0D) {
            double var15 = this.theOwner.posX - this.thePet.posX;
            double var16 = this.theOwner.posY - this.thePet.posY;
            double var17 = this.theOwner.posZ - this.thePet.posZ;
            double var18 = var15 * var15 + var16 * var16 + var17 * var17;
            var18 = (double)MathHelper.sqrt_double(var18);
            if(this.isCourseTraversable(this.theOwner.posX, this.theOwner.posY, this.theOwner.posZ, var18)) {
               this.thePet.motionX += var15 / var18 * 0.1D;
               if(this.thePet.posY < this.theOwner.posY + 2.0D) {
                  this.thePet.motionY += var16 / var18 * 0.1D + 0.1D;
               } else {
                  this.thePet.motionY += var16 / var18 * 0.1D;
               }

               this.thePet.motionZ += var17 / var18 * 0.1D;
            } else {
               double var19 = this.thePet.posX + (double)((this.thePet.worldObj.rand.nextFloat() * 8.0F - 4.0F) * 6.0F);
               double newY1 = this.thePet.posY + (double)((this.thePet.worldObj.rand.nextFloat() * 2.0F - 1.0F) * 6.0F);
               double newZ = this.thePet.posZ + (double)((this.thePet.worldObj.rand.nextFloat() * 8.0F - 4.0F) * 6.0F);
               var15 = var19 - this.thePet.posX;
               var16 = newY1 - this.thePet.posY;
               var17 = newZ - this.thePet.posZ;
               var18 = var15 * var15 + var16 * var16 + var17 * var17;
               var18 = (double)MathHelper.sqrt_double(var18);
               this.thePet.motionX += var15 / var18 * 0.1D;
               this.thePet.motionY += var16 / var18 * 0.1D + 0.1D;
               this.thePet.motionZ += var17 / var18 * 0.1D;
            }
         } else {
            int d0 = MathHelper.floor_double(this.theOwner.posX) - 2;
            int z = MathHelper.floor_double(this.theOwner.posZ) - 2;
            int d1 = MathHelper.floor_double(this.theOwner.boundingBox.minY) - 2;

            for(int dx = 0; dx <= 4; ++dx) {
               for(int d2 = 0; d2 <= 4; ++d2) {
                  for(int dy = 0; dy <= 4; ++dy) {
                     int d3 = d0 + d2;
                     int newY = d1 + dy;
                     int newX = z + d2;
                     if(this.theOwner.worldObj.getBlock(d3, newY - 1, newX).isSideSolid(this.theOwner.worldObj, d3, newY - 1, newX, ForgeDirection.UP) && !this.theOwner.worldObj.getBlock(d3, newY, newX).isNormalCube() && !this.theOwner.worldObj.getBlock(d3, newY + 1, newX).isNormalCube()) {
                        ItemGeneral var10000 = Witchery.Items.GENERIC;
                        ItemGeneral.teleportToLocation(this.theWorld, 0.5D + (double)d3, 0.01D + (double)newY, 0.5D + (double)newX, this.theOwner.dimension, this.thePet, true);
                        return;
                     }
                  }
               }
            }
         }

         this.thePet.renderYawOffset = this.thePet.rotationYaw = -((float)Math.atan2(this.thePet.motionX, this.thePet.motionZ)) * 180.0F / 3.1415927F;
      }

   }

   private boolean isCourseTraversable(double par1, double par3, double par5, double par7) {
      double d4 = (par1 - this.thePet.posX) / par7;
      double d5 = (par3 - this.thePet.posY) / par7;
      double d6 = (par5 - this.thePet.posZ) / par7;
      AxisAlignedBB axisalignedbb = this.thePet.boundingBox.copy();

      for(int i = 1; (double)i < par7; ++i) {
         axisalignedbb.offset(d4, d5, d6);
         if(!this.thePet.worldObj.getCollidingBoundingBoxes(this.thePet, axisalignedbb).isEmpty()) {
            return false;
         }
      }

      return true;
   }
}
