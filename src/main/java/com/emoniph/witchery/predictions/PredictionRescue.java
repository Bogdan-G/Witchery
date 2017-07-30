package com.emoniph.witchery.predictions;

import com.emoniph.witchery.entity.EntityOwl;
import com.emoniph.witchery.predictions.PredictionAlwaysForced;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PredictionRescue extends PredictionAlwaysForced {

   private final Class entityClass;


   public PredictionRescue(int id, int itemWeight, double selfFulfillmentProbabilityPerSec, String translationKey, int regularFulfillmentDurationInTicks, double regularFulfillmentProbability, Class entityClass) {
      super(id, itemWeight, selfFulfillmentProbabilityPerSec, translationKey, regularFulfillmentDurationInTicks, regularFulfillmentProbability);
      this.entityClass = entityClass;
   }

   public boolean checkIfFulfilled(World world, EntityPlayer player, LivingHurtEvent event, boolean isPastDue, boolean veryOld) {
      if(!event.isCanceled()) {
         Entity attackingEntity = event.source.getEntity();
         if(attackingEntity != null && attackingEntity instanceof EntityLivingBase) {
            try {
               int ex = MathHelper.floor_double(player.posX);
               int y = MathHelper.floor_double(player.posY);
               int z = MathHelper.floor_double(player.posZ);
               if(!world.isRemote) {
                  boolean MAX_DISTANCE = true;
                  boolean MIN_DISTANCE = true;
                  byte activeRadius = 2;
                  int ax = world.rand.nextInt(activeRadius * 2 + 1);
                  if(ax > activeRadius) {
                     ax += 4;
                  }

                  int nx = ex - 4 + ax;
                  int az = world.rand.nextInt(activeRadius * 2 + 1);
                  if(az > activeRadius) {
                     az += 4;
                  }

                  int nz = z - 4 + az;

                  int ny;
                  for(ny = y; !world.isAirBlock(nx, ny, nz) && ny < y + 8; ++ny) {
                     ;
                  }

                  while(world.isAirBlock(nx, ny, nz) && ny > 0) {
                     --ny;
                  }

                  int hy;
                  for(hy = 0; world.isAirBlock(nx, ny + hy + 1, nz) && hy < 6; ++hy) {
                     ;
                  }

                  Constructor ctor = this.entityClass.getConstructor(new Class[]{World.class});
                  EntityLiving entity = (EntityLiving)ctor.newInstance(new Object[]{world});
                  if((float)hy >= entity.height) {
                     entity.setLocationAndAngles(0.5D + (double)nx, 0.05D + (double)ny + 1.0D, 0.5D + (double)nz, 0.0F, 0.0F);
                     world.spawnEntityInWorld(entity);
                     Object entitylivingData = null;
                     entity.onSpawnWithEgg((IEntityLivingData)entitylivingData);
                     if(entity instanceof EntityOwl) {
                        ((EntityOwl)entity).setTimeToLive(300);
                     }

                     entity.setAttackTarget((EntityLivingBase)attackingEntity);
                     if(entity instanceof EntityCreature) {
                        ((EntityCreature)entity).setTarget((EntityLivingBase)attackingEntity);
                        ((EntityCreature)entity).setRevengeTarget((EntityLivingBase)attackingEntity);
                     }

                     ParticleEffect.SMOKE.send(SoundEffect.NONE, entity, 0.5D, 2.0D, 16);
                     return true;
                  }

                  return false;
               }
            } catch (NoSuchMethodException var22) {
               ;
            } catch (InvocationTargetException var23) {
               ;
            } catch (InstantiationException var24) {
               ;
            } catch (IllegalAccessException var25) {
               ;
            }

            Log.instance().debug(String.format("Prediction for rescue by fulfilled as predicted", new Object[]{attackingEntity.toString()}));
            return false;
         }
      }

      return false;
   }
}
