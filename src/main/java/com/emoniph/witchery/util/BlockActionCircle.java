package com.emoniph.witchery.util;

import net.minecraft.world.World;

public abstract class BlockActionCircle {

   public abstract void onBlock(World var1, int var2, int var3, int var4);

   public void onComplete() {}

   public void processHollowCircle(World world, int x0, int y0, int z0, int radius) {
      if(radius == 1) {
         this.drawPixel(world, x0, z0, y0);
      } else {
         --radius;
         int x = radius;
         int z = 0;
         int radiusError = 1 - radius;

         while(x >= z) {
            this.drawPixel(world, x + x0, z + z0, y0);
            this.drawPixel(world, z + x0, x + z0, y0);
            this.drawPixel(world, -x + x0, z + z0, y0);
            this.drawPixel(world, -z + x0, x + z0, y0);
            this.drawPixel(world, -x + x0, -z + z0, y0);
            this.drawPixel(world, -z + x0, -x + z0, y0);
            this.drawPixel(world, x + x0, -z + z0, y0);
            this.drawPixel(world, z + x0, -x + z0, y0);
            ++z;
            if(radiusError < 0) {
               radiusError += 2 * z + 1;
            } else {
               --x;
               radiusError += 2 * (z - x + 1);
            }
         }
      }

      this.onComplete();
   }

   public void processFilledCircle(World world, int x0, int y0, int z0, int radius) {
      if(radius == 1) {
         this.drawPixel(world, x0, z0, y0);
      } else {
         --radius;
         int x = radius;
         int z = 0;
         int radiusError = 1 - radius;
         boolean obsidianMelted = false;

         while(x >= z) {
            this.drawLine(world, -x + x0, x + x0, z + z0, y0);
            this.drawLine(world, -z + x0, z + x0, x + z0, y0);
            this.drawLine(world, -x + x0, x + x0, -z + z0, y0);
            this.drawLine(world, -z + x0, z + x0, -x + z0, y0);
            ++z;
            if(radiusError < 0) {
               radiusError += 2 * z + 1;
            } else {
               --x;
               radiusError += 2 * (z - x + 1);
            }
         }
      }

      this.onComplete();
   }

   private void drawLine(World world, int x1, int x2, int z, int y) {
      for(int x = x1; x <= x2; ++x) {
         this.drawPixel(world, x, z, y);
      }

   }

   private void drawPixel(World world, int x, int z, int y) {
      this.onBlock(world, x, y, z);
   }
}
