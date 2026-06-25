package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class RiteInfusePlayers extends Rite {

   private final Infusion infusion;
   private final int charges;
   private final int radius;


   public RiteInfusePlayers(Infusion infusion, int charges, int radius) {
      this.infusion = infusion;
      this.charges = charges;
      this.radius = radius;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteInfusePlayers.StepInfusePlayers(this));
   }

   private static class StepInfusePlayers extends RitualStep {

      private final RiteInfusePlayers rite;


      public StepInfusePlayers(RiteInfusePlayers rite) {
         super(false);
         this.rite = rite;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            if(!world.isRemote) {
               int r = this.rite.radius;
               AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)(posX - r), (double)posY, (double)(posZ - r), (double)(posX + r), (double)(posY + 1), (double)(posZ + r));
               Iterator i$ = world.getEntitiesWithinAABB(EntityPlayer.class, bounds).iterator();

               while(i$.hasNext()) {
                  Object obj = i$.next();
                  EntityPlayer player = (EntityPlayer)obj;
                  if(Coord.distance(player.posX, player.posY, player.posZ, (double)posX, (double)posY, (double)posZ) <= (double)r) {
                     player.attackEntityFrom(DamageSource.magic, 100.0F);
                     if(player.getHealth() > 0.1F) {
                        this.rite.infusion.infuse(player, this.rite.charges);
                     }
                  }
               }

               ParticleEffect.HUGE_EXPLOSION.send(SoundEffect.RANDOM_FIZZ, world, (double)posX, (double)posY, (double)posZ, 3.0D, 3.0D, 16);
            }

            return RitualStep.Result.COMPLETED;
         }
      }
   }
}
