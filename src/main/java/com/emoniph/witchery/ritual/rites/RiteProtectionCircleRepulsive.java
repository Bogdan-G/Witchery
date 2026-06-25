package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.ritual.rites.RiteProtectionCircle;
import com.emoniph.witchery.util.Coord;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class RiteProtectionCircleRepulsive extends RiteProtectionCircle {

   public RiteProtectionCircleRepulsive(int radius, float upkeepPowerCost, int ticksTolive) {
      super(radius, upkeepPowerCost, ticksTolive);
   }

   protected void update(World world, int posX, int posY, int posZ, int radius, long ticks) {
      this.repulse(world, posX, posY, posZ, (float)radius);
   }

   private void repulse(World world, int posX, int posY, int posZ, float radius) {
      AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)((float)posX - radius), (double)((float)posY - radius), (double)((float)posZ - radius), (double)((float)posX + radius), (double)((float)posY + radius), (double)((float)posZ + radius));
      List list = world.getEntitiesWithinAABB(EntityCreature.class, bounds);
      Iterator iterator = list.iterator();

      while(iterator.hasNext()) {
         Entity entity = (Entity)iterator.next();
         if(Coord.distance(entity.posX, entity.posY, entity.posZ, (double)posX, (double)posY, (double)posZ) < (double)radius) {
            push(world, entity, (double)posX, (double)posY, (double)posZ);
         }
      }

   }

   public static void push(World world, Entity entity, double posX, double posY, double posZ) {
      push(world, entity, posX, posY, posZ, true);
   }

   public static void push(World world, Entity entity, double posX, double posY, double posZ, boolean restricted) {
      if(!restricted || !(entity instanceof EntityPlayer) && !(entity instanceof EntityDragon)) {
         double d = posX - entity.posX;
         double d1 = posY - entity.posY;
         double d2 = posZ - entity.posZ;
         double d4 = d * d + d1 * d1 + d2 * d2;
         d4 *= d4;
         if(d4 <= Math.pow(6.0D, 4.0D)) {
            double d5 = -(d * 0.01999999955296516D / d4) * Math.pow(6.0D, 3.0D);
            double d6 = -(d1 * 0.01999999955296516D / d4) * Math.pow(6.0D, 3.0D);
            double d7 = -(d2 * 0.01999999955296516D / d4) * Math.pow(6.0D, 3.0D);
            if(d5 > 0.0D) {
               d5 = 0.22D;
            } else if(d5 < 0.0D) {
               d5 = -0.22D;
            }

            if(d6 > 0.2D) {
               d6 = 0.12D;
            } else if(d6 < -0.1D) {
               d6 = 0.12D;
            }

            if(d7 > 0.0D) {
               d7 = 0.22D;
            } else if(d7 < 0.0D) {
               d7 = -0.22D;
            }

            entity.motionX += d5;
            entity.motionY += d6;
            entity.motionZ += d7;
         }
      }

   }
}
