package com.emoniph.witchery.util;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public final class TargetPointUtil {

   public static TargetPoint from(Entity entity, double range) {
      return entity != null?new TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, range):new TargetPoint(0, 0.0D, 0.0D, 0.0D, range);
   }

   public static TargetPoint from(World world, double x, double y, double z, double range) {
      return new TargetPoint(world.provider.dimensionId, x, y, z, range);
   }
}
