package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.TimeUtil;
import java.util.ArrayList;
import java.util.Hashtable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class RiteEclipse extends Rite {

   private static Hashtable lastEclipseTimes = new Hashtable();


   public void addSteps(ArrayList steps, int initialStage) {
      steps.add(new RiteEclipse.StepEclipse(this, initialStage));
   }


   private static class StepEclipse extends RitualStep {

      private final RiteEclipse rite;
      private int stage;


      public StepEclipse(RiteEclipse rite, int initialStage) {
         super(false);
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
               long riteOfEclipseCooldown = (long)TimeUtil.secsToTicks(Config.instance().riteOfEclipseCooldownInSecs);
               EntityPlayer player = ritual.getInitiatingPlayer(world);
               long i;
               if(riteOfEclipseCooldown > 0L && world.playerEntities.size() > 1 && RiteEclipse.lastEclipseTimes.containsKey(Integer.valueOf(world.provider.dimensionId))) {
                  i = ((Long)RiteEclipse.lastEclipseTimes.get(Integer.valueOf(world.provider.dimensionId))).longValue();
                  if(world.getTotalWorldTime() < i + riteOfEclipseCooldown) {
                     if(player != null) {
                        ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.rite.eclipse.cooldown", new Object[0]);
                     }

                     return RitualStep.Result.ABORTED_REFUND;
                  }
               }

               i = world.getWorldInfo().getWorldTime();
               world.getWorldInfo().setWorldTime(i - i % 24000L + 18000L);
               RiteEclipse.lastEclipseTimes.put(Integer.valueOf(world.provider.dimensionId), Long.valueOf(world.getTotalWorldTime()));
            }

            return RitualStep.Result.COMPLETED;
         }
      }
   }
}
