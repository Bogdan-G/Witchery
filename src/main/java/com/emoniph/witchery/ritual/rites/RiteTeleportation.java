package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import java.util.ArrayList;
import net.minecraft.world.World;

public abstract class RiteTeleportation extends Rite {

   protected final int radius;


   public RiteTeleportation(int radius) {
      this.radius = radius;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteTeleportation.StepTeleportation(this));
   }

   protected abstract boolean teleport(World var1, int var2, int var3, int var4, BlockCircle.TileEntityCircle.ActivatedRitual var5);

   private static class StepTeleportation extends RitualStep {

      private final RiteTeleportation rite;


      public StepTeleportation(RiteTeleportation rite) {
         super(false);
         this.rite = rite;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         return ticks % 20L != 0L?RitualStep.Result.STARTING:(this.rite.teleport(world, posX, posY, posZ, ritual)?RitualStep.Result.COMPLETED:RitualStep.Result.ABORTED_REFUND);
      }
   }
}
