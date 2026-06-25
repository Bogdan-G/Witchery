package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class RiteGlyphicTransformation extends Rite {

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteGlyphicTransformation.StepGlyphicTransformation(this));
   }

   private static class StepGlyphicTransformation extends RitualStep {

      private final RiteGlyphicTransformation rite;


      public StepGlyphicTransformation(RiteGlyphicTransformation rite) {
         super(false);
         this.rite = rite;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 30L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            if(!world.isRemote) {
               double RADIUS = 4.0D;
               List items = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox((double)posX - 4.0D, (double)(posY - 2), (double)posZ - 4.0D, (double)posX + 4.0D, (double)(posY + 2), (double)posZ + 4.0D));
               int whiteChalk = 0;
               int purpleChalk = 0;
               int redChalk = 0;
               Iterator blockID = items.iterator();

               boolean c;
               while(blockID.hasNext()) {
                  Object size = blockID.next();
                  EntityItem a = (EntityItem)size;
                  ItemStack b = a.getEntityItem();
                  if(redChalk == 0 && purpleChalk == 0 && b.isItemEqual(new ItemStack(Witchery.Items.CHALK_RITUAL, 1, 0))) {
                     c = whiteChalk == 0;
                     whiteChalk += b.stackSize;
                     if(c) {
                        --b.stackSize;
                        if(b.stackSize <= 0) {
                           world.removeEntity(a);
                        }
                     }
                  } else if(redChalk == 0 && whiteChalk == 0 && b.isItemEqual(new ItemStack(Witchery.Items.CHALK_OTHERWHERE, 1, 0))) {
                     c = purpleChalk == 0;
                     purpleChalk += b.stackSize;
                     if(c) {
                        --b.stackSize;
                        if(b.stackSize <= 0) {
                           world.removeEntity(a);
                        }
                     }
                  } else {
                     if(purpleChalk != 0 || whiteChalk != 0 || !b.isItemEqual(new ItemStack(Witchery.Items.CHALK_INFERNAL, 1, 0))) {
                        continue;
                     }

                     c = redChalk == 0;
                     redChalk += b.stackSize;
                     if(c) {
                        --b.stackSize;
                        if(b.stackSize <= 0) {
                           world.removeEntity(a);
                        }
                     }
                  }

                  ParticleEffect.SMOKE.send(SoundEffect.RANDOM_POP, a, 1.0D, 1.0D, 16);
               }

               Block var31 = Blocks.air;
               int var30 = 0;
               if(whiteChalk == 0 && redChalk == 0 && purpleChalk == 0) {
                  return RitualStep.Result.ABORTED_REFUND;
               }

               if(redChalk > 0) {
                  var31 = Witchery.Blocks.GLYPH_INFERNAL;
                  var30 = Math.min(redChalk, 3);
               } else if(purpleChalk > 0) {
                  var31 = Witchery.Blocks.GLYPH_OTHERWHERE;
                  var30 = Math.min(purpleChalk, 3);
               } else if(whiteChalk > 0) {
                  var31 = Witchery.Blocks.GLYPH_RITUAL;
                  var30 = Math.min(whiteChalk, 3);
               }

               boolean var32 = true;
               boolean var33 = true;
               c = true;
               boolean _ = false;
               int[][] PATTERN = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0}, {0, 0, 0, 3, 0, 0, 2, 2, 2, 2, 2, 0, 0, 3, 0, 0, 0}, {0, 0, 3, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 3, 0, 0}, {0, 3, 0, 0, 2, 0, 0, 1, 1, 1, 0, 0, 2, 0, 0, 3, 0}, {0, 3, 0, 2, 0, 0, 1, 0, 0, 0, 1, 0, 0, 2, 0, 3, 0}, {0, 3, 0, 2, 0, 1, 0, 0, 0, 0, 0, 1, 0, 2, 0, 3, 0}, {0, 3, 0, 2, 0, 1, 0, 0, 4, 0, 0, 1, 0, 2, 0, 3, 0}, {0, 3, 0, 2, 0, 1, 0, 0, 0, 0, 0, 1, 0, 2, 0, 3, 0}, {0, 3, 0, 2, 0, 0, 1, 0, 0, 0, 1, 0, 0, 2, 0, 3, 0}, {0, 3, 0, 0, 2, 0, 0, 1, 1, 1, 0, 0, 2, 0, 0, 3, 0}, {0, 0, 3, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 3, 0, 0}, {0, 0, 0, 3, 0, 0, 2, 2, 2, 2, 2, 0, 0, 3, 0, 0, 0}, {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
               int offsetZ = (PATTERN.length - 1) / 2;

               for(int z = 0; z < PATTERN.length - 1; ++z) {
                  int worldZ = posZ - offsetZ + z;
                  int offsetX = (PATTERN[z].length - 1) / 2;

                  for(int x = 0; x < PATTERN[z].length; ++x) {
                     int worldX = posX - offsetX + x;
                     int item = PATTERN[PATTERN.length - 1 - z][x];
                     if(item == var30) {
                        Block currentBlockID = world.getBlock(worldX, posY, worldZ);
                        if((currentBlockID == Witchery.Blocks.GLYPH_INFERNAL || currentBlockID == Witchery.Blocks.GLYPH_OTHERWHERE || currentBlockID == Witchery.Blocks.GLYPH_RITUAL) && currentBlockID != var31) {
                           int meta = world.getBlockMetadata(worldX, posY, worldZ);
                           world.setBlock(worldX, posY, worldZ, var31, meta, 3);
                           ParticleEffect.SMOKE.send(SoundEffect.NONE, world, (double)worldX, (double)(posY + 1), (double)worldZ, 0.5D, 1.0D, 16);
                        }
                     }
                  }
               }
            }

            return RitualStep.Result.COMPLETED;
         }
      }
   }
}
