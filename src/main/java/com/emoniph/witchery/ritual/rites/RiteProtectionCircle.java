package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockAltar;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class RiteProtectionCircle extends Rite {

   private final int radius;
   private final float upkeepPowerCost;
   private final int ticksToLive;


   public RiteProtectionCircle(int radius, float upkeepPowerCost, int ticksToLive) {
      this.radius = radius;
      this.upkeepPowerCost = upkeepPowerCost;
      this.ticksToLive = ticksToLive;
   }

   public void addSteps(ArrayList steps, int initialStage) {
      steps.add(new RiteProtectionCircle.ProtectionCircleStep(this, initialStage));
   }

   protected abstract void update(World var1, int var2, int var3, int var4, int var5, long var6);

   private static class ProtectionCircleStep extends RitualStep {

      private final RiteProtectionCircle rite;
      private boolean activated = false;
      protected int ticksSoFar;
      Coord powerSourceCoord;
      static final int POWER_SOURCE_RADIUS = 16;


      public ProtectionCircleStep(RiteProtectionCircle rite, int ticksSoFar) {
         super(true);
         this.rite = rite;
         this.ticksSoFar = ticksSoFar;
      }

      public int getCurrentStage() {
         return this.ticksSoFar;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(!this.activated) {
            if(ticks % 20L != 0L) {
               return RitualStep.Result.STARTING;
            }

            this.activated = true;
            SoundEffect.RANDOM_FIZZ.playAt(world, (double)super.sourceX, (double)super.sourceY, (double)super.sourceZ);
         }

         if(this.rite.upkeepPowerCost > 0.0F) {
            IPowerSource powerSource = this.getPowerSource(world, super.sourceX, super.sourceY, super.sourceZ);
            if(powerSource == null) {
               return RitualStep.Result.ABORTED;
            }

            this.powerSourceCoord = powerSource.getLocation();
            if(!powerSource.consumePower(this.rite.upkeepPowerCost)) {
               return RitualStep.Result.ABORTED;
            }
         }

         if(this.rite.ticksToLive > 0 && ticks % 20L == 0L && ++this.ticksSoFar >= this.rite.ticksToLive) {
            return RitualStep.Result.COMPLETED;
         } else {
            this.rite.update(world, posX, posY, posZ, this.rite.radius, ticks);
            return RitualStep.Result.UPKEEP;
         }
      }

      IPowerSource getPowerSource(World world, int posX, int posY, int posZ) {
         if(this.powerSourceCoord != null && world.rand.nextInt(5) != 0) {
            TileEntity tileEntity = this.powerSourceCoord.getBlockTileEntity(world);
            if(!(tileEntity instanceof BlockAltar.TileEntityAltar)) {
               return this.findNewPowerSource(world, posX, posY, posZ);
            } else {
               BlockAltar.TileEntityAltar altarTileEntity = (BlockAltar.TileEntityAltar)tileEntity;
               return (IPowerSource)(!altarTileEntity.isValid()?this.findNewPowerSource(world, posX, posY, posZ):altarTileEntity);
            }
         } else {
            return this.findNewPowerSource(world, posX, posY, posZ);
         }
      }

      private IPowerSource findNewPowerSource(World world, int posX, int posY, int posZ) {
         ArrayList sources = PowerSources.instance() != null?PowerSources.instance().get(world, new Coord(posX, posY, posZ), 16):null;
         return sources != null && sources.size() > 0?((PowerSources.RelativePowerSource)sources.get(0)).source():null;
      }
   }
}
