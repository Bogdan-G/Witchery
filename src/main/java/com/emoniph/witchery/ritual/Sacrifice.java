package com.emoniph.witchery.ritual;

import java.util.ArrayList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public abstract class Sacrifice {

   public abstract boolean isMatch(World var1, int var2, int var3, int var4, int var5, ArrayList var6, ArrayList var7);

   protected static double distance(double firstX, double firstY, double firstZ, double secondX, double secondY, double secondZ) {
      double dX = firstX - secondX;
      double dY = firstY - secondY;
      double dZ = firstZ - secondZ;
      double distance = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
      return distance;
   }

   public void addSteps(ArrayList steps, AxisAlignedBB bounds, int maxDistance) {}

   public void addDescription(StringBuffer sb) {}
}
