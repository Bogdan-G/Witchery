package com.emoniph.witchery.predictions;

import com.emoniph.witchery.entity.ai.EntityAIMateWithPlayer;
import com.emoniph.witchery.predictions.PredictionAlwaysForced;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PredictionFallInLove extends PredictionAlwaysForced {

   public PredictionFallInLove(int id, int itemWeight, double selfFulfillmentProbabilityPerSec, String translationKey, int regularFulfillmentDurationInTicks, double regularFulfillmentProbability) {
      super(id, itemWeight, selfFulfillmentProbabilityPerSec, translationKey, regularFulfillmentDurationInTicks, regularFulfillmentProbability);
   }

   public boolean checkIfFulfilled(World world, EntityPlayer player, LivingUpdateEvent event, boolean isPastDue, boolean veryOld) {
      if(!this.shouldWeActivate(world, player, isPastDue)) {
         return false;
      } else {
         int x = MathHelper.floor_double(player.posX);
         int y = MathHelper.floor_double(player.posY);
         int z = MathHelper.floor_double(player.posZ);
         boolean MAX_DISTANCE = true;
         boolean MIN_DISTANCE = true;
         byte activeRadius = 2;
         int ax = world.rand.nextInt(activeRadius * 2 + 1);
         if(ax > activeRadius) {
            ax += 8;
         }

         int nx = x - 6 + ax;
         int az = world.rand.nextInt(activeRadius * 2 + 1);
         if(az > activeRadius) {
            az += 8;
         }

         int nz = z - 6 + az;

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

         EntityVillager entity = new EntityVillager(world, 0);
         if((float)hy >= entity.height && world.getBlock(nx, ny - 1, nz).isNormalCube()) {
            entity.setLocationAndAngles(0.5D + (double)nx, 0.05D + (double)ny + 1.0D, 0.5D + (double)nz, 0.0F, 0.0F);
            world.spawnEntityInWorld(entity);
            Log.instance().debug(String.format("Forcing prediction for lover by %s", new Object[]{entity.toString()}));
            EntityAIMateWithPlayer task = new EntityAIMateWithPlayer(entity);
            task.forceTask(player);
            entity.tasks.addTask(1, task);
            ParticleEffect.SMOKE.send(SoundEffect.NONE, entity, 0.5D, 2.0D, 16);
            SoundEffect.WITCHERY_RANDOM_LOVED.playAtPlayer(world, player);
            return true;
         } else {
            return false;
         }
      }
   }
}
