package com.emoniph.witchery.ritual;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.ritual.Sacrifice;
import com.emoniph.witchery.util.Coord;
import java.util.ArrayList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class SacrificePower extends Sacrifice {

   public final float powerRequired;
   public final int powerFrequencyInTicks;


   public SacrificePower(float powerRequired, int powerFrequencyInTicks) {
      this.powerRequired = powerRequired;
      this.powerFrequencyInTicks = powerFrequencyInTicks;
   }

   public void addDescription(StringBuffer sb) {
      sb.append(String.format("\n§8%s§0 %s\n", new Object[]{Witchery.resource("witchery.book.altarpower"), Integer.valueOf(MathHelper.floor_float(this.powerRequired))}));
   }

   public boolean isMatch(World world, int posX, int posY, int posZ, int maxDistance, ArrayList entities, ArrayList grassperStacks) {
      return true;
   }

   public void addSteps(ArrayList steps, AxisAlignedBB bounds, int maxDistance) {
      steps.add(new SacrificePower.SacrificePowerStep(this));
   }

   private static class SacrificePowerStep extends RitualStep {

      private final SacrificePower sacrifice;
      private static final int POWER_SOURCE_RADIUS = 16;


      public SacrificePowerStep(SacrificePower sacrifice) {
         super(false);
         this.sacrifice = sacrifice;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % (long)this.sacrifice.powerFrequencyInTicks != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            IPowerSource powerSource = this.findNewPowerSource(world, posX, posY, posZ);
            if(powerSource == null) {
               RiteRegistry.RiteError("witchery.rite.missingpowersource", ritual.getInitiatingPlayerName(), world);
               return RitualStep.Result.ABORTED_REFUND;
            } else if(powerSource.consumePower(this.sacrifice.powerRequired)) {
               return RitualStep.Result.COMPLETED;
            } else {
               RiteRegistry.RiteError("witchery.rite.insufficientpower", ritual.getInitiatingPlayerName(), world);
               return RitualStep.Result.ABORTED_REFUND;
            }
         }
      }

      private IPowerSource findNewPowerSource(World world, int posX, int posY, int posZ) {
         ArrayList sources = PowerSources.instance() != null?PowerSources.instance().get(world, new Coord(posX, posY, posZ), 16):null;
         return sources != null && sources.size() > 0?((PowerSources.RelativePowerSource)sources.get(0)).source():null;
      }
   }
}
