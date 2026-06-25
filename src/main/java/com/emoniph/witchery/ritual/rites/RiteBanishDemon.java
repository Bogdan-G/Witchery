package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.entity.EntityDeath;
import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.entity.EntityImp;
import com.emoniph.witchery.entity.EntityLordOfTorment;
import com.emoniph.witchery.entity.EntityReflection;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class RiteBanishDemon extends Rite {

   private final int radius;


   public RiteBanishDemon(int radius) {
      this.radius = radius;
   }

   public void addSteps(ArrayList steps, int initialStage) {
      steps.add(new RiteBanishDemon.BanishDemonStep(this, initialStage));
   }

   private static class BanishDemonStep extends RitualStep {

      private final RiteBanishDemon rite;
      protected int ticksSoFar;


      public BanishDemonStep(RiteBanishDemon rite, int ticksSoFar) {
         super(false);
         this.rite = rite;
         this.ticksSoFar = ticksSoFar;
      }

      public int getCurrentStage() {
         return this.ticksSoFar;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            SoundEffect.RANDOM_FIZZ.playAt(world, (double)posX, (double)posY, (double)posZ);
            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)(posX - this.rite.radius), (double)(posY - this.rite.radius), (double)(posZ - this.rite.radius), (double)(posX + this.rite.radius), (double)(posY + this.rite.radius), (double)(posZ + this.rite.radius));
            List list = world.getEntitiesWithinAABB(EntityLiving.class, bounds);
            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
               EntityLiving entity = (EntityLiving)i$.next();
               if((entity instanceof EntityDemon || entity instanceof EntityDeath || entity instanceof EntityLordOfTorment || entity instanceof EntityImp || entity instanceof EntityReflection) && Coord.distanceSq(entity.posX, entity.posY, entity.posZ, (double)posX, (double)posY, (double)posZ) < (double)(this.rite.radius * this.rite.radius)) {
                  entity.setDead();
                  ParticleEffect.EXPLODE.send(SoundEffect.NONE, entity, 1.0D, 2.0D, 16);
               }
            }

            return RitualStep.Result.COMPLETED;
         }
      }
   }
}
