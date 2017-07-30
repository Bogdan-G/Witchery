package com.emoniph.witchery.ritual;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.ritual.Sacrifice;
import com.emoniph.witchery.util.Const;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class SacrificeLiving extends Sacrifice {

   final Class entityLivingClass;


   public SacrificeLiving(Class entityLivingClass) {
      this.entityLivingClass = entityLivingClass;
   }

   public void addDescription(StringBuffer sb) {
      String s = (String)EntityList.classToStringMapping.get(this.entityLivingClass);
      if(s == null) {
         s = "generic";
      }

      sb.append("§8>§0 ");
      sb.append(StatCollector.translateToLocal("entity." + s + ".name"));
      sb.append(Const.BOOK_NEWLINE);
   }

   public boolean isMatch(World world, int posX, int posY, int posZ, int maxDistance, ArrayList entities, ArrayList grassperStacks) {
      return true;
   }

   public void addSteps(ArrayList steps, AxisAlignedBB bounds, int maxDistance) {
      steps.add(new SacrificeLiving.StepSacrificeLiving(this, bounds, maxDistance));
   }

   private static class StepSacrificeLiving extends RitualStep {

      private final SacrificeLiving sacrifice;
      private final AxisAlignedBB bounds;
      private final int maxDistance;


      public StepSacrificeLiving(SacrificeLiving sacrifice, AxisAlignedBB bounds, int maxDistance) {
         super(false);
         this.sacrifice = sacrifice;
         this.bounds = bounds;
         this.maxDistance = maxDistance + 1;
      }

      public RitualStep.Result process(World worldObj, int xCoord, int yCoord, int zCoord, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            Iterator i$ = worldObj.getEntitiesWithinAABB(EntityLiving.class, this.bounds).iterator();

            EntityLiving entity;
            do {
               if(!i$.hasNext()) {
                  RiteRegistry.RiteError("witchery.rite.missinglivingsacrifice", ritual.getInitiatingPlayerName(), worldObj);
                  return RitualStep.Result.ABORTED_REFUND;
               }

               Object obj = i$.next();
               entity = (EntityLiving)obj;
            } while(!this.sacrifice.entityLivingClass.isInstance(entity) || Sacrifice.distance((double)xCoord, (double)yCoord, (double)zCoord, entity.posX, entity.posY, entity.posZ) > (double)this.maxDistance);

            if(!worldObj.isRemote) {
               entity.setDead();
               ParticleEffect.PORTAL.send(SoundEffect.RANDOM_POP, entity, 1.0D, 2.0D, 16);
            }

            return RitualStep.Result.COMPLETED;
         }
      }
   }
}
