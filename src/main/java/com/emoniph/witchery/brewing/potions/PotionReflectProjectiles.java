package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class PotionReflectProjectiles extends PotionBase {

   public PotionReflectProjectiles(int id, int color) {
      super(id, color);
   }

   public boolean isReady(int duration, int amplifier) {
      return true;
   }

   public void performEffect(EntityLivingBase entity, int amplifier) {
      World world = entity.worldObj;
      double RADIUS = 2.0D;
      double RADIUS_SQ = 4.0D;
      AxisAlignedBB bounds = entity.boundingBox.expand(2.0D, 2.0D, 2.0D);
      List projectileList = world.getEntitiesWithinAABB(IProjectile.class, bounds);
      Iterator i$ = projectileList.iterator();

      while(i$.hasNext()) {
         IProjectile projectile = (IProjectile)i$.next();
         boolean isArrow = false;
         if(projectile instanceof EntityArrow) {
            EntityArrow projectileEntity = (EntityArrow)projectile;
            isArrow = true;
            if(projectileEntity.shootingEntity == entity) {
               continue;
            }
         } else if(projectile instanceof EntityThrowable) {
            EntityThrowable projectileEntity2 = (EntityThrowable)projectile;
            if(projectileEntity2.getThrower() == entity) {
               continue;
            }
         }

         if(projectile instanceof Entity) {
            Entity projectileEntity1 = (Entity)projectile;
            projectileEntity1.motionX *= -0.25D * (1.0D + (double)amplifier);
            if(projectileEntity1.motionX > 0.0D || projectileEntity1.motionZ > 0.0D) {
               projectileEntity1.motionY *= -0.25D * (1.0D + (double)amplifier);
            }

            projectileEntity1.motionZ *= -0.25D * (1.0D + (double)amplifier);
            if(isArrow) {
               ;
            }
         }
      }

   }
}
