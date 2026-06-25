package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.entity.EntitySummonedUndead;
import com.emoniph.witchery.item.ItemSpectralStone;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class RiteSummonSpectralStone extends Rite {

   private final int radius;


   public RiteSummonSpectralStone(int radius) {
      this.radius = radius;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteSummonSpectralStone.StepSummonItem(this));
   }

   private static class StepSummonItem extends RitualStep {

      private final RiteSummonSpectralStone rite;


      public StepSummonItem(RiteSummonSpectralStone rite) {
         super(false);
         this.rite = rite;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            if(!world.isRemote) {
               int r = this.rite.radius;
               int r2 = r * r;
               AxisAlignedBB bb = AxisAlignedBB.getBoundingBox((double)(posX - r), (double)(posY - r), (double)(posZ - r), (double)(posX + r), (double)(posY + r), (double)(posZ + r));
               List entities = world.getEntitiesWithinAABB(EntitySummonedUndead.class, bb);
               Class entityType = null;
               int count = 0;
               Iterator stack = entities.iterator();

               while(stack.hasNext()) {
                  Object entity = stack.next();
                  EntitySummonedUndead entity1 = (EntitySummonedUndead)entity;
                  if(entity1.getDistanceSq(0.5D + (double)posX, (double)posY, 0.5D + (double)posZ) <= (double)r2) {
                     Class foundType = entity1.getClass();
                     if(entityType == null) {
                        entityType = foundType;
                     }

                     if(entityType == foundType) {
                        ++count;
                        if(!world.isRemote) {
                           entity1.setDead();
                           ParticleEffect.PORTAL.send(SoundEffect.RANDOM_POP, entity1, 1.0D, 2.0D, 16);
                        }

                        if(count >= 3) {
                           break;
                        }
                     }
                  }
               }

               if(count <= 0) {
                  RiteRegistry.RiteError("witchery.rite.missinglivingsacrifice", ritual.getInitiatingPlayerName(), world);
                  return RitualStep.Result.ABORTED_REFUND;
               }

               ItemStack var19 = new ItemStack(Witchery.Items.SPECTRAL_STONE, 1, ItemSpectralStone.metaFromCreature(entityType, count));
               EntityItem var18 = new EntityItem(world, 0.5D + (double)posX, (double)posY + 1.5D, 0.5D + (double)posZ, var19);
               var18.motionX = 0.0D;
               var18.motionY = 0.3D;
               var18.motionZ = 0.0D;
               world.spawnEntityInWorld(var18);
               ParticleEffect.SPELL.send(SoundEffect.RANDOM_FIZZ, var18, 0.5D, 0.5D, 16);
            }

            return RitualStep.Result.COMPLETED;
         }
      }
   }
}
