package com.emoniph.witchery.predictions;

import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.entity.IOwnable;
import com.emoniph.witchery.predictions.Prediction;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TameableUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PredictionFight extends Prediction {

   private final Class entityClass;
   private final boolean bindTameable;


   public PredictionFight(int id, int itemWeight, double selfFulfillmentProbabilityPerSec, String translationKey, Class entityClass, boolean bindTameable) {
      super(id, itemWeight, selfFulfillmentProbabilityPerSec, translationKey);
      this.entityClass = entityClass;
      this.bindTameable = bindTameable;
   }

   public boolean doSelfFulfillment(World world, EntityPlayer player) {
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
            if((float)hy < entity.height) {
               return false;
            }

            boolean bound = false;
            if(entity instanceof EntityDemon) {
               ((EntityDemon)entity).setPlayerCreated(true);
            } else if(this.bindTameable && entity instanceof EntityTameable) {
               ((EntityTameable)entity).setTamed(true);
               TameableUtil.setOwner((EntityTameable)entity, player);
               bound = true;
            } else if(this.bindTameable && entity instanceof IOwnable) {
               ((IOwnable)entity).setOwner(player.getCommandSenderName());
               bound = true;
            }

            entity.setLocationAndAngles(0.5D + (double)nx, 0.05D + (double)ny + 1.0D, 0.5D + (double)nz, 0.0F, 0.0F);
            world.spawnEntityInWorld(entity);
            Log.instance().debug(String.format("Forcing prediction for attack by %s", new Object[]{entity.toString()}));
            Object entitylivingData = null;
            entity.onSpawnWithEgg((IEntityLivingData)entitylivingData);
            if(!bound) {
               entity.setAttackTarget(player);
            }

            ParticleEffect.SMOKE.send(SoundEffect.NONE, entity, 0.5D, 2.0D, 16);
         }
      } catch (NoSuchMethodException var19) {
         ;
      } catch (InvocationTargetException var20) {
         ;
      } catch (InstantiationException var21) {
         ;
      } catch (IllegalAccessException var22) {
         ;
      }

      return true;
   }

   public boolean checkIfFulfilled(World world, EntityPlayer player, LivingHurtEvent event, boolean isPastDue, boolean veryOld) {
      if(!event.isCanceled()) {
         Entity attackingEntity = event.source.getEntity();
         if(attackingEntity != null) {
            boolean attackedByCreature = this.entityClass.isAssignableFrom(attackingEntity.getClass());
            if(attackedByCreature) {
               Log.instance().debug(String.format("Prediction for attack by %s fulfilled as predicted", new Object[]{attackingEntity.toString()}));
            }

            return attackedByCreature;
         }
      }

      return false;
   }
}
