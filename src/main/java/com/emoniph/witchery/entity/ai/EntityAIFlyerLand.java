package com.emoniph.witchery.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIFlyerLand extends EntityAIBase {

   private double speed;
   int[] target;
   World worldObj;
   public int courseChangeCooldown;
   public double waypointX;
   public double waypointY;
   public double waypointZ;
   public boolean findTrees;
   EntityLiving living;


   public EntityAIFlyerLand(EntityLiving par1EntityCreature, double par2, boolean findTrees) {
      this.living = par1EntityCreature;
      this.worldObj = this.living.worldObj;
      this.speed = par2;
      this.findTrees = findTrees;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      return !this.isLanded() && !this.liquidBelow((int)this.living.posY - 1) && !this.liquidBelow((int)this.living.posY) && this.worldObj.rand.nextInt(20) == 0;
   }

   private boolean liquidBelow(int y) {
      return this.worldObj.getBlock(MathHelper.floor_double(this.living.posX), y, MathHelper.floor_double(this.living.posZ)).getMaterial().isLiquid();
   }

   public boolean continueExecuting() {
      boolean cont = !this.isLanded() && !this.liquidBelow((int)this.living.posY - 1) && !this.liquidBelow((int)this.living.posY);
      return cont;
   }

   public void startExecuting() {
      this.courseChangeCooldown = 100;
      int x0 = MathHelper.floor_double(this.living.posX);
      int y0 = MathHelper.floor_double(this.living.posY);
      int z0 = MathHelper.floor_double(this.living.posZ);
      this.target = this.findTrees?this.findTreeTop(x0, y0, z0):null;
      if(this.target == null) {
         this.target = this.findGround(x0, y0, z0);
      }

      if(this.target != null) {
         ;
      }

   }

   public void resetTask() {
      this.target = null;
      super.resetTask();
   }

   private int[] findTreeTop(int x0, int y0, int z0) {
      boolean RADIUS = true;
      boolean Y_RADIUS = true;

      for(int y = Math.max(y0 - 3, 1); y <= y0 + 3; ++y) {
         for(int x = x0 - 16; x <= x0 + 16; ++x) {
            for(int z = z0 - 16; z <= z0 + 16; ++z) {
               Block blockID = this.worldObj.getBlock(x, y, z);
               if(blockID.getMaterial() == Material.leaves) {
                  for(int y2 = y; y2 < y0 + 10; ++y2) {
                     if(this.worldObj.isAirBlock(x, y2, z)) {
                        double d0 = (double)x - this.living.posX;
                        double d1 = (double)y2 - this.living.posY;
                        double d2 = (double)z - this.living.posZ;
                        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                        d3 = (double)MathHelper.sqrt_double(d3);
                        if(this.isCourseTraversable((double)x, (double)y2, (double)z, d3)) {
                           return new int[]{x, y2 + 2, z};
                        }
                     }
                  }
               }
            }
         }
      }

      return null;
   }

   private int[] findGround(int x0, int y0, int z0) {
      for(int y = y0; y > 1; --y) {
         Material material = this.worldObj.getBlock(x0, y, z0).getMaterial();
         if(material != Material.air) {
            if(!material.isLiquid()) {
               return new int[]{x0, y + 1, z0};
            }

            for(int i = 0; i < 10; ++i) {
               int j = MathHelper.floor_double(this.living.posX + (double)this.worldObj.rand.nextInt(20) - 10.0D);
               int k = MathHelper.floor_double(this.living.boundingBox.minY + (double)this.worldObj.rand.nextInt(6) - 3.0D);
               int l = MathHelper.floor_double(this.living.posZ + (double)this.worldObj.rand.nextInt(20) - 10.0D);
               Block blockID = this.worldObj.getBlock(j, k, l);
               double d0 = (double)j - this.living.posX;
               double d1 = (double)k - this.living.posY;
               double d2 = (double)l - this.living.posZ;
               double d3 = d0 * d0 + d1 * d1 + d2 * d2;
               d3 = (double)MathHelper.sqrt_double(d3);
               if((blockID.getMaterial() == Material.leaves || blockID.getMaterial().isSolid()) && this.worldObj.isAirBlock(j, k + 1, l) && this.isCourseTraversable((double)j, (double)k, (double)l, d3)) {
                  return new int[]{j, k + 1, l};
               }
            }
         }
      }

      return null;
   }

   public void updateTask() {
      if(!this.isLanded()) {
         if(this.target != null && this.living.getDistanceSq((double)this.target[0], this.living.posY, (double)this.target[2]) > 1.0D && this.courseChangeCooldown-- > 0) {
            double d0 = (double)this.target[0] - this.living.posX;
            double d1 = (double)this.target[1] - this.living.posY;
            double d2 = (double)this.target[2] - this.living.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            d3 = (double)MathHelper.sqrt_double(d3);
            if(this.isCourseTraversable((double)this.target[0], (double)this.target[1], (double)this.target[2], d3)) {
               this.living.motionX += d0 / d3 * 0.05D;
               this.living.motionY += d1 / d3 * 0.05D;
               this.living.motionZ += d2 / d3 * 0.05D;
            }
         } else if(!this.liquidBelow((int)(this.living.posY - 1.0D))) {
            this.living.motionY = -0.1D;
         }

         this.living.renderYawOffset = this.living.rotationYaw = -((float)Math.atan2(this.living.motionX, this.living.motionZ)) * 180.0F / 3.1415927F;
      }

      this.living.renderYawOffset = this.living.rotationYaw = -((float)Math.atan2(this.living.motionX, this.living.motionZ)) * 180.0F / 3.1415927F;
   }

   private boolean isLanded() {
      Block blockID = this.worldObj.getBlock(MathHelper.floor_double(this.living.posX), (int)(this.living.posY - 0.01D), MathHelper.floor_double(this.living.posZ));
      Material material = blockID.getMaterial();
      return material == Material.leaves || material.isSolid();
   }

   private boolean isCourseTraversable(double par1, double par3, double par5, double par7) {
      double d4 = (par1 - this.living.posX) / par7;
      double d5 = (par3 - this.living.posY) / par7;
      double d6 = (par5 - this.living.posZ) / par7;
      AxisAlignedBB axisalignedbb = this.living.boundingBox.copy();

      for(int i = 1; (double)i < par7; ++i) {
         axisalignedbb.offset(d4, d5, d6);
         if(!this.worldObj.getCollidingBoundingBoxes(this.living, axisalignedbb).isEmpty()) {
            return false;
         }
      }

      return true;
   }
}
