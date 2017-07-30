package com.emoniph.witchery.predictions;

import com.emoniph.witchery.predictions.Prediction;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.Log;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PredictionWet extends Prediction {

   public PredictionWet(int id, int itemWeight, double selfFulfillmentProbabilityPerSec, String translationKey) {
      super(id, itemWeight, selfFulfillmentProbabilityPerSec, translationKey);
   }

   public boolean doSelfFulfillment(World world, EntityPlayer player) {
      boolean FALL_DISTANCE = true;
      boolean RADIUS = true;
      int x0 = MathHelper.floor_double(player.posX);
      int y0 = MathHelper.floor_double(player.posY) - 1;
      int z0 = MathHelper.floor_double(player.posZ);
      if(!world.isRemote && y0 > 5 && !world.provider.isHellWorld) {
         int dirtCount = 0;

         int x;
         int z;
         for(x = x0 - 1; x <= x0 + 1; ++x) {
            for(z = z0 - 1; z <= z0 + 1; ++z) {
               Material y = world.getBlock(x, y0, z).getMaterial();
               if(y == Material.ground || y == Material.grass) {
                  ++dirtCount;
               }
            }
         }

         if((double)dirtCount == Math.pow(3.0D, 2.0D)) {
            for(x = x0 - 1; x <= x0 + 1; ++x) {
               for(z = z0 - 1; z <= z0 + 1; ++z) {
                  for(int var12 = y0; var12 > y0 - 3; --var12) {
                     if(var12 == y0) {
                        world.setBlock(x, var12, z, Blocks.gravel);
                     } else if(BlockProtect.canBreak(world.getBlock(x, var12, z), world)) {
                        world.setBlock(x, var12, z, Blocks.water);
                     }
                  }
               }
            }

            Log.instance().debug(String.format("Prediction for getting wet has been forced", new Object[0]));
            return true;
         }
      }

      return false;
   }

   public boolean checkIfFulfilled(World world, EntityPlayer player, LivingUpdateEvent event, boolean isPastDue, boolean veryOld) {
      if(player.isWet()) {
         Log.instance().debug(String.format("Prediction for WET fulfilled as predicted", new Object[0]));
         return true;
      } else {
         return false;
      }
   }
}
