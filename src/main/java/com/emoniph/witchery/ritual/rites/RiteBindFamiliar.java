package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class RiteBindFamiliar extends Rite {

   private final int radius;


   public RiteBindFamiliar(int radius) {
      this.radius = radius;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteBindFamiliar.StepBindFamiliar(this));
   }

   private static class StepBindFamiliar extends RitualStep {

      private final RiteBindFamiliar rite;


      public StepBindFamiliar(RiteBindFamiliar rite) {
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
               ArrayList boundPlayers = new ArrayList();
               Iterator i$ = world.getEntitiesWithinAABB(EntityTameable.class, bounds).iterator();

               while(i$.hasNext()) {
                  Object obj = i$.next();
                  EntityTameable tameable = (EntityTameable)obj;
                  if(tameable.isTamed() && Familiar.canBecomeFamiliar(tameable) && Coord.distance(tameable.posX, tameable.posY, tameable.posZ, (double)posX, (double)posY, (double)posZ) <= (double)r) {
                     EntityLivingBase player = tameable.getOwner();
                     if(player != null && player instanceof EntityPlayer && Coord.distance(player.posX, player.posY, player.posZ, (double)posX, (double)posY, (double)posZ) <= (double)r && !boundPlayers.contains(player)) {
                        Familiar.bindToPlayer((EntityPlayer)player, tameable);
                        boundPlayers.add((EntityPlayer)player);
                        bound = true;
                     }
                  }
               }

               if(!bound) {
                  return RitualStep.Result.ABORTED_REFUND;
               }

               ParticleEffect.HEART.send(SoundEffect.RANDOM_FIZZ, world, (double)posX, (double)posY, (double)posZ, 3.0D, 3.0D, 16);
            }

            return RitualStep.Result.COMPLETED;
         }
      }
   }
}
