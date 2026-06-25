package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.Coord;
import java.util.ArrayList;
import net.minecraft.world.World;

public class RitePartEarth extends Rite {

   private final int length;
   private final int width;
   private final int depth;


   public RitePartEarth(int length, int width, int depth) {
      this.length = length;
      this.width = width;
      this.depth = depth;
   }

   public void addSteps(ArrayList steps, int initialStage) {
      steps.add(new RitePartEarth.StepPartEarth(this, initialStage));
   }

   private static class StepPartEarth extends RitualStep {

      private final RitePartEarth rite;
      private int stage = 0;
      Coord coord;
      ArrayList coords = new ArrayList();


      public StepPartEarth(RitePartEarth rite, int initialStage) {
         super(false);
         this.rite = rite;
         this.stage = initialStage;
      }

      public int getCurrentStage() {
         return this.stage;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(this.stage == 0 && ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            int length = this.rite.length;
            int width = this.rite.width + (ritual.covenSize > 2?2:0);
            int depth = this.rite.depth;
            if(++this.stage == 1 || this.coords.isEmpty()) {
               this.coords.clear();
               this.coord = new Coord(posX, posY - 1, posZ);
               this.coords.add(this.coord);
               int DELAY = ritual.sacrificedItems != null && !ritual.sacrificedItems.isEmpty()?this.coord.getHeading(((RitualStep.SacrificedItem)ritual.sacrificedItems.get(0)).location):0;
               byte c = 20;

               for(int l = 0; l < length - 1; ++l) {
                  DELAY = this.move(world, DELAY, this.coord, Math.max(c - l / 2, 6));
                  this.coords.add(this.coord);
               }
            }

            boolean var15 = true;
            if(!world.isRemote) {
               Coord var14 = (Coord)this.coords.get(this.stage + 4);
               this.drawFilledCircle(world, var14.x, var14.z, var14.y, width + (world.rand.nextInt(3) == 0?1:0), depth - 2 + world.rand.nextInt(5));
            }

            return this.stage >= this.coords.size() - 4 - 1?RitualStep.Result.COMPLETED:RitualStep.Result.UPKEEP;
         }
      }

      protected void drawFilledCircle(World world, int x0, int z0, int y, int radius, int depth) {
         int x = radius;
         int z = 0;
         int radiusError = 1 - radius;

         while(x >= z) {
            this.drawLine(world, -x + x0, x + x0, z + z0, y, depth);
            this.drawLine(world, -z + x0, z + x0, x + z0, y, depth);
            this.drawLine(world, -x + x0, x + x0, -z + z0, y, depth);
            this.drawLine(world, -z + x0, z + x0, -x + z0, y, depth);
            ++z;
            if(radiusError < 0) {
               radiusError += 2 * z + 1;
            } else {
               --x;
               radiusError += 2 * (z - x + 1);
            }
         }

      }

      protected void drawLine(World world, int x1, int x2, int z, int y, int depth) {
         for(int x = x1; x <= x2; ++x) {
            this.drawPixel(world, x, z, y, depth);
         }

      }

      protected void drawPixel(World world, int x, int z, int y, int depth) {
         for(int d = 0; d < depth; ++d) {
            if(BlockProtect.canBreak(x, y - d, z, world)) {
               world.setBlockToAir(x, y - d, z);
            }
         }

      }

      private int move(World world, int last, Coord coord, int probability) {
         int val = world.rand.nextInt(probability);
         switch(last) {
         case 0:
            if(val == 0) {
               this.coord = coord.northEast();
               return 1;
            } else {
               if(val == 1) {
                  this.coord = coord.northWest();
                  return 7;
               }

               this.coord = coord.north();
               return 0;
            }
         case 1:
            if(val == 0) {
               this.coord = coord.north();
               return 0;
            } else {
               if(val == 1) {
                  this.coord = coord.east();
                  return 2;
               }

               this.coord = coord.northEast();
               return 1;
            }
         case 2:
            if(val == 0) {
               this.coord = coord.northEast();
               return 1;
            } else {
               if(val == 1) {
                  this.coord = coord.southEast();
                  return 3;
               }

               this.coord = coord.east();
               return 2;
            }
         case 3:
            if(val == 0) {
               this.coord = coord.east();
               return 2;
            } else {
               if(val == 1) {
                  this.coord = coord.south();
                  return 4;
               }

               this.coord = coord.southEast();
               return 3;
            }
         case 4:
            if(val == 0) {
               this.coord = coord.southEast();
               return 3;
            } else {
               if(val == 1) {
                  this.coord = coord.southWest();
                  return 5;
               }

               this.coord = coord.south();
               return 4;
            }
         case 5:
            if(val == 0) {
               this.coord = coord.south();
               return 4;
            } else {
               if(val == 1) {
                  this.coord = coord.west();
                  return 6;
               }

               this.coord = coord.southWest();
               return 5;
            }
         case 6:
            if(val == 0) {
               this.coord = coord.southWest();
               return 5;
            } else {
               if(val == 1) {
                  this.coord = coord.northWest();
                  return 7;
               }

               this.coord = coord.west();
               return 6;
            }
         case 7:
         default:
            if(val == 0) {
               this.coord = coord.west();
               return 6;
            } else if(val == 1) {
               this.coord = coord.north();
               return 0;
            } else {
               this.coord = coord.northWest();
               return 7;
            }
         }
      }
   }
}
