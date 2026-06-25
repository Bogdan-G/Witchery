package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RiteCrater extends Rite {

   private final int radius;
   private final int height;


   public RiteCrater(int radius, int height) {
      this.radius = radius;
      this.height = height;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteCrater.StepCrater(this, intialStage));
   }

   private static class StepCrater extends RitualStep {

      private final RiteCrater rite;
      private int stage = 0;


      public StepCrater(RiteCrater rite, int initialStage) {
         super(true);
         this.rite = rite;
         this.stage = initialStage;
      }

      public int getCurrentStage() {
         return (byte)this.stage;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 10L != 0L) {
            return RitualStep.Result.STARTING;
         } else if(world.isRemote) {
            return RitualStep.Result.COMPLETED;
         } else {
            if(++this.stage == 1) {
               ParticleEffect.PORTAL.send(SoundEffect.RANDOM_FIZZ, world, (double)posX, (double)posY, (double)posZ, 0.5D, 1.0D, 16);
            }

            int height = this.rite.height;
            float radius = (float)this.rite.radius;
            if(this.stage > height) {
               return RitualStep.Result.COMPLETED;
            } else {
               for(int y = 1; y <= this.stage; ++y) {
                  float r = radius - (float)(height - this.stage - 1 + y) * radius / (float)height;
                  Log.instance().debug(String.format("Stage: %d, r=%f y=%d", new Object[]{Integer.valueOf(this.stage), Float.valueOf(r), Integer.valueOf(y)}));
                  this.drawFilledCircle(world, posX, posZ, posY - y, Math.max((int)Math.ceil((double)r), 1), posY);
               }

               return RitualStep.Result.UPKEEP;
            }
         }
      }

      protected void drawFilledCircle(World world, int x0, int z0, int y, int radius, int height) {
         int x = radius;
         int z = 0;
         int radiusError = 1 - radius;

         while(x >= z) {
            this.drawLine(world, -x + x0, x + x0, z + z0, y, x0, z0, radius, height);
            this.drawLine(world, -z + x0, z + x0, x + z0, y, x0, z0, radius, height);
            this.drawLine(world, -x + x0, x + x0, -z + z0, y, x0, z0, radius, height);
            this.drawLine(world, -z + x0, z + x0, -x + z0, y, x0, z0, radius, height);
            ++z;
            if(radiusError < 0) {
               radiusError += 2 * z + 1;
            } else {
               --x;
               radiusError += 2 * (z - x + 1);
            }
         }

      }

      protected void drawLine(World world, int x1, int x2, int z, int y, int midX, int midZ, int radius, int midY) {
         int modX1 = radius > 1 && world.rand.nextInt(5) == 0?x1 + 1:x1;
         int modX2 = radius > 1 && world.rand.nextInt(5) == 0?x2 - 1:x2;
         boolean var10000;
         if(midZ + radius != z && midZ - radius != z) {
            var10000 = false;
         } else {
            var10000 = true;
         }

         for(int done = modX1; done <= modX2; ++done) {
            this.drawPixel(world, done, z, y, midX, midY, midZ);
         }

         boolean var14 = true;
      }

      protected void drawPixel(World world, int x, int z, int y, int midX, int midY, int midZ) {
         if(!world.isRemote && (x != midX || z != midZ) && (y < midY - 3 || Coord.distance((double)x, (double)midY, (double)z, (double)midX, (double)midY, (double)midZ) > (double)(this.rite.radius - 3 - (midY - y)))) {
            Block blockID = world.getBlock(x, y, z);
            int blockMetadata = world.getBlockMetadata(x, y, z);
            if(BlockProtect.canBreak(x, y, z, world)) {
               world.setBlockToAir(x, y, z);
               if(blockID != Blocks.air && blockID != Blocks.stone && blockID != Blocks.dirt && blockID != Blocks.grass && blockID != Blocks.sand && blockID != Blocks.sandstone && blockID != Blocks.gravel) {
                  ItemStack stack = new ItemStack(blockID, 1, blockMetadata);
                  EntityItem entity = new EntityItem(world, (double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, stack);
                  world.spawnEntityInWorld(entity);
               }
            }
         }

      }
   }
}
