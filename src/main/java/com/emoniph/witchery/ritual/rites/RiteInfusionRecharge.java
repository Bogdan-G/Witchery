package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockAltar;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class RiteInfusionRecharge extends Rite {

   private final float upkeepPowerCost;
   private final int charges;
   private final int radius;
   private final int ticksToLive;


   public RiteInfusionRecharge(int charges, int radius, float upkeepPowerCost, int ticksToLive) {
      this.charges = charges;
      this.radius = radius;
      this.upkeepPowerCost = upkeepPowerCost;
      this.ticksToLive = ticksToLive;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteInfusionRecharge.StepInfusePlayers(this, intialStage));
   }

   private static class StepInfusePlayers extends RitualStep {

      private final RiteInfusionRecharge rite;
      private boolean activated = false;
      protected int ticksSoFar;
      Coord powerSourceCoord;
      static final int POWER_SOURCE_RADIUS = 16;


      public StepInfusePlayers(RiteInfusionRecharge rite, int ticksSoFar) {
         super(false);
         this.rite = rite;
         this.ticksSoFar = ticksSoFar;
      }

      public int getCurrentStage() {
         return this.ticksSoFar;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            if(!world.isRemote) {
               if(this.rite.upkeepPowerCost > 0.0F) {
                  IPowerSource r = this.getPowerSource(world, posX, posY, posZ);
                  if(r == null) {
                     return RitualStep.Result.ABORTED;
                  }

                  this.powerSourceCoord = r.getLocation();
                  if(!r.consumePower(this.rite.upkeepPowerCost)) {
                     return RitualStep.Result.ABORTED;
                  }
               }

               if(this.rite.ticksToLive > 0 && ticks % 20L == 0L && ++this.ticksSoFar >= this.rite.ticksToLive) {
                  return RitualStep.Result.COMPLETED;
               }

               int var15 = this.rite.radius;
               AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)(posX - var15), (double)posY, (double)(posZ - var15), (double)(posX + var15), (double)(posY + 1), (double)(posZ + var15));
               Iterator i$ = world.getEntitiesWithinAABB(EntityPlayer.class, bounds).iterator();

               while(i$.hasNext()) {
                  Object obj = i$.next();
                  EntityPlayer player = (EntityPlayer)obj;
                  if(Coord.distance(player.posX, player.posY, player.posZ, (double)posX, (double)posY, (double)posZ) <= (double)var15) {
                     int currentEnergy = Infusion.getCurrentEnergy(player);
                     int maxEnergy = Infusion.getMaxEnergy(player);
                     if(currentEnergy < maxEnergy) {
                        Infusion.setCurrentEnergy(player, Math.min(currentEnergy + this.rite.charges, maxEnergy));
                        ParticleEffect.INSTANT_SPELL.send(SoundEffect.NOTE_PLING, player, 1.0D, 2.0D, 8);
                     }
                  }
               }
            }

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
