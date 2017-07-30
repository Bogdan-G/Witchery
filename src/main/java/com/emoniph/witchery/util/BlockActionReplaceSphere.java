package com.emoniph.witchery.util;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public abstract class BlockActionReplaceSphere {

   protected abstract boolean onShouldReplace(World var1, int var2, int var3, int var4, Block var5);

   protected abstract void onReplaceBlock(World var1, int var2, int var3, int var4, Block var5);

   protected void onComplete() {}

   public void replaceBlocks(World world, int x0, int y0, int z0, int radius) {
      this.replaceBlocks(world, x0, y0, z0, x0, y0, z0, radius);
      this.onComplete();
   }

   private void replaceBlocks(World world, int x, int y, int z, int x0, int y0, int z0, int range) {
      double rangeSq = (double)(range * range);
      if((double)((x0 - x) * (x0 - x) + (y0 - y) * (y0 - y) + (z0 - z) * (z0 - z)) < rangeSq) {
         if(this.replaceBlock(world, x + 1, y, z)) {
            this.replaceBlocks(world, x + 1, y, z, x0, y0, z0, range);
         }

         if(this.replaceBlock(world, x - 1, y, z)) {
            this.replaceBlocks(world, x - 1, y, z, x0, y0, z0, range);
         }

         if(this.replaceBlock(world, x, y, z + 1)) {
            this.replaceBlocks(world, x, y, z + 1, x0, y0, z0, range);
         }

         if(this.replaceBlock(world, x, y, z - 1)) {
            this.replaceBlocks(world, x, y, z - 1, x0, y0, z0, range);
         }

         if(this.replaceBlock(world, x, y + 1, z)) {
            this.replaceBlocks(world, x, y + 1, z, x0, y0, z0, range);
         }

         if(this.replaceBlock(world, x, y - 1, z)) {
            this.replaceBlocks(world, x, y - 1, z, x0, y0, z0, range);
         }

      }
   }

   private boolean replaceBlock(World world, int x, int y, int z) {
      Block block = world.getBlock(x, y, z);
      if(this.onShouldReplace(world, x, y, z, block)) {
         this.onReplaceBlock(world, x, y, z, block);
         return true;
      } else {
         return false;
      }
   }
}
