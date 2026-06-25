package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.entity.EntityToad;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

public class RiteRainOfToads extends Rite {

   private final int minRadius;
   private final int maxRadius;
   private final int bolts;


   public RiteRainOfToads(int minRadius, int maxRadius, int bolts) {
      this.minRadius = minRadius;
      this.maxRadius = maxRadius;
      this.bolts = bolts;
   }

   public void addSteps(ArrayList steps, int initialStage) {
      steps.add(new RiteRainOfToads.StepRainOfToads(this, initialStage));
   }

   private static class StepRainOfToads extends RitualStep {

      private final RiteRainOfToads rite;
      private int stage;


      public StepRainOfToads(RiteRainOfToads rite, int initialStage) {
         super(true);
         this.rite = rite;
         this.stage = initialStage;
      }

      public int getCurrentStage() {
         return this.stage;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 30L != 0L) {
            return RitualStep.Result.STARTING;
         } else if(ritual.covenSize < 1) {
            EntityPlayer var16 = ritual.getInitiatingPlayer(world);
            SoundEffect.NOTE_SNARE.playAt(world, (double)posX, (double)posY, (double)posZ);
            if(var16 != null) {
               ChatUtil.sendTranslated(EnumChatFormatting.RED, var16, "witchery.rite.coventoosmall", new Object[0]);
            }

            return RitualStep.Result.ABORTED_REFUND;
         } else {
            ++this.stage;
            int n;
            switch(this.stage) {
            case 1:
               this.spawnBolt(world, posX, posY, posZ);
               return RitualStep.Result.STARTING;
            case 2:
               this.spawnBolt(world, posX, posY, posZ);
               return RitualStep.Result.STARTING;
            case 3:
               this.spawnBolt(world, posX, posY, posZ);
               return RitualStep.Result.STARTING;
            case 4:
               if(world instanceof WorldServer && !world.isRaining()) {
                  WorldInfo activeRadius = ((WorldServer)world).getWorldInfo();
                  n = (300 + world.rand.nextInt(600)) * 20;
                  activeRadius.setRainTime(n);
                  activeRadius.setRaining(true);
               }

               this.spawnBolt(world, posX, posY, posZ);
               return RitualStep.Result.STARTING;
            default:
               int var17 = this.rite.maxRadius - this.rite.minRadius;

               for(n = 0; n < world.rand.nextInt(this.rite.bolts) + 8; ++n) {
                  int ax = world.rand.nextInt(var17 * 2 + 1);
                  if(ax > var17) {
                     ax += this.rite.minRadius * 2;
                  }

                  int x = posX - this.rite.maxRadius + ax;
                  int az = world.rand.nextInt(var17 * 2 + 1);
                  if(az > var17) {
                     az += this.rite.minRadius * 2;
                  }

                  int z = posZ - this.rite.maxRadius + az;
                  int y = world.getTopSolidOrLiquidBlock(x, z);
                  if(world.isAirBlock(x, y, z)) {
                     EntityToad toad = new EntityToad(world);
                     toad.setLocationAndAngles((double)x, (double)(y + 8 + world.rand.nextInt(7)), (double)z, 0.0F, 0.0F);
                     toad.setTimeToLive(30, true);
                     world.spawnEntityInWorld(toad);
                  }
               }

               return this.stage < 200?RitualStep.Result.UPKEEP:RitualStep.Result.COMPLETED;
            }
         }
      }

      private void spawnBolt(World world, int posX, int posY, int posZ) {
         int activeRadius = this.rite.maxRadius - this.rite.minRadius;
         int ax = world.rand.nextInt(activeRadius * 2 + 1);
         if(ax > activeRadius) {
            ax += this.rite.minRadius * 2;
         }

         int x = posX - this.rite.maxRadius + ax;
         int az = world.rand.nextInt(activeRadius * 2 + 1);
         if(az > activeRadius) {
            az += this.rite.minRadius * 2;
         }

         int z = posZ - this.rite.maxRadius + az;
         EntityLightningBolt bolt = new EntityLightningBolt(world, (double)x, (double)posY, (double)z);
         world.addWeatherEffect(bolt);
      }
   }
}
