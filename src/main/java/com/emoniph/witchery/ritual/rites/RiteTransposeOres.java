package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RiteTransposeOres extends Rite {

   protected final int radius;
   protected final int pulses;
   protected final Block[] blocks;


   public RiteTransposeOres(int radius, int pulses, Block[] blocks) {
      this.radius = radius;
      this.pulses = pulses;
      this.blocks = blocks;
   }

   public void addSteps(ArrayList steps, int initialStep) {
      steps.add(new RiteTransposeOres.StepTeleportation(this, initialStep));
   }

   private static class StepTeleportation extends RitualStep {

      private final RiteTransposeOres rite;
      private int step;


      public StepTeleportation(RiteTransposeOres rite, int initialStep) {
         super(false);
         this.rite = rite;
         this.step = initialStep;
      }

      public int getCurrentStage() {
         return this.step;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 10L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            ++this.step;
            int r = this.rite.radius;
            int y = posY - this.step;
            int blockTypes = ritual.covenSize == 6?2:1;

            for(int x = posX - r; x <= posX + r; ++x) {
               for(int z = posZ - r; z <= posZ + r; ++z) {
                  Block blockID = world.getBlock(x, y, z);

                  for(int t = 0; t < blockTypes; ++t) {
                     if(blockID == this.rite.blocks[t]) {
                        ItemStack stack = new ItemStack(this.rite.blocks[t]);
                        EntityItem entity = new EntityItem(world, (double)(posX - r + world.rand.nextInt(2 * r + 1)), (double)(posY + 2), (double)(posZ - r + world.rand.nextInt(2 * r + 1)), stack);
                        if(!world.isRemote) {
                           world.setBlockToAir(x, y, z);
                           world.spawnEntityInWorld(entity);
                        }
                     }
                  }
               }
            }

            return this.step < this.rite.pulses + 5 * ritual.covenSize && y > 2?RitualStep.Result.UPKEEP:RitualStep.Result.COMPLETED;
         }
      }
   }
}
