package com.emoniph.witchery.predictions;

import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.predictions.Prediction;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Log;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PredictionNetherTrip extends Prediction {

   public PredictionNetherTrip(int id, int itemWeight, double selfFulfillmentProbabilityPerSec, String translationKey) {
      super(id, itemWeight, selfFulfillmentProbabilityPerSec, translationKey);
   }

   public boolean isPredictionPossible(World world, EntityPlayer player) {
      try {
         NBTTagCompound e = Infusion.getNBT(player);
         boolean wasInNether = e != null && e.hasKey("WITCVisitedNether") && e.getBoolean("WITCVisitedNether");
         boolean isPossible = player.dimension != -1 && wasInNether;
         return isPossible;
      } catch (Throwable var7) {
         Log.instance().warning(var7, "Error occurred while checking if a nether visit has occurred for the nether prediction.");
         return false;
      }
   }

   public boolean doSelfFulfillment(World world2, EntityPlayer player) {
      boolean FALL_DISTANCE = true;
      boolean RADIUS = true;
      int x = MathHelper.floor_double(player.posX);
      int y = MathHelper.floor_double(player.posY) - 1;
      int z = MathHelper.floor_double(player.posZ);
      if(!world2.isRemote && player.dimension != -1) {
         ChatUtil.sendTranslated(EnumChatFormatting.LIGHT_PURPLE, player, "witchery.prediction.tothenether.summoned", new Object[0]);
         player.setInPortal();
         World world = player.worldObj;
         boolean MAX_DISTANCE = true;
         boolean MIN_DISTANCE = true;
         byte activeRadius = 2;
         int ax = world.rand.nextInt(activeRadius * 2 + 1);
         if(ax > activeRadius) {
            ax += 4;
         }

         int nx = x - 4 + ax;
         int az = world.rand.nextInt(activeRadius * 2 + 1);
         if(az > activeRadius) {
            az += 4;
         }

         int nz = z - 4 + az;

         int ny;
         for(ny = y; !world.isAirBlock(nx, ny, nz) && ny < y + 8; ++ny) {
            ;
         }

         while(world.isAirBlock(nx, ny, nz) && ny > 0) {
            --ny;
         }

         int hy;
         for(hy = 0; world.isAirBlock(nx, ny + hy + 1, nz) && hy < 6; ++hy) {
            ;
         }

         EntityBlaze entity = new EntityBlaze(world);
         if((float)hy >= entity.height) {
            entity.setLocationAndAngles((double)nx, (double)ny, (double)nz, 0.0F, 0.0F);
            world.spawnEntityInWorld(entity);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean checkIfFulfilled(World world, EntityPlayer player, LivingUpdateEvent event, boolean isPastDue, boolean veryOld) {
      if(player.dimension == -1) {
         Log.instance().debug(String.format("Prediction for got to nether fulfilled as predicted", new Object[0]));
         return true;
      } else {
         return false;
      }
   }
}
