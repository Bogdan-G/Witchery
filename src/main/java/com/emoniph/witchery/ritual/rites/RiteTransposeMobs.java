package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class RiteTransposeMobs extends Rite {

   protected final int radius;
   protected final int pulses;
   protected final int minDistance;


   public RiteTransposeMobs(int radius, int minDistance, int pulses) {
      this.radius = radius;
      this.pulses = pulses;
      this.minDistance = minDistance;
   }

   public void addSteps(ArrayList steps, int initialStep) {
      steps.add(new RiteTransposeMobs.StepTeleportation(this, initialStep));
   }

   private static class StepTeleportation extends RitualStep {

      private final RiteTransposeMobs rite;
      private int step;


      public StepTeleportation(RiteTransposeMobs rite, int initialStep) {
         super(false);
         this.rite = rite;
         this.step = initialStep;
      }

      public int getCurrentStage() {
         return this.step;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            ++this.step;
            int r = this.rite.radius;
            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)(posX - r), 1.0D, (double)(posZ - r), (double)(posX + r), (double)(posY - 1), (double)(posZ + r));
            Iterator i$ = world.getEntitiesWithinAABB(EntityMob.class, bounds).iterator();

            while(i$.hasNext()) {
               Object obj = i$.next();
               EntityMob entity = (EntityMob)obj;
               world.removeEntity(entity);
               entity.isDead = false;
               int activeRadius = this.rite.radius;
               int ax = world.rand.nextInt(activeRadius * 2 + 1);
               if(ax > activeRadius) {
                  ax += this.rite.minDistance * 2;
               }

               int x = posX - this.rite.radius - this.rite.minDistance + ax;
               int az = world.rand.nextInt(activeRadius * 2 + 1);
               if(az > activeRadius) {
                  az += this.rite.minDistance * 2;
               }

               int z = posZ - this.rite.radius - this.rite.minDistance + az;
               entity.setLocationAndAngles((double)x, (double)posY, (double)z, 0.0F, 0.0F);
               world.spawnEntityInWorld(entity);
               ParticleEffect.PORTAL.send(SoundEffect.RANDOM_FIZZ, entity, 0.5D, 2.0D, 16);
            }

            return this.step >= this.rite.pulses?RitualStep.Result.COMPLETED:RitualStep.Result.UPKEEP;
         }
      }
   }
}
