package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PotionAttractProjectiles extends PotionBase {

   public PotionAttractProjectiles(int id, int color) {
      super(id, true, color);
   }

   public boolean isReady(int duration, int amplifier) {
      return true;
   }

   public void performEffect(EntityLivingBase target, int amplifier) {
      World world = target.worldObj;
      double RADIUS = (1.0D + (double)amplifier) * 3.0D;
      double RADIUS_SQ = RADIUS * RADIUS;
      AxisAlignedBB bounds = target.boundingBox.expand(RADIUS, RADIUS, RADIUS);
      List projectileList = world.getEntitiesWithinAABB(IProjectile.class, bounds);
      Iterator i$ = projectileList.iterator();

      while(i$.hasNext()) {
         IProjectile projectile = (IProjectile)i$.next();
         if(projectile instanceof Entity) {
            Entity arrow = (Entity)projectile;
            double velocitySq = arrow.motionX * arrow.motionX + arrow.motionY * arrow.motionY + arrow.motionZ * arrow.motionZ;
            double FAST_SQ = 0.25D;
            if(arrow.ticksExisted >= (velocitySq > 0.25D?1:10)) {
               double d0 = target.posX - arrow.posX;
               double d1 = target.boundingBox.minY + (double)target.height * 0.75D - arrow.posY;
               double d2 = target.posZ - arrow.posZ;
               double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
               if(d3 >= 1.0E-7D) {
                  projectile.setThrowableHeading(d0, d1, d2, 1.0F, 1.0F);
               }
            }
         }
      }

   }
}
