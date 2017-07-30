package com.emoniph.witchery.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntityAIToadSit extends EntityAIBase {

   private final EntityTameable theOcelot;
   private final double field_75404_b;
   private int currentTick;
   private int field_75402_d;
   private int maxSittingTicks;
   private int sitableBlockX;
   private int sitableBlockY;
   private int sitableBlockZ;


   public EntityAIToadSit(EntityTameable par1EntityOcelot, double par2) {
      this.theOcelot = par1EntityOcelot;
      this.field_75404_b = par2;
      this.setMutexBits(5);
   }

   public boolean shouldExecute() {
      return !this.theOcelot.isSitting() && this.theOcelot.getRNG().nextDouble() <= 0.006500000134110451D && this.getNearbySitableBlockDistance();
   }

   public boolean continueExecuting() {
      return this.currentTick <= this.maxSittingTicks && this.field_75402_d <= 60 && this.isSittableBlock(this.theOcelot.worldObj, this.sitableBlockX, this.sitableBlockY, this.sitableBlockZ);
   }

   public void startExecuting() {
      this.theOcelot.getNavigator().tryMoveToXYZ((double)((float)this.sitableBlockX) + 0.5D, (double)(this.sitableBlockY + 1), (double)((float)this.sitableBlockZ) + 0.5D, this.field_75404_b);
      this.currentTick = 0;
      this.field_75402_d = 0;
      this.maxSittingTicks = this.theOcelot.getRNG().nextInt(this.theOcelot.getRNG().nextInt(1200) + 1200) + 1200;
   }

   public void resetTask() {
      this.theOcelot.setSitting(false);
   }

   public void updateTask() {
      ++this.currentTick;
      this.theOcelot.func_70907_r().setSitting(false);
      if(this.theOcelot.getDistanceSq((double)this.sitableBlockX, (double)(this.sitableBlockY + 1), (double)this.sitableBlockZ) > 1.0D) {
         this.theOcelot.setSitting(false);
         this.theOcelot.getNavigator().tryMoveToXYZ((double)((float)this.sitableBlockX) + 0.5D, (double)(this.sitableBlockY + 1), (double)((float)this.sitableBlockZ) + 0.5D, this.field_75404_b);
         ++this.field_75402_d;
      } else if(!this.theOcelot.isSitting()) {
         this.theOcelot.setSitting(true);
      } else {
         --this.field_75402_d;
      }

   }

   protected boolean getNearbySitableBlockDistance() {
      int i = (int)this.theOcelot.posY;
      double d0 = 4.147483647E9D;

      for(int j = (int)this.theOcelot.posX - 8; (double)j < this.theOcelot.posX + 8.0D; ++j) {
         for(int k = (int)this.theOcelot.posZ - 8; (double)k < this.theOcelot.posZ + 8.0D; ++k) {
            for(int y = (int)this.theOcelot.posY - 2; (double)y < this.theOcelot.posY + 3.0D; ++y) {
               if(this.isSittableBlock(this.theOcelot.worldObj, j, y, k) && this.theOcelot.worldObj.isAirBlock(j, y + 1, k)) {
                  double d1 = this.theOcelot.getDistanceSq((double)j, (double)y, (double)k);
                  if(d1 < d0) {
                     this.sitableBlockX = j;
                     this.sitableBlockY = y;
                     this.sitableBlockZ = k;
                     d0 = d1;
                  }
               }
            }
         }
      }

      return d0 < 2.147483647E9D;
   }

   protected boolean isSittableBlock(World par1World, int par2, int par3, int par4) {
      Block l = par1World.getBlock(par2, par3, par4);
      par1World.getBlockMetadata(par2, par3, par4);
      return l == Blocks.waterlily;
   }
}
