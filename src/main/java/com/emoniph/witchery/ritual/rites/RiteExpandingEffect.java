package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.entity.EntityWitchHunter;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class RiteExpandingEffect extends Rite {

   protected final int maxRadius;
   protected final int height;
   protected final boolean curse;


   public RiteExpandingEffect(int radius, int height, boolean curse) {
      this.maxRadius = radius;
      this.height = height;
      this.curse = curse;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteExpandingEffect.StepExpansion(this, intialStage));
   }

   public abstract void doBlockAction(World var1, int var2, int var3, int var4, int var5, EntityPlayer var6, boolean var7);

   public abstract boolean doRadiusAction(World var1, int var2, int var3, int var4, int var5, EntityPlayer var6, boolean var7);

   public boolean isComplete(World world, int posX, int posY, int posZ, int radius, EntityPlayer player, long ticks, boolean fullyExpanded, boolean enhanced) {
      return fullyExpanded;
   }

   private static class StepExpansion extends RitualStep {

      private final RiteExpandingEffect rite;
      private int stage = 0;
      private boolean activated;


      public StepExpansion(RiteExpandingEffect rite, int initialStage) {
         super(true);
         this.rite = rite;
         this.stage = initialStage;
      }

      public int getCurrentStage() {
         return (byte)this.stage;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(!this.activated) {
            if(ticks % 20L != 0L) {
               return RitualStep.Result.STARTING;
            }

            this.activated = true;
            SoundEffect.RANDOM_FIZZ.playAt(world, (double)posX, (double)posY, (double)posZ);
         }

         if(world.isRemote) {
            return RitualStep.Result.COMPLETED;
         } else if(ticks % 5L == 0L) {
            ++this.stage;
            if(this.stage == 1 && this.rite.curse) {
               EntityWitchHunter.blackMagicPerformed(ritual.getInitiatingPlayer(world));
            }

            int height = this.rite.height;
            float maxRadius = (float)(this.rite.maxRadius + 2 * ritual.covenSize);
            EntityPlayer player = ritual.getInitiatingPlayer(world);
            int currentRadius = this.stage + 3;
            boolean enhanced = player != null && Familiar.hasActiveCurseMasteryFamiliar(player);
            return (float)currentRadius <= maxRadius && !this.applyCircle(world, posX, posZ, posY, currentRadius, height, player, enhanced)?RitualStep.Result.ABORTED:(this.stage <= 250 && !this.rite.isComplete(world, posX, posY, posZ, currentRadius, player, ticks, (float)currentRadius >= maxRadius, enhanced)?RitualStep.Result.UPKEEP:RitualStep.Result.COMPLETED);
         } else {
            return RitualStep.Result.UPKEEP;
         }
      }

      protected boolean applyCircle(World world, int x0, int z0, int y0, int radius, int height, EntityPlayer player, boolean enhanced) {
         if(!this.rite.doRadiusAction(world, x0, y0, z0, radius, player, enhanced)) {
            return false;
         } else {
            int x = radius;
            int z = 0;
            int radiusError = 1 - radius;

            while(x >= z) {
               this.drawPixel(world, x + x0, z + z0, y0, height, radius, player, enhanced);
               this.drawPixel(world, z + x0, x + z0, y0, height, radius, player, enhanced);
               this.drawPixel(world, -x + x0, z + z0, y0, height, radius, player, enhanced);
               this.drawPixel(world, -z + x0, x + z0, y0, height, radius, player, enhanced);
               this.drawPixel(world, -x + x0, -z + z0, y0, height, radius, player, enhanced);
               this.drawPixel(world, -z + x0, -x + z0, y0, height, radius, player, enhanced);
               this.drawPixel(world, x + x0, -z + z0, y0, height, radius, player, enhanced);
               this.drawPixel(world, z + x0, -x + z0, y0, height, radius, player, enhanced);
               ++z;
               if(radiusError < 0) {
                  radiusError += 2 * z + 1;
               } else {
                  --x;
                  radiusError += 2 * (z - x + 1);
               }
            }

            return true;
         }
      }

      protected void drawPixel(World world, int x, int z, int y, int height, int currentRadius, EntityPlayer player, boolean enhanced) {
         for(int i = 0; i < height; ++i) {
            if(world.getBlock(x, y + i, z).getMaterial() != Material.air && world.isAirBlock(x, y + i + 1, z)) {
               if(this.rite.curse) {
                  ParticleEffect.MOB_SPELL.send(SoundEffect.NONE, world, 0.5D + (double)x, (double)(y + i + 1), 0.5D + (double)z, 1.0D, 1.0D, 16);
               } else {
                  ParticleEffect.SPELL.send(SoundEffect.NONE, world, 0.5D + (double)x, (double)(y + i + 1), 0.5D + (double)z, 1.0D, 1.0D, 16);
               }

               this.rite.doBlockAction(world, x, y + i, z, currentRadius, player, enhanced);
               break;
            }

            if(i > 0 && world.getBlock(x, y - i, z).getMaterial() != Material.air && world.isAirBlock(x, y - i + 1, z)) {
               if(this.rite.curse) {
                  ParticleEffect.MOB_SPELL.send(SoundEffect.NONE, world, 0.5D + (double)x, (double)(y - i + 1), 0.5D + (double)z, 1.0D, 1.0D, 32);
               } else {
                  ParticleEffect.SPELL.send(SoundEffect.NONE, world, 0.5D + (double)x, (double)(y - i + 1), 0.5D + (double)z, 1.0D, 1.0D, 32);
               }

               this.rite.doBlockAction(world, x, y - i, z, currentRadius, player, enhanced);
               break;
            }
         }

      }
   }
}
