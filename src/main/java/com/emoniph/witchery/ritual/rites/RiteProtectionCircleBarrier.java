package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBarrier;
import com.emoniph.witchery.ritual.rites.RiteProtectionCircle;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class RiteProtectionCircleBarrier extends RiteProtectionCircle {

   protected final int height;
   protected final boolean blockPlayers;
   protected static final int TICKS_TO_LIVE_WITHOUT_PULSE = 30;


   public RiteProtectionCircleBarrier(int radius, int height, float upkeepPowerCost, boolean blockPlayers, int ticksToLive) {
      super(radius, upkeepPowerCost, ticksToLive);
      this.height = height;
      this.blockPlayers = blockPlayers;
   }

   protected void update(World world, int posX, int posY, int posZ, int radius, long ticks) {
      if(ticks % 20L == 0L) {
         this.drawFilledCircle(world, posX, posZ, posY - 1, radius);
         this.drawCircleCylinder(world, posX, posZ, posY, radius);
         this.drawFilledCircle(world, posX, posZ, posY + this.height, radius);
      }

   }

   protected void drawCircleCylinder(World world, int x0, int z0, int y, int radius) {
      int x = radius;
      int z = 0;
      int radiusError = 1 - radius;

      while(x >= z) {
         this.drawPixelColumn(world, x + x0, z + z0, y);
         this.drawPixelColumn(world, z + x0, x + z0, y);
         this.drawPixelColumn(world, -x + x0, z + z0, y);
         this.drawPixelColumn(world, -z + x0, x + z0, y);
         this.drawPixelColumn(world, -x + x0, -z + z0, y);
         this.drawPixelColumn(world, -z + x0, -x + z0, y);
         this.drawPixelColumn(world, x + x0, -z + z0, y);
         this.drawPixelColumn(world, z + x0, -x + z0, y);
         ++z;
         if(radiusError < 0) {
            radiusError += 2 * z + 1;
         } else {
            --x;
            radiusError += 2 * (z - x + 1);
         }
      }

   }

   protected void drawPixelColumn(World world, int x, int z, int y) {
      for(int dy = y; dy < y + this.height; ++dy) {
         this.drawPixel(world, x, z, dy);
      }

   }

   protected void drawPixel(World world, int x, int z, int y) {
      Block blockID = world.getBlock(x, y, z);
      boolean isBarrier = blockID == Witchery.Blocks.BARRIER;
      if(blockID == Blocks.air || blockID.getMaterial().isReplaceable() || isBarrier) {
         BlockBarrier.setBlock(world, x, y, z, 30, this.blockPlayers, (EntityPlayer)null, isBarrier);
      }

   }

   protected void drawFilledCircle(World world, int x0, int z0, int y, int radius) {
      int x = radius;
      int z = 0;
      int radiusError = 1 - radius;

      while(x >= z) {
         this.drawLine(world, -x + x0, x + x0, z + z0, y);
         this.drawLine(world, -z + x0, z + x0, x + z0, y);
         this.drawLine(world, -x + x0, x + x0, -z + z0, y);
         this.drawLine(world, -z + x0, z + x0, -x + z0, y);
         ++z;
         if(radiusError < 0) {
            radiusError += 2 * z + 1;
         } else {
            --x;
            radiusError += 2 * (z - x + 1);
         }
      }

   }

   protected void drawLine(World world, int x1, int x2, int z, int y) {
      for(int x = x1; x <= x2; ++x) {
         this.drawPixel(world, x, z, y);
      }

   }
}
