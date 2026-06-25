package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class RiteCallFamiliar extends Rite {

   private final int radius;


   public RiteCallFamiliar(int radius) {
      this.radius = radius;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteCallFamiliar.StepCallFamiliar(this));
   }

   private static class StepCallFamiliar extends RitualStep {

      private final RiteCallFamiliar rite;


      public StepCallFamiliar(RiteCallFamiliar rite) {
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
               boolean bound = false;
               new ArrayList();
               Iterator i$ = world.getEntitiesWithinAABB(EntityPlayer.class, bounds).iterator();

               while(i$.hasNext()) {
                  Object obj = i$.next();
                  EntityPlayer player = (EntityPlayer)obj;
                  EntityTameable entity = Familiar.getFamiliarEntity(player);
                  if(entity != null) {
                     ItemGeneral var10000 = Witchery.Items.GENERIC;
                     ItemGeneral.teleportToLocation(player.worldObj, player.posX, player.posY, player.posZ, player.dimension, entity, false);
                     bound = true;
                  } else {
                     EntityTameable familiar = Familiar.summonFamiliar(player, 0.5D + (double)posX, 0.001D + (double)posY, 0.5D + (double)posZ);
                     if(familiar != null) {
                        bound = true;
                     }
                  }
               }

               if(!bound) {
                  return RitualStep.Result.ABORTED_REFUND;
               }

               ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, world, (double)posX, (double)posY, (double)posZ, 1.0D, 2.0D, 16);
            }

            return RitualStep.Result.COMPLETED;
         }
      }
   }
}
