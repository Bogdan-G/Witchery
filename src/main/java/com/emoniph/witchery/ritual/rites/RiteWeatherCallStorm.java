package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import java.util.ArrayList;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

public class RiteWeatherCallStorm extends Rite {

   private final int minRadius;
   private final int maxRadius;
   private final int bolts;


   public RiteWeatherCallStorm(int minRadius, int maxRadius, int bolts) {
      this.minRadius = minRadius;
      this.maxRadius = maxRadius;
      this.bolts = bolts;
   }

   public void addSteps(ArrayList steps, int initialStage) {
      steps.add(new RiteWeatherCallStorm.StepWeatherCallStorm(this, initialStage));
   }

   private static class StepWeatherCallStorm extends RitualStep {

      private final RiteWeatherCallStorm rite;
      private int stage;


      public StepWeatherCallStorm(RiteWeatherCallStorm rite, int initialStage) {
         super(true);
         this.rite = rite;
         this.stage = initialStage;
      }

      public int getCurrentStage() {
         return this.stage;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 30L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            if(!world.isRemote) {
               ++this.stage;
               switch(this.stage) {
               case 1:
                  this.spawnBolt(world, posX, posY, posZ);
                  break;
               case 2:
                  this.spawnBolt(world, posX, posY, posZ);
                  break;
               case 3:
                  this.spawnBolt(world, posX, posY, posZ);
                  this.spawnBolt(world, posX, posY, posZ);
                  break;
               case 4:
                  if(world instanceof WorldServer && !world.isThundering()) {
                     WorldInfo i = ((WorldServer)world).getWorldInfo();
                     int i1 = (300 + world.rand.nextInt(600)) * 20;
                     i.setRainTime(i1);
                     i.setThunderTime(i1);
                     i.setRaining(true);
                     i.setThundering(true);
                  }

                  this.spawnBolt(world, posX, posY, posZ);
                  break;
               default:
                  for(int var10 = 0; var10 < world.rand.nextInt(4); ++var10) {
                     this.spawnBolt(world, posX, posY, posZ);
                     if(var10 > 0) {
                        ++this.stage;
                     }
                  }
               }
            }

            return this.stage < this.rite.bolts?RitualStep.Result.STARTING:RitualStep.Result.COMPLETED;
         }
      }

      private void spawnBolt(World world, int posX, int posY, int posZ) {
         int activeRadius = this.rite.maxRadius - this.rite.minRadius;
         int ax = world.rand.nextInt(activeRadius * 2 + 1);
         if(ax > activeRadius) {
            ax += this.rite.minRadius * 2;
         }

         int x = posX - this.rite.maxRadius + ax;
         int az = world.rand.nextInt(activeRadius * 2 + 1);
         if(az > activeRadius) {
            az += this.rite.minRadius * 2;
         }

         int z = posZ - this.rite.maxRadius + az;
         EntityLightningBolt bolt = new EntityLightningBolt(world, (double)x, (double)posY, (double)z);
         world.addWeatherEffect(bolt);
      }
   }
}
