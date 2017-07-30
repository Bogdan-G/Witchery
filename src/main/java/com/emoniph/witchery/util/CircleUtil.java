package com.emoniph.witchery.util;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class CircleUtil {

   public static boolean isSmallCircle(World world, int x, int y, int z, Block block) {
      int[][] circle = new int[][]{{x, z - 2}, {x + 1, z - 2}, {x + 2, z - 1}, {x + 2, z}, {x + 2, z + 1}, {x + 1, z + 2}, {x, z + 2}, {x - 1, z + 2}, {x - 2, z + 1}, {x - 2, z}, {x - 2, z - 1}, {x - 1, z - 2}};
      int[][] arr$ = circle;
      int len$ = circle.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         int[] coord = arr$[i$];
         if(world.getBlock(coord[0], y, coord[1]) != block) {
            return false;
         }
      }

      return true;
   }

   public static boolean isMediumCircle(World world, int x, int y, int z, Block block) {
      int[][] circle = new int[][]{{x, z - 4}, {x + 1, z - 4}, {x + 2, z - 4}, {x + 3, z - 3}, {x + 4, z - 2}, {x + 4, z - 1}, {x + 4, z}, {x + 4, z + 1}, {x + 4, z + 2}, {x + 3, z + 3}, {x + 2, z + 4}, {x + 1, z + 4}, {x, z + 4}, {x - 1, z + 4}, {x - 2, z + 4}, {x - 3, z + 3}, {x - 4, z + 2}, {x - 4, z + 1}, {x - 4, z}, {x - 4, z - 1}, {x - 4, z - 2}, {x - 3, z - 3}, {x - 2, z - 4}, {x - 1, z - 4}};
      int[][] arr$ = circle;
      int len$ = circle.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         int[] coord = arr$[i$];
         if(world.getBlock(coord[0], y, coord[1]) != block) {
            return false;
         }
      }

      return true;
   }
}
