package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class RiteSphereEffect extends Rite {

   protected final int maxRadius;
   protected final Block block;


   public RiteSphereEffect(int radius, Block block) {
      this.maxRadius = radius;
      this.block = block;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteSphereEffect.StepExpansion(this, intialStage));
   }

   private static class StepExpansion extends RitualStep {

      private final RiteSphereEffect rite;
      private int stage = 0;
      private boolean activated;


      public StepExpansion(RiteSphereEffect rite, int initialStage) {
         super(false);
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

         if(!world.isRemote) {
            if(ticks % 5L != 0L) {
               return RitualStep.Result.UPKEEP;
            } else {
               EntityPlayer player = ritual.getInitiatingPlayer(world);
               if(ritual.covenSize < 2) {
                  SoundEffect.NOTE_SNARE.playAt(world, (double)posX, (double)posY, (double)posZ);
                  if(player != null) {
                     ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.rite.coventoosmall", new Object[0]);
                  }

                  return RitualStep.Result.ABORTED_REFUND;
               } else {
                  ++this.stage;
                  int maxRadius = (int)(ritual.covenSize <= 2?(double)this.rite.maxRadius:(ritual.covenSize <= 5?1.5D * (double)this.rite.maxRadius:2.0D * (double)this.rite.maxRadius));
                  int currentRadius = this.stage + 4;
                  if(currentRadius <= maxRadius) {
                     if(this.stage % 2 == 0) {
                        drawSphere(world, posX, posY, posZ, currentRadius, this.rite.block);
                        drawSphere(world, posX, posY, posZ, currentRadius - 2, Blocks.air);
                     }

                     if(currentRadius == maxRadius) {
                        fillWithAir(world, posX, posY, posZ, maxRadius, this.rite.block);
                     }
                  }

                  return this.stage <= 250 && currentRadius < maxRadius?RitualStep.Result.UPKEEP:RitualStep.Result.COMPLETED;
               }
            }
         } else {
            return RitualStep.Result.COMPLETED;
         }
      }

      private static void fillWithAir(World world, int posX, int posY, int posZ, int radius, Block removalBlock) {
         fillHalfWithAirY(world, posX, posY, posZ, 1, radius, removalBlock);
         fillHalfWithAirY(world, posX, posY - 1, posZ, -1, radius, removalBlock);
      }

      private static void fillHalfWithAirY(World world, int posX, int posY, int posZ, int dy, int radius, Block removalBlock) {
         for(int y = 0; y <= radius; ++y) {
            int realY = posY + y * dy;
            if(world.getBlock(posX, realY, posZ) == removalBlock) {
               break;
            }

            fillSliceWithAir(world, posX, realY, posZ, radius, removalBlock);
         }

      }

      private static void fillSliceWithAir(World world, int posX, int posY, int posZ, int radius, Block removalBlock) {
         fillHalfWithAirX(world, posX, posY, posZ, 1, radius, removalBlock);
         fillHalfWithAirX(world, posX - 1, posY, posZ, -1, radius, removalBlock);
      }

      private static void fillHalfWithAirX(World world, int posX, int posY, int posZ, int dx, int radius, Block removalBlock) {
         for(int x = 0; x <= radius; ++x) {
            int realX = posX + x * dx;
            if(world.getBlock(realX, x, posZ) == removalBlock) {
               break;
            }

            fillLineWithAir(world, realX, posY, posZ, radius, removalBlock);
         }

      }

      private static void fillLineWithAir(World world, int posX, int posY, int posZ, int radius, Block removalBlock) {
         fillHalfWithAirZ(world, posX, posY, posZ, 1, radius, removalBlock);
         fillHalfWithAirZ(world, posX, posY, posZ - 1, -1, radius, removalBlock);
      }

      private static void fillHalfWithAirZ(World world, int posX, int posY, int posZ, int dz, int radius, Block removalBlock) {
         for(int z = 0; z <= radius; ++z) {
            int realZ = posZ + z * dz;
            Block foundBlock = world.getBlock(posX, posY, realZ);
            if(foundBlock == removalBlock) {
               break;
            }

            if(foundBlock == Blocks.water || foundBlock == Blocks.flowing_water) {
               world.setBlock(posX, posY, realZ, Blocks.air);
            }
         }

      }

      public static void drawSphere(World world, int x0, int y0, int z0, int radius, Block blockID) {
         int x = radius;
         int y = 0;
         int radiusError = 1 - radius;

         while(x >= y) {
            drawCircle(world, x0, y0, z0, y, x, radiusError, blockID);
            ++y;
            if(radiusError < 0) {
               radiusError += 2 * y + 1;
            } else {
               --x;
               radiusError += 2 * (y - x + 1);
            }
         }

      }

      protected static boolean drawCircle(World world, int x0, int y0, int z0, int y1, int radius, int error0, Block blockID) {
         int x = radius;
         int z = 0;
         int radiusError = error0;

         while(x >= z) {
            drawPixel(world, x0 + x, z0 + z, y0 + y1, blockID);
            drawPixel(world, x0 - x, z0 + z, y0 + y1, blockID);
            drawPixel(world, x0 + x, z0 + z, y0 - y1, blockID);
            drawPixel(world, x0 - x, z0 + z, y0 - y1, blockID);
            drawPixel(world, x0 + x, z0 - z, y0 + y1, blockID);
            drawPixel(world, x0 - x, z0 - z, y0 + y1, blockID);
            drawPixel(world, x0 + x, z0 - z, y0 - y1, blockID);
            drawPixel(world, x0 - x, z0 - z, y0 - y1, blockID);
            drawPixel(world, x0 + z, z0 + x, y0 + y1, blockID);
            drawPixel(world, x0 - z, z0 + x, y0 + y1, blockID);
            drawPixel(world, x0 + z, z0 + x, y0 - y1, blockID);
            drawPixel(world, x0 - z, z0 + x, y0 - y1, blockID);
            drawPixel(world, x0 + z, z0 - x, y0 + y1, blockID);
            drawPixel(world, x0 - z, z0 - x, y0 + y1, blockID);
            drawPixel(world, x0 + z, z0 - x, y0 - y1, blockID);
            drawPixel(world, x0 - z, z0 - x, y0 - y1, blockID);
            drawPixel(world, x0 + y1, z0 + z, y0 + x, blockID);
            drawPixel(world, x0 - y1, z0 + z, y0 + x, blockID);
            drawPixel(world, x0 + y1, z0 + z, y0 - x, blockID);
            drawPixel(world, x0 - y1, z0 + z, y0 - x, blockID);
            drawPixel(world, x0 + y1, z0 - z, y0 + x, blockID);
            drawPixel(world, x0 - y1, z0 - z, y0 + x, blockID);
            drawPixel(world, x0 + y1, z0 - z, y0 - x, blockID);
            drawPixel(world, x0 - y1, z0 - z, y0 - x, blockID);
            drawPixel(world, x0 + z, z0 + y1, y0 + x, blockID);
            drawPixel(world, x0 - z, z0 + y1, y0 + x, blockID);
            drawPixel(world, x0 + z, z0 + y1, y0 - x, blockID);
            drawPixel(world, x0 - z, z0 + y1, y0 - x, blockID);
            drawPixel(world, x0 + z, z0 - y1, y0 + x, blockID);
            drawPixel(world, x0 - z, z0 - y1, y0 + x, blockID);
            drawPixel(world, x0 + z, z0 - y1, y0 - x, blockID);
            drawPixel(world, x0 - z, z0 - y1, y0 - x, blockID);
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

      protected static void drawPixel(World world, int x, int z, int y, Block replaceBlockID) {
         Block blockID = world.getBlock(x, y, z);
         if((blockID == Blocks.water || blockID == Blocks.flowing_water || blockID == Blocks.air || blockID == Blocks.ice || blockID == Blocks.snow || blockID == Blocks.tallgrass || blockID == Blocks.vine || blockID == Blocks.waterlily || blockID == Blocks.red_flower || blockID == Blocks.yellow_flower || blockID == Blocks.cactus || blockID == Blocks.deadbush || blockID == Witchery.Blocks.PERPETUAL_ICE) && blockID != replaceBlockID) {
            world.setBlock(x, y, z, replaceBlockID);
         }

      }
   }
}
