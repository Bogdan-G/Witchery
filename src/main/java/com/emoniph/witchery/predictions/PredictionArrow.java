package com.emoniph.witchery.predictions;

import com.emoniph.witchery.predictions.PredictionFight;
import com.emoniph.witchery.util.Log;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PredictionArrow extends PredictionFight {

   public PredictionArrow(int id, int itemWeight, double selfFulfillmentProbabilityPerSec, String translationKey) {
      super(id, itemWeight, selfFulfillmentProbabilityPerSec, translationKey, EntitySkeleton.class, false);
   }

   public boolean checkIfFulfilled(World world, EntityPlayer player, LivingHurtEvent event, boolean isPastDue, boolean veryOld) {
      if(!event.isCanceled()) {
         boolean hitByArrow = event.source.damageType == "arrow";
         if(hitByArrow) {
            Log.instance().debug(String.format("Prediction for hit by arrow came true", new Object[0]));
         }

         return hitByArrow;
      } else {
         return false;
      }
   }
}
